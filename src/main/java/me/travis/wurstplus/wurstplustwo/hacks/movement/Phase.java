package me.travis.wurstplus.wurstplustwo.hacks.movement;

import java.util.function.Predicate;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class Phase
        extends WurstplusHack {
    WurstplusSetting debug = this.create("Debug", "Debug", false);
    WurstplusSetting twodelay = this.create("2Delay", "2Delay", true);
    WurstplusSetting advd = this.create("AVD", "AVD", false);
    WurstplusSetting EnhancedRots = this.create("EnchancedControl", "EnchancedControl", false);
    WurstplusSetting invert = this.create("InvertedYaw", "InvertedYaw", false);
    WurstplusSetting SendRotPackets = this.create("SendRotPackets", "SendRotPackets", true);
    WurstplusSetting twobeepvp = this.create("2b2tpvp", "2b2tpvp", true);
    WurstplusSetting PUP = this.create("PUP", "PUP", true);
    WurstplusSetting tickDelay = this.create("TickDelay", "TickDelay", 2, 0, 40);
    WurstplusSetting EnhancedRotsAmount = this.create("EnhancedCtrlSpeed", "EnhancedCtrlSpeed", 2, 0, 20);
    WurstplusSetting speed = this.create("Speed", "Speed", 6.25, 0.0, 6.25);
    WurstplusSetting cmode = this.create("ControlMode", "ControlMode", "Rel", this.combobox("Rel", "Abs"));
    @EventHandler
    private Listener<WurstplusEventPacket.ReceivePacket> receiveListener = new Listener<WurstplusEventPacket.ReceivePacket>(event -> {
        SPacketPlayerPosLook pak;
        if (event.get_packet() instanceof SPacketPlayerPosLook) {
            pak = (SPacketPlayerPosLook)event.get_packet();
            pak.yaw = Phase.mc.player.rotationYaw;
            pak.pitch = Phase.mc.player.rotationPitch;
        }
        if (event.get_packet() instanceof SPacketPlayerPosLook) {
            pak = (SPacketPlayerPosLook)event.get_packet();
            double dx = Math.abs(pak.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.X) ? pak.getX() : Phase.mc.player.posX - pak.getX());
            double dy = Math.abs(pak.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Y) ? pak.getY() : Phase.mc.player.posY - pak.getY());
            double dz = Math.abs(pak.getFlags().contains((Object)SPacketPlayerPosLook.EnumFlags.Z) ? pak.getZ() : Phase.mc.player.posZ - pak.getZ());
            if (dx < 0.001) {
                dx = 0.0;
            }
            if (dz < 0.001) {
                dz = 0.0;
            }
            if ((dx != 0.0 || dy != 0.0 || dz != 0.0) && this.debug.get_value(false)) {
                Phase.mc.player.sendMessage((ITextComponent)new TextComponentString("position pak, dx=" + dx + " dy=" + dy + " dz=" + dz));
            }
            if (pak.yaw != Phase.mc.player.rotationYaw || pak.pitch != Phase.mc.player.rotationPitch) {
                if (this.SendRotPackets.get_value(true)) {
                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Rotation(Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, Phase.mc.player.onGround));
                }
                pak.yaw = Phase.mc.player.rotationYaw;
                pak.pitch = Phase.mc.player.rotationPitch;
            }
        }
    }, new Predicate[0]);
    KeyBinding left;
    KeyBinding right;
    KeyBinding down;
    KeyBinding up;
    long last = 0L;

    public Phase() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Phase";
        this.tag = "Phase";
        this.description = "Phase";
    }

    @Override
    public void update() {
        try {
            double zDir;
            double xDir;
            Phase.mc.player.noClip = true;
            if (this.tickDelay.get_value(2) > 0 && Phase.mc.player.ticksExisted % this.tickDelay.get_value(2) != 0 && this.twodelay.get_value(true)) {
                return;
            }
            int eca = this.EnhancedRotsAmount.get_value(2);
            if (this.EnhancedRots.get_value(false) && this.up.isKeyDown()) {
                Phase.mc.player.rotationPitch -= (float)eca;
            }
            if (this.EnhancedRots.get_value(false) && this.down.isKeyDown()) {
                Phase.mc.player.rotationPitch += (float)eca;
            }
            if (this.EnhancedRots.get_value(false) && this.left.isKeyDown()) {
                Phase.mc.player.rotationYaw -= (float)eca;
            }
            if (this.EnhancedRots.get_value(false) && this.right.isKeyDown()) {
                Phase.mc.player.rotationYaw += (float)eca;
            }
            double yaw = (Phase.mc.player.rotationYaw + 90.0f) * (float)(this.invert.get_value(false) ? -1 : 1);
            if (this.cmode.in("Rel")) {
                double dO_numer = 0.0;
                double dO_denom = 0.0;
                if (Phase.mc.gameSettings.keyBindLeft.isKeyDown()) {
                    dO_numer -= 90.0;
                    dO_denom += 1.0;
                }
                if (Phase.mc.gameSettings.keyBindRight.isKeyDown()) {
                    dO_numer += 90.0;
                    dO_denom += 1.0;
                }
                if (Phase.mc.gameSettings.keyBindBack.isKeyDown()) {
                    dO_numer += 180.0;
                    dO_denom += 1.0;
                }
                if (Phase.mc.gameSettings.keyBindForward.isKeyDown()) {
                    dO_denom += 1.0;
                }
                if (dO_denom > 0.0) {
                    yaw += dO_numer / dO_denom % 361.0;
                }
                if (yaw < 0.0) {
                    yaw = 360.0 - yaw;
                }
                if (yaw > 360.0) {
                    yaw %= 361.0;
                }
                xDir = Math.cos(Math.toRadians(yaw));
                zDir = Math.sin(Math.toRadians(yaw));
            } else {
                xDir = 0.0;
                zDir = 0.0;
                xDir += Phase.mc.gameSettings.keyBindForward.isKeyDown() ? 1.0 : 0.0;
                xDir -= Phase.mc.gameSettings.keyBindBack.isKeyDown() ? 1.0 : 0.0;
                zDir += Phase.mc.gameSettings.keyBindLeft.isKeyDown() ? 1.0 : 0.0;
                zDir -= Phase.mc.gameSettings.keyBindRight.isKeyDown() ? 1.0 : 0.0;
            }
            if (Phase.mc.gameSettings.keyBindForward.isKeyDown() || Phase.mc.gameSettings.keyBindLeft.isKeyDown() || Phase.mc.gameSettings.keyBindRight.isKeyDown() || Phase.mc.gameSettings.keyBindBack.isKeyDown()) {
                Phase.mc.player.motionX = xDir * (this.speed.get_value(6.25) / 100.0);
                Phase.mc.player.motionZ = zDir * (this.speed.get_value(6.25) / 100.0);
            }
            Phase.mc.player.motionY = 0.0;
            boolean yes = false;
            if (this.advd.get_value(false)) {
                if (this.last + 50L >= System.currentTimeMillis()) {
                    yes = false;
                } else {
                    this.last = System.currentTimeMillis();
                    yes = true;
                }
            }
            Phase.mc.player.noClip = true;
            if (yes) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Phase.mc.player.posX + Phase.mc.player.motionX, Phase.mc.player.posY + (Phase.mc.player.posY < (this.twobeepvp.get_value(true) ? 1.1 : -0.98) ? this.speed.get_value(6.25) / 100.0 : 0.0) + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? this.speed.get_value(6.25) / 100.0 : 0.0) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? this.speed.get_value(6.25) / 100.0 : 0.0), Phase.mc.player.posZ + Phase.mc.player.motionZ, false));
            }
            if (this.PUP.get_value(true)) {
                Phase.mc.player.noClip = true;
                Phase.mc.player.setLocationAndAngles(Phase.mc.player.posX + Phase.mc.player.motionX, Phase.mc.player.posY, Phase.mc.player.posZ + Phase.mc.player.motionZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch);
            }
            Phase.mc.player.noClip = true;
            if (yes) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Phase.mc.player.posX + Phase.mc.player.motionX, Phase.mc.player.posY - 42069.0, Phase.mc.player.posZ + Phase.mc.player.motionZ, true));
            }
            double dx = 0.0;
            double dy = 0.0;
            double dz = 0.0;
            if (Phase.mc.gameSettings.keyBindSneak.isKeyDown()) {
                dy = -0.0625;
            }
            if (Phase.mc.gameSettings.keyBindJump.isKeyDown()) {
                dy = 0.0625;
            }
            Phase.mc.player.setLocationAndAngles(Phase.mc.player.posX, Phase.mc.player.posY, Phase.mc.player.posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch);
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Phase.mc.player.posX, Phase.mc.player.posY, Phase.mc.player.posZ, false));
        }
        catch (Exception e) {
            this.disable();
        }
    }

    @Override
    protected void enable() {
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }

    @Override
    protected void disable() {
        if (Phase.mc.player != null) {
            Phase.mc.player.noClip = false;
        }
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Phase.mc.player.posX), Math.floor(Phase.mc.player.posY), Math.floor(Phase.mc.player.posZ));
    }
}
