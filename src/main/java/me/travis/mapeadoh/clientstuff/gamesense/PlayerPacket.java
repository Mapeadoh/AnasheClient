/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 */
package me.travis.mapeadoh.clientstuff.gamesense;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class PlayerPacket {
    private WurstplusHack hack;
    private Vec3d position;
    private Vec2f rotation;

    public PlayerPacket(WurstplusHack module, Vec2f rotation) {
        this(module, null, rotation);
    }

    public PlayerPacket(WurstplusHack module, Vec3d position) {
        this(module, position, null);
    }

    public PlayerPacket(WurstplusHack module, Vec3d position, Vec2f rotation) {
        this(position, rotation);
    }

    private PlayerPacket(Vec3d position, Vec2f rotation) {
        this.position = position;
        this.rotation = rotation;
    }


    public Vec3d getPosition() {
        return this.position;
    }

    public Vec2f getRotation() {
        return this.rotation;
    }
}

