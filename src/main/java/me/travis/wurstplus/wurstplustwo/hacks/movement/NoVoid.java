//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;

public class NoVoid extends WurstplusHack {
    WurstplusSetting height = this.create("Height", "Height", 0, 0, 256);

    public NoVoid() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "NoVoid";
        this.tag = "NoVoid";
        this.description = "avoids getting voided";
    }

    public void update() {
        if (mc.world != null) {
            if (mc.player.noClip || mc.player.posY > (double)this.height.get_value(1)) {
                return;
            }

            RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0D, mc.player.posZ), false, false, false);
            if (trace != null && trace.typeOfHit == Type.BLOCK) {
                return;
            }

            mc.player.setVelocity(0.0D, 0.0D, 0.0D);
        }

    }
}
