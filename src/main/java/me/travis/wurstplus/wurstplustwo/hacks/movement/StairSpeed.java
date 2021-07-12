package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class StairSpeed extends WurstplusHack {
    public StairSpeed(){
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Stair Speed";
        this.tag = "StairSpeed";
        this.description = "lasube";
    }

    @Override
    public void update() {
        if (StairSpeed.mc.player.onGround && StairSpeed.mc.player.posY - Math.floor(StairSpeed.mc.player.posY) > 0.0 && StairSpeed.mc.player.moveForward != 0.0f) {
            StairSpeed.mc.player.jump();
        }
    }
}