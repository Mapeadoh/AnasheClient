package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LegCrystal
        extends WurstplusHack {
    WurstplusSetting range = this.create("Range", "Range", 1.0, 5.5, 10.0);
    boolean switchCooldown;

    public LegCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "LegCrystals";
        this.tag = "LegCrystals";
        this.description = "LegCrystals";
    }

    @Override
    public void update() {
        if (LegCrystal.mc.player != null) {
            Vec3d targetVector;
            EntityPlayer closestTarget;
            int crystalSlot = -1;
            if (LegCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                crystalSlot = LegCrystal.mc.player.inventory.currentItem;
            } else {
                for (int slot = 0; slot < 9; ++slot) {
                    if (LegCrystal.mc.player.inventory.getStackInSlot(slot).getItem() != Items.END_CRYSTAL) continue;
                    crystalSlot = slot;
                    break;
                }
            }
            if (crystalSlot != -1 && (closestTarget = this.findClosestTarget()) != null && (targetVector = this.findPlaceableBlock(closestTarget.getPositionVector().add(0.0, -1.0, 0.0))) != null) {
                BlockPos targetBlock = new BlockPos(targetVector);
                if (LegCrystal.mc.player.inventory.currentItem != crystalSlot) {
                    LegCrystal.mc.player.inventory.currentItem = crystalSlot;
                    this.switchCooldown = true;
                } else if (this.switchCooldown) {
                    this.switchCooldown = false;
                } else {
                    LegCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(targetBlock, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            }
        }
    }

    private Vec3d findPlaceableBlock(Vec3d startPos) {
        if (this.canPlaceCrystal(startPos.add(Offsets.NORTH2)) && !this.isExplosionProof(startPos.add(Offsets.NORTH1).add(0.0, 1.0, 0.0))) {
            return startPos.add(Offsets.NORTH2);
        }
        if (this.canPlaceCrystal(startPos.add(Offsets.NORTH1))) {
            return startPos.add(Offsets.NORTH1);
        }
        if (this.canPlaceCrystal(startPos.add(Offsets.EAST2)) && !this.isExplosionProof(startPos.add(Offsets.EAST1).add(0.0, 1.0, 0.0))) {
            return startPos.add(Offsets.EAST2);
        }
        if (this.canPlaceCrystal(startPos.add(Offsets.EAST1))) {
            return startPos.add(Offsets.EAST1);
        }
        if (this.canPlaceCrystal(startPos.add(Offsets.SOUTH2)) && !this.isExplosionProof(startPos.add(Offsets.SOUTH1).add(0.0, 1.0, 0.0))) {
            return startPos.add(Offsets.SOUTH2);
        }
        if (this.canPlaceCrystal(startPos.add(Offsets.SOUTH1))) {
            return startPos.add(Offsets.SOUTH1);
        }
        if (this.canPlaceCrystal(startPos.add(Offsets.WEST2)) && !this.isExplosionProof(startPos.add(Offsets.WEST1).add(0.0, 1.0, 0.0))) {
            return startPos.add(Offsets.WEST2);
        }
        return this.canPlaceCrystal(startPos.add(Offsets.WEST1)) ? startPos.add(Offsets.WEST1) : null;
    }

    private EntityPlayer findClosestTarget() {
        EntityPlayer closestTarget = null;
        for (EntityPlayer target : LegCrystal.mc.world.playerEntities) {
            if (target == LegCrystal.mc.player || WurstplusFriendUtil.isFriend(target.getName()) || !WurstplusEntityUtil.isLiving((Entity)target) || target.getHealth() <= 0.0f || (double) LegCrystal.mc.player.getDistance((Entity)target) > Double.valueOf(this.range.get_value(1.0))) continue;
            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }
            if (!(LegCrystal.mc.player.getDistance((Entity)target) < LegCrystal.mc.player.getDistance((Entity)closestTarget))) continue;
            closestTarget = target;
        }
        return closestTarget;
    }

    private boolean canPlaceCrystal(Vec3d vec3d) {
        BlockPos blockPos = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (LegCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || LegCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && LegCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && LegCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && LegCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && LegCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    private boolean isExplosionProof(Vec3d vec3d) {
        BlockPos blockPos = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
        Block block = LegCrystal.mc.world.getBlockState(blockPos).getBlock();
        if (block == Blocks.BEDROCK) {
            return true;
        }
        if (block == Blocks.OBSIDIAN) {
            return true;
        }
        if (block == Blocks.ANVIL) {
            return true;
        }
        if (block == Blocks.ENDER_CHEST) {
            return true;
        }
        return block == Blocks.BARRIER;
    }

    private static class Offsets {
        private static final Vec3d NORTH1 = new Vec3d(0.0, 0.0, -1.0);
        private static final Vec3d NORTH2 = new Vec3d(0.0, 0.0, -2.0);
        private static final Vec3d EAST1 = new Vec3d(1.0, 0.0, 0.0);
        private static final Vec3d EAST2 = new Vec3d(2.0, 0.0, 0.0);
        private static final Vec3d SOUTH1 = new Vec3d(0.0, 0.0, 1.0);
        private static final Vec3d SOUTH2 = new Vec3d(0.0, 0.0, 2.0);
        private static final Vec3d WEST1 = new Vec3d(-1.0, 0.0, 0.0);
        private static final Vec3d WEST2 = new Vec3d(-2.0, 0.0, 0.0);

        private Offsets() {
        }
    }
}