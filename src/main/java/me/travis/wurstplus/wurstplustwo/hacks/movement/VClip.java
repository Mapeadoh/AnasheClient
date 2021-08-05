//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.hacks.movement;

import java.util.Iterator;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusBlockInteractHelper;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

public class VClip extends WurstplusHack {
    WurstplusSetting offset = this.create("Strength", "Strength", -5.599999904632568D, -20.0D, 20.0D);
    WurstplusSetting delay = this.create("Delay", "Delay", 100, 0, 1000);
    WurstplusTimer timer = new WurstplusTimer();
    int lastHotbarSlot;
    int playerHotbarSlot;
    boolean isSneaking;

    public VClip() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "VClip";
        this.tag = "VClip";
        this.description = "tps you to y 0";
    }

    protected void enable() {
        if (mc.player != null) {
            if (mc.isSingleplayer()) {
                WurstplusMessageUtil.send_client_error_message("You are in singleplayer");
                this.set_disable();
            } else {
                BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                if (this.intersectsWithEntity(pos)) {
                    WurstplusMessageUtil.send_client_error_message("Intercepted by entity");
                }

                this.playerHotbarSlot = mc.player.inventory.currentItem;
                this.lastHotbarSlot = -1;
                mc.player.jump();
                this.timer.reset();
            }
        }
    }

    protected void disable() {
        if (mc.player != null) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                mc.player.inventory.currentItem = this.playerHotbarSlot;
            }

            if (this.isSneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
                this.isSneaking = false;
            }

            this.playerHotbarSlot = -1;
            this.lastHotbarSlot = -1;
        }
    }

    public void update() {
        if (this.timer.passed((long)this.delay.get_value(1))) {
        }

        if (this.isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
            this.isSneaking = false;
        }

        AnasheClient.get_hack_manager().get_module_with_tag("NoVoid").set_active(true);
        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + (double)this.offset.get_value(1), mc.player.posZ, false));
        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
        BlockPos offsetPos = new BlockPos(0, -1, 0);
        BlockPos targetPos = (new BlockPos(mc.player.getPositionVector())).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
        if (this.placeBlock(targetPos)) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }

            mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + (double)this.offset.get_value(1), mc.player.posZ, false));
        }

        this.set_disable();
    }

    private boolean placeBlock(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        } else {
            EnumFacing side = WurstplusBlockInteractHelper.getPlaceableSide(pos);
            if (side == null) {
                return false;
            } else {
                BlockPos neighbour = pos.offset(side);
                EnumFacing opposite = side.getOpposite();
                if (!WurstplusBlockInteractHelper.canBeClicked(neighbour)) {
                    return false;
                } else {
                    Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
                    Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
                    int obiSlot = this.find_in_hotbar();
                    if (obiSlot == -1) {
                        this.set_disable();
                    }

                    if (this.lastHotbarSlot != obiSlot) {
                        mc.player.inventory.currentItem = obiSlot;
                        this.lastHotbarSlot = obiSlot;
                    }

                    if (!this.isSneaking && WurstplusBlockInteractHelper.blackList.contains(neighbourBlock) || WurstplusBlockInteractHelper.shulkerList.contains(neighbourBlock)) {
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                        this.isSneaking = true;
                    }

                    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.rightClickDelayTimer = 4;
                    if (mc.playerController.getCurrentGameType().equals(GameType.CREATIVE)) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(net.minecraft.network.play.client.CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
                    }

                    return true;
                }
            }
        }
    }

    private int find_in_hotbar() {
        for(int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest) {
                    return i;
                }

                if (block instanceof BlockObsidian) {
                    return i;
                }
            }
        }

        return -1;
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        Iterator var2 = mc.world.loadedEntityList.iterator();

        Entity entity;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            entity = (Entity)var2.next();
        } while(entity.equals(mc.player) || entity instanceof EntityItem || entity instanceof EntityXPOrb || !(new AxisAlignedBB(pos)).intersects(entity.getEntityBoundingBox()));

        return true;
    }
}
