package me.travis.wurstplus.wurstplustwo.hacks.misc;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.travis.mapeadoh.clientstuff.wp1.BlockInteractionHelper;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoWither extends WurstplusHack {
    public AutoWither(){
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "AutoWither";
        this.tag = "AutoWither";
        this.description = "rewrite";
    }

    private static boolean isSneaking;
    WurstplusSetting mode = create("Mode", "Mode", "Spam", combobox("Spam", "Single"));
    WurstplusSetting range = create("Range", "Range", 3.5, 2.0, 10.0);
    WurstplusSetting delay = create("Delay", "Delay", 20, 12, 100);
    WurstplusSetting rotate = create("Rotate", "Rotate", false);
    WurstplusSetting debug = create("Debug", "Debug", false);
    private BlockPos placeTarget;
    private boolean rotationPlaceableX;
    private boolean rotationPlaceableZ;
    private int bodySlot;
    private int headSlot;
    private int buildStage;
    private int delayStep;

    private static void placeBlock(BlockPos pos, boolean rotate) {
        EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        if (!isSneaking && (BlockInteractionHelper.blackList.contains((Object)neighbourBlock) || BlockInteractionHelper.shulkerList.contains((Object)neighbourBlock))) {
            mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;
    }

    private static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.offset(side);
            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false) || (blockState = mc.world.getBlockState(neighbour)).getMaterial().isReplaceable() || blockState.getBlock() instanceof BlockTallGrass || blockState.getBlock() instanceof BlockDeadBush) continue;
            return side;
        }
        return null;
    }

    public void enable() {
        if (mc.player == null) {
            this.disable();
            return;
        }
        this.buildStage = 1;
        this.delayStep = 1;
    }

    private boolean checkBlocksInHotbar() {
        this.headSlot = -1;
        this.bodySlot = -1;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (stack.getItem() == Items.SKULL && stack.getItemDamage() == 1) {
                if (mc.player.inventory.getStackInSlot((int)i).stackSize < 3) continue;
                this.headSlot = i;
                continue;
            }
            if (!(stack.getItem() instanceof ItemBlock)) continue;
            block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockSoulSand && mc.player.inventory.getStackInSlot((int)i).stackSize >= 4) {
                this.bodySlot = i;
            }

            if (block != Blocks.SNOW || mc.player.inventory.getStackInSlot((int)i).stackSize < 2) continue;
            this.bodySlot = i;
        }
        return this.bodySlot != -1 && this.headSlot != -1;
    }

    private boolean testStructure() {
        return this.testWitherStructure();
    }

    private boolean testWitherStructure() {
        boolean noRotationPlaceable = true;
        this.rotationPlaceableX = true;
        this.rotationPlaceableZ = true;
        boolean isShitGrass = false;
        if (mc.world.getBlockState(this.placeTarget) == null) {
            return false;
        }
        Block block = mc.world.getBlockState(this.placeTarget).getBlock();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (getPlaceableSide(this.placeTarget.up()) == null) {
            return false;
        }
        for (BlockPos pos : BodyParts.bodyBase) {
            if (!this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) continue;
            noRotationPlaceable = false;
        }
        for (BlockPos pos : BodyParts.ArmsX) {
            if (!this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) && !this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) continue;
            this.rotationPlaceableX = false;
        }
        for (BlockPos pos : BodyParts.ArmsZ) {
            if (!this.placingIsBlocked(this.placeTarget.add((Vec3i)pos)) && !this.placingIsBlocked(this.placeTarget.add((Vec3i)pos.down()))) continue;
            this.rotationPlaceableZ = false;
        }
        for (BlockPos pos : BodyParts.headsX) {
            if (!this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) continue;
            this.rotationPlaceableX = false;
        }
        for (BlockPos pos : BodyParts.headsZ) {
            if (!this.placingIsBlocked(this.placeTarget.add((Vec3i)pos))) continue;
            this.rotationPlaceableZ = false;
        }
        return !isShitGrass && noRotationPlaceable && (this.rotationPlaceableX || this.rotationPlaceableZ);
    }

    public void update() {
        if (mc.player == null) {
            return;
        }
        if (this.buildStage == 1) {
            isSneaking = false;
            this.rotationPlaceableX = false;
            this.rotationPlaceableZ = false;
            if (!this.checkBlocksInHotbar()) {
                WurstplusMessageUtil.send_client_error_message("no blocks in hotbar");
                this.toggle();
                return;
            }
            List<BlockPos> blockPosList = BlockInteractionHelper.getSphere(mc.player.getPosition().down(), this.range.get_value(1), this.range.get_value(1), false, true, 0);
            boolean noPositionInArea = true;
            for (BlockPos pos : blockPosList) {
                this.placeTarget = pos.down();
                if (!this.testStructure()) continue;
                noPositionInArea = false;
                break;
            }
            if (noPositionInArea) {
                if (mode.in("Single")) {
                    if (this.debug.get_value(true)) {
                        WurstplusMessageUtil.send_client_error_message("[AutoWither] " + ChatFormatting.RED.toString() + "Position not valid, disabling.");
                        this.toggle();
                    }
                }
                return;
            }
            mc.player.inventory.currentItem = this.bodySlot;
            for (BlockPos pos : BodyParts.bodyBase) {
                placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.get_value(true));
            }
            if (this.rotationPlaceableX) {
                for (BlockPos pos : BodyParts.ArmsX) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.get_value(true));
                }
            } else if (this.rotationPlaceableZ) {
                for (BlockPos pos : BodyParts.ArmsZ) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.get_value(true));
                }
            }
            this.buildStage = 2;
        } else if (this.buildStage == 2) {
            mc.player.inventory.currentItem = this.headSlot;
            if (this.rotationPlaceableX) {
                for (BlockPos pos : BodyParts.headsX) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.get_value(true));
                }
            } else if (this.rotationPlaceableZ) {
                for (BlockPos pos : BodyParts.headsZ) {
                    placeBlock(this.placeTarget.add((Vec3i)pos), this.rotate.get_value(true));
                }
            }
            if (isSneaking) {
                mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                isSneaking = false;
            }
            if (this.mode.in("Single")) {
                this.disable();
            }
            this.buildStage = 3;
        } else if (this.buildStage == 3) {
            if (this.delayStep < this.delay.get_value(1)) {
                ++this.delayStep;
            } else {
                this.delayStep = 1;
                this.buildStage = 1;
            }
        }
    }

    private boolean placingIsBlocked(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir)) {
            return true;
        }
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
            return true;
        }
        return false;
    }

    private static class BodyParts {
        private static final BlockPos[] bodyBase = new BlockPos[]{new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};
        private static final BlockPos[] ArmsX = new BlockPos[]{new BlockPos(-1, 2, 0), new BlockPos(1, 2, 0)};
        private static final BlockPos[] ArmsZ = new BlockPos[]{new BlockPos(0, 2, -1), new BlockPos(0, 2, 1)};
        private static final BlockPos[] headsX = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(-1, 3, 0), new BlockPos(1, 3, 0)};
        private static final BlockPos[] headsZ = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(0, 3, -1), new BlockPos(0, 3, 1)};
        private static final BlockPos[] head = new BlockPos[]{new BlockPos(0, 3, 0)};

        private BodyParts() {
        }
    }
}