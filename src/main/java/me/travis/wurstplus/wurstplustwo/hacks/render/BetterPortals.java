package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.travis.mapeadoh.clientstuff.phobos.PacketEvent;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;

public class BetterPortals extends WurstplusHack {

    private static BetterPortals INSTANCE = new BetterPortals();
    public WurstplusSetting portalChat = create ("Chat", "Allow chat in Portal",true);
    public WurstplusSetting godmode = create ("Godmode", "Portal GodMode", false);
    public WurstplusSetting fastPortal = create ("FastPortal","FastPortal", false);
    public WurstplusSetting cooldown = create ("Cooldown", "Cooldown", 5, 1, 10);
    public WurstplusSetting time = create ("Time", "Time", 5, 0, 80);

    public BetterPortals(){
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "BetterPortals";
        this.tag = "BetterPortals";
        this.description = "BetterPortals";
    }

    public static BetterPortals getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BetterPortals();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public String getDisplayInfo() {
        if (this.godmode.get_value(true)) {
            return "Godmode";
        }
        return null;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.godmode.get_value(true) && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}