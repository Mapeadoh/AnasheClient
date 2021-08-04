package me.travis.wurstplus.wurstplustwo.hacks.misc;


import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.util.math.MathHelper;

public class YawLock extends WurstplusHack {
    public YawLock(){
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "YawLock";
        this.tag = "YawLock";
        this.description = "watafac w+1 era mejor q w+2";
    }

    WurstplusSetting direction= create("Direction", "Direction", "North", combobox("North", "South", "West", "East"));

    @Override
    public void update() {
        if (direction.in("North")) {
            mc.player.rotationYaw = MathHelper.clamp(180, -180, 180);
        }
        else if (direction.in("South")) {
            mc.player.rotationYaw = MathHelper.clamp(0, -180, 180);
        }
        else if (direction.in("West")) {
            mc.player.rotationYaw = MathHelper.clamp(90, -180, 180);
        }
        else if (direction.in("East")) {
            mc.player.rotationYaw = -90;
        }
    }
}