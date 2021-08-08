package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import me.travis.mapeadoh.clientstuff.gamesense.BlockUtil;
import me.travis.mapeadoh.clientstuff.gamesense.SpoofRotationUtil;
import me.travis.mapeadoh.clientstuff.phobos.EntityUtil;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.events.BlockChangeEvent;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.chat.WurstplusAutoEz;
import me.travis.wurstplus.wurstplustwo.util.HoleUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusPlayerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Elevator
        extends WurstplusHack {
    WurstplusSetting target;
    WurstplusSetting placeMode;
    WurstplusSetting supportDelay;
    WurstplusSetting pistonDelay;
    WurstplusSetting redstoneDelay;
    WurstplusSetting blocksPerTick;
    WurstplusSetting tickBreakRedstone;
    WurstplusSetting enemyRange;
    WurstplusSetting debugMode;
    WurstplusSetting trapMode;
    WurstplusSetting trapAfter;
    WurstplusSetting rotate;
    WurstplusSetting forceBurrow;
    EntityPlayer aimTarget;
    double[][] sur_block;
    double[] enemyCoordsDouble;
    int[][] disp_surblock;
    int[] slot_mat;
    int[] enemyCoordsInt;
    int[] meCoordsInt;
    int lastStage;
    int blockPlaced;
    int tickPassedRedstone;
    int delayTimeTicks;
    boolean redstoneBlockMode;
    boolean enoughSpace;
    boolean isHole;
    boolean noMaterials;
    boolean redstoneAbovePiston;
    boolean isSneaking;
    boolean redstonePlaced;
    structureTemp toPlace;
    @EventHandler
    private final Listener<BlockChangeEvent> blockChangeEventListener;
    final ArrayList<EnumFacing> exd;

    public Elevator() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "Elevator";
        this.tag = "Elevator";
        this.description = "Elevator";
        this.target = this.create("Target", "Target", "Nearest", this.combobox("Nearest", "Looking"));
        this.placeMode = this.create("Place", "Place", "Torch", this.combobox("Torch", "Block", "Both"));
        this.supportDelay = this.create("Support Delay", "Support Delay", 0, 0, 8);
        this.pistonDelay = this.create("Piston Delay", "Piston Delay", 0, 0, 8);
        this.redstoneDelay = this.create("Redstone Delay", "Redstone Delay", 0, 0, 8);
        this.blocksPerTick = this.create("Blocks per Tick", "Blocks per Tick", 4, 1, 8);
        this.tickBreakRedstone = this.create("Tick Break Redstone", "Tick Break Redstone", 2, 0, 10);
        this.enemyRange = this.create("Range", "Range", 4.9, 0.0, 6.0);
        this.debugMode = this.create("Debug Mode", "Debug Mode", false);
        this.trapMode = this.create("Trap Mode", "Trap Mode", false);
        this.trapAfter = this.create("Trap After", "Trap After", false);
        this.rotate = this.create("Rotate", "Rotate", false);
        this.forceBurrow = this.create("Force Burrow", "Force Burrow", false);
        this.disp_surblock = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
        BlockPos[] temp = new BlockPos[1];
        boolean n = false;
        BlockPos blockPos = null;
        this.blockChangeEventListener = new Listener<BlockChangeEvent>(event -> {
            if (Elevator.mc.player == null || Elevator.mc.world == null) {
                return;
            }
            if (event.getBlock() == null || event.getPosition() == null) {
                return;
            }
            event.getPosition().getX();
            temp[0] = this.compactBlockPos(2);
            if (0 == blockPos.getX() && event.getPosition().getY() == temp[0].getY() && event.getPosition().getZ() == temp[0].getZ()) {
                if (event.getBlock() instanceof BlockRedstoneTorch) {
                    if (this.tickBreakRedstone.get_value(2) == 0) {
                        this.breakBlock(temp[0]);
                        this.lastStage = 2;
                    } else {
                        this.lastStage = 3;
                    }
                } else if (event.getBlock() instanceof BlockAir && this.redstoneDelay.get_value(0) == 0) {
                    this.placeBlock(temp[0], 0.0, 0.0, 0.0, false, false, this.slot_mat[2], -1);
                    Elevator.mc.world.setBlockState(temp[0], Blocks.REDSTONE_TORCH.getDefaultState());
                }
            }
        }, new Predicate[0]);
        this.exd = new ArrayList<EnumFacing>(){
            {
                this.add(EnumFacing.DOWN);
            }
        };
    }

    private void breakBlock(BlockPos pos) {
        EnumFacing side;
        if (this.redstoneBlockMode) {
            Elevator.mc.player.inventory.currentItem = this.slot_mat[3];
        }
        if ((side = BlockUtil.getPlaceableSide(pos)) != null) {
            if (this.rotate.get_value(false)) {
                BlockPos neighbour = pos.offset(side);
                EnumFacing opposite = side.getOpposite();
                Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.0, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
                BlockUtil.faceVectorPacketInstant(hitVec, true);
            }
            Elevator.mc.player.swingArm(EnumHand.MAIN_HAND);
            Elevator.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
            Elevator.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
        }
    }

    @Override
    protected void enable() {
        this.initValues();
        if (this.getAimTarget()) {
            return;
        }
        this.playerChecks();
    }

    @Override
    protected void disable() {
        if (this.isSneaking) {
            Elevator.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Elevator.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        String output = "";
        String materialsNeeded = "";
        if (this.aimTarget == null) {
            output = "No target found...";
        } else if (!this.isHole) {
            output = "The enemy is not in a hole...";
        } else if (!this.enoughSpace) {
            output = "Not enough space...";
        } else if (this.noMaterials) {
            output = "No materials detected...";
            materialsNeeded = this.getMissingMaterials();
        }
        WurstplusMessageUtil.send_client_message(output + "Elevator turned OFF!");
        if (!materialsNeeded.equals("")) {
            WurstplusMessageUtil.send_client_message("Materials missing:" + materialsNeeded);
        }
    }

    String getMissingMaterials() {
        StringBuilder output = new StringBuilder();
        if (this.slot_mat[0] == -1) {
            output.append(" Obsidian");
        }
        if (this.slot_mat[1] == -1) {
            output.append(" Piston");
        }
        if (this.slot_mat[2] == -1) {
            output.append(" Redstone");
        }
        if (this.slot_mat[3] == -1 && this.redstoneBlockMode) {
            output.append(" Pick");
        }
        if (this.slot_mat[4] == -1 && this.forceBurrow.get_value(false)) {
            output.append(" Skull");
        }
        return output.toString();
    }

    @Override
    public void update() {
        if (Elevator.mc.player == null) {
            this.disable();
            return;
        }
        int toWait = 0;
        switch (this.lastStage) {
            case 0: {
                toWait = this.supportDelay.get_value(0);
                break;
            }
            case 1: {
                toWait = this.pistonDelay.get_value(0);
                break;
            }
            case 2: {
                toWait = this.redstoneDelay.get_value(0);
                break;
            }
            case 3: {
                toWait = this.tickBreakRedstone.get_value(2);
                break;
            }
            default: {
                toWait = 0;
            }
        }
        if (this.delayTimeTicks < toWait) {
            ++this.delayTimeTicks;
            return;
        }
        SpoofRotationUtil.ROTATION_UTIL.shouldSpoofAngles(true);
        if (this.enemyCoordsDouble == null || this.aimTarget == null) {
            if (this.aimTarget == null) {
                this.aimTarget = WurstplusPlayerUtil.findLookingPlayer(this.enemyRange.get_value(4.9));
                if (this.aimTarget != null) {
                    if (AnasheClient.get_hack_manager().get_module_with_tag("AutoEz").is_active()) {
                        WurstplusAutoEz.add_target(this.aimTarget.getName());
                    }
                    this.playerChecks();
                }
            }
            return;
        }
        if (this.checkVariable()) {
            return;
        }
        if (this.placeSupport()) {
            BlockPos temp = this.compactBlockPos(1);
            if (BlockUtil.getBlock(temp) instanceof BlockAir) {
                this.placeBlock(temp, this.toPlace.offsetX, this.toPlace.offsetY, this.toPlace.offsetZ, false, true, this.slot_mat[1], this.toPlace.position);
                if (this.continueBlock()) {
                    this.lastStage = 1;
                    return;
                }
            }
            if (BlockUtil.getBlock(temp = this.compactBlockPos(2)) instanceof BlockAir) {
                this.placeBlock(temp, 0.0, 0.0, 0.0, false, false, this.slot_mat[2], -1);
                this.lastStage = 2;
                return;
            }
            if (this.lastStage == 3) {
                this.breakBlock(this.compactBlockPos(2));
                this.lastStage = 2;
            }
        }
    }

    boolean continueBlock() {
        return ++this.blockPlaced == this.blocksPerTick.get_value(4);
    }

    boolean placeSupport() {
        if (this.toPlace.supportBlock > 0) {
            if (this.forceBurrow.get_value(false) && BlockUtil.getBlock(this.aimTarget.getPosition()) instanceof BlockAir) {
                boolean temp = this.redstoneAbovePiston;
                this.redstoneAbovePiston = true;
                this.placeBlock(this.aimTarget.getPosition(), 0.0, 0.0, 0.0, true, false, this.slot_mat[4], -1);
                this.redstoneAbovePiston = temp;
                if (this.continueBlock()) {
                    this.lastStage = 0;
                    return false;
                }
            }
            for (int i = 0; i < this.toPlace.supportBlock; ++i) {
                BlockPos targetPos = this.getTargetPos(i);
                if (!(BlockUtil.getBlock(targetPos) instanceof BlockAir)) continue;
                this.placeBlock(targetPos, 0.0, 0.0, 0.0, false, false, this.slot_mat[0], -1);
                if (!this.continueBlock()) continue;
                this.lastStage = 0;
                return false;
            }
        }
        return true;
    }

    boolean placeBlock(BlockPos pos, double offsetX, double offsetY, double offsetZ, boolean redstone, boolean piston, int slot, int position) {
        Block block = Elevator.mc.world.getBlockState(pos).getBlock();
        EnumFacing side = redstone && this.redstoneAbovePiston ? BlockUtil.getPlaceableSideExlude(pos, this.exd) : BlockUtil.getPlaceableSide(pos);
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5 + offsetX, 0.5, 0.5 + offsetZ).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = Elevator.mc.world.getBlockState(neighbour).getBlock();
        if (Elevator.mc.player.inventory.getStackInSlot(slot) != ItemStack.EMPTY && Elevator.mc.player.inventory.currentItem != slot) {
            if (slot == -1) {
                this.noMaterials = true;
                return false;
            }
            Elevator.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            Elevator.mc.player.inventory.currentItem = slot;
        }
        if (!this.isSneaking && BlockUtil.blackList.contains((Object)neighbourBlock) || BlockUtil.shulkerList.contains((Object)neighbourBlock)) {
            Elevator.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Elevator.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.get_value(false)) {
            BlockUtil.faceVectorPacketInstant(hitVec, true);
        } else if (piston) {
            switch (position) {
                case 0: {
                    Elevator.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(0.0f, 0.0f, Elevator.mc.player.onGround));
                    break;
                }
                case 1: {
                    Elevator.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(180.0f, 0.0f, Elevator.mc.player.onGround));
                    break;
                }
                case 2: {
                    Elevator.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(-90.0f, 0.0f, Elevator.mc.player.onGround));
                    break;
                }
                default: {
                    Elevator.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(90.0f, 0.0f, Elevator.mc.player.onGround));
                }
            }
        }
        Elevator.mc.playerController.processRightClickBlock(Elevator.mc.player, Elevator.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        Elevator.mc.player.swingArm(EnumHand.MAIN_HAND);
        return true;
    }

    BlockPos getTargetPos(int idx) {
        BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(idx));
        return new BlockPos(this.enemyCoordsDouble[0] + (double)offsetPos.getX(), this.enemyCoordsDouble[1] + (double)offsetPos.getY(), this.enemyCoordsDouble[2] + (double)offsetPos.getZ());
    }

    public BlockPos compactBlockPos(int step) {
        BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + step - 1));
        return new BlockPos(this.enemyCoordsDouble[0] + (double)offsetPos.getX(), this.enemyCoordsDouble[1] + (double)offsetPos.getY(), this.enemyCoordsDouble[2] + (double)offsetPos.getZ());
    }

    boolean checkVariable() {
        if (this.noMaterials || !this.isHole || !this.enoughSpace) {
            this.disable();
            return true;
        }
        return false;
    }

    void initValues() {
        this.sur_block = new double[4][3];
        this.slot_mat = new int[]{-1, -1, -1, -1, -1};
        this.enemyCoordsDouble = new double[3];
        this.toPlace = new structureTemp(0.0, 0, null, -1);
        boolean redstoneBlockMode = false;
        this.redstonePlaced = false;
        this.noMaterials = false;
        this.redstoneBlockMode = false;
        this.isHole = true;
        this.aimTarget = null;
        this.lastStage = -1;
        this.delayTimeTicks = 0;
    }

    boolean getMaterialsSlot() {
        if (this.placeMode.in("Block")) {
            this.redstoneBlockMode = true;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Elevator.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (stack.getItem() instanceof ItemPickaxe) {
                this.slot_mat[3] = i;
            } else if (this.forceBurrow.get_value(false) && stack.getItem() instanceof ItemSkull) {
                this.slot_mat[4] = i;
            }
            if (!(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                this.slot_mat[0] = i;
                continue;
            }
            if (block instanceof BlockPistonBase) {
                this.slot_mat[1] = i;
                continue;
            }
            if (!this.placeMode.in("Block") && block instanceof BlockRedstoneTorch) {
                this.slot_mat[2] = i;
                this.redstoneBlockMode = false;
                continue;
            }
            if (this.placeMode.in("Torch") || !block.translationKey.equals("blockRedstone")) continue;
            this.slot_mat[2] = i;
            this.redstoneBlockMode = true;
        }
        int count = 0;
        for (int val : this.slot_mat) {
            if (val == -1) continue;
            ++count;
        }
        if (this.debugMode.get_value(false)) {
            WurstplusMessageUtil.send_client_error_message(String.format("%d %d %d %d", this.slot_mat[0], this.slot_mat[1], this.slot_mat[2], this.slot_mat[3]));
        }
        return count >= 3 + (this.redstoneBlockMode ? 1 : 0) + (this.forceBurrow.get_value(false) ? 1 : 0);
    }

    boolean getAimTarget() {
        this.aimTarget = this.target.in("Nearest") ? WurstplusPlayerUtil.findClosestTarget(this.enemyRange.get_value(4.9), this.aimTarget) : WurstplusPlayerUtil.findLookingPlayer(this.enemyRange.get_value(4.9));
        if (this.aimTarget == null || !this.target.in("Looking")) {
            if (!this.target.in("Looking") && this.aimTarget == null) {
                this.disable();
            }
            return this.aimTarget == null;
        }
        return false;
    }

    void playerChecks() {
        if (this.getMaterialsSlot()) {
            if (this.is_in_hole()) {
                this.enemyCoordsDouble = new double[]{this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ};
                this.enemyCoordsInt = new int[]{(int)this.enemyCoordsDouble[0], (int)this.enemyCoordsDouble[1], (int)this.enemyCoordsDouble[2]};
                this.meCoordsInt = new int[]{(int)Elevator.mc.player.posX, (int)Elevator.mc.player.posY, (int)Elevator.mc.player.posZ};
                this.enoughSpace = this.createStructure();
            } else {
                this.isHole = false;
            }
        } else {
            this.noMaterials = true;
        }
    }

    boolean is_in_hole() {
        this.sur_block = new double[][]{{this.aimTarget.posX + 1.0, this.aimTarget.posY, this.aimTarget.posZ}, {this.aimTarget.posX - 1.0, this.aimTarget.posY, this.aimTarget.posZ}, {this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ + 1.0}, {this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ - 1.0}};
        return HoleUtil.isHole(WurstplusEntityUtil.getPosition((Entity)this.aimTarget), true, true).getType() != HoleUtil.HoleType.NONE;
    }

    boolean createStructure() {
        structureTemp addedStructure = new structureTemp(Double.MAX_VALUE, 0, null, -1);
        for (int i = 0; i < 4; ++i) {
            float offsetZ;
            float offsetX;
            double d = 0;
            double[] pistonCoordsAbs = new double[]{this.sur_block[i][0], this.sur_block[i][1] + 1.0, this.sur_block[i][2]};
            int[] pistonCoordsRel = new int[]{this.disp_surblock[i][0], this.disp_surblock[i][1] + 1, this.disp_surblock[i][2]};
            double distanceNowCrystal = Elevator.mc.player.getDistance(pistonCoordsAbs[0], pistonCoordsAbs[1], pistonCoordsAbs[2]);
            if (!(d < addedStructure.distance) || !(BlockUtil.getBlock(pistonCoordsAbs[0], pistonCoordsAbs[1], pistonCoordsAbs[2]) instanceof BlockAir) && !(BlockUtil.getBlock(pistonCoordsAbs[0], pistonCoordsAbs[1], pistonCoordsAbs[2]) instanceof BlockPistonBase)) continue;
            double[] redstoneCoordsAbs = new double[3];
            int[] redstoneCoordsRel = new int[3];
            double minFound = 1000.0;
            double minNow = -1.0;
            boolean foundOne = false;
            for (int[] pos : this.disp_surblock) {
                double d2 = 0;
                double[] torchCoords = new double[]{pistonCoordsAbs[0] + (double)pos[0], pistonCoordsAbs[1], pistonCoordsAbs[2] + (double)pos[2]};
                minNow = Elevator.mc.player.getDistance(torchCoords[0], torchCoords[1], torchCoords[2]);
                if (!(d2 <= minFound) || PistonCrystal.someoneInCoords(torchCoords[0], torchCoords[2]) || !(BlockUtil.getBlock(torchCoords[0], torchCoords[1], torchCoords[2]) instanceof BlockRedstoneTorch) && !(BlockUtil.getBlock(torchCoords[0], torchCoords[1], torchCoords[2]) instanceof BlockAir)) continue;
                redstoneCoordsAbs = new double[]{torchCoords[0], torchCoords[1], torchCoords[2]};
                redstoneCoordsRel = new int[]{pistonCoordsRel[0] + pos[0], pistonCoordsRel[1], pistonCoordsRel[2] + pos[2]};
                foundOne = true;
                minFound = minNow;
            }
            this.redstoneAbovePiston = false;
            if (!foundOne) {
                if (!this.redstoneBlockMode && BlockUtil.getBlock(pistonCoordsAbs[0], pistonCoordsAbs[1] + 1.0, pistonCoordsAbs[2]) instanceof BlockAir) {
                    redstoneCoordsAbs = new double[]{pistonCoordsAbs[0], pistonCoordsAbs[1] + 1.0, pistonCoordsAbs[2]};
                    redstoneCoordsRel = new int[]{pistonCoordsRel[0], pistonCoordsRel[1] + 1, pistonCoordsRel[2]};
                    this.redstoneAbovePiston = true;
                }
                if (!this.redstoneAbovePiston) continue;
            }
            ArrayList<Vec3d> toPlaceTemp = new ArrayList<Vec3d>();
            int supportBlock = 0;
            if (!this.redstoneBlockMode) {
                if (this.redstoneAbovePiston) {
                    int[] toAdd = this.enemyCoordsInt[0] == (int)pistonCoordsAbs[0] && this.enemyCoordsInt[2] == (int)pistonCoordsAbs[2] ? new int[]{pistonCoordsRel[0], pistonCoordsRel[1], 0} : new int[]{pistonCoordsRel[0], pistonCoordsRel[1], pistonCoordsRel[2]};
                    for (int hight = -1; hight < 2; ++hight) {
                        if (PistonCrystal.someoneInCoords(pistonCoordsAbs[0] + (double)toAdd[0], pistonCoordsAbs[2] + (double)toAdd[2]) || !(BlockUtil.getBlock(pistonCoordsAbs[0] + (double)toAdd[0], pistonCoordsAbs[1] + (double)hight, pistonCoordsAbs[2] + (double)toAdd[2]) instanceof BlockAir)) continue;
                        toPlaceTemp.add(new Vec3d((double)(pistonCoordsRel[0] + toAdd[0]), (double)(pistonCoordsRel[1] + hight), (double)(pistonCoordsRel[2] + toAdd[2])));
                        ++supportBlock;
                    }
                } else if (BlockUtil.getBlock(redstoneCoordsAbs[0], redstoneCoordsAbs[1] - 1.0, redstoneCoordsAbs[2]) instanceof BlockAir) {
                    toPlaceTemp.add(new Vec3d((double)redstoneCoordsRel[0], (double)(redstoneCoordsRel[1] - 1), (double)redstoneCoordsRel[2]));
                    ++supportBlock;
                }
            }
            if (this.trapMode.get_value(false)) {
                toPlaceTemp.addAll(Arrays.asList(new Vec3d[]{new Vec3d(-1.0, -1.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, 2.0, -1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 1.0)}));
                supportBlock += 10;
            }
            toPlaceTemp.add(new Vec3d((double)pistonCoordsRel[0], (double)pistonCoordsRel[1], (double)pistonCoordsRel[2]));
            toPlaceTemp.add(new Vec3d((double)redstoneCoordsRel[0], (double)redstoneCoordsRel[1], (double)redstoneCoordsRel[2]));
            int position = this.disp_surblock[i][0] == 0 ? (this.disp_surblock[i][2] == 1 ? 0 : 1) : (this.disp_surblock[i][0] == 1 ? 2 : 3);
            if (this.disp_surblock[i][0] != 0) {
                offsetX = this.disp_surblock[i][0];
                offsetZ = Elevator.mc.player.getDistanceSq(pistonCoordsAbs[0], pistonCoordsAbs[1], pistonCoordsAbs[2] + 0.5) > Elevator.mc.player.getDistanceSq(pistonCoordsAbs[0], pistonCoordsAbs[1], pistonCoordsAbs[2] - 0.5) ? 0.5f : -0.5f;
            } else {
                offsetZ = this.disp_surblock[i][2];
                offsetX = Elevator.mc.player.getDistanceSq(pistonCoordsAbs[0] + 0.5, pistonCoordsAbs[1], pistonCoordsAbs[2]) > Elevator.mc.player.getDistanceSq(pistonCoordsAbs[0] - 0.5, pistonCoordsAbs[1], pistonCoordsAbs[2]) ? 0.5f : -0.5f;
            }
            float offsetY = this.meCoordsInt[1] - this.enemyCoordsInt[1] == -1 ? 0.0f : 1.0f;
            addedStructure.replaceValues(distanceNowCrystal, supportBlock, toPlaceTemp, -1, offsetX, offsetZ, offsetY, position);
            this.toPlace = addedStructure;
        }
        if (this.debugMode.get_value(false) && addedStructure.to_place != null) {
            WurstplusMessageUtil.send_client_error_message("Skeleton structure:");
            for (Vec3d parte : addedStructure.to_place) {
                WurstplusMessageUtil.send_client_error_message(String.format("%f %f %f", parte.x, parte.y, parte.z));
            }
            WurstplusMessageUtil.send_client_error_message(String.format("X: %f Y: %f Z: %f", Float.valueOf(this.toPlace.offsetX), Float.valueOf(this.toPlace.offsetY), Float.valueOf(this.toPlace.offsetZ)));
        }
        return addedStructure.to_place != null;
    }

    class structureTemp {
        public double distance;
        public int supportBlock;
        public List<Vec3d> to_place;
        public int direction;
        public float offsetX;
        public float offsetY;
        public float offsetZ;
        public int position;

        public structureTemp(double distance, int supportBlock, List<Vec3d> to_place, int position) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = -1;
            this.position = position;
        }

        public void replaceValues(double distance, int supportBlock, List<Vec3d> to_place, int direction, float offsetX, float offsetZ, float offsetY, int position) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = direction;
            this.offsetX = offsetX;
            this.offsetZ = offsetZ;
            this.offsetY = offsetY;
            this.position = position;
        }
    }
}
