package me.travis.wurstplus.wurstplustwo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CachooxCAUtils {
    static final Minecraft mc = Minecraft.getMinecraft();

    public static List<BlockPos> possiblePlacePositions(float placeRange, boolean thirteen) {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)CachooxCAUtils.getSphere(CachooxCAUtils.getPlayerPos((EntityPlayer)CachooxCAUtils.mc.player), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, thirteen)).collect(Collectors.toList()));
        return positions;
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f = y;
                    float f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean thirteen) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        BlockPos final_boost = blockPos.add(0, 3, 0);
        try {
            if (CachooxCAUtils.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && CachooxCAUtils.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if (CachooxCAUtils.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || CachooxCAUtils.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR && !thirteen) {
                return false;
            }
            for (Object entity : CachooxCAUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                if (entity instanceof EntityEnderCrystal) continue;
                return false;
            }
            for (Object entity : CachooxCAUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                if (entity instanceof EntityEnderCrystal) continue;
                return false;
            }
            for (Object entity : CachooxCAUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(final_boost))) {
                if (!(entity instanceof EntityEnderCrystal)) continue;
                return false;
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        Block block = CachooxCAUtils.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            Block floor = CachooxCAUtils.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            Block ceil = CachooxCAUtils.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && CachooxCAUtils.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && CachooxCAUtils.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        if (entity == CachooxCAUtils.mc.player && CachooxCAUtils.mc.player.capabilities.isCreativeMode) {
            return 0.0f;
        }
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = CachooxCAUtils.getBlastReduction((EntityLivingBase)entity, CachooxCAUtils.getDamageMultiplied(damage), new Explosion((World)CachooxCAUtils.mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage((Explosion)explosion);
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)ep.getTotalArmorValue(), (float)((float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)ep.getArmorInventoryList(), (DamageSource)ds);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = CachooxCAUtils.mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return CachooxCAUtils.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
}
