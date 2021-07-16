package me.travis.wurstplus.wurstplustwo.hacks.movement;

import java.text.DecimalFormat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.travis.mapeadoh.clientstuff.teslaclient.MotionUtils;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class NewStep extends WurstplusHack
{
    WurstplusSetting height;
    WurstplusSetting mode;

    public NewStep() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.height = this.create("Height", "Height", 2.5, 0.5, 2.5);
        this.mode = this.create("Mode", "Modes", "Normal", this.combobox("Vanilla", "Normal"));
        this.name = "NewStep";
        this.tag = "NewStep";
        this.description = "pasted from tesla jijijiji";
    }

    @Override
    public void update() {
        if (NewStep.mc.world == null || NewStep.mc.player == null || NewStep.mc.player.isInWater() || NewStep.mc.player.isInLava() || NewStep.mc.player.isOnLadder() || NewStep.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (this.mode.in("Normal")) {
            if (NewStep.mc.player != null && NewStep.mc.player.onGround && !NewStep.mc.player.isInWater() && !NewStep.mc.player.isOnLadder()) {
                for (double y = 0.0; y < this.height.get_value(1) + 0.5; y += 0.01) {
                    if (!NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                        NewStep.mc.player.motionY = -10.0;
                        break;
                    }
                }
            }
            final double[] dir = MotionUtils.forward(0.1);
            boolean twofive = false;
            boolean two = false;
            boolean onefive = false;
            boolean one = false;
            if (NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 2.6, dir[1])).isEmpty() && !NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 2.4, dir[1])).isEmpty()) {
                twofive = true;
            }
            if (NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 2.1, dir[1])).isEmpty() && !NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 1.9, dir[1])).isEmpty()) {
                two = true;
            }
            if (NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 1.6, dir[1])).isEmpty() && !NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 1.4, dir[1])).isEmpty()) {
                onefive = true;
            }
            if (NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 1.0, dir[1])).isEmpty() && !NewStep.mc.world.getCollisionBoxes((Entity) NewStep.mc.player, NewStep.mc.player.getEntityBoundingBox().offset(dir[0], 0.6, dir[1])).isEmpty()) {
                one = true;
            }
            if (NewStep.mc.player.collidedHorizontally && (NewStep.mc.player.moveForward != 0.0f || NewStep.mc.player.moveStrafing != 0.0f) && NewStep.mc.player.onGround) {
                if (one && this.height.get_value(1) >= 1.0) {
                    final double[] oneOffset = { 0.42, 0.753 };
                    for (int i = 0; i < oneOffset.length; ++i) {
                        NewStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(NewStep.mc.player.posX, NewStep.mc.player.posY + oneOffset[i], NewStep.mc.player.posZ, NewStep.mc.player.onGround));
                    }
                    NewStep.mc.player.setPosition(NewStep.mc.player.posX, NewStep.mc.player.posY + 1.0, NewStep.mc.player.posZ);
                }
                if (onefive && this.height.get_value(1) >= 1.5) {
                    final double[] oneFiveOffset = { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
                    for (int i = 0; i < oneFiveOffset.length; ++i) {
                        NewStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(NewStep.mc.player.posX, NewStep.mc.player.posY + oneFiveOffset[i], NewStep.mc.player.posZ, NewStep.mc.player.onGround));
                    }
                    NewStep.mc.player.setPosition(NewStep.mc.player.posX, NewStep.mc.player.posY + 1.5, NewStep.mc.player.posZ);
                }
                if (two && this.height.get_value(1) >= 2.0) {
                    final double[] twoOffset = { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
                    for (int i = 0; i < twoOffset.length; ++i) {
                        NewStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(NewStep.mc.player.posX, NewStep.mc.player.posY + twoOffset[i], NewStep.mc.player.posZ, NewStep.mc.player.onGround));
                    }
                    NewStep.mc.player.setPosition(NewStep.mc.player.posX, NewStep.mc.player.posY + 2.0, NewStep.mc.player.posZ);
                }
                if (twofive && this.height.get_value(1) >= 2.5) {
                    final double[] twoFiveOffset = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                    for (int i = 0; i < twoFiveOffset.length; ++i) {
                        NewStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(NewStep.mc.player.posX, NewStep.mc.player.posY + twoFiveOffset[i], NewStep.mc.player.posZ, NewStep.mc.player.onGround));
                    }
                    NewStep.mc.player.setPosition(NewStep.mc.player.posX, NewStep.mc.player.posY + 2.5, NewStep.mc.player.posZ);
                }
            }
        }
        if (this.mode.in("Vanilla")) {
            final DecimalFormat df = new DecimalFormat("#");
            NewStep.mc.player.stepHeight = Float.parseFloat(df.format(this.height.get_value(1)));
        }
    }
}
