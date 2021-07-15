package me.travis.wurstplus.wurstplustwo.hacks.combat;

import com.google.common.collect.Sets;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import me.travis.mapeadoh.clientstuff.wp3.BlockUtil;
import me.travis.mapeadoh.clientstuff.wp3.EntityUtil;
import me.travis.mapeadoh.clientstuff.wp3.HoleUtil;
import me.travis.mapeadoh.clientstuff.wp3.PlayerUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewHoleFill extends WurstplusHack {
    public NewHoleFill() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Holefill";
        this.tag = "HoleFill";
        this.description = "asd";

    }
    WurstplusSetting blockmode = create("BlockMode","BlockMode", "Obi", combobox("Obi", "EC", "Web", "Skull"));
    WurstplusSetting range = create("Range", "Range", 3, 1, 6);
    WurstplusSetting holesPerSecond = create("HPS", "HolesPerSecond", 3, 1, 6);
    WurstplusSetting fillMode = create("Mode", "Mode", "Normal", combobox("Normal", "Smart", "Auto"));
    WurstplusSetting smartRange = create("Player Range", "AutoRange", 2, 1, 5);
    WurstplusSetting doubleHoles = create("Double Fill","DoubleFill", true);
    WurstplusSetting rotate = create("Rotate", "Rotate", true);
    WurstplusSetting toggle = create("Toggle", "Toggle", false);
    WurstplusSetting swing = create("Swing", "Swing", "Mainhand", combobox("Mainhand", "Offhand", "None"));

    int new_slot = -1;

    @Override
    public void enable() {

        if (mc.player != null) {

            new_slot = find_in_hotbar();

            if (new_slot == -1) {
                WurstplusMessageUtil.send_client_error_message("cannot find required items in hotbar");
                set_active(false);
            }

        }

    }

    @Override
    public void update() {
        List<BlockPos> holes = findHoles();
        BlockPos posToFill = null;
        if (holes.isEmpty() && toggle.get_value(true)) {
            this.disable();
            return;
        }
        for (int i = 0; i < holesPerSecond.get_value(1); i++) {
            double bestDistance = 10;
            for (BlockPos pos : new ArrayList<>(holes)) {
                BlockUtil.ValidResult result = BlockUtil.valid(pos);
                if (result != BlockUtil.ValidResult.Ok) {
                    holes.remove(pos);
                    continue;
                }
                if (!fillMode.in("Normal")) {
                    for (EntityPlayer target : mc.world.playerEntities) {
                        double distance = target.getDistance(pos.getX(), pos.getY(), pos.getZ());
                        if (target == mc.player) continue;
                        if (WurstplusFriendUtil.isFriend(mc.player.getName())) continue;
                        if (distance > (fillMode.in("Auto") ? smartRange.get_value(1) : 5)) continue;
                        if (distance < bestDistance) {
                            posToFill = pos;
                        }
                    }
                } else {
                    posToFill = pos;
                }
            }


            if (posToFill != null) {
                BlockUtil.placeBlock(posToFill, find_in_hotbar(), rotate.get_value(true), rotate.get_value(true), swing);
                holes.remove(posToFill);
            }
        }
    }
    private int find_in_hotbar() {
        for (int ii = 0; ii < 9; ++ii) {
            if (blockmode.in("EC")) {
                final ItemStack stack = mc.player.inventory.getStackInSlot(ii);
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock) stack.getItem()).getBlock();

                    if (block instanceof BlockEnderChest) {
                        return ii;
                    }
                }
            }
            if (blockmode.in("Obi")) {
                final ItemStack stack2 = mc.player.inventory.getStackInSlot(ii);
                if (stack2 != ItemStack.EMPTY && stack2.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock) stack2.getItem()).getBlock();

                    if (block instanceof BlockObsidian) {
                        return ii;
                    }
                }
            }
            if (blockmode.in("Web")) {
                final ItemStack stack = mc.player.inventory.getStackInSlot(ii);
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock) stack.getItem()).getBlock();

                    if (block instanceof BlockWeb) {
                        return ii;
                    }
                }
            }
            if (blockmode.in("Skull")) {
                final ItemStack stack = mc.player.inventory.getStackInSlot(ii);

                if (stack.getItem() == Item.getItemById(397)) {
                    return ii;
                }
            }
        }
        return -1;
    }

    public List<BlockPos> findHoles() {
        int range = this.range.get_value(1);

        HashSet<BlockPos> possibleHoles = Sets.newHashSet();
        List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), range, range, false, true, 0);
        List<BlockPos> holes = new ArrayList<>();

        for (BlockPos pos : blockPosList) {

            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                possibleHoles.add(pos);
            }
        }

        for (BlockPos pos : possibleHoles) {
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType != HoleUtil.HoleType.NONE) {

                AxisAlignedBB centreBlocks = holeInfo.getCentre();

                if (centreBlocks == null)
                    continue;
                if (holeType == HoleUtil.HoleType.DOUBLE && !doubleHoles.get_value(true))
                    continue;

                holes.add(pos);
            }
        }

        return holes;
    }

}