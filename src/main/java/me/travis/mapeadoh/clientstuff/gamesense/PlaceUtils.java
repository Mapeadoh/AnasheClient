
package me.travis.mapeadoh.clientstuff.gamesense;

import java.util.ArrayList;

import me.travis.mapeadoh.clientstuff.wp3.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class PlaceUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int placementConnections = 0;
    private static boolean isSneaking = false;

    public static void onEnable() {
        ++placementConnections;
    }

    public static void onDisable() {
        if (--placementConnections == 0 && isSneaking) {
            PlaceUtils.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity) PlaceUtils.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }
    }
    public static boolean placeItem(BlockPos blockPos, EnumHand hand, boolean rotate, Class<? extends Item> itemToPlace) {
        int oldSlot = PlaceUtils.mc.player.inventory.currentItem;
        int newSlot = InventoryUtil.findFirstItemSlot(itemToPlace, 0, 8);
        if (newSlot == -1) {
            return false;
        }
        PlaceUtils.mc.player.inventory.currentItem = newSlot;
        boolean output = PlaceUtils.place(blockPos, hand, rotate);
        PlaceUtils.mc.player.inventory.currentItem = oldSlot;
        return output;
    }

    public static boolean place(BlockPos blockPos, EnumHand hand, boolean rotate) {
        return PlaceUtils.placeBlock(blockPos, hand, rotate, true, null);
    }

    public static boolean place(BlockPos blockPos, EnumHand hand, boolean rotate, ArrayList<EnumFacing> forceSide) {
        return PlaceUtils.placeBlock(blockPos, hand, rotate, true, forceSide);
    }

    public static boolean place(BlockPos blockPos, EnumHand hand, boolean rotate, boolean checkAction) {
        return PlaceUtils.placeBlock(blockPos, hand, rotate, checkAction, null);
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand hand, boolean rotate, boolean checkAction, ArrayList<EnumFacing> forceSide) {
        EnumFacing side;
        EntityPlayerSP player = PlaceUtils.mc.player;
        WorldClient world = PlaceUtils.mc.world;
        PlayerControllerMP playerController = PlaceUtils.mc.playerController;
        if (player == null || world == null || playerController == null) {
            return false;
        }
        if (!world.getBlockState(blockPos).getMaterial().isReplaceable()) {
            return false;
        }
        EnumFacing enumFacing = side = forceSide != null ? BlockUtil.getPlaceableSideExlude(blockPos, forceSide) : BlockUtil.getPlaceableSide(blockPos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = blockPos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = world.getBlockState(neighbour).getBlock();
        if (!isSneaking && BlockUtil.blackList.contains((Object)neighbourBlock) || BlockUtil.shulkerList.contains((Object)neighbourBlock)) {
            player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate) {
            BlockUtil.faceVectorPacketInstant(hitVec, true);
        }
        EnumActionResult action = playerController.processRightClickBlock(player, world, neighbour, opposite, hitVec, hand);
        if (!checkAction || action == EnumActionResult.SUCCESS) {
            player.swingArm(hand);
            PlaceUtils.mc.rightClickDelayTimer = 4;
        }
        return action == EnumActionResult.SUCCESS;
    }

    public static boolean placePrecise(BlockPos blockPos, EnumHand hand, boolean rotate, Vec3d precise, EnumFacing forceSide, boolean onlyRotation, boolean support) {
        EnumFacing side;
        EntityPlayerSP player = PlaceUtils.mc.player;
        WorldClient world = PlaceUtils.mc.world;
        PlayerControllerMP playerController = PlaceUtils.mc.playerController;
        if (player == null || world == null || playerController == null) {
            return false;
        }
        if (!world.getBlockState(blockPos).getMaterial().isReplaceable()) {
            return false;
        }
        EnumFacing enumFacing = side = forceSide == null ? BlockUtil.getPlaceableSide(blockPos) : forceSide;
        if (side == null) {
            return false;
        }
        BlockPos neighbour = blockPos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = world.getBlockState(neighbour).getBlock();
        if (!isSneaking && BlockUtil.blackList.contains((Object)neighbourBlock) || BlockUtil.shulkerList.contains((Object)neighbourBlock)) {
            player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate && !support) {
            BlockUtil.faceVectorPacketInstant(precise == null ? hitVec : precise, true);
        }
        if (!onlyRotation) {
            EnumActionResult action = playerController.processRightClickBlock(player, world, neighbour, opposite, precise == null ? hitVec : precise, hand);
            if (action == EnumActionResult.SUCCESS) {
                player.swingArm(hand);
                PlaceUtils.mc.rightClickDelayTimer = 4;
            }
            return action == EnumActionResult.SUCCESS;
        }
        return true;
    }
}

