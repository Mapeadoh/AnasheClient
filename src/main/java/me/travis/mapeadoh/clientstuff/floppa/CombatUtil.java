package me.travis.mapeadoh.clientstuff.floppa;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class CombatUtil
{
    private static final Minecraft mc;

    public static EnumFacing getCorrectEnumFacing(final EntityPlayer target) {
        boolean ableToRunOnCurrentFacing = true;
        EnumFacing correctEnumFacing = null;
        for (final EnumFacing facing : EnumFacing.values()) {
            final BlockPos posToCheck = target.getPosition().offset(facing).add(0, 1, 0);
            final BlockPos posToCheck2 = target.getPosition().offset(facing).offset(facing).add(0, 1, 0);
            final BlockPos posToCheck3 = target.getPosition().offset(facing).offset(facing).offset(facing).add(0, 1, 0);
            if (!CombatUtil.mc.world.getBlockState(posToCheck).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck2).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck3).getBlock().equals(Blocks.AIR)) {
                ableToRunOnCurrentFacing = false;
            }
            if (ableToRunOnCurrentFacing) {
                correctEnumFacing = facing;
            }
        }
        return correctEnumFacing;
    }

    public static boolean checkBlocksEmpty(final EntityPlayer target, final EnumFacing facing) {
        final BlockPos posToCheck = target.getPosition().offset(facing).add(0, 1, 0);
        final BlockPos posToCheck2 = target.getPosition().offset(facing).offset(facing).add(0, 1, 0);
        final BlockPos posToCheck3 = target.getPosition().offset(facing).offset(facing).offset(facing).add(0, 1, 0);
        return !CombatUtil.mc.world.getBlockState(posToCheck).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck2).getBlock().equals(Blocks.AIR) || !CombatUtil.mc.world.getBlockState(posToCheck3).getBlock().equals(Blocks.AIR);
    }

    public static List<EntityPlayer> getPlayersSorted(final float range) {
        if (CombatUtil.mc.world == null || CombatUtil.mc.player == null) {
            return new ArrayList<EntityPlayer>();
        }
        synchronized (CombatUtil.mc.world.playerEntities) {
            final List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
            for (final EntityPlayer player : CombatUtil.mc.world.playerEntities) {
                if (CombatUtil.mc.player == player) {
                    continue;
                }
                if (CombatUtil.mc.player.getDistance((Entity)player) > range) {
                    continue;
                }
                playerList.add(player);
            }
            playerList.sort(Comparator.comparing(eP -> CombatUtil.mc.player.getDistance(eP)));
            return playerList;
        }
    }

    public static float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }

    public static List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}
