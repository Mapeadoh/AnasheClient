package me.travis.wurstplus.wurstplustwo.hacks.client;


import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import java.awt.Desktop;
import java.net.URI;


public class DiscordInvite extends WurstplusHack {
    public DiscordInvite() {
        super(WurstplusCategory.WURSTPLUS_CLIENT);

        this.name        = "DiscordInvite";
        this.tag         = "Discord";
        this.description = "Discord invite";
    }

    @Override
    public void enable()
    {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://discord.gg/nullname"));
            }
        } catch (Exception e) {e.printStackTrace();}
        toggle();
    }

}
