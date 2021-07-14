package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class SalHackAutoReconect extends WurstplusHack {
    public static SalHackAutoReconect INSTANCE;
    public SalHackAutoReconect(){
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "AutoReconect";
        this.tag = "AutoReconect";
        this.description = "";
        INSTANCE = this;
    }
    public WurstplusSetting delay = create("Delay", "Delay", 5.0f, 0.0f, 20.0f);
}