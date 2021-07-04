package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.util.math.AxisAlignedBB;

public class MapeadohReverseStep extends WurstplusHack {
    
    public MapeadohReverseStep() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

		this.name        = "Reverse Step";
		this.tag         = "ReverseStep";
		this.description = "anashei";
    }
    WurstplusSetting blocks = create("Blocks", "Blocks", 1, 1, 5);
    @Override
    public void update() {

    if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip) return;
    if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) return;
    
    final double n = get_n_normal();
    mc.player.motionY = - blocks.get_value(1);
    }
    public double get_n_normal() {
        
        mc.player.stepHeight = 0.5f;

        double max_y = -blocks.get_value(1);

        final AxisAlignedBB grow = mc.player.getEntityBoundingBox().offset(0, 0.05, 0).grow(0.05);

        if (!mc.world.getCollisionBoxes(mc.player, grow.offset(0, 2, 0)).isEmpty()) return 100;

        for (final AxisAlignedBB aabb : mc.world.getCollisionBoxes(mc.player, grow)) {

            if (aabb.maxY > max_y) {
                max_y = aabb.maxY;
            }

        }

        return max_y - mc.player.posY;

    }



}
    