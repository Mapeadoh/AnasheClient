package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk
        extends WurstplusHack {
    public AutoWalk() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Auto Walk";
        this.tag = "AutoWalk";
        this.description = "automatically walks";
    }

    @Override
    public void update() {
        if (!nullCheck()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }
}