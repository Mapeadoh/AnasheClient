package me.travis.wurstplus.wurstplustwo.hacks.chat;

import java.util.function.Predicate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class GlobalLocations extends WurstplusHack {
    WurstplusSetting thunder = this.create("Thunder", "Thunder", true);
    WurstplusSetting slimes = this.create("Slimes", "Slimes", false);
    WurstplusSetting wither = this.create("Wither", "Wither", false);
    WurstplusSetting end_portal = this.create("End Portal", "EndPortal", true);
    WurstplusSetting ender_dragon = this.create("Ender Dragon", "EnderDragon", false);
    WurstplusSetting donkey = this.create("Donkey", "Donkey", false);
    @EventHandler
    private final Listener<WurstplusEventPacket> packet_event = new Listener<WurstplusEventPacket>(event -> {
        if (event.get_packet() instanceof SPacketSpawnMob) {
            SPacketSpawnMob packet = (SPacketSpawnMob)event.get_packet();
            if (this.slimes.get_value(true)) {
                Minecraft mc = Minecraft.getMinecraft();
                if (packet.getEntityType() == 55 && packet.getY() <= 40.0 && !mc.world.getBiome(mc.player.getPosition()).getBiomeName().toLowerCase().contains("swamp")) {
                    BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                    WurstplusMessageUtil.send_client_message("Slime Spawned in chunk X:" + mc.world.getChunk((BlockPos)pos).x + " Z:" + mc.world.getChunk((BlockPos)pos).z);
                }
            }
            if (this.donkey.get_value(true) && packet.getEntityType() == 31) {
                WurstplusMessageUtil.send_client_message(String.format("Donkey spawned at %s %s %s", packet.getX(), packet.getY(), packet.getZ()));
            }
        } else if (event.get_packet() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.get_packet();
            if (this.thunder.get_value(true) && packet.getCategory() == SoundCategory.WEATHER && packet.getSound() == SoundEvents.ENTITY_LIGHTNING_THUNDER) {
                float yaw = 0.0f;
                double difX = packet.getX() - Minecraft.getMinecraft().player.posX;
                double difZ = packet.getZ() - Minecraft.getMinecraft().player.posZ;
                yaw = (float)((double)yaw + MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0 - (double)yaw)));
                WurstplusMessageUtil.send_client_message("Lightning spawned X:" + Minecraft.getMinecraft().player.posX + " Z:" + Minecraft.getMinecraft().player.posZ + " Angle:" + yaw);
            }
        } else if (event.get_packet() instanceof SPacketEffect) {
            SPacketEffect packet = (SPacketEffect)event.get_packet();
            if (packet.getSoundType() == 1023 && this.wither.get_value(true)) {
                double theta = Math.atan2((double)packet.getSoundPos().getZ() - Minecraft.getMinecraft().player.posZ, (double)packet.getSoundPos().getX() - Minecraft.getMinecraft().player.posX);
                double angle = Math.toDegrees(theta += 1.5707963267948966);
                if (angle < 0.0) {
                    angle += 360.0;
                }
                WurstplusMessageUtil.send_client_message("Wither spawned in direction " + (angle -= 180.0) + " with y position: " + packet.getSoundPos().getY());
            } else if (packet.getSoundType() == 1038 && this.end_portal.get_value(true)) {
                WurstplusMessageUtil.send_client_message("End portal spawned at " + packet.getSoundPos().toString());
            } else if (packet.getSoundType() == 1028 && this.ender_dragon.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Ender dragon died at " + packet.getSoundPos().toString());
            }
        }
    }, new Predicate[0]);

    public GlobalLocations() {
        super(WurstplusCategory.WURSTPLUS_CHAT);
        this.name = "Global Location";
        this.tag = "GlobalLocation";
        this.description = "i may or not have skided this from salhack";
    }
}

