package me.travis.wurstplus.wurstplustwo.hacks.client;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.AnasheRPC;

public class DiscordRPC extends WurstplusHack {
    public DiscordRPC() {
        super(WurstplusCategory.WURSTPLUS_CLIENT);
        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "Discord Rich Presence";
    }
    @Override
    public void enable() {
        AnasheRPC.init();
    }
    @Override
    public void disable() {
        AnasheRPC.shutdown();
    }
}