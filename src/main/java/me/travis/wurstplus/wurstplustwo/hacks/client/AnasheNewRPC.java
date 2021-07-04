package me.travis.wurstplus.wurstplustwo.hacks.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import me.travis.wurstplus.Wurstplus;
import net.minecraft.client.Minecraft;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewRPC;

public class AnasheNewRPC
{// from H+2 because sex
    private static final String ClientId = "855201268020674560";
    private static final Minecraft mc;
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static String details;
    private static String state;
    private static int img;
    private static int img2;
    private static int img3;

    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        AnasheNewRPC.rpc.Discord_Initialize("855201268020674560", handlers, true, "");
        AnasheNewRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        AnasheNewRPC.presence.details = "TokenLogged by " + Wurstplus.WURSTPLUS_NAME;
        AnasheNewRPC.presence.state = mc.player.getName() + " on Menu";

        if(NewRPC.INSTANCE.image.get_value(true)) {
            if (NewRPC.INSTANCE.mode.in("NullName")) {
                if (img == 31) {

                    img = 0;
                }
                AnasheNewRPC.presence.largeImageKey = "nullname" + img;
                ++img;
            } else {
                AnasheNewRPC.presence.largeImageKey = "large_circular";
            }
        }

        AnasheNewRPC.presence.largeImageText = Wurstplus.ANASHECLIENT;
        AnasheNewRPC.presence.smallImageKey = "mapeadoh1";
        AnasheNewRPC.presence.smallImageText = "Mapeadoh";
        AnasheNewRPC.rpc.Discord_UpdatePresence(AnasheNewRPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    AnasheNewRPC.rpc.Discord_RunCallbacks();
                    AnasheNewRPC.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                    AnasheNewRPC.state = "";
                    if (AnasheNewRPC.mc.isIntegratedServerRunning()) {
                        AnasheNewRPC.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                        AnasheNewRPC.state ="Learning CPvP on single";
                    }
                    else if (AnasheNewRPC.mc.getCurrentServerData() != null) {
                        if (!AnasheNewRPC.mc.getCurrentServerData().serverIP.equals("")) {
                            AnasheNewRPC.presence.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                            AnasheNewRPC.state ="Ownin: " + AnasheNewRPC.mc.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                        AnasheNewRPC.details = mc.player.getName() + " | " + Wurstplus.ANASHECLIENT;
                        AnasheNewRPC.state = "Being epic at menu";
                    }
                    if (!AnasheNewRPC.details.equals(AnasheNewRPC.presence.details) || !AnasheNewRPC.state.equals(AnasheNewRPC.presence.state)) {
                        //WarriorRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    AnasheNewRPC.presence.details = AnasheNewRPC.details;
                    AnasheNewRPC.presence.state = AnasheNewRPC.state;
                    AnasheNewRPC.rpc.Discord_UpdatePresence(AnasheNewRPC.presence);
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
        AnasheNewRPC.presence = new DiscordRichPresence();
    }
    public static void shutdown() {
        rpc.Discord_Shutdown();
    }
}

