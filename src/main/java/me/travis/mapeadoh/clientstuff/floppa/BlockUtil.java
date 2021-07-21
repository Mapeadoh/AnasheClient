package me.travis.mapeadoh.clientstuff.floppa;

import java.util.Arrays;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.client.Minecraft;

public class BlockUtil
{
    public static final Minecraft mc;
    public static final List<Block> blackList;
    public static final List<Block> shulkerList;

    public static BlockPos getPlayerPositionFloored(final EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = BlockUtil.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)BlockUtil.mc.world, pos) != -1.0f;
    }

    public static boolean placeBlock(final BlockPos blockPos, final boolean offhand, final boolean rotate) {
        if (!checkCanPlace(blockPos)) {
            return false;
        }
        final EnumFacing placeSide = getPlaceSide(blockPos);
        final BlockPos adjacentBlock = blockPos.offset(placeSide);
        final EnumFacing opposingSide = placeSide.getOpposite();
        if (!BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(adjacentBlock), false)) {
            return false;
        }
        boolean isSneak = false;
        if (BlockUtil.blackList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock()) || BlockUtil.shulkerList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock())) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneak = true;
        }
        final Vec3d hitVector = getHitVector(adjacentBlock, opposingSide);
        if (rotate) {
            final float[] angle = getLegitRotations(hitVector);
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], angle[1], BlockUtil.mc.player.onGround));
        }
        final EnumHand actionHand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, adjacentBlock, opposingSide, hitVector, actionHand);
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(actionHand));
        if (isSneak) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return true;
    }

    public static boolean placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        boolean sneaking = false;
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking()) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            faceVector(hitVec, true);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockUtil.mc.rightClickDelayTimer = 4;
        return sneaking || isSneaking;
    }

    public static void placeBlock(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!BlockUtil.mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + enumFacing.getXOffset() * 0.5, pos.getY() + 0.5 + enumFacing.getYOffset() * 0.5, pos.getZ() + 0.5 + enumFacing.getZOffset() * 0.5);
                final float[] old = { BlockUtil.mc.player.rotationYaw, BlockUtil.mc.player.rotationPitch };
                BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - BlockUtil.mc.player.posZ, vec.x - BlockUtil.mc.player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight()), Math.sqrt((vec.x - BlockUtil.mc.player.posX) * (vec.x - BlockUtil.mc.player.posX) + (vec.z - BlockUtil.mc.player.posZ) * (vec.z - BlockUtil.mc.player.posZ))))), BlockUtil.mc.player.onGround));
                BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
                BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(old[0], old[1], BlockUtil.mc.player.onGround));
                return;
            }
        }
    }

    public static void placeBlock(final BlockPos pos, final int slot) {
        if (slot == -1) {
            return;
        }
        final int prev = BlockUtil.mc.player.inventory.currentItem;
        BlockUtil.mc.player.inventory.currentItem = slot;
        placeBlock(pos);
        BlockUtil.mc.player.inventory.currentItem = prev;
    }

    public static List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final List<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (BlockUtil.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = BlockUtil.mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(final BlockPos pos) {
        final Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            final EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ);
    }

    public static float[] getLegitRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { BlockUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - BlockUtil.mc.player.rotationYaw), BlockUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - BlockUtil.mc.player.rotationPitch) };
    }

    public static void faceVector(final Vec3d vec, final boolean normalizeAngle) {
        final float[] rotations = getLegitRotations(vec);
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? ((float)MathHelper.normalizeAngle((int)rotations[1], 360)) : rotations[1], BlockUtil.mc.player.onGround));
    }

    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            final float f = (float)(vec.x - pos.getX());
            final float f2 = (float)(vec.y - pos.getY());
            final float f3 = (float)(vec.z - pos.getZ());
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        }
        else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockUtil.mc.rightClickDelayTimer = 4;
    }

    public static boolean isIntercepted(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public static boolean placeBlock(final BlockPos blockPos, final boolean offhand, final boolean rotate, final boolean doSwitch, final boolean silentSwitch, final int toSwitch) {
        if (!checkCanPlace(blockPos)) {
            return false;
        }
        final EnumFacing placeSide = getPlaceSide(blockPos);
        final BlockPos adjacentBlock = blockPos.offset(placeSide);
        final EnumFacing opposingSide = placeSide.getOpposite();
        if (!BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(adjacentBlock), false)) {
            return false;
        }
        if (doSwitch) {
            if (silentSwitch) {
                BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(toSwitch));
            }
            else if (BlockUtil.mc.player.inventory.currentItem != toSwitch) {
                BlockUtil.mc.player.inventory.currentItem = toSwitch;
            }
        }
        boolean isSneak = false;
        if (BlockUtil.blackList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock()) || BlockUtil.shulkerList.contains(BlockUtil.mc.world.getBlockState(adjacentBlock).getBlock())) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneak = true;
        }
        final Vec3d hitVector = getHitVector(adjacentBlock, opposingSide);
        if (rotate) {
            final float[] angle = getLegitRotations(hitVector);
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0], angle[1], BlockUtil.mc.player.onGround));
        }
        final EnumHand actionHand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, adjacentBlock, opposingSide, hitVector, actionHand);
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(actionHand));
        if (isSneak) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return true;
    }

    private static EnumFacing getPlaceSide(final BlockPos blockPos) {
        EnumFacing placeableSide = null;
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos adjacent = blockPos.offset(side);
            if (BlockUtil.mc.world.getBlockState(adjacent).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(adjacent), false) && !BlockUtil.mc.world.getBlockState(adjacent).getMaterial().isReplaceable()) {
                placeableSide = side;
            }
        }
        return placeableSide;
    }

    public static boolean checkCanPlace(final BlockPos pos) {
        if (!(BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockAir) && !(BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : BlockUtil.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb) && !(entity instanceof EntityArrow)) {
                return false;
            }
        }
        return getPlaceSide(pos) != null;
    }

    private static Vec3d getHitVector(final BlockPos pos, final EnumFacing opposingSide) {
        return new Vec3d((Vec3i)pos).add(new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(opposingSide.getDirectionVec()).scale(0.5));
    }

    static {
        mc = Minecraft.getMinecraft();
        blackList = Arrays.asList((Block)Blocks.TALLGRASS, Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    }
}
