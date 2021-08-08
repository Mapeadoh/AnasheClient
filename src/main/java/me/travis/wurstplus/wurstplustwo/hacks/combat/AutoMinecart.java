package me.travis.wurstplus.wurstplustwo.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.*;
import java.util.List;

import me.travis.mapeadoh.clientstuff.noraAndDelux.BadlionTessellator;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

public class AutoMinecart
        extends WurstplusHack {
    private boolean firstSwap = true;
    private boolean secondSwap = true;
    private boolean beginPlacing = false;
    private int lighterSlot;
    private EntityPlayer closestTarget;
    private BlockPos targetPos;
    WurstplusSetting announceUsage = this.create("Announce Usage", "Announce Usage", true);
    WurstplusSetting debug = this.create("Debug Mode", "Debug Mode", false);
    WurstplusSetting carts = this.create("Place Duration ", "CartsToPlace", 60.0, 1.0, 100.0);
    // from ca bc yes
    WurstplusSetting r = create("R", "R", 255, 0, 255);
    WurstplusSetting g = create("G", "G", 255, 0, 255);
    WurstplusSetting b = create("B", "B", 255, 0, 255);
    WurstplusSetting a = create("A", "A", 100, 0, 255);
    WurstplusSetting rainbow_mode = create("Rainbow", "Rainbow", false);

    public AutoMinecart() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "AutoMinecart";
        this.tag = "AutoTNTCart";
        this.description = "0% pasted from delux";
    }

    @Override
    protected void enable() {
        if (AutoMinecart.mc.player != null && AutoMinecart.mc.world != null) {
            WurstplusEventBus.EVENT_BUS.subscribe(this);
            MinecraftForge.EVENT_BUS.register((Object)this);
            this.tickDelay = 0;
            try {
                this.findClosestTarget();
            }
            catch (Exception ex) {}
            if (this.closestTarget != null) {
                if (this.announceUsage.get_value(true)) {
                    WurstplusMessageUtil.send_client_message("Attempting to TNT cart " + ChatFormatting.BLUE.toString() + this.closestTarget.getName() + ChatFormatting.WHITE.toString() + " ...");
                }
                this.targetPos = new BlockPos(this.closestTarget.getPositionVector());
            }
            else {
                if (this.announceUsage.get_value(true)) {
                    WurstplusMessageUtil.send_client_message("No target within range to TNT Cart.");
                }
                this.toggle();
            }
        }
        else {
            this.toggle();
        }
    }

    @Override
    protected void disable() {
        if (AutoMinecart.mc.player != null) {
            if (AutoMinecart.mc.world != null) {
                if (this.announceUsage.get_value(true)) {
                    WurstplusMessageUtil.client_message_simple((Object)TextFormatting.BLUE + "[" + (Object)TextFormatting.GOLD + "AutoTNTCart" + (Object)TextFormatting.BLUE + "]" + ChatFormatting.RED.toString() + " Disabled!");
                }
                this.firstSwap = true;
                this.secondSwap = true;
                this.beginPlacing = false;
                this.tickDelay = 0;
                WurstplusEventBus.EVENT_BUS.unsubscribe(this);
                MinecraftForge.EVENT_BUS.unregister((Object)this);
            }
        }
    }

    @Override
    public void update() {
        if(rainbow_mode.get_value(true)){
            cycle_rainbow();
        }
        if (AutoMinecart.mc.player != null) {
            if (AutoMinecart.mc.world != null) {
                int tntSlot = this.findTNTCart();
                int railSlot = this.findRail();
                if (railSlot > -1 && this.firstSwap) {
                    AutoMinecart.mc.player.inventory.currentItem = railSlot;
                    this.firstSwap = false;
                    this.placeBlock(this.targetPos, EnumFacing.DOWN);
                    if (this.debug.get_value(true)) {
                        WurstplusMessageUtil.send_client_message("Place Rail");
                    }
                }
                if (tntSlot > -1 && this.secondSwap && !this.firstSwap) {
                    AutoMinecart.mc.player.inventory.currentItem = tntSlot;
                    this.secondSwap = false;
                    this.beginPlacing = true;
                    if (this.debug.get_value(true)) {
                        WurstplusMessageUtil.send_client_message("Swap Tnt & Place");
                    }
                }
                if (!this.firstSwap && !this.secondSwap && this.beginPlacing) {
                    int flint;
                    if (this.tickDelay > 0) {
                        AutoMinecart.mc.player.swingArm(EnumHand.MAIN_HAND);
                        AutoMinecart.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.targetPos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                    if ((double)this.tickDelay == this.carts.get_value(60.0)) {
                        flint = this.findPick();
                        if (flint > -1) {
                            AutoMinecart.mc.player.inventory.currentItem = flint;
                        }
                        AutoMinecart.mc.player.swingArm(EnumHand.MAIN_HAND);
                        AutoMinecart.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.targetPos, EnumFacing.DOWN));
                        AutoMinecart.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.targetPos, EnumFacing.DOWN));
                        if (this.debug.get_value(true)) {
                            WurstplusMessageUtil.send_client_message("Break Rail");
                        }
                    }
                    if ((double)this.tickDelay == this.carts.get_value(60.0) + 5.0) {
                        flint = this.findFlint();
                        if (flint > -1) {
                            AutoMinecart.mc.player.inventory.currentItem = flint;
                            this.placeBlock(this.targetPos, EnumFacing.DOWN);
                        } else {
                            this.invGrabFlint();
                            AutoMinecart.mc.player.inventory.currentItem = 0;
                            this.placeBlock(this.targetPos, EnumFacing.DOWN);
                        }
                        this.toggle();
                    }
                }
            }
        }
    }

    private void findClosestTarget() {
        List<EntityPlayer> playerList = AutoMinecart.mc.world.playerEntities;
        this.closestTarget = null;
        for (EntityPlayer target : playerList) {
            if (target == AutoMinecart.mc.player || WurstplusFriendUtil.isFriend(target.getName()) || !AutoMinecart.isLiving((Entity)target) || target.getHealth() <= 0.0f) continue;
            if (AutoMinecart.mc.player.getDistance((Entity)target) > 6.0f || this.closestTarget != null) continue;
            this.closestTarget = target;
        }
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }

    private int findTNTCart() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            Item item;
            ItemStack stack = AutoMinecart.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || stack.getItem() instanceof ItemBlock || !((item = stack.getItem()) instanceof ItemMinecart)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private int findRail() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = AutoMinecart.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || !((block = ((ItemBlock)stack.getItem()).getBlock()) instanceof BlockRailBase)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private int findFlint() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            Item item;
            ItemStack stack = AutoMinecart.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || stack.getItem() instanceof ItemBlock || !((item = stack.getItem()) instanceof ItemFlintAndSteel)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private void invGrabFlint() {
        block6: {
            block5: {
                if (AutoMinecart.mc.currentScreen == null) break block5;
                if (AutoMinecart.mc.currentScreen instanceof GuiContainer) break block6;
            }
            if (AutoMinecart.mc.player.inventory.getStackInSlot(0).getItem() != Items.FLINT_AND_STEEL) {
                for (int i = 9; i < 36; ++i) {
                    if (AutoMinecart.mc.player.inventory.getStackInSlot(i).getItem() != Items.FLINT_AND_STEEL) continue;
                    AutoMinecart.mc.playerController.windowClick(AutoMinecart.mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, (EntityPlayer) AutoMinecart.mc.player);
                    this.lighterSlot = i;
                    break;
                }
            }
        }
    }

    private int findPick() {
        int pickSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoMinecart.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemPickaxe)) continue;
            pickSlot = i;
            break;
        }
        return pickSlot;
    }

    private void placeBlock(BlockPos pos, EnumFacing side) {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        AutoMinecart.mc.playerController.processRightClickBlock(AutoMinecart.mc.player, AutoMinecart.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoMinecart.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    public void cycle_rainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 1, 1);

        r.set_value((color_rgb_o >> 16) & 0xFF);
        g.set_value((color_rgb_o >> 8) & 0xFF);
        b.set_value(color_rgb_o & 0xFF);

    }

    @Override
    public void render() {
        if (AutoMinecart.mc.world != null && this.targetPos != null) {
            try {
                float posx = this.targetPos.getX();
                float posy = this.targetPos.getY();
                float posz = this.targetPos.getZ();
                BadlionTessellator.prepare("lines");
                BadlionTessellator.draw_cube_line_full(posx, posy, posz, new Color(r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1)).getRGB(), "all");
            }
            catch (Exception exception) {
                // empty catch block
            }
            BadlionTessellator.release();
        }
    }
}

