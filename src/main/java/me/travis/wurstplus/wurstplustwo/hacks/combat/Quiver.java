//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Quiver extends WurstplusHack {
    WurstplusSetting speed = this.create("Speed", "QuiverSpeed", true);
    WurstplusSetting strength = this.create("Strength", "QuiverStrength", true);
    int randomVariation;
    private ChatFormatting r = ChatFormatting.RESET;
    private ChatFormatting b = ChatFormatting.BOLD;

    public Quiver() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "Quiver";
        this.tag = "Quiver";
        this.description = "fixed bc this module is shit";
    }
    public void enable(){
        if(find_bow_hotbar() == -1){
            WurstplusMessageUtil.send_client_error_message( "" + ChatFormatting.DARK_RED + b + "Quiver" + r + ChatFormatting.GRAY + "> " + r + "Error, bow not found");
        }
    }

    public void update() {
        PotionEffect speedEffect = mc.player.getActivePotionEffect(Potion.getPotionById(1));
        PotionEffect strengthEffect = mc.player.getActivePotionEffect(Potion.getPotionById(5));
        boolean hasSpeed = speedEffect != null;
        boolean hasStrength = strengthEffect != null;
        if (mc.player.inventory.currentItem == this.find_bow_hotbar()) {
            mc.player.connection.sendPacket(new Rotation(0.0F, -90.0F, true));
        }

        if (this.strength.get_value(true) && !hasStrength && mc.player.inventory.getCurrentItem().getItem() == Items.BOW && this.isArrowInInventory("Arrow of Strength")) {
            if (mc.player.getItemInUseMaxCount() >= this.getBowCharge()) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.stopActiveHand();
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.setActiveHand(EnumHand.MAIN_HAND);
            } else if (mc.player.getItemInUseMaxCount() == 0) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.setActiveHand(EnumHand.MAIN_HAND);
            }
        }

        if (this.speed.get_value(true) && !hasSpeed && mc.player.inventory.getCurrentItem().getItem() == Items.BOW && this.isArrowInInventory("Arrow of Speed")) {
            if (mc.player.getItemInUseMaxCount() >= this.getBowCharge()) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.stopActiveHand();
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.setActiveHand(EnumHand.MAIN_HAND);
            } else if (mc.player.getItemInUseMaxCount() == 0) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.setActiveHand(EnumHand.MAIN_HAND);
            }
        }

    }

    private int find_bow_hotbar() {
        for(int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BOW) {
                return i;
            }
        }

        return -1;
    }

    private boolean isArrowInInventory(String type) {
        boolean inInv = false;

        for(int i = 0; i < 36; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW && itemStack.getDisplayName().equalsIgnoreCase(type)) {
                inInv = true;
                this.switchArrow(i);
                break;
            }
            else {
                WurstplusMessageUtil.send_client_error_message( "" + ChatFormatting.DARK_RED + b + "Quiver" + r + ChatFormatting.GRAY + "> " + r + "Error, arrows not found");
            }
        }

        return inInv;
    }

    private void switchArrow(int oldSlot) {
        WurstplusMessageUtil.send_client_message("Switching arrows!");
        int bowSlot = mc.player.inventory.currentItem;
        int placeSlot = bowSlot + 1;
        if (placeSlot > 8) {
            placeSlot = 1;
        }

        if (placeSlot != oldSlot) {
            if (mc.currentScreen instanceof GuiContainer) {
                return;
            }

            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, placeSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
        }

    }

    private int getBowCharge() {
        if (this.randomVariation == 0) {
            this.randomVariation = 1;
        }

        return 1 + this.randomVariation;
    }
}
