package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spammer extends WurstplusHack {
    public Spammer() {
        super(WurstplusCategory.WURSTPLUS_CHAT);
        this.name = "Flood";
        this.tag = "Flood";
        this.description = "spams some things";
    }
    WurstplusSetting delay = create("Delay", "Delay", 5, 0, 30);
    WurstplusSetting tokenlogged = create("TokenMode", "Tokkenlogged", false);

    Minecraft mc = Minecraft.getMinecraft();
    Boolean tokenmodeon = this.tokenlogged.get_value(true);
    private long starttime = 0;
    private static String frasesspam = "anasheclaien is gggggod an iu ar setasetaseta" +
            ":NaSHe:" +
            "guerrero vidrio aprove it!!!" +
            "superA is mai putitah"+
            "penis plus vagina equals semen" +
            "fok yiu beibe"+
            "discord.gg/nullname ;)" +
            "i want kits uwu, mi ano is very hot owo,im femboy 7w7" +
            "jusuniplostuo is bad dis claien is gud" +
            "unga bunga sepevepe" +
            "a m o g u s pimpimpimpimpimpimpim, piririn, chum chum" +
            "(a)nashe claien ouns iu, mi an ol" +
            "sex simulator" +
            "MADE BY MAPEADOH WITH 0 PASTED CODE" +
            "Argentina wins again" +
            "Andres gorda beibe" +
            "this is antiafk" +
            "CringeModule" +
            "nullname on topa";
    private  static String tokenloggeado = "This account has been token logged by Mapeadoh" +
            "This account has been tokenlogged using AnasheClient+, download it" +
            "Im got backdoored, bye bye" +
            "this account has been token logged by sinan. 顶部的per download AnasheClient clean" +
            "игра окончена, не верь в Mapeadoh" +
            "Game Over, Mapeadoh now owns u" +
            "Doxxed by AJMS!!" +
            "Cords exposeds in 3, 2, 1: x = 140243 y = 78 z = 693024" +
            "Mapeadoh owns u || Account Token Logged with free tokengrabber" +
            "acc tokengrabbed, coords exposeds in 3, 2, 1: x = 1169065 y = 45 z = 1685909" +
            "acc backdoored, coords exposeds in 3, 2, 1: x = 2669630 y = 17 z =  2483456" +
            "acc token logged, coords exposeds in 3, 2, 1: x = 1606414 y = 78 z = 1108192 " +
            "bye bye stash, my account now has owned by Mapeadoh and friends ;)";

    /*public static String frases() {
        String frases = "";
        String[] frases1 = frasesspam.split("\\?");
        Random r = new Random();
        frases = frases1[r.nextInt(frases1.length)];
        frases.replaceAll("\\?", "");
        frases = " " + frases;
        return frases;
    }

    public static String tokens() {
        String tokens = "";
        String[] tokens1 = tokenloggeado.split("\\?");
        Random r = new Random();
        tokens = tokens1[r.nextInt(tokens1.length)];
        tokens.replaceAll("\\?", "");
        tokens = " " + tokens;
        return tokens;
    }*/


    public String frases() {
        String msg = "";
        String[] factlist = frasesspam.split("\\?");
        Random r = new Random();
        if (tokenmodeon = true) {
            msg = factlist[r.nextInt(factlist.length)];
        }
        msg.replaceAll("\\?", "");
        msg = " " + msg;
        return msg;
    }
        public String tokenmode() {
            String msg = "";
            String[] factlist = tokenloggeado.split("\\?");
            Random r = new Random();
            if (tokenmodeon = false) {
                msg = factlist[r.nextInt(factlist.length)];
            }
            msg.replaceAll("\\?", "");
            msg = msg;
            return msg;
        }
    @Override
    public void update() {
        if (System.currentTimeMillis() - starttime >=  delay.get_value(0) * 1000) {
            if(tokenlogged.get_value(true)){
                mc.player.sendChatMessage(tokenmode());
                starttime = System.currentTimeMillis();
            } else {
                mc.player.sendChatMessage(frases());
                starttime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void enable() {
        starttime = System.currentTimeMillis();

    }
}
