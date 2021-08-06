package me.travis.mapeadoh.clientstuff.gamesense;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Vec2f getRotationTo(AxisAlignedBB box) {
        EntityPlayerSP player = RotationUtil.mc.player;
        if (player == null) {
            return Vec2f.ZERO;
        }
        Vec3d eyePos = player.getPositionEyes(1.0f);
        if (player.getEntityBoundingBox().intersects(box)) {
            return RotationUtil.getRotationTo(eyePos, box.getCenter());
        }
        double x = MathHelper.clamp((double)eyePos.x, (double)box.minX, (double)box.maxX);
        double y = MathHelper.clamp((double)eyePos.y, (double)box.minY, (double)box.maxY);
        double z = MathHelper.clamp((double)eyePos.z, (double)box.minZ, (double)box.maxZ);
        return RotationUtil.getRotationTo(eyePos, new Vec3d(x, y, z));
    }

    public static Vec2f getRotationTo(Vec3d posTo) {
        EntityPlayerSP player = RotationUtil.mc.player;
        return player != null ? RotationUtil.getRotationTo(player.getPositionEyes(1.0f), posTo) : Vec2f.ZERO;
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        return RotationUtil.getRotationFromVec(posTo.subtract(posFrom));
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double lengthXZ = Math.hypot(vec.x, vec.z);
        double yaw = RotationUtil.normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        double pitch = RotationUtil.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, lengthXZ)));
        return new Vec2f((float)yaw, (float)pitch);
    }

    public static double normalizeAngle(double angle) {
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static float normalizeAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
}
