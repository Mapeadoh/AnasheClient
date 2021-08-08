package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;

public class MoonWalk
        extends WurstplusHack {
    public WurstplusSetting poop = this.create("Slipperiness", "Poop", 69.0, 6.9, 420.0);

    public MoonWalk() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "MoonWalk";
        this.tag = "MoonWalk";
        this.description = "Old RusherHack Modules";
    }

    @Override
    public void update() {
        if (MoonWalk.mc.player.onGround && MoonWalk.mc.gameSettings.keyBindJump.isPressed()) {
            MoonWalk.mc.player.motionY = 0.25;
        } else if (MoonWalk.mc.player.isAirBorne && !MoonWalk.mc.player.isInWater() && !MoonWalk.mc.player.isOnLadder() && !MoonWalk.mc.player.isInsideOfMaterial(Material.LAVA)) {
            MoonWalk.mc.player.motionY = 1.0E-6;
            EntityPlayerSP player = MoonWalk.mc.player;
            player.jumpMovementFactor *= 1.21337f;
        }
    }
}
