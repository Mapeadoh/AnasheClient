package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.mapeadoh.clientstuff.phobos.MoveEvent;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NewStep extends WurstplusHack {
    WurstplusSetting vanilla = create("Vanilla", "Vanilla", true);
    WurstplusSetting stepHeight = create("StepHeght", "StepHeight", 2, 1, 2);
    WurstplusSetting turnOff = create("Disable", "Disable", false);
    /*private static Step instance;
    public Setting<Boolean> vanilla = this.register(new Setting<Boolean>("Vanilla", false));
    public Setting<Integer> stepHeight = this.register(new Setting<Integer>("Height", 2, 1, 2));
    public Setting<Boolean> turnOff = this.register(new Setting<Boolean>("Disable", false));*/

    public NewStep() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "PhobosStep";
        this.tag = "NewStep";
        this.description = "step from phobos";
    }


    @SubscribeEvent
    public void onStep(MoveEvent.StepEvent event) {
        if (mc.player.onGround && !mc.player.isInsideOfMaterial(Material.WATER) && !mc.player.isInsideOfMaterial(Material.LAVA) && mc.player.collidedVertically && mc.player.fallDistance == 0.0f && !mc.gameSettings.keyBindJump.pressed && !mc.player.isOnLadder()) {
            event.setHeight(this.stepHeight.get_value(1));
            double rheight = mc.player.getEntityBoundingBox().minY - mc.player.posY;
            if (rheight >= 0.625) {
                if (!this.vanilla.get_value(true)) {
                    this.ncpStep(rheight);
                }
                if (this.turnOff.get_value(true)) {
                    this.disable();
                }
            }
        } else {
            event.setHeight(0.6f);
        }
    }

    private void ncpStep(double height) {
        block12:
        {
            double y;
            double posZ;
            double posX;
            block11:
            {
                posX = mc.player.posX;
                posZ = mc.player.posZ;
                y = mc.player.posY;
                if (!(height < 1.1)) break block11;
                double first = 0.42;
                double second = 0.75;
                if (height != 1.0) {
                    first *= height;
                    second *= height;
                    if (first > 0.425) {
                        first = 0.425;
                    }
                    if (second > 0.78) {
                        second = 0.78;
                    }
                    if (second < 0.49) {
                        second = 0.49;
                    }
                }
                mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + first, posZ, false));
                if (!(y + second < y + height)) break block12;
                mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + second, posZ, false));
                break block12;
            }
            if (height < 1.6) {
                double[] offset;
                for (double off : offset = new double[]{0.42, 0.33, 0.24, 0.083, -0.078}) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y += off, posZ, false));
                }
            } else if (height < 2.1) {
                double[] heights;
                for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869}) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            } else {
                double[] heights;
                for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907}) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            }
        }
    }
}