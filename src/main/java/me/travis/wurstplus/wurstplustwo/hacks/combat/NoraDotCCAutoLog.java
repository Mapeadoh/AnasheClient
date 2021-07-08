package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.client.gui.GuiMainMenu;

public class NoraDotCCAutoLog extends WurstplusHack {
    public NoraDotCCAutoLog(){
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "AutoLogout";
        this.tag = "AutoLogout";
        this.description = "AntiClip";
    }

    public WurstplusSetting health = create ("Health", "Health", 10, 1, 20);

    public void update() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.getHealth() <= health.get_value(1)) {
            set_active(false);
            mc.world.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}