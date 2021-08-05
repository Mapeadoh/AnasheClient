//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static me.travis.wurstplus.wurstplustwo.util.Wrapper.getPlayer;

public class Jesus extends WurstplusHack {
    public Jesus() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Jesus";
        this.tag = "Jesus";
        this.description = "walk on water";
    }

    public void update() {
        if (mc.world != null) {
            if (isInWater(mc.player) && !mc.player.isSneaking()) {
                mc.player.motionY = 0.1D;
                if (mc.player.getRidingEntity() != null) {
                    mc.player.getRidingEntity().motionY = 0.2D;
                }
            }

            if (isAboveWater(getPlayer()) && !isInWater(getPlayer()) && !isAboveLand(getPlayer()) && !mc.player.isSneaking()) {
                mc.player.motionY = 0.0D;
                mc.player.onGround = true;
            }
        }

    }

    public static boolean isInWater(Entity entity) {
        if (entity == null) {
            return false;
        } else {
            double y = entity.posY + 0.01D;

            for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
                for(int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                    BlockPos pos = new BlockPos(x, (int)y, z);
                    if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private static boolean isAboveWater(Entity entity) {
        double y = entity.posY - 0.03D;

        for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for(int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAboveLand(Entity entity) {
        if (entity == null) {
            return false;
        } else {
            double y = entity.posY - 0.01D;

            for(int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
                for(int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                    BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                    if (mc.world.getBlockState(pos).getBlock().isFullBlock(mc.world.getBlockState(pos))) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

}
