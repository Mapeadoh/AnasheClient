package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import me.travis.wurstplus.wurstplustwo.hacks.client.DiscordRPCModule;

public class AnasheRPC { // from phobos why not
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static Thread thread;
    private static int index;
    private static Minecraft mc = Minecraft.getMinecraft();

    static {
        index = 1;
        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
    }

    public static void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("855201268020674560", handlers, true, "");
        AnasheRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        AnasheRPC.presence.details = mc.player.getName() + " ft. " + DiscordRPCModule.INSTANCE.mode.get_current_value();
        AnasheRPC.presence.state = mc.currentScreen instanceof GuiMainMenu ? "In Menu." : " Now: Owning " + (mc.getCurrentServerData() != null ? (DiscordRPCModule.INSTANCE.showIP.get_value(true) ? " on " + mc.getCurrentServerData().serverIP + "." : " multiplasher") : " on single");
        //default img
        AnasheRPC.presence.largeImageKey = "large_circular";
        AnasheRPC.presence.largeImageText = DiscordRPCModule.INSTANCE.mode.get_current_value();
        AnasheRPC.presence.smallImageKey = "mapeadoh1";
        AnasheRPC.presence.smallImageText = "Mapeadoh";
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                AnasheRPC.presence.details = mc.player.getName() + " ft." + DiscordRPCModule.INSTANCE.mode.get_current_value();
                AnasheRPC.presence.state = mc.currentScreen instanceof GuiMainMenu ? "In Menu." : " Now: Owning " + (mc.getCurrentServerData() != null ? (DiscordRPCModule.INSTANCE.showIP.get_value(true) ? " on " + mc.getCurrentServerData().serverIP + "." : " multiplasher") : " on single");
                // testing this, new gifs can be added
                if (DiscordRPCModule.INSTANCE.catMode.get_value(true)) {
                    if (index == 31) {
                        index = 0;
                    }
                    AnasheRPC.presence.largeImageKey = "nullname" + index;
                    ++index;
                }
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException interruptedException) {
                }
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }
}