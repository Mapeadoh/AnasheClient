package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.item.ItemEndCrystal;
import me.travis.mapeadoh.clientstuff.floppa.BlockUtil;
import me.travis.mapeadoh.clientstuff.floppa.InventoryUtil;
import net.minecraft.block.BlockPistonBase;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.util.math.BlockPos;
import me.travis.mapeadoh.clientstuff.floppa.CombatUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;

public class PistonCrystal extends WurstplusHack
{
    WurstplusSetting placeRange = create("Range", "Range", 4.5f, 0.0f, 10.0f);
    WurstplusSetting offhand = create("Offhand", "Offhand", false);
    WurstplusSetting rotate = create("Rotate", "Rotate", false);
    WurstplusSetting switchBack = create("SwitchBack", "SwitchBack", false);
    WurstplusSetting pistonDelay = create("PistonDelay", "PistonDelay", 0, 0, 100);
    WurstplusSetting crystalDelay = create("CrystalDelay", "CrystalDelay", 2, 0, 100);
    WurstplusSetting powerDelay = create("PowerOnDelay", "PoweronDelay", 4, 0, 100);
    WurstplusSetting breakDelay = create("BreakDelay", "BreakDelay", 6, 0, 100);
    WurstplusTimer timer = new WurstplusTimer();
    private EntityPlayer target;
    private EnumFacing offsetEnumFacing;

    public PistonCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "PistonAura";
        this.tag = "PistonAura";
        this.description = "ty floppa";
    }

    @Override
    public void enable() {
        this.target = null;
        this.timer.reset();
    }

    @Override
    public void update() {
        if (!this.getTarget()) {
            return;
        }
        this.offsetEnumFacing = CombatUtil.getCorrectEnumFacing(this.target);
        if (CombatUtil.checkBlocksEmpty(this.target, this.offsetEnumFacing)) {
            this.placeBlocks();
        }
    }

    private void placeBlocks() {
        this.placeBlock(this.target.getPosition().offset(this.offsetEnumFacing).offset(this.offsetEnumFacing).up(), 0, this.switchBack.get_value(true));
        this.placeBlock(this.target.getPosition().up(), 1, this.switchBack.get_value(true));
        this.placeBlock(this.target.getPosition().offset(this.offsetEnumFacing).offset(this.offsetEnumFacing).offset(this.offsetEnumFacing).up(), 2, this.switchBack.get_value(true));
        this.breakCrystal(this.target.getPosition().offset(this.offsetEnumFacing).up());
    }

    private void breakCrystal(final BlockPos crystalPosition) {
        if (PistonCrystal.mc.world.getBlockState(crystalPosition).getBlock().equals(BlockPistonExtension.class)) {
            final BlockPos crystalHittable = crystalPosition.offset(this.offsetEnumFacing.getOpposite());
            final double x = crystalHittable.getX();
            final double y = crystalHittable.getY() + 1.0;
            final double z = crystalHittable.getZ();
            final AxisAlignedBB scanArea = new AxisAlignedBB(x, y, z, x, y + 0.1, z);
            final List<EntityEnderCrystal> crystals = (List<EntityEnderCrystal>)PistonCrystal.mc.world.getEntitiesWithinAABB((Class)EntityEnderCrystal.class, scanArea);
            if (!crystals.isEmpty()) {
                final CPacketUseEntity attackPacket = new CPacketUseEntity();
                attackPacket.entityId = crystals.get(0).getEntityId();
                attackPacket.action = CPacketUseEntity.Action.ATTACK;
                PistonCrystal.mc.player.connection.sendPacket((Packet)attackPacket);
                if (this.timer.passed(this.breakDelay.get_value(1))) {
                    PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity());
                }
            }
        }
    }

    private void placeBlock(final BlockPos pos, final int mode, final boolean switchBack) {
        final int currentSlot = PistonCrystal.mc.player.inventory.currentItem;
        switch (mode) {
            case 0: {
                final Class<?> block = BlockPistonBase.class;
                if (InventoryUtil.findSlotHotbar(block) == -1) {
                    WurstplusMessageUtil.send_client_error_message("Cannot find " + block.getName() + " in hotbar");
                    this.toggle();
                    return;
                }
                InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(block));
                if (this.timer.passed(this.pistonDelay.get_value(1))) {
                    BlockUtil.placeBlock(pos, this.offhand.get_value(true), this.rotate.get_value(true));
                    break;
                }
                break;
            }
            case 1: {
                final Class<?> block = ItemEndCrystal.class;
                if (InventoryUtil.findSlotHotbar(block) == -1) {
                    WurstplusMessageUtil.send_client_error_message("Cannot find " + block.getName() + " in hotbar");
                    this.toggle();
                    return;
                }
                InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(block));
                if (this.timer.passed(this.crystalDelay.get_value(1))) {
                    BlockUtil.placeBlock(pos, this.offhand.get_value(true), this.rotate.get_value(true));
                    break;
                }
                break;
            }
            case 2: {
                final Class<?> block = BlockRedstoneTorch.class;
                if (InventoryUtil.findSlotHotbar(block) == -1) {
                    WurstplusMessageUtil.send_client_error_message("Cannot find " + block.getName() + " in hotbar");
                    this.toggle();
                    return;
                }
                InventoryUtil.switchToSlot(InventoryUtil.findSlotHotbar(block));
                if (this.timer.passed(this.powerDelay.get_value(1))) {
                    BlockUtil.placeBlock(pos, this.offhand.get_value(true), this.rotate.get_value(true));
                    break;
                }
                break;
            }
        }
        if (switchBack) {
            InventoryUtil.switchToSlot(currentSlot);
        }
    }

    private boolean getTarget() {
        final List<EntityPlayer> playerList = CombatUtil.getPlayersSorted(this.placeRange.get_value(1));
        if (playerList.isEmpty()) {
            return false;
        }
        this.target = playerList.get(0);
        return true;
    }
}
