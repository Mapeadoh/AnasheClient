package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketDisconnect;

public class AutoLog extends WurstplusHack {
    public AutoLog(){
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "AutoLogout";
        this.tag = "AutoLogout";
        this.description = "AntiClip";
    }
    WurstplusSetting healthmode = create("HealthMode", "HealthMode", true);
    public WurstplusSetting health = create ("Health", "Health", 10, 1, 20);
    public WurstplusSetting totems = create("NoTotem", "NoTotems", false);

    public void update() {
        if (mc.player == null || mc.world == null) return;
        if(healthmode.get_value(true)){
            totems.set_value(false);
            if (mc.player.getHealth() <= health.get_value(1)) {
                this.toggle();
                mc.getConnection().handleDisconnect(new SPacketDisconnect());
            }
        }
        if(totems.get_value(true)){
            healthmode.set_value(false);
            if (getItemCount(Items.TOTEM_OF_UNDYING) == 0){
                mc.getConnection().handleDisconnect(new SPacketDisconnect());
                this.toggle();
            }
        }
    }

    public static int getItemCount(Item item) {
        int count = 0;
        for (int i = 44; i >= 0; --i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == item) {
            count += mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getCount();
        }

        return count;
    }
}