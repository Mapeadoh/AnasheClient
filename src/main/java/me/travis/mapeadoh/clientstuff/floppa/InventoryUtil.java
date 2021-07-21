package me.travis.mapeadoh.clientstuff.floppa;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;

public class InventoryUtil
{
    public static final Minecraft mc;

    public static int findSlotFullInventory(final Class clazz) {
        for (int i = 0; i < 45; ++i) {
            final ItemStack item = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (item != ItemStack.EMPTY) {
                if (clazz.isInstance(item.getItem())) {
                    return i;
                }
                if (item.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)item.getItem()).getBlock();
                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static int findSlotHotbar(final Class clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (item != ItemStack.EMPTY) {
                if (clazz.isInstance(item.getItem())) {
                    return i;
                }
                if (item.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)item.getItem()).getBlock();
                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static void switchToSlot(final int slot) {
        InventoryUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        InventoryUtil.mc.player.inventory.currentItem = slot;
        InventoryUtil.mc.playerController.updateController();
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}
