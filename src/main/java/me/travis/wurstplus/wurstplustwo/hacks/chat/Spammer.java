package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spammer extends WurstplusHack {
    public Spammer() {
        super(WurstplusCategory.WURSTPLUS_CHAT);
        this.name = "Flood";
        this.tag = "Flood";
        this.description = "cringe module but idk";
    }
    WurstplusSetting delay = create("Delay", "Delay", 5, 0, 30);
    WurstplusSetting tokenlogged = create("TokenMode", "Tokkenlogged", false);

    List<String> spam = new ArrayList<>();
    List<String> tokenmode = new ArrayList<>();

    Random random = new Random();
    int tickDelay;

    public static String customMsgArg = "";
    public static void setMessage(String msg) {
        customMsgArg = msg;
    }

    public void enable() {
        spam.clear();
        tokenmode.clear();

        tickDelay = 0;

        spam.add("anasheclaien is gggggod an iu ar setasetaseta");
        spam.add(":NaSHe:");
        spam.add("penis plus vagina equals semen");
        spam.add("i want kits uwu, mi ano is very hot owo,im femboy 7w7");
        spam.add("SKID MADE BY MAPEADOH WITH 0 PASTED CODE");
        spam.add("why ar u lookin at dis");
        spam.add("average w+2 skid");
        spam.add("this is antiafk");
        spam.add("anasheclient, the best OyVey tokenlogger");

        tokenmode.add("This account has been token logged by Mapeadoh");
        tokenmode.add("This account has been tokenlogged using AnasheClient+, download it");
        tokenmode.add("this account has been token logged by sinan. 顶部的per download AnasheClient clean");
        tokenmode.add("Mapeadoh owns u || Account Token Logged with free and deobf tokengrabber");
        tokenmode.add("i just got new postage from my local post office thanks to postman!");
        tokenmode.add("luscius tenia razon");
        tokenmode.add("travis scott discord tokenlogger + w+2 = AnasheClient");
        tokenmode.add("Doxxed by AJMS!!");
        tokenmode.add("bye bye stash, im using AnasheClient D:");

    }

    public void disable() {
        spam.clear();
        tokenmode.clear();
    }

    @Override
    public void update() {
        if(tokenlogged.get_value(false)) {
            tickDelay++;
            if (tickDelay < delay.get_value(1) * 10) return;

            String message = spam.get(random.nextInt(spam.size()));

            mc.player.sendChatMessage(message);
            tickDelay = 0;
        }
        if(tokenlogged.get_value(true)) {
            tickDelay++;
            if (tickDelay < delay.get_value(1) * 10) return;

            String message = tokenmode.get(random.nextInt(tokenmode.size()));

            mc.player.sendChatMessage(message);

            tickDelay = 0;
        }
    }
}
