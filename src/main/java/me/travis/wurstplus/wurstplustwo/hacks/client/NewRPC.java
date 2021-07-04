package me.travis.wurstplus.wurstplustwo.hacks.client;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class NewRPC extends WurstplusHack {
    public static NewRPC INSTANCE;
    public NewRPC() {
        super(WurstplusCategory.WURSTPLUS_CLIENT);
        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "Discord Rich Presence";
        INSTANCE = this;
    }
    WurstplusSetting image = create("AnimatedImg", "AnimatedImg", false);
    WurstplusSetting mode = create("Mode", "Mode", "NullName", combobox("NullName"/*, "Dancin Boy", "Dancin Cat", "Vibing Man"*/));

    @Override
    public void enable() {
        AnasheNewRPC.init();
    }
    @Override
    public void disable() {
        AnasheNewRPC.shutdown();
    }
}