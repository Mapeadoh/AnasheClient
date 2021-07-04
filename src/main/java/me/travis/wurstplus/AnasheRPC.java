package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;

public class AnasheRPC
{// from H+2 because sex
    private static final String ClientId = "855201268020674560";
    private static final Minecraft mc;
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static String details;
    private static String state;

    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        AnasheRPC.rpc.Discord_Initialize("855201268020674560", handlers, true, "");
        AnasheRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        AnasheRPC.presence.details = "TokenLogged by " + Wurstplus.WURSTPLUS_NAME;
        AnasheRPC.presence.state = mc.player.getName() + " on Menu";
        AnasheRPC.presence.largeImageKey = "large_circular";
        AnasheRPC.presence.largeImageText = Wurstplus.ANASHECLIENT;
        AnasheRPC.presence.smallImageKey = "mapeadoh1";
        AnasheRPC.presence.smallImageText = "Mapeadoh";
        AnasheRPC.rpc.Discord_UpdatePresence(AnasheRPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    AnasheRPC.rpc.Discord_RunCallbacks();
                    AnasheRPC.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                    AnasheRPC.state = "";
                    if (AnasheRPC.mc.isIntegratedServerRunning()) {
                        AnasheRPC.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                        AnasheRPC.state ="Learning CPvP on single";
                    }
                    else if (AnasheRPC.mc.getCurrentServerData() != null) {
                        if (!AnasheRPC.mc.getCurrentServerData().serverIP.equals("")) {
                            AnasheRPC.presence.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                            AnasheRPC.state ="Ownin: " + AnasheRPC.mc.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                        AnasheRPC.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                        AnasheRPC.state = "Being epic at menu";
                    }
                    if (!AnasheRPC.details.equals(AnasheRPC.presence.details) || !AnasheRPC.state.equals(AnasheRPC.presence.state)) {
                        //WarriorRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    AnasheRPC.presence.details = AnasheRPC.details;
                    AnasheRPC.presence.state = AnasheRPC.state;
                    AnasheRPC.rpc.Discord_UpdatePresence(AnasheRPC.presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }

    static {
        mc = Minecraft.getMinecraft();
        rpc = DiscordRPC.INSTANCE;
        AnasheRPC.presence = new DiscordRichPresence();
    }
    public static void shutdown() {
        rpc.Discord_Shutdown();
    }
}

