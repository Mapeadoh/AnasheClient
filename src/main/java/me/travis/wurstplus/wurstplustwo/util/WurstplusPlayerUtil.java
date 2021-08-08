package me.travis.wurstplus.wurstplustwo.util;

import java.util.ArrayList;

import me.travis.mapeadoh.clientstuff.wp3.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import java.util.List;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class WurstplusPlayerUtil
{

    public static EntityPlayer findClosestTarget(double rangeMax, EntityPlayer aimTarget) {
        rangeMax *= rangeMax;
        List<EntityPlayer> playerList = WurstplusPlayerUtil.mc.world.playerEntities;
        EntityPlayer closestTarget = null;
        for (EntityPlayer entityPlayer : playerList) {
            if (EntityUtil.basicChecksEntity((Entity)entityPlayer)) continue;
            if (aimTarget == null && WurstplusPlayerUtil.mc.player.getDistanceSq((Entity)entityPlayer) <= rangeMax) {
                closestTarget = entityPlayer;
                continue;
            }
            if (aimTarget == null || WurstplusPlayerUtil.mc.player.getDistanceSq((Entity)entityPlayer) > rangeMax || WurstplusPlayerUtil.mc.player.getDistanceSq((Entity)entityPlayer) >= WurstplusPlayerUtil.mc.player.getDistanceSq((Entity)aimTarget)) continue;
            closestTarget = entityPlayer;
        }
        return closestTarget;
    }    private static final Minecraft mc;

    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(WurstplusPlayerUtil.mc.player.posX), Math.floor(WurstplusPlayerUtil.mc.player.posY), Math.floor(WurstplusPlayerUtil.mc.player.posZ));
    }

    public static FacingDirection GetFacing() {
        switch (MathHelper.floor(WurstplusPlayerUtil.mc.player.rotationYaw * 8.0f / 360.0f + 0.5) & 0x7) {
            case 0:
            case 1: {
                return FacingDirection.South;
            }
            case 2:
            case 3: {
                return FacingDirection.West;
            }
            case 4:
            case 5: {
                return FacingDirection.North;
            }
            case 6:
            case 7: {
                return FacingDirection.East;
            }
            default: {
                return FacingDirection.North;
            }
        }
    }

    public double getMoveYaw() {
        float strafe = 90.0f * WurstplusPlayerUtil.mc.player.moveStrafing;
        strafe *= (float)((WurstplusPlayerUtil.mc.player.moveForward != 0.0f) ? (WurstplusPlayerUtil.mc.player.moveForward * 0.5) : 1.0);
        float yaw = WurstplusPlayerUtil.mc.player.rotationYaw - strafe;
        yaw -= ((WurstplusPlayerUtil.mc.player.moveForward < 0.0f) ? 180.0f : 0.0f);
        return Math.toRadians(yaw);
    }

    public static void placeBlock(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!Minecraft.getMinecraft().world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + enumFacing.getXOffset() * 0.5, pos.getY() + 0.5 + enumFacing.getYOffset() * 0.5, pos.getZ() + 0.5 + enumFacing.getZOffset() * 0.5);
                final float[] old = { Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch };
                Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - Minecraft.getMinecraft().player.posZ, vec.x - Minecraft.getMinecraft().player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight()), Math.sqrt((vec.x - Minecraft.getMinecraft().player.posX) * (vec.x - Minecraft.getMinecraft().player.posX) + (vec.z - Minecraft.getMinecraft().player.posZ) * (vec.z - Minecraft.getMinecraft().player.posZ))))), Minecraft.getMinecraft().player.onGround));
                Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
                Minecraft.getMinecraft().playerController.processRightClickBlock(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
                Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(old[0], old[1], Minecraft.getMinecraft().player.onGround));
                return;
            }
        }
    }

    public double getSpeed() {
        return Math.hypot(WurstplusPlayerUtil.mc.player.motionX, WurstplusPlayerUtil.mc.player.motionZ);
    }

    public void setSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        WurstplusPlayerUtil.mc.player.motionX = -Math.sin(yaw) * speed;
        WurstplusPlayerUtil.mc.player.motionZ = Math.cos(yaw) * speed;
    }

    public static boolean isIntercepted(final BlockPos pos) {
        for (final Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public void addSpeed(final Double speed) {
        final Double yaw = this.getMoveYaw();
        EntityPlayerSP player;
        player = player = WurstplusPlayerUtil.mc.player;
        player.motionX -= Math.sin(yaw) * speed;
        EntityPlayerSP player2;
        player2 = player2 = WurstplusPlayerUtil.mc.player;
        player2.motionZ += Math.cos(yaw) * speed;
    }

    public void setTimer(final float speed) {
        WurstplusPlayerUtil.mc.timer.tickLength = 50.0f / speed;
    }

    public void step(final float height, final double[] offset, final boolean flag, final float speed) {
        if (flag) {
            this.setTimer(speed);
        }
        for (int i = 0; i < offset.length; ++i) {
            WurstplusPlayerUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(WurstplusPlayerUtil.mc.player.posX, WurstplusPlayerUtil.mc.player.posY + offset[i], WurstplusPlayerUtil.mc.player.posZ, WurstplusPlayerUtil.mc.player.onGround));
        }
        WurstplusPlayerUtil.mc.player.stepHeight = height;
    }

    public static boolean IsEating() {
        return WurstplusPlayerUtil.mc.player != null && WurstplusPlayerUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && WurstplusPlayerUtil.mc.player.isHandActive();
    }

    public static EntityPlayer findLookingPlayer(final double rangeMax) {
        final ArrayList<EntityPlayer> listPlayer = new ArrayList<EntityPlayer>();
        for (final EntityPlayer playerSin : WurstplusPlayerUtil.mc.world.playerEntities) {
            if (!playerSin.getName().equals(WurstplusPlayerUtil.mc.player.getName()) && !WurstplusFriendUtil.isFriend(playerSin.getName()) && !playerSin.isDead && WurstplusPlayerUtil.mc.player.getDistance((Entity)playerSin) <= rangeMax) {
                listPlayer.add(playerSin);
            }
        }
        EntityPlayer target = null;
        final Vec3d positionEyes = WurstplusPlayerUtil.mc.player.getPositionEyes(WurstplusPlayerUtil.mc.getRenderPartialTicks());
        final Vec3d rotationEyes = WurstplusPlayerUtil.mc.player.getLook(WurstplusPlayerUtil.mc.getRenderPartialTicks());
        final int precision = 2;
        for (int i = 0; i < (int)rangeMax; ++i) {
            for (int j = precision; j > 0; --j) {
                for (final Entity targetTemp : listPlayer) {
                    final AxisAlignedBB playerBox = targetTemp.getEntityBoundingBox();
                    final double xArray = positionEyes.x + rotationEyes.x * i + rotationEyes.x / j;
                    final double yArray = positionEyes.y + rotationEyes.y * i + rotationEyes.y / j;
                    final double zArray = positionEyes.z + rotationEyes.z * i + rotationEyes.z / j;
                    if (playerBox.maxY >= yArray && playerBox.minY <= yArray && playerBox.maxX >= xArray && playerBox.minX <= xArray && playerBox.maxZ >= zArray && playerBox.minZ <= zArray) {
                        target = (EntityPlayer)targetTemp;
                    }
                }
            }
        }
        return target;
    }

    public static double[] motion(final float speed) {
        final float moveForward = WurstplusPlayerUtil.mc.player.movementInput.moveForward;
        final float moveStrafe = WurstplusPlayerUtil.mc.player.movementInput.moveStrafe;
        final float rotationYaw = WurstplusPlayerUtil.mc.player.rotationYaw;
        final double x = moveForward * speed * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * speed * Math.sin(Math.toRadians(rotationYaw + 90.0f));
        final double z = moveForward * speed * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw + 90.0f));
        return new double[] { x, z };
    }

    static {
        mc = Minecraft.getMinecraft();
    }

    public enum FacingDirection
    {
        North,
        South,
        East,
        West;
    }
}
