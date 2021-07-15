package me.travis.wurstplus.wurstplustwo.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEnemyUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class ExtraTab extends WurstplusHack {
    public ExtraTab(){
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "ExtraTab";
        this.tag = "ExtraTab";
        this.description = "";

    }
    public WurstplusSetting count = create ("Count", "Count",250, 0, 1000 );

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String name = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (WurstplusFriendUtil.isFriend(name)) {
            return ChatFormatting.AQUA + name;
        }
        if (WurstplusEnemyUtil.isEnemy(name)) {
            return ChatFormatting.RED + name;
        }
        return name;
    }

    @Override
    public void enable(){
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }

    @Override
    public void disable(){
        WurstplusEventBus.EVENT_BUS.unsubscribe(this);
    }
}