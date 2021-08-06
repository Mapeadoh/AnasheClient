package me.travis.mapeadoh.clientstuff.gamesense;

import java.util.function.Predicate;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class SpoofRotationUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final SpoofRotationUtil ROTATION_UTIL = new SpoofRotationUtil();
    private int rotationConnections = 0;
    private boolean shouldSpoofAngles;
    private boolean isSpoofingAngles;
    private double yaw;
    private double pitch;
    @EventHandler
    private final Listener<WurstplusEventPacket.SendPacket> packetSendListener = new Listener<WurstplusEventPacket.SendPacket>(event -> {
        Packet packet = event.get_packet();
        if (packet instanceof CPacketPlayer && this.shouldSpoofAngles && this.isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)this.yaw;
            ((CPacketPlayer)packet).pitch = (float)this.pitch;
        }
    }, new Predicate[0]);

    private SpoofRotationUtil() {
    }

    public void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = WurstplusEntityUtil.calculateLookAt(px, py, pz, (Entity)me);
        this.setYawAndPitch((float)v[0], (float)v[1]);
    }

    public void setYawAndPitch(float yaw1, float pitch1) {
        this.yaw = yaw1;
        this.pitch = pitch1;
        this.isSpoofingAngles = true;
    }

    public void resetRotation() {
        if (this.isSpoofingAngles) {
            this.yaw = SpoofRotationUtil.mc.player.rotationYaw;
            this.pitch = SpoofRotationUtil.mc.player.rotationPitch;
            this.isSpoofingAngles = false;
        }
    }

    public void shouldSpoofAngles(boolean e) {
        this.shouldSpoofAngles = e;
    }

    public boolean isSpoofingAngles() {
        return this.isSpoofingAngles;
    }
}

