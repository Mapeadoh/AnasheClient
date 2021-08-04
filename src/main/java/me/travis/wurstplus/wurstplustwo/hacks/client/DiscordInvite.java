package me.travis.wurstplus.wurstplustwo.hacks.client;


import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
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
    WurstplusSetting mode = create("Mode", "Mode", "NullName", combobox("NullName", "AnasheClient", "iJese"));

    @Override
    public void enable() {
        if (mode.in("NullName")) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://discord.gg/friv"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            toggle();
        }
        if (mode.in("AnasheClient")) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://discord.gg/friv"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            toggle();
        }
        if (mode.in("iJese yt bc gae")) {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/channel/UCC1u-p7XZA8X7-ykrTM1TPA"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            toggle();
        }
    }
}
