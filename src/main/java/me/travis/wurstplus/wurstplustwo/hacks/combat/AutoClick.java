package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.settings.KeyBinding;

public class AutoClick extends WurstplusHack {
    public AutoClick(){
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "AutoClicker";
        this.tag = "AutoClicker";
        this.description = "only on hold :(";

    }

    private long lastClick;
    private long hold;

    private double speed;
    private double holdLength;

    public void update() {
        if(Mouse.isButtonDown(0)) {
            if(System.currentTimeMillis() - lastClick > speed * 1000) {
                lastClick = System.currentTimeMillis();
                if(hold < lastClick) {
                    hold = lastClick;
                }
                int key = mc.gameSettings.keyBindAttack.getKeyCode();
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
            } else if (System.currentTimeMillis() - hold > holdLength * 1000) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
            }
        }
    }
}