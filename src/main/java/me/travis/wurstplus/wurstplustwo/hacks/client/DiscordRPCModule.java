package me.travis.wurstplus.wurstplustwo.hacks.client;

import me.travis.wurstplus.AnasheRPC;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class DiscordRPCModule extends WurstplusHack {
    public static DiscordRPCModule INSTANCE;
    public DiscordRPCModule() {
        super(WurstplusCategory.WURSTPLUS_CLIENT);
        this.name = "DiscordNewRPC";
        this.tag = "DiscordRPCModule";
        this.description = "errpisi";
        INSTANCE = this;
    }

    public WurstplusSetting catMode = create("Anim", "Anim", false);
    public WurstplusSetting showIP = create("ShowIP","ShowIP", true);
    public WurstplusSetting smallft = create("SmallIMG", "SmallImg", true);
    public WurstplusSetting mode = create("Mode", "Mode", "AnasheClient", combobox("AnasheClient", "NullWare+", "AnasheClient+ B0.9", "NullNameBackdoor", "W+2Better", "AnotherRatedClient", "OyVeyTokenLogger"));

    @Override
    public void enable() {
        AnasheRPC.start();
    }

    @Override
    public void disable() {
        AnasheRPC.stop();
    }
}
