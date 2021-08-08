package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.mapeadoh.clientstuff.gamesense.BlockUtil;
import me.travis.mapeadoh.clientstuff.gamesense.PlaceUtils;
import me.travis.mapeadoh.clientstuff.gamesense.SpoofRotationUtil;
import me.travis.mapeadoh.clientstuff.wp3.InventoryUtil;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Blocker
        extends WurstplusHack {
    WurstplusSetting rotate = this.create("Rotate", "Rotate", true);
    WurstplusSetting anvilBlocker = this.create("Anvil", "Anvil", true);
    WurstplusSetting offHandObby = this.create("Off Hand Obby", "Off Hand Obby", true);
    WurstplusSetting pistonBlocker = this.create("Piston", "Piston", true);
    WurstplusSetting BlocksPerTick = this.create("Blocks Per Tick", "Blocks Per Tick", 4, 0, 10);
    WurstplusSetting blockPlaced = this.create("Block Place", "Block Place", "String", this.combobox("Pressure", "String"));
    WurstplusSetting tickDelay = this.create("Tick Delay", "Tick Delay", 5, 0, 10);
    private int delayTimeTicks = 0;
    private boolean noObby;
    private boolean noActive;
    private boolean activedBefore;

    public Blocker() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "Blocker";
        this.tag = "Blocker";
        this.description = "Blocker";
    }

    @Override
    protected void enable() {
        if (Blocker.mc.player == null) {
            this.disable();
        } else {
            if (!this.anvilBlocker.get_value(true) && !this.pistonBlocker.get_value(true)) {
                this.noActive = true;
                this.disable();
            }
            this.noObby = false;
        }
    }

    @Override
    protected void disable() {
        if (Blocker.mc.player != null) {
            if (this.noActive) {
                WurstplusMessageUtil.send_client_message("Nothing is active... Blocker turned OFF!");
            } else if (this.noObby) {
                WurstplusMessageUtil.send_client_message("Obsidian not found... Blocker turned OFF!");
            }
        }
    }

    @Override
    public void update() {
        if (Blocker.mc.player == null) {
            this.disable();
        } else if (this.noObby) {
            this.disable();
        } else if (this.delayTimeTicks < this.tickDelay.get_value(5)) {
            ++this.delayTimeTicks;
        } else {
            SpoofRotationUtil.ROTATION_UTIL.shouldSpoofAngles(true);
            this.delayTimeTicks = 0;
            if (this.anvilBlocker.get_value(true)) {
                this.blockAnvil();
            }
            if (this.pistonBlocker.get_value(true)) {
                this.blockPiston();
            }
        }
    }

    private void blockAnvil() {
        boolean found = false;
        for (Entity t : Blocker.mc.world.loadedEntityList) {
            Block ex;
            if (!(t instanceof EntityFallingBlock) || !((ex = (Block)((EntityFallingBlock)t).getBlock()) instanceof BlockAnvil) || (int)t.posX != (int)Blocker.mc.player.posX || (int)t.posZ != (int)Blocker.mc.player.posZ || !(BlockUtil.getBlock(Blocker.mc.player.posX, Blocker.mc.player.posY + 2.0, Blocker.mc.player.posZ) instanceof BlockAir)) continue;
            this.placeBlock(new BlockPos(Blocker.mc.player.posX, Blocker.mc.player.posY + 2.0, Blocker.mc.player.posZ));
            WurstplusMessageUtil.send_client_message("AutoAnvil detected... Anvil Blocked!");
            found = true;
        }
        if (!found && this.activedBefore) {
            this.activedBefore = false;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void blockPiston() {
        for (final Entity t : Blocker.mc.world.loadedEntityList) {
            if (t instanceof EntityEnderCrystal && t.posX >= Blocker.mc.player.posX - 1.5 && t.posX <= Blocker.mc.player.posX + 1.5 && t.posZ >= Blocker.mc.player.posZ - 1.5 && t.posZ <= Blocker.mc.player.posZ + 1.5) {
                for (int i = -2; i < 3; ++i) {
                    for (int j = -2; j < 3; ++j) {
                        if ((i == 0 || j == 0) && BlockUtil.getBlock(t.posX + i, t.posY, t.posZ + j) instanceof BlockPistonBase) {
                            this.breakCrystalPiston(t);
                            WurstplusMessageUtil.send_client_message("PistonCrystal detected... Destroyed crystal!");
                        }
                    }
                }
            }
        }
    }

    private void placeBlock(BlockPos pos) {
        EnumHand handSwing = EnumHand.MAIN_HAND;
        int obsidianSlot = InventoryUtil.findObsidianSlot(this.offHandObby.get_value(true), this.activedBefore);
        if (obsidianSlot == -1) {
            this.noObby = true;
        } else {
            if (obsidianSlot == 9) {
                this.activedBefore = true;
                if (!(Blocker.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock) || !(((ItemBlock)Blocker.mc.player.getHeldItemOffhand().getItem()).getBlock() instanceof BlockObsidian)) {
                    return;
                }
                handSwing = EnumHand.OFF_HAND;
            }
            if (Blocker.mc.player.inventory.currentItem != obsidianSlot && obsidianSlot != 9) {
                Blocker.mc.player.inventory.currentItem = obsidianSlot;
            }
            PlaceUtils.place(pos, handSwing, this.rotate.get_value(false), true);
        }
    }

    private void breakCrystalPiston(Entity crystal) {
        if (this.rotate.get_value(false)) {
            SpoofRotationUtil.ROTATION_UTIL.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)Blocker.mc.player);
        }
        WurstplusCrystalUtil.breakCrystal(crystal);
        if (this.rotate.get_value(false)) {
            SpoofRotationUtil.ROTATION_UTIL.resetRotation();
        }
    }
}

