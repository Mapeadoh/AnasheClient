package me.travis.wurstplus.wurstplustwo.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusPlayerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;


public class NewOffhand extends WurstplusHack {
    public static NewOffhand INSTANCE;

    public NewOffhand() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "NewOffhand";
        this.tag = "NewOffhand";
        this.description = "offhand test";
        INSTANCE = this;
    }

    WurstplusSetting info = create("info", "Info", "InTest");
    public WurstplusSetting switch_mode = create("Offhand", "OffhandOffhand", "Totem", combobox("Totem", "Crystal", "Gapple", "None"));
    WurstplusSetting totem_switch = create("Totem HP", "OffhandTotemHP", 16, 0, 36);
    //another settings
    WurstplusSetting gapple_in_hole = create("Gapple In Hole", "OffhandGapple", false);
    WurstplusSetting gapple_hole_hp = create("Gapple Hole HP", "OffhandGappleHP", 8, 0, 36);
     WurstplusSetting sword_gapple = create("Gapple in Sword", "SwordGapple", false);
    WurstplusSetting sword_gapple_hp = create("Sword Gapple HP", "SwordGappleHP", 20, 0, 36);
    //WurstplusSetting totemonelytra = create("ElytraTotem", "ElytraTotem", true);

    //idk
   // WurstplusSetting debug = create("Debug", "Debug", false);
    WurstplusSetting nocreativebugs = create("NoCreativeMode", "NoCreat", true);

    WurstplusSetting delay = create("Delay", "OffhandDelay", false);

    private boolean switching = false;
    private int last_slot;
    String iteminoffhand;

    private ChatFormatting r = ChatFormatting.RESET;
    private ChatFormatting da = ChatFormatting.DARK_AQUA;
    private ChatFormatting g = ChatFormatting.GRAY;
    private ChatFormatting go = ChatFormatting.GOLD;

    @Override
    public void update() {
        AnasheClient.get_module_manager().get_module_with_tag("Offhand").set_disable();
        if(nocreativebugs.get_value(true)){
            if (mc.player.isCreative()) {
                this.toggle();
            }
        }

        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {

            if (switching) {
                swap_items(last_slot, 2);
                    return;
            }

            float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();

            if (hp > totem_switch.get_value(1)) {
                if (switch_mode.in("Crystal") && AnasheClient.get_hack_manager().get_module_with_tag("NewAutoCrystal").is_active()) {
                    swap_items(get_item_slot(Items.END_CRYSTAL),0);
                    return;
                }
                if (gapple_in_hole.get_value(true) && hp > gapple_hole_hp.get_value(1) && is_in_hole()) {
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), delay.get_value(true) ? 1 : 0);
                    return;
                }
                if (sword_gapple.get_value(true) && hp > sword_gapple_hp.get_value(1) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD){
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), delay.get_value(true) ? 1 : 0);
                    return;
                }
                if (switch_mode.in("Totem")) {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
                    return;
                }
                if (switch_mode.in("Gapple")) {
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), delay.get_value(true) ? 1 : 0);
                    return;
                }
                if (switch_mode.in("Crystal") && !AnasheClient.get_hack_manager().get_module_with_tag("NewAutoCrystal").is_active()) {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING),0);
                    return;
                }
            } else {
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
                return;
            }

            if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
            }

        }

    }

    public void swap_items(int slot, int step) {
        if (slot == -1) return;
        if (step == 0) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        }
        if (step == 1) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = true;
            last_slot = slot;
        }
        if (step == 2) {
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            String offhanditem = String.valueOf(mc.player.getHeldItemOffhand().getItem());
            switch (offhanditem) {
                case "Items.END_CRYSTAL":
                    iteminoffhand = "Ender Crystal";
                    return;
                case "Items.GOLDEN_APPLE":
                    iteminoffhand = "Gapple";
                    return;
                case "Items.TOTEM_OF_UNDYING":
                    iteminoffhand = "Totem";
                    return;
            }
            WurstplusMessageUtil.send_client_message(g + "[" + r + da + "OffHandModule" + r + g + "]" + r + " Now switching to: " + go + iteminoffhand);
            switching = false;
        }

        mc.playerController.updateController();
    }

    private boolean is_in_hole() {

        BlockPos player_block = WurstplusPlayerUtil.GetLocalPlayerPosFloored();

        return mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.west()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.north()).getBlock() != Blocks.AIR
                && mc.world.getBlockState(player_block.south()).getBlock() != Blocks.AIR ||
                mc.world.getBlockState(player_block.east()).getBlock() != Blocks.WATER
                && mc.world.getBlockState(player_block.west()).getBlock() != Blocks.WATER
                && mc.world.getBlockState(player_block.north()).getBlock() != Blocks.WATER
                && mc.world.getBlockState(player_block.south()).getBlock() != Blocks.WATER;
    }


    private int get_item_slot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for (int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                if (i < 9) {
                    if (input == Items.GOLDEN_APPLE) {
                        return -1;
                    }
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }
}