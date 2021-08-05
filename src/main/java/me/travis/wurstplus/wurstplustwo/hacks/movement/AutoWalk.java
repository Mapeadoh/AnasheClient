package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk extends WurstplusHack {
    public AutoWalk(){
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "AutoWalk";
        this.tag = "AutoWalk";
        this.description = "baritone Zzz modulo de 25 lineas GOOOOOOD";
    }
    public void update() {
        if (!(mc.world == null)) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }
}