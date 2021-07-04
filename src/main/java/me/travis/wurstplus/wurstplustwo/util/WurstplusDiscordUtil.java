package me.travis.wurstplus.wurstplustwo.util;

import me.travis.wurstplus.Wurstplus;
import net.minecraft.client.Minecraft;

import net.minecraft.client.multiplayer.ServerData;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
public class WurstplusDiscordUtil {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static String details;
    public static String state;
    public static int players;
    public static int maxPlayers;
    public static int players2;
    public static int maxPlayers2;
    public static ServerData svr;
    public static String[] popInfo;

    public static void start() {

        String applicationId = "855201268020674560";
        String steamId = "";

        DiscordRichPresence presence = new DiscordRichPresence();
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        DiscordRPC.INSTANCE.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);

        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "we plashing";
        presence.state = Wurstplus.ANASHECLIENT;
        presence.largeImageKey = "large_circular";
        presence.largeImageText = Wurstplus.ANASHECLIENT;
        presence.smallImageKey = "mapeadoh1";
        presence.smallImageText = "Mapeadoh the dev (skider?XDDD)";
        

        /*
        static void UpdatePresence()
{
    DiscordRichPresence discordPresence;
    memset(&discordPresence, 0, sizeof(discordPresence));
    discordPresence.state = "server: " + svr.serverIP + " | discord.gg/nullname";
    discordPresence.details = "unga bunga cpvp | plashin in a serer, come with me";
    discordPresence.startTimestamp = 1507665886;
    discordPresence.endTimestamp = 1507665886;
    discordPresence.largeImageKey = "large_circular";
    discordPresence.largeImageText = "AnasheClient+Beta0.5";
    discordPresence.smallImageKey = "mapeadoh";
    discordPresence.smallImageText = "Mapeadoh the dev (skider?XDDD)";
    Discord_UpdatePresence(&discordPresence);
}
        */

        new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    details = "zzzz";
                    state = "zzzzz";
                    players = 0;
                    maxPlayers = 0;
                    if (mc.isIntegratedServerRunning()) {
                        details = mc.player.getName() +" in single";
                        state = "new beta incoming pog????";
                    }
                    else if (mc.getCurrentServerData() != null) {
                        svr = mc.getCurrentServerData();
                        if (!svr.serverIP.equals("")) {
                            details = mc.player.getName() + " plashin in";
                            state = svr.serverIP;
                            if (svr.populationInfo != null) {
                                popInfo = svr.populationInfo.split("/");
                                if (popInfo.length > 2) {
                                    players2 = Integer.parseInt(popInfo[0]);
                                    maxPlayers2 = Integer.parseInt(popInfo[1]);
                                }
                            }
                        }
                    }
                    else {
                        details = mc.player.getName() +" in menuzzz";
                        state = "afk/waiting/fapping/etc";
                    }
                    if (!details.equals(presence.details) || !state.equals(presence.state)) {
                    //    presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    presence.details = details;
                    presence.state = state;
                    DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
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
        }, "RPC-Callback-Handler").start();
    }

    public static void stop() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
        DiscordRPC.INSTANCE.Discord_ClearPresence();
    }

}