package me.travis.wurstplus.wurstplustwo.hacks.chat;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.mapeadoh.clientstuff.cb.Facts;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;

public class FactSpammer extends WurstplusHack {

    public FactSpammer() {
        super(WurstplusCategory.WURSTPLUS_CHAT);
        this.name = "Fact Spammer";
        this.tag = "FactSpammer";
        this.description = "spams snapple facts in chat";
    }
    private long starttime = 0;

    WurstplusSetting delay = create("Delay", "FactDelay", 7f, 7f, 60f);

    @Override
    public void update()
    {
        if (System.currentTimeMillis() - starttime >=  delay.get_value(0) * 1000) {
            mc.player.sendChatMessage(Facts.randomfact());
            starttime = System.currentTimeMillis();
        }
    }

    @Override
    public void enable() {
        starttime = System.currentTimeMillis();

    }
}
