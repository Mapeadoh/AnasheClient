
package me.travis.wurstplus.wurstplustwo.hacks.combat;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.mapeadoh.clientstuff.gamesense.*;
import me.travis.mapeadoh.clientstuff.wp3.HoleUtil;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventCancellable;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewPistonAura extends WurstplusHack {
    public NewPistonAura(){
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "NewPistonAura";
        this.tag = "NewPistonAura";
        this.description = "pistonaura for gs++, made by me (PistonCrystal is skided by Warrior)";
    }
    WurstplusSetting breakType = create("Break Types", "AutoPistonBreakTypes", "Swing", combobox("Swing", "Packet"));
    WurstplusSetting placeMode = create("Place Mode", "AutoPistonPlaceMode", "Torch", combobox("Block", "Torch", "Both"));
    WurstplusSetting target = create("Target Mode", "AutoPistonTargetMode", "Nearest", combobox("Nearest", "Looking"));
    WurstplusSetting enemyRange = create("Range", "AutoPistonRange", 4.91, 0.0, 6.0);
    WurstplusSetting crystalDeltaBreak = create("Center Break", "AutoPistonCenterBreak", 1, 0, 5);
    WurstplusSetting crystalPlaceTry = create("CrystalPlaceTry", "PlaceTry", 15,2,30);
    WurstplusSetting blocksPerTick = create("Blocks Per Tick", "AutoPistonBPS", 4, 0, 20);
    WurstplusSetting minHealth = create("MinHealth", "MinHealth", 8, 0, 20);
    WurstplusSetting supBlocksDelay = create("Surround Delay", "AutoPistonSurroundDelay", 4, 0, 20);
    WurstplusSetting preRotation = create("PreRotation", "PreRotation", false);
    WurstplusSetting preRotationDelay = create("PreRotDelay", "PreRotDelay", 0, 0, 20);
    WurstplusSetting afterRotationDelay = create("AfterRotDelay", "AfterRotDelay", 0, 0,20);
    WurstplusSetting startDelay = create("Start Delay", "AutoPistonStartDelay", 4, 0, 20);
    WurstplusSetting pistonDelay = create("Piston Delay", "AutoPistonPistonDelay", 2, 0, 20);
    WurstplusSetting crystalDelay = create("Crystal Delay", "AutoPistonCrystalDelay", 2, 0, 20);
    WurstplusSetting redstoneDelay = create("RedstoneDelay", "RedstoneDelay", 2, 0, 20);
    WurstplusSetting midHitDelay = create("Mid HitDelay", "AutoPistonMidHitDelay", 5, 0, 20);
    WurstplusSetting hitDelay = create("Hit Delay", "AutoPistonHitDelay", 2, 0, 20);
    WurstplusSetting stuckDetector = create("Stuck Check", "AutoPistonStuckDetector", 35, 0, 200);
    WurstplusSetting maxYincr = create("Max Y", "AutoPistonMaxY", 3, 0, 5);
    WurstplusSetting blockPlayer = create("Block Player", "AutoPistonBlockPlayer", true);
    WurstplusSetting rotate = create("Rotate", "AutoPistonRotate", false);
    WurstplusSetting confirmBreak = create("Confirm Break", "AutoPistonConfirmBreak", true);
    WurstplusSetting confirmPlace = create("Confirm Place", "AutoPistonConfirmPlace", true);
    WurstplusSetting allowCheapMode = create("Cheap Mode", "AutoPistonCheapMode", false);
    WurstplusSetting betterPlacement = create("Better Place", "AutoPistonBetterPlace", true);
    WurstplusSetting bypassObsidian = create("Bypass", "AutoPistonBypass", false);
    WurstplusSetting antiWeakness = create("Anti Weakness", "AutoPistonAntiWeakness", false);
    WurstplusSetting debugMode = create("DebugMode", "DebugMode", false);
    WurstplusSetting speedMeter = create("SpeedMeter", "SpeedMeter", false);
    WurstplusSetting packetReducer = create("PacketReducer", "PacketReducer", true);
    WurstplusSetting forceRotation = create("ForceRotation", "ForceRotation", false);
    WurstplusSetting chatMsg = create("Chat Messages", "AutoPistongChatMSG", true);
    private boolean noMaterials = false;
    private boolean hasMoved = false;
    private boolean isSneaking = false;
    private boolean yUnder = false;
    private boolean isHole = true;
    private boolean enoughSpace = true;
    private boolean redstoneBlockMode = false;
    private boolean fastModeActive = false;
    private boolean broken;
    private boolean brokenCrystalBug;
    private boolean brokenRedstoneTorch;
    private boolean stoppedCa;
    private boolean deadPl;
    private boolean rotationPlayerMoved;
    private boolean preRotationBol = false;
    private boolean minHp;
    private boolean itemCrystal;
    private int oldSlot = -1;
    private int stage;
    private int delayTimeTicks;
    private int stuck = 0;
    private int hitTryTick;
    private int round = 0;
    private int nCrystal;
    private int redstoneTickDelay;
    private int preRotationTick;
    private int afterRotationTick;
    private int placeTry;
    private long startTime;
    private long endTime;
    private int[] slot_mat;
    private int[] delayTable;
    private int[] meCoordsInt;
    private int[] enemyCoordsInt;
    private double[] enemyCoordsDouble;
    private structureTemp toPlace;
    int[][] disp_surblock = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    Double[][] sur_block = new Double[4][3];
    private EntityPlayer aimTarget;
    @EventHandler
    private final Listener<WurstplusEventPacket.ReceivePacket> packetReceiveListener = new Listener<WurstplusEventPacket.ReceivePacket>(event -> {
        SPacketSoundEffect packet;
        if (event.get_packet() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.get_packet()).getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE && (int)packet.getX() == this.enemyCoordsInt[0] && (int)packet.getZ() == this.enemyCoordsInt[2]) {
            this.stage = 1;
        }
    }, new Predicate[0]);
    int lenTable;
    Vec3d lastHitVec;
    @EventHandler
    private final Listener<WurstplusEventMotionUpdate> onUpdateWalkingPlayerEventListener = new Listener<WurstplusEventMotionUpdate>(event -> {
        if (event.get_era() != WurstplusEventCancellable.Era.EVENT_PRE || !(this.rotate.get_value(true) || this.lastHitVec == null || !forceRotation.get_value(true))) {
            return;
        }
        Vec2f rotation = RotationUtil.getRotationTo(this.lastHitVec);
    }, new Predicate[0]);
    private final ArrayList<EnumFacing> exd = new ArrayList<EnumFacing>(){
        {
            this.add(EnumFacing.DOWN);
        }
    };
    boolean redstoneAbovePiston;

    @Override
    public void enable() {
        this.initValues();
        if (this.getAimTarget()) {
            return;
        }
        this.playerChecks();
    }

    private boolean getAimTarget() {
        this.aimTarget = (this.target.in("Nearest") ? PlayerUtil.findClosestTarget(enemyRange.get_value(1.0), this.aimTarget) : PlayerUtil.findLookingPlayer(enemyRange.get_value(1.0)));
        if (this.aimTarget == null || !target.in("Looking")) {
            if (!target.in("Looking") && this.aimTarget == null) {
                this.disable();
            }
            return this.aimTarget == null;
        }
        return false;
    }

    private void playerChecks() {
        if (this.getMaterialsSlot()) {
            if (this.is_in_hole()) {
                this.enemyCoordsDouble = new double[]{this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ};
                this.enemyCoordsInt = new int[]{(int)this.enemyCoordsDouble[0], (int)this.enemyCoordsDouble[1], (int)this.enemyCoordsDouble[2]};
                this.meCoordsInt = new int[]{(int)PistonCrystal.mc.player.posX, (int)PistonCrystal.mc.player.posY, (int)PistonCrystal.mc.player.posZ};
                this.antiAutoDestruction();
                this.enoughSpace = this.createStructure();
            } else {
                this.isHole = false;
            }
        } else {
            this.noMaterials = true;
        }
    }

    private void antiAutoDestruction() {
        if (this.redstoneBlockMode || rotate.get_value(true)) {
            this.betterPlacement.set_value(false);
        }
    }

    private void initValues() {
        this.preRotationBol = false;
        this.afterRotationTick = 0;
        this.preRotationTick = 0;
        this.lastHitVec = null;
        this.aimTarget = null;
        this.delayTable = new int[]{(Integer)this.startDelay.get_value(1), (Integer)this.supBlocksDelay.get_value(1), (Integer)this.pistonDelay.get_value(1), (Integer)this.crystalDelay.get_value(1), (Integer)this.hitDelay.get_value(1)};
        this.lenTable = this.delayTable.length;
        this.toPlace = new structureTemp(0.0, 0, null);
        this.minHp = true;
        this.isHole = true;
        this.fastModeActive = false;
        this.redstoneBlockMode = false;
        this.yUnder = false;
        this.brokenRedstoneTorch = false;
        this.brokenCrystalBug = false;
        this.broken = false;
        this.deadPl = false;
        this.rotationPlayerMoved = false;
        this.itemCrystal = false;
        this.hasMoved = false;
        this.slot_mat = new int[]{-1, -1, -1, -1, -1, -1};
        this.stuck = 0;
        this.delayTimeTicks = 0;
        this.stage = 0;
        if (PistonCrystal.mc.player == null) {
            this.disable();
            return;
        }
        if (chatMsg.get_value(true)) {
            PistonCrystal.printDebug("PistonCrystal turned ON!", false);
        }
        this.oldSlot = PistonCrystal.mc.player.inventory.currentItem;
        this.stoppedCa = false;
        if (AnasheClient.get_module_manager().get_module_with_tag("CachooxAC").is_active() || AnasheClient.get_module_manager().get_module_with_tag("NewAutoCrystal").is_active()) {
            AnasheClient.get_module_manager().get_module_with_tag("CachooxAC").set_disable();
            AnasheClient.get_module_manager().get_module_with_tag("NewAutoCrystal").set_disable();
            this.stoppedCa = true;
        }
        if (this.debugMode.get_value(true) || this.speedMeter.get_value(true)) {
            PistonCrystal.printDebug("Started pistonCrystal n^" + ++this.round, false);
            this.startTime = System.currentTimeMillis();
            this.nCrystal = 0;
        }
    }

    @Override
    public void disable() {
        if (PistonCrystal.mc.player == null) {
            return;
        }
        String output = "";
        String materialsNeeded = "";
        if (this.aimTarget == null) {
            output = "No target found...";
        } else if (this.yUnder) {
            output = String.format("Sorry but you cannot be 2+ blocks under the enemy or %d above...", this.maxYincr.get_value(1));
        } else if (this.noMaterials) {
            output = "No Materials Detected...";
            materialsNeeded = this.getMissingMaterials();
        } else if (!this.isHole) {
            output = "The enemy is not in a hole...";
        } else if (!this.enoughSpace) {
            output = "Not enough space...";
        } else if (this.hasMoved) {
            output = "Out of range...";
        } else if (this.deadPl) {
            output = "Enemy is dead, gg! ";
        } else if (this.rotationPlayerMoved) {
            output = "You cannot move from your hole if you have rotation on. ";
        } else if (!this.minHp) {
            output = "Your hp is low";
        } else if (this.itemCrystal) {
            output = "An item is where the crystal should be placed";
        }
        WurstplusMessageUtil.send_client_error_message(output + "PistonCrystal turned OFF!");
        if (!materialsNeeded.equals("")) {
            WurstplusMessageUtil.send_client_error_message("Materials missing:" + materialsNeeded);
        }
        if (this.stoppedCa) {
            this.stoppedCa = false;
        }
        if (this.isSneaking) {
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)PistonCrystal.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        if (this.oldSlot != PistonCrystal.mc.player.inventory.currentItem && this.oldSlot != -1) {
            PistonCrystal.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.noMaterials = false;
        if (this.debugMode.get_value(true) || this.speedMeter.get_value(true)) {
            PistonCrystal.printDebug("Ended pistonCrystal n^" + this.round, false);
        }
    }

    private String getMissingMaterials() {
        StringBuilder output = new StringBuilder();
        if (this.slot_mat[0] == -1) {
            output.append(" Obsidian");
        }
        if (this.slot_mat[1] == -1) {
            output.append(" Piston");
        }
        if (this.slot_mat[2] == -1) {
            output.append(" Crystals");
        }
        if (this.slot_mat[3] == -1) {
            output.append(" Redstone");
        }
        if (this.antiWeakness.get_value(true) && this.slot_mat[4] == -1) {
            output.append(" Sword");
        }
        if (this.redstoneBlockMode && this.slot_mat[5] == -1) {
            output.append(" Pick");
        }
        return output.toString();
    }

    @Override
    public void update() {
        if (PistonCrystal.mc.player == null) {
            this.disable();
            return;
        }
        if (this.stage >= this.lenTable) {
            this.stage = 0;
        }
        if (this.delayTimeTicks < this.delayTable[this.stage]) {
            ++this.delayTimeTicks;
            return;
        }
        this.delayTimeTicks = 0;
        SpoofRotationUtil.ROTATION_UTIL.shouldSpoofAngles(true);
        if (this.enemyCoordsDouble == null || this.aimTarget == null) {
            if (this.aimTarget == null) {
                this.aimTarget = PlayerUtil.findLookingPlayer(this.enemyRange.get_value(1.0));
                if (this.aimTarget != null) {
                    this.playerChecks();
                }
            } else {
                this.checkVariable();
            }
            return;
        }
        if (this.aimTarget.isDead) {
            this.deadPl = true;
        }
        if (PlayerUtil.getHealth() <= (float)(minHealth.get_value(1))) {
            this.minHp = false;
        }
        if (rotate.get_value(true) && (int)PistonCrystal.mc.player.posX != this.meCoordsInt[0] && (int)PistonCrystal.mc.player.posZ != this.meCoordsInt[2]) {
            this.rotationPlayerMoved = true;
        }
        if ((int)this.aimTarget.posX != (int)this.enemyCoordsDouble[0] || (int)this.aimTarget.posZ != (int)this.enemyCoordsDouble[2]) {
            this.hasMoved = true;
        }
        if (this.checkVariable()) {
            return;
        }
        if (this.placeSupport()) {
            switch (this.stage) {
                case 1: {
                    this.placeTry = 0;
                    if (confirmBreak.get_value(true) && (this.checkCrystalPlaceExt(false) || this.checkCrystalPlaceIns() != null)) {
                        this.stage = 4;
                        break;
                    }
                    if (this.checkPistonPlace(false)) {
                        ++this.stage;
                        return;
                    }
                    if (preRotation.get_value(true) && !this.preRotationBol) {
                        this.placeBlockThings(this.stage, false, true, false);
                        if (this.preRotationTick == preRotationDelay.get_value(1)) {
                            this.preRotationBol = true;
                            this.preRotationTick = 0;
                        } else {
                            ++this.preRotationTick;
                            break;
                        }
                    }
                    if (this.afterRotationTick != afterRotationDelay.get_value(1)) {
                        ++this.afterRotationTick;
                        break;
                    }
                    if (debugMode.get_value(true)) {
                        PistonCrystal.printDebug("step 1", false);
                    }
                    if (this.fastModeActive || this.breakRedstone()) {
                        if (!this.fastModeActive || this.checkCrystalPlaceExt(true)) {
                            this.placeBlockThings(this.stage, false, false, false);
                        } else {
                            this.stage = 2;
                            this.afterRotationTick = 0;
                        }
                    }
                    this.preRotationBol = false;
                    break;
                }
                case 2: {
                    if (this.placeTry++ >= crystalPlaceTry.get_value(1)) {
                        this.itemCrystal = true;
                        return;
                    }
                    if (this.afterRotationTick != afterRotationDelay.get_value(1)) {
                        ++this.afterRotationTick;
                        break;
                    }
                    if (preRotation.get_value(true) && !this.preRotationBol) {
                        this.placeBlockThings(this.stage, false, true, false);
                        if (this.preRotationTick == preRotationDelay.get_value(1)) {
                            this.preRotationBol = true;
                            this.preRotationTick = 0;
                            break;
                        }
                        ++this.preRotationTick;
                        break;
                    }
                    if (debugMode.get_value(true)) {
                        PistonCrystal.printDebug("step 2", false);
                    }
                    if (this.fastModeActive || !confirmPlace.get_value(true) || this.checkPistonPlace(true)) {
                        this.placeBlockThings(this.stage, false, false, false);
                    }
                    this.redstoneTickDelay = 0;
                    this.preRotationBol = false;
                    break;
                }
                case 3: {
                    if (this.afterRotationTick != afterRotationDelay.get_value(1)) {
                        ++this.afterRotationTick;
                        break;
                    }
                    if (preRotation.get_value(true) && !this.preRotationBol) {
                        this.placeBlockThings(this.stage, false, true, false);
                        if (this.preRotationTick == preRotationDelay.get_value(1)) {
                            this.preRotationBol = true;
                            this.preRotationTick = 0;
                            break;
                        }
                        ++this.preRotationTick;
                        break;
                    }
                    if (this.redstoneTickDelay++ != redstoneDelay.get_value(1)) {
                        this.delayTimeTicks = 99;
                        break;
                    }
                    this.redstoneTickDelay = 0;
                    if (debugMode.get_value(true)) {
                        PistonCrystal.printDebug("step 3", false);
                    }
                    if (this.fastModeActive || !confirmPlace.get_value(true) || this.checkCrystalPlaceExt(true)) {
                        this.placeBlockThings(this.stage, true, false, false);
                        this.hitTryTick = 0;
                        if (this.fastModeActive && !this.checkPistonPlace(true)) {
                            this.stage = 1;
                        }
                    }
                    this.preRotationBol = false;
                    break;
                }
                case 4: {
                    if (this.afterRotationTick != afterRotationDelay.get_value(1)) {
                        ++this.afterRotationTick;
                        break;
                    }
                    if (debugMode.get_value(true)) {
                        PistonCrystal.printDebug("step 4", false);
                    }
                    this.destroyCrystalAlgo();
                    this.preRotationBol = false;
                    if (!confirmPlace.get_value(true) || !this.checkRedstonePlace()) break;
                    this.stage = 3;
                }
            }
        }
    }

    private boolean checkRedstonePlace() {
        BlockPos targetPosPist = this.compactBlockPos(3);
        return !BlockUtil.getBlock(targetPosPist.getX(), targetPosPist.getY(), targetPosPist.getZ()).getRegistryName().toString().contains("redstone");
    }

    public void destroyCrystalAlgo() {
        Entity crystal = this.checkCrystalPlaceIns();
        if (confirmBreak.get_value(true) && this.broken && crystal == null) {
            this.stuck = 0;
            this.stage = 0;
            this.broken = false;
            if ((debugMode.get_value(true) || speedMeter.get_value(true)) && ++this.nCrystal == 3) {
                this.printTimeCrystals();
            }
        }
        if (crystal != null) {
            this.breakCrystalPiston(crystal);
            if (confirmBreak.get_value(true)) {
                this.broken = true;
            } else {
                this.stuck = 0;
                this.stage = 0;
                if ((debugMode.get_value(true) || this.speedMeter.get_value(true)) && ++this.nCrystal == 3) {
                    this.printTimeCrystals();
                }
            }
        } else if (++this.stuck >= stuckDetector.get_value(1)) {
            if (!this.checkPistonPlace(true)) {
                BlockPos crystPos = this.getTargetPos(this.toPlace.supportBlock + 1);
                PistonCrystal.printDebug(String.format("aim: %d %d", crystPos.getX(), crystPos.getZ()), false);
                Entity crystalF = null;
                for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                    if (!(t instanceof EntityEnderCrystal) || (int)(t.posX - 0.5) != crystPos.getX() || (int)(t.posZ - 0.5) != crystPos.getZ()) continue;
                    crystalF = t;
                }
                if (confirmBreak.get_value(true) && this.brokenCrystalBug && crystalF == null) {
                    this.stuck = 0;
                    this.stage = 0;
                }
                if (crystalF != null) {
                    this.breakCrystalPiston(crystalF);
                    if (confirmBreak.get_value(true)) {
                        this.brokenCrystalBug = true;
                    } else {
                        this.stuck = 0;
                        this.stage = 0;
                    }
                }
                PistonCrystal.printDebug("Stuck detected: piston not placed", true);
                return;
            }
            boolean found = false;
            for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)t.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                found = true;
                break;
            }
            if (!found) {
                BlockPos offsetPosPist = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 2));
                BlockPos pos = new BlockPos(this.aimTarget.getPositionVector()).add(offsetPosPist.getX(), offsetPosPist.getY(), offsetPosPist.getZ());
                if (confirmBreak.get_value(true) && this.brokenRedstoneTorch && BlockUtil.getBlock(pos.getX(), pos.getY(), pos.getZ()) instanceof BlockAir) {
                    this.stage = 1;
                    this.brokenRedstoneTorch = false;
                } else {
                    EnumFacing side = BlockUtil.getPlaceableSide(pos);
                    if (side != null) {
                        this.breakRedstone();
                        if (confirmBreak.get_value(true)) {
                            this.brokenRedstoneTorch = true;
                        } else {
                            this.stage = 1;
                            if ((debugMode.get_value(true) || speedMeter.get_value(true)) && ++this.nCrystal == 3) {
                                this.printTimeCrystals();
                            }
                        }
                        PistonCrystal.printDebug("Stuck detected: crystal not placed", true);
                    }
                }
            } else {
                boolean ext = false;
                for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                    if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)t.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                    ext = true;
                    break;
                }
                if (confirmBreak.get_value(true) && this.brokenCrystalBug && !ext) {
                    this.stuck = 0;
                    this.stage = 0;
                    this.brokenCrystalBug = false;
                }
                if (ext) {
                    this.breakCrystalPiston(crystal);
                    if (confirmBreak.get_value(true)) {
                        this.brokenCrystalBug = true;
                    } else {
                        this.stuck = 0;
                        this.stage = 0;
                    }
                    PistonCrystal.printDebug("Stuck detected: crystal is stuck in the moving piston", true);
                    }
                }
            }
        }
    private void printTimeCrystals() {
        this.endTime = System.currentTimeMillis();
        PistonCrystal.printDebug("3 crystal, time took: " + (this.endTime - this.startTime), false);
        this.nCrystal = 0;
        this.startTime = System.currentTimeMillis();
    }

    private void breakCrystalPiston(Entity crystal) {
        if (this.hitTryTick++ < midHitDelay.get_value(1)) {
            return;
        }
        this.hitTryTick = 0;
        if (antiWeakness.get_value(true)) {
            PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[4];
        }
        if (rotate.get_value(true)) {
            SpoofRotationUtil.ROTATION_UTIL.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)PistonCrystal.mc.player);
        }
        if (forceRotation.get_value(true)) {
            this.lastHitVec = new Vec3d(crystal.posX, crystal.posY, crystal.posZ);
        }
        if (breakType.in("Swing")) {
            WurstplusCrystalUtil.breakCrystal(crystal);
        } else if (breakType.in("Packet")) {
            try {
                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
                PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            catch (NullPointerException nullPointerException) {
                // empty catch block
            }
        }
        if (rotate.get_value(true)) {
            SpoofRotationUtil.ROTATION_UTIL.resetRotation();
        }
    }

    private boolean breakRedstone() {
        BlockPos offsetPosPist = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 2));
        BlockPos pos = new BlockPos(this.aimTarget.getPositionVector()).add(offsetPosPist.getX(), offsetPosPist.getY(), offsetPosPist.getZ());
        if (!(BlockUtil.getBlock(pos.getX(), pos.getY(), pos.getZ()) instanceof BlockAir)) {
            this.breakBlock(pos);
            return false;
        }
        return true;
    }

    private void breakBlock(BlockPos pos) {
        EnumFacing side;
        if (this.redstoneBlockMode) {
            PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[5];
        }
        if ((side = BlockUtil.getPlaceableSide(pos)) != null) {
            if (rotate.get_value(true)) {
                BlockPos neighbour = pos.offset(side);
                EnumFacing opposite = side.getOpposite();
                Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.0, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
                BlockUtil.faceVectorPacketInstant(hitVec, true);
                if (forceRotation.get_value(true)) {
                    this.lastHitVec = hitVec;
                }
            }
            PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
        }
    }

    private boolean checkPistonPlace(boolean decr) {
        BlockPos targetPosPist = this.compactBlockPos(1);
        if (!(BlockUtil.getBlock(targetPosPist.getX(), targetPosPist.getY(), targetPosPist.getZ()) instanceof BlockPistonBase)) {
            if (this.stage != 4 && decr) {
                --this.stage;
            }
            return false;
        }
        return true;
    }

    private boolean checkCrystalPlaceExt(boolean decr) {
        for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
            if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)(this.aimTarget.posX + this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x) || (int)t.posZ != (int)(this.aimTarget.posZ + this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z)) continue;
            return true;
        }
        if (decr) {
            --this.stage;
        }
        return false;
    }

    private Entity checkCrystalPlaceIns() {
        for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
            if (!(t instanceof EntityEnderCrystal) || ((int)t.posX != this.enemyCoordsInt[0] || (int)(t.posZ - crystalDeltaBreak.get_value(1)) != this.enemyCoordsInt[2] && (int)(t.posZ + crystalDeltaBreak.get_value(1)) != this.enemyCoordsInt[2]) && ((int)t.posZ != this.enemyCoordsInt[2] || (int)(t.posX - crystalDeltaBreak.get_value(1)) != this.enemyCoordsInt[0] && (int)(t.posX + crystalDeltaBreak.get_value(1)) != this.enemyCoordsInt[0])) continue;
            return t;
        }
        return null;
    }

    private boolean placeSupport() {
        int checksDone = 0;
        int blockDone = 0;
        if (this.toPlace.supportBlock > 0) {
            do {
                BlockPos targetPos;
                if (!(BlockUtil.getBlock(targetPos = this.getTargetPos(checksDone)) instanceof BlockAir)) continue;
                if (preRotation.get_value(true) && !this.preRotationBol) {
                    if (this.preRotationTick == 0) {
                        this.placeBlockConfirm(targetPos, 0, 0.0, 0.0, 1.0, false, true, false);
                    }
                    if (this.preRotationTick == preRotationDelay.get_value(1)) {
                        this.preRotationBol = true;
                        this.preRotationTick = 0;
                    } else {
                        ++this.preRotationTick;
                        return false;
                    }
                }
                if (!(packetReducer.get_value(true) ? this.placeBlockConfirm(targetPos, 0, 0.0, 0.0, 1.0, false, false, false) : this.placeBlock(targetPos, 0, 0.0, 0.0, 1.0, false))) continue;
                this.preRotationBol = false;
                if (++blockDone != blocksPerTick.get_value(1)) continue;
                return false;
            } while (++checksDone != this.toPlace.supportBlock);
        }
        this.stage = this.stage == 0 ? 1 : this.stage;
        return true;
    }

    private boolean placeBlock(BlockPos pos, int step, double offsetX, double offsetZ, double offsetY, boolean redstone) {
        Block neighbourBlock;
        Vec3d hitVec;
        EnumFacing opposite;
        BlockPos neighbour;
        block16: {
            Block block = PistonCrystal.mc.world.getBlockState(pos).getBlock();
            EnumFacing side = redstone && this.redstoneAbovePiston ? BlockUtil.getPlaceableSideExlude(pos, this.exd) : BlockUtil.getPlaceableSide(pos);
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
                return false;
            }
            if (side == null) {
                return false;
            }
            neighbour = pos.offset(side);
            opposite = side.getOpposite();
            if (!BlockUtil.canBeClicked(neighbour)) {
                return false;
            }
            hitVec = new Vec3d((Vec3i)neighbour).add(0.5 + offsetX, offsetY, 0.5 + offsetZ).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
            neighbourBlock = PistonCrystal.mc.world.getBlockState(neighbour).getBlock();
            try {
                if (this.slot_mat[step] == 11 || PistonCrystal.mc.player.inventory.getStackInSlot(this.slot_mat[step]) != ItemStack.EMPTY) {
                    if (PistonCrystal.mc.player.inventory.currentItem != this.slot_mat[step]) {
                        if (this.slot_mat[step] == -1) {
                            this.noMaterials = true;
                            return false;
                        }
                        PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[step] == 11 ? PistonCrystal.mc.player.inventory.currentItem : this.slot_mat[step];
                    }
                    break block16;
                }
                this.noMaterials = true;
                return false;
            }
            catch (Exception e) {
                PistonCrystal.printDebug("Fatal Error during the creation of the structure. Please, report this bug in the discor's server", true);
                Logger LOGGER = LogManager.getLogger((String)"GameSense");
                LOGGER.error("[PistonCrystal] error during the creation of the structure.");
                if (e.getMessage() != null) {
                    LOGGER.error("[PistonCrystal] error message: " + e.getClass().getName() + " " + e.getMessage());
                } else {
                    LOGGER.error("[PistonCrystal] cannot find the cause");
                }
                boolean i5 = false;
                if (e.getStackTrace().length != 0) {
                    LOGGER.error("[PistonCrystal] StackTrace Start");
                    for (StackTraceElement errorMess : e.getStackTrace()) {
                        LOGGER.error("[PistonCrystal] " + errorMess.toString());
                    }
                    LOGGER.error("[PistonCrystal] StackTrace End");
                }
                PistonCrystal.printDebug(Integer.toString(step), true);
                this.disable();
            }
        }
        if (!this.isSneaking && BlockUtil.blackList.contains((Object)neighbourBlock) || BlockUtil.shulkerList.contains((Object)neighbourBlock)) {
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)PistonCrystal.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (rotate.get_value(true) || step == 1) {
            Vec3d positionHit = hitVec;
            if (!rotate.get_value(true) && step == 1) {
                positionHit = new Vec3d(PistonCrystal.mc.player.posX + offsetX, PistonCrystal.mc.player.posY + (offsetY == -1.0 ? offsetY : 0.0), PistonCrystal.mc.player.posZ + offsetZ);
            }
            BlockUtil.faceVectorPacketInstant(positionHit, true);
        }
        EnumHand handSwing = EnumHand.MAIN_HAND;
        if (this.slot_mat[step] == 11) {
            handSwing = EnumHand.OFF_HAND;
        }
        PistonCrystal.mc.playerController.processRightClickBlock(PistonCrystal.mc.player, PistonCrystal.mc.world, neighbour, opposite, hitVec, handSwing);
        PistonCrystal.mc.player.swingArm(handSwing);
        return true;
    }

    private boolean placeBlockConfirm(BlockPos pos, int step, double offsetX, double offsetZ, double offsetY, boolean redstone, boolean onlyRotation, boolean support) {
        Vec3d hitVec;
        EnumFacing side;
        block16: {
            Block block = PistonCrystal.mc.world.getBlockState(pos).getBlock();
            side = redstone && this.redstoneAbovePiston ? BlockUtil.getPlaceableSideExlude(pos, this.exd) : BlockUtil.getPlaceableSide(pos);
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
            hitVec = new Vec3d((Vec3i)neighbour).add(0.5 + offsetX, 0.5, 0.5 + offsetZ).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
            if (forceRotation.get_value(true)) {
                this.lastHitVec = hitVec;
            }
            try {
                if (this.slot_mat[step] == 11 || PistonCrystal.mc.player.inventory.getStackInSlot(this.slot_mat[step]) != ItemStack.EMPTY) {
                    if (PistonCrystal.mc.player.inventory.currentItem != this.slot_mat[step]) {
                        if (this.slot_mat[step] == -1) {
                            this.noMaterials = true;
                            return false;
                        }
                        PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[step] == 11 ? PistonCrystal.mc.player.inventory.currentItem : this.slot_mat[step];
                    }
                    break block16;
                }
                this.noMaterials = true;
                return false;
            }
            catch (Exception e) {
                PistonCrystal.printDebug("Fatal Error during the creation of the structure. Please, report this bug in the discor's server", true);
                Logger LOGGER = LogManager.getLogger((String)"GameSense");
                LOGGER.error("[PistonCrystal] error during the creation of the structure.");
                if (e.getMessage() != null) {
                    LOGGER.error("[PistonCrystal] error message: " + e.getClass().getName() + " " + e.getMessage());
                } else {
                    LOGGER.error("[PistonCrystal] cannot find the cause");
                }
                boolean i5 = false;
                if (e.getStackTrace().length != 0) {
                    LOGGER.error("[PistonCrystal] StackTrace Start");
                    for (StackTraceElement errorMess : e.getStackTrace()) {
                        LOGGER.error("[PistonCrystal] " + errorMess.toString());
                    }
                    LOGGER.error("[PistonCrystal] StackTrace End");
                }
                PistonCrystal.printDebug(Integer.toString(step), true);
                this.disable();
            }
        }
        Vec3d positionHit = null;
        if (rotate.get_value(true) || step == 1) {
            positionHit = hitVec;
            if (!rotate.get_value(true) && step == 1) {
                positionHit = new Vec3d(PistonCrystal.mc.player.posX + offsetX, PistonCrystal.mc.player.posY + (offsetY == -1.0 ? offsetY : 0.0), PistonCrystal.mc.player.posZ + offsetZ);
            }
        }
        EnumHand handSwing = EnumHand.MAIN_HAND;
        if (this.slot_mat[step] == 11) {
            handSwing = EnumHand.OFF_HAND;
        }
        PlaceUtils.placePrecise(pos, handSwing, step == 1 || (Boolean)this.rotate.get_value(true) && this.forceRotation.get_value(true) == false, positionHit, side, onlyRotation, !support || forceRotation.get_value(true) == false);
        return true;
    }

    public void placeBlockThings(int step, boolean redstone, boolean preRotation, boolean support) {
        BlockPos targetPos = this.compactBlockPos(step);
        if ((packetReducer.get_value(true) ? this.placeBlockConfirm(targetPos, step, this.toPlace.offsetX, this.toPlace.offsetZ, this.toPlace.offsetY, redstone, preRotation, support) : this.placeBlock(targetPos, step, this.toPlace.offsetX, this.toPlace.offsetZ, this.toPlace.offsetY, redstone)) && !preRotation) {
            ++this.stage;
            this.afterRotationTick = 0;
        }
    }

    public BlockPos compactBlockPos(int step) {
        BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + step - 1));
        return new BlockPos(this.enemyCoordsDouble[0] + (double)offsetPos.getX(), this.enemyCoordsDouble[1] + (double)offsetPos.getY(), this.enemyCoordsDouble[2] + (double)offsetPos.getZ());
    }

    private BlockPos getTargetPos(int idx) {
        BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(idx));
        return new BlockPos(this.enemyCoordsDouble[0] + (double)offsetPos.getX(), this.enemyCoordsDouble[1] + (double)offsetPos.getY(), this.enemyCoordsDouble[2] + (double)offsetPos.getZ());
    }

    private boolean checkVariable() {
        if (this.noMaterials || !this.isHole || !this.enoughSpace || this.hasMoved || this.deadPl || this.rotationPlayerMoved || !this.minHp || this.itemCrystal) {
            this.disable();
            return true;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    private boolean createStructure() {
        final structureTemp addedStructure = new structureTemp(Double.MAX_VALUE, 0, null);
        try {
            if (this.meCoordsInt[1] - this.enemyCoordsInt[1] > -1 && this.meCoordsInt[1] - this.enemyCoordsInt[1] <= this.maxYincr.get_value(1)) {
                for (int startH = 1; startH >= 0; --startH) {
                    if (addedStructure.to_place == null) {
                        int incr = 0;
                        final List<Vec3d> highSup = new ArrayList<Vec3d>();
                        while (this.meCoordsInt[1] > this.enemyCoordsInt[1] + incr) {
                            ++incr;
                            for (final int[] cordSupport : this.disp_surblock) {
                                highSup.add(new Vec3d((double)cordSupport[0], (double)incr, (double)cordSupport[2]));
                            }
                        }
                        incr += startH;
                        int i = -1;
                        for (final Double[] cord_b : this.sur_block) {
                            ++i;
                            final double[] crystalCordsAbs = { cord_b[0], cord_b[1] + incr, cord_b[2] };
                            final int[] crystalCordsRel = { this.disp_surblock[i][0], this.disp_surblock[i][1] + incr, this.disp_surblock[i][2] };
                            Label_3196: {
                                final double distanceNowCrystal;
                                if ((distanceNowCrystal = PistonCrystal.mc.player.getDistance(crystalCordsAbs[0], crystalCordsAbs[1], crystalCordsAbs[2])) < addedStructure.distance) {
                                    if (BlockUtil.getBlock(crystalCordsAbs[0], crystalCordsAbs[1], crystalCordsAbs[2]) instanceof BlockAir) {
                                        if (BlockUtil.getBlock(crystalCordsAbs[0], crystalCordsAbs[1] + 1.0, crystalCordsAbs[2]) instanceof BlockAir) {
                                            if (!someoneInCoords(crystalCordsAbs[0], crystalCordsAbs[2])) {
                                                if (BlockUtil.getBlock(crystalCordsAbs[0], crystalCordsAbs[1] - 1.0, crystalCordsAbs[2]) instanceof BlockObsidian || BlockUtil.getBlock(crystalCordsAbs[0], crystalCordsAbs[1] - 1.0, crystalCordsAbs[2]).getRegistryName().getPath().equals("bedrock")) {
                                                    double[] pistonCordAbs = new double[3];
                                                    int[] pistonCordRel = new int[3];
                                                    if (this.rotate.get_value(true) || !this.betterPlacement.get_value(true)) {
                                                        pistonCordAbs = new double[] { crystalCordsAbs[0] + this.disp_surblock[i][0], crystalCordsAbs[1], crystalCordsAbs[2] + this.disp_surblock[i][2] };
                                                        final Block tempBlock;
                                                        if ((tempBlock = BlockUtil.getBlock(pistonCordAbs[0], pistonCordAbs[1], pistonCordAbs[2])) instanceof BlockPistonBase == tempBlock instanceof BlockAir) {
                                                            break Label_3196;
                                                        }
                                                        if (someoneInCoords(pistonCordAbs[0], pistonCordAbs[2])) {
                                                            break Label_3196;
                                                        }
                                                        pistonCordRel = new int[] { crystalCordsRel[0] * 2, crystalCordsRel[1], crystalCordsRel[2] * 2 };
                                                    }
                                                    else {
                                                        double distancePist = Double.MAX_VALUE;
                                                        for (final int[] disp : this.disp_surblock) {
                                                            final BlockPos blockPiston = new BlockPos(crystalCordsAbs[0] + disp[0], crystalCordsAbs[1], crystalCordsAbs[2] + disp[2]);
                                                            final double distanceNowPiston;
                                                            if ((distanceNowPiston = PistonCrystal.mc.player.getDistanceSqToCenter(blockPiston)) <= distancePist) {
                                                                if (BlockUtil.getBlock(blockPiston.getX(), blockPiston.getY(), blockPiston.getZ()) instanceof BlockPistonBase || BlockUtil.getBlock(blockPiston.getX(), blockPiston.getY(), blockPiston.getZ()) instanceof BlockAir) {
                                                                    if (!someoneInCoords(crystalCordsAbs[0] + disp[0], crystalCordsAbs[2] + disp[2])) {
                                                                        if (BlockUtil.getBlock(blockPiston.getX() - crystalCordsRel[0], blockPiston.getY(), blockPiston.getZ() - crystalCordsRel[2]) instanceof BlockAir) {
                                                                            distancePist = distanceNowPiston;
                                                                            pistonCordAbs = new double[] { crystalCordsAbs[0] + disp[0], crystalCordsAbs[1], crystalCordsAbs[2] + disp[2] };
                                                                            pistonCordRel = new int[] { crystalCordsRel[0] + disp[0], crystalCordsRel[1], crystalCordsRel[2] + disp[2] };
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (distancePist == Double.MAX_VALUE) {
                                                            break Label_3196;
                                                        }
                                                    }
                                                    if (this.rotate.get_value(true)) {
                                                        final int[] pistonCordInt = { (int)pistonCordAbs[0], (int)pistonCordAbs[1], (int)pistonCordAbs[2] };
                                                        boolean behindBol = false;
                                                        for (final int checkBehind : new int[] { 0, 2 }) {
                                                            if (this.meCoordsInt[checkBehind] == pistonCordInt[checkBehind]) {
                                                                final int idx = (checkBehind == 2) ? 0 : 2;
                                                                if (pistonCordInt[idx] >= this.enemyCoordsInt[idx] == this.meCoordsInt[idx] >= this.enemyCoordsInt[idx]) {
                                                                    behindBol = true;
                                                                }
                                                            }
                                                        }
                                                        if (!behindBol && Math.abs(this.meCoordsInt[0] - this.enemyCoordsInt[0]) == 2 && Math.abs(this.meCoordsInt[2] - this.enemyCoordsInt[2]) == 2 && ((this.meCoordsInt[0] == pistonCordInt[0] && Math.abs(this.meCoordsInt[2] - pistonCordInt[2]) >= 2) || (this.meCoordsInt[2] == pistonCordInt[2] && Math.abs(this.meCoordsInt[0] - pistonCordInt[0]) >= 2))) {
                                                            behindBol = true;
                                                        }
                                                        if ((!behindBol && Math.abs(this.meCoordsInt[0] - this.enemyCoordsInt[0]) > 2 && this.meCoordsInt[2] != this.enemyCoordsInt[2]) || (Math.abs(this.meCoordsInt[2] - this.enemyCoordsInt[2]) > 2 && this.meCoordsInt[0] != this.enemyCoordsInt[0])) {
                                                            behindBol = true;
                                                        }
                                                        if (behindBol) {
                                                            break Label_3196;
                                                        }
                                                    }
                                                    double[] redstoneCoordsAbs = new double[3];
                                                    int[] redstoneCoordsRel = new int[3];
                                                    double minFound = Double.MAX_VALUE;
                                                    double minNow = -1.0;
                                                    boolean foundOne = true;
                                                    for (final int[] pos : this.disp_surblock) {
                                                        final double[] torchCoords = { pistonCordAbs[0] + pos[0], pistonCordAbs[1], pistonCordAbs[2] + pos[2] };
                                                        if ((minNow = PistonCrystal.mc.player.getDistance(torchCoords[0], torchCoords[1], torchCoords[2])) < minFound) {
                                                            if (!this.redstoneBlockMode || pos[0] == crystalCordsRel[0]) {
                                                                if (!someoneInCoords(torchCoords[0], torchCoords[2]) && (BlockUtil.getBlock(torchCoords[0], torchCoords[1], torchCoords[2]) instanceof BlockRedstoneTorch || BlockUtil.getBlock(torchCoords[0], torchCoords[1], torchCoords[2]) instanceof BlockAir)) {
                                                                    if ((int)torchCoords[0] != (int)crystalCordsAbs[0] || (int)torchCoords[2] != (int)crystalCordsAbs[2]) {
                                                                        boolean torchFront = false;
                                                                        for (final int part : new int[] { 0, 2 }) {
                                                                            final int contPart = (part == 0) ? 2 : 0;
                                                                            if ((int)torchCoords[contPart] == (int)pistonCordAbs[contPart] && (int)torchCoords[part] == this.enemyCoordsInt[part]) {
                                                                                torchFront = true;
                                                                            }
                                                                        }
                                                                        if (!torchFront) {
                                                                            redstoneCoordsAbs = new double[] { torchCoords[0], torchCoords[1], torchCoords[2] };
                                                                            redstoneCoordsRel = new int[] { pistonCordRel[0] + pos[0], pistonCordRel[1], pistonCordRel[2] + pos[2] };
                                                                            foundOne = false;
                                                                            minFound = minNow;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    this.redstoneAbovePiston = false;
                                                    if (foundOne) {
                                                        if (this.redstoneBlockMode || !(BlockUtil.getBlock(pistonCordAbs[0], pistonCordAbs[1] + 1.0, pistonCordAbs[2]) instanceof BlockAir)) {
                                                            break Label_3196;
                                                        }
                                                        redstoneCoordsAbs = new double[] { pistonCordAbs[0], pistonCordAbs[1] + 1.0, pistonCordAbs[2] };
                                                        redstoneCoordsRel = new int[] { pistonCordRel[0], pistonCordRel[1] + 1, pistonCordRel[2] };
                                                        this.redstoneAbovePiston = true;
                                                    }
                                                    if (this.redstoneBlockMode && this.allowCheapMode.get_value(true) && (BlockUtil.getBlock(redstoneCoordsAbs[0], redstoneCoordsAbs[1] - 1.0, redstoneCoordsAbs[2]) instanceof BlockAir || BlockUtil.getBlock(redstoneCoordsAbs[0], redstoneCoordsAbs[1] - 1.0, redstoneCoordsAbs[2]).translationKey.equals("blockRedstone"))) {
                                                        pistonCordAbs = new double[] { redstoneCoordsAbs[0], redstoneCoordsAbs[1], redstoneCoordsAbs[2] };
                                                        pistonCordRel = new int[] { redstoneCoordsRel[0], redstoneCoordsRel[1], redstoneCoordsRel[2] };
                                                        redstoneCoordsAbs = new double[] { redstoneCoordsAbs[0], redstoneCoordsAbs[1] - 1.0, redstoneCoordsRel[2] };
                                                        redstoneCoordsRel = new int[] { redstoneCoordsRel[0], redstoneCoordsRel[1] - 1, redstoneCoordsRel[2] };
                                                        this.fastModeActive = true;
                                                    }
                                                    final List<Vec3d> toPlaceTemp = new ArrayList<Vec3d>();
                                                    int supportBlock = 0;
                                                    if (BlockUtil.getBlock(crystalCordsAbs[0], crystalCordsAbs[1] - 1.0, crystalCordsAbs[2]) instanceof BlockAir) {
                                                        toPlaceTemp.add(new Vec3d((double)crystalCordsRel[0], (double)(crystalCordsRel[1] - 1), (double)crystalCordsRel[2]));
                                                        ++supportBlock;
                                                    }
                                                    if (!this.fastModeActive && BlockUtil.getBlock(pistonCordAbs[0], pistonCordAbs[1] - 1.0, pistonCordAbs[2]) instanceof BlockAir) {
                                                        toPlaceTemp.add(new Vec3d((double)pistonCordRel[0], (double)(pistonCordRel[1] - 1), (double)pistonCordRel[2]));
                                                        ++supportBlock;
                                                    }
                                                    if (!this.fastModeActive) {
                                                        if (this.redstoneAbovePiston) {
                                                            int[] toAdd;
                                                            if (this.enemyCoordsInt[0] == (int)pistonCordAbs[0] && this.enemyCoordsInt[2] == (int)pistonCordAbs[2]) {
                                                                toAdd = new int[] { crystalCordsRel[0], 0, 0 };
                                                            }
                                                            else {
                                                                toAdd = new int[] { crystalCordsRel[0], 0, crystalCordsRel[2] };
                                                            }
                                                            for (int hight = 0; hight < 2; ++hight) {
                                                                if (BlockUtil.getBlock(pistonCordAbs[0] + toAdd[0], pistonCordAbs[1] + hight, pistonCordAbs[2] + toAdd[2]) instanceof BlockAir) {
                                                                    toPlaceTemp.add(new Vec3d((double)(pistonCordRel[0] + toAdd[0]), (double)(pistonCordRel[1] + hight), (double)(pistonCordRel[2] + toAdd[2])));
                                                                    ++supportBlock;
                                                                }
                                                            }
                                                        }
                                                        else if (!this.redstoneBlockMode && BlockUtil.getBlock(redstoneCoordsAbs[0], redstoneCoordsAbs[1] - 1.0, redstoneCoordsAbs[2]) instanceof BlockAir) {
                                                            toPlaceTemp.add(new Vec3d((double)redstoneCoordsRel[0], (double)(redstoneCoordsRel[1] - 1), (double)redstoneCoordsRel[2]));
                                                            ++supportBlock;
                                                        }
                                                    }
                                                    else if (BlockUtil.getBlock(redstoneCoordsAbs[0] - crystalCordsRel[0], redstoneCoordsAbs[1] - 1.0, redstoneCoordsAbs[2] - crystalCordsRel[2]) instanceof BlockAir) {
                                                        toPlaceTemp.add(new Vec3d((double)(redstoneCoordsRel[0] - crystalCordsRel[0]), (double)redstoneCoordsRel[1], (double)(redstoneCoordsRel[2] - crystalCordsRel[2])));
                                                        ++supportBlock;
                                                    }
                                                    toPlaceTemp.add(new Vec3d((double)pistonCordRel[0], (double)pistonCordRel[1], (double)pistonCordRel[2]));
                                                    toPlaceTemp.add(new Vec3d((double)crystalCordsRel[0], (double)crystalCordsRel[1], (double)crystalCordsRel[2]));
                                                    toPlaceTemp.add(new Vec3d((double)redstoneCoordsRel[0], (double)redstoneCoordsRel[1], (double)redstoneCoordsRel[2]));
                                                    if (incr > 1) {
                                                        for (int i2 = 0; i2 < highSup.size(); ++i2) {
                                                            toPlaceTemp.add(0, highSup.get(i2));
                                                            ++supportBlock;
                                                        }
                                                    }
                                                    float offsetX;
                                                    float offsetZ;
                                                    if (this.disp_surblock[i][0] != 0) {
                                                        offsetX = (this.rotate.get_value(true) ? (this.disp_surblock[i][0] / 2.0f) : ((float)this.disp_surblock[i][0]));
                                                        if (this.rotate.get_value(true)) {
                                                            if (PistonCrystal.mc.player.getDistanceSq(pistonCordAbs[0], pistonCordAbs[1], pistonCordAbs[2] + 0.5) > PistonCrystal.mc.player.getDistanceSq(pistonCordAbs[0], pistonCordAbs[1], pistonCordAbs[2] - 0.5)) {
                                                                offsetZ = -0.5f;
                                                            }
                                                            else {
                                                                offsetZ = 0.5f;
                                                            }
                                                        }
                                                        else {
                                                            offsetZ = (float)this.disp_surblock[i][2];
                                                        }
                                                    }
                                                    else {
                                                        offsetZ = (this.rotate.get_value(true) ? (this.disp_surblock[i][2] / 2.0f) : ((float)this.disp_surblock[i][2]));
                                                        if (this.rotate.get_value(true)) {
                                                            if (PistonCrystal.mc.player.getDistanceSq(pistonCordAbs[0] + 0.5, pistonCordAbs[1], pistonCordAbs[2]) > PistonCrystal.mc.player.getDistanceSq(pistonCordAbs[0] - 0.5, pistonCordAbs[1], pistonCordAbs[2])) {
                                                                offsetX = -0.5f;
                                                            }
                                                            else {
                                                                offsetX = 0.5f;
                                                            }
                                                        }
                                                        else {
                                                            offsetX = (float)this.disp_surblock[i][0];
                                                        }
                                                    }
                                                    final float offsetY = (this.meCoordsInt[1] - this.enemyCoordsInt[1] == -1) ? 0.0f : 1.0f;
                                                    addedStructure.replaceValues(distanceNowCrystal, supportBlock, toPlaceTemp, -1, offsetX, offsetZ, offsetY);
                                                    if (this.blockPlayer.get_value(true)) {
                                                        final Vec3d valuesStart = addedStructure.to_place.get(addedStructure.supportBlock + 1);
                                                        final int[] valueBegin = { (int)(-valuesStart.x), (int)valuesStart.y, (int)(-valuesStart.z) };
                                                        if (!this.bypassObsidian.get_value(true) || (int)PistonCrystal.mc.player.posY == this.enemyCoordsInt[1]) {
                                                            addedStructure.to_place.add(0, new Vec3d(0.0, (double)(incr + 1), 0.0));
                                                            addedStructure.to_place.add(0, new Vec3d((double)valueBegin[0], (double)(incr + 1), (double)valueBegin[2]));
                                                            addedStructure.to_place.add(0, new Vec3d((double)valueBegin[0], (double)incr, (double)valueBegin[2]));
                                                            final structureTemp structureTemp = addedStructure;
                                                            structureTemp.supportBlock += 3;
                                                        }
                                                        else {
                                                            addedStructure.to_place.add(0, new Vec3d(0.0, (double)incr, 0.0));
                                                            addedStructure.to_place.add(0, new Vec3d((double)valueBegin[0], (double)incr, (double)valueBegin[2]));
                                                            final structureTemp structureTemp2 = addedStructure;
                                                            structureTemp2.supportBlock += 2;
                                                        }
                                                    }
                                                    this.toPlace = addedStructure;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                this.yUnder = true;
            }
        }
        catch (Exception e) {
            printDebug("Fatal Error during the creation of the structure. Please, report this bug in the discor's server", true);
            final Logger LOGGER = LogManager.getLogger("GameSense");
            LOGGER.error("[PistonCrystal] error during the creation of the structure.");
            if (e.getMessage() != null) {
                LOGGER.error("[PistonCrystal] error message: " + e.getClass().getName() + " " + e.getMessage());
            }
            else {
                LOGGER.error("[PistonCrystal] cannot find the cause");
            }
            int i3 = 0;
            if (e.getStackTrace().length != 0) {
                LOGGER.error("[PistonCrystal] StackTrace Start");
                for (final StackTraceElement errorMess : e.getStackTrace()) {
                    LOGGER.error("[PistonCrystal] " + errorMess.toString());
                }
                LOGGER.error("[PistonCrystal] StackTrace End");
            }
            if (this.aimTarget != null) {
                LOGGER.error("[PistonCrystal] closest target is not null");
            }
            else {
                LOGGER.error("[PistonCrystal] closest target is null somehow");
            }
            for (final Double[] cord_b2 : this.sur_block) {
                if (cord_b2 != null) {
                    LOGGER.error("[PistonCrystal] " + i3 + " is not null");
                }
                else {
                    LOGGER.error("[PistonCrystal] " + i3 + " is null");
                }
                ++i3;
            }
        }
        if (this.debugMode.get_value(true) && addedStructure.to_place != null) {
            printDebug("Skeleton structure:", false);
            for (final Vec3d parte : addedStructure.to_place) {
                printDebug(String.format("%f %f %f", parte.x, parte.y, parte.z), false);
            }
        }
        return addedStructure.to_place != null;
    }

    public static boolean someoneInCoords(double x, double z) {
        int xCheck = (int)x;
        int zCheck = (int)z;
        List<EntityPlayer> playerList = PistonCrystal.mc.world.playerEntities;
        for (EntityPlayer player : playerList) {
            if ((int)player.posX != xCheck || (int)player.posZ != zCheck) continue;
            return true;
        }
        return false;
    }

    private boolean getMaterialsSlot() {
        if (PistonCrystal.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            this.slot_mat[2] = 11;
        }
        if (placeMode.in("Block")) {
            this.redstoneBlockMode = true;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = PistonCrystal.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (this.slot_mat[2] == -1 && stack.getItem() instanceof ItemEndCrystal) {
                this.slot_mat[2] = i;
            } else if (antiWeakness.get_value(true) && stack.getItem() instanceof ItemSword) {
                this.slot_mat[4] = i;
            } else if (stack.getItem() instanceof ItemPickaxe) {
                this.slot_mat[5] = i;
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
            if (!placeMode.in("Block") && block instanceof BlockRedstoneTorch) {
                this.slot_mat[3] = i;
                this.redstoneBlockMode = false;
                continue;
            }
            if (placeMode.in("Torch") || !block.translationKey.equals("blockRedstone")) continue;
            this.slot_mat[3] = i;
            this.redstoneBlockMode = true;
        }
        if (!this.redstoneBlockMode) {
            this.slot_mat[5] = -1;
        }
        int count = 0;
        for (int val : this.slot_mat) {
            if (val == -1) continue;
            ++count;
        }
        if (debugMode.get_value(true)) {
            PistonCrystal.printDebug(String.format("%d %d %d %d %d %d", this.slot_mat[0], this.slot_mat[1], this.slot_mat[2], this.slot_mat[3], this.slot_mat[4], this.slot_mat[5]), false);
        }
        return count >= 4 + (antiWeakness.get_value(true) ? 1 : 0) + (this.redstoneBlockMode ? 1 : 0);
    }

    private boolean is_in_hole() {
        this.sur_block = new Double[][]{{this.aimTarget.posX + 1.0, this.aimTarget.posY, this.aimTarget.posZ}, {this.aimTarget.posX - 1.0, this.aimTarget.posY, this.aimTarget.posZ}, {this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ + 1.0}, {this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ - 1.0}};
        return HoleUtil.isHole(WurstplusEntityUtil.getPosition((Entity)this.aimTarget), true, true).getType() != HoleUtil.HoleType.NONE;
    }

    public static void printDebug(String text, Boolean error) {
        WurstplusMessageUtil.send_client_message((Object)(error != false ? ChatFormatting.GRAY : ChatFormatting.DARK_AQUA ) + text);
    }

    private static class structureTemp {
        public double distance;
        public int supportBlock;
        public List<Vec3d> to_place;
        public int direction;
        public float offsetX;
        public float offsetY;
        public float offsetZ;

        public structureTemp(double distance, int supportBlock, List<Vec3d> to_place) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = -1;
        }

        public void replaceValues(double distance, int supportBlock, List<Vec3d> to_place, int direction, float offsetX, float offsetZ, float offsetY) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = direction;
            this.offsetX = offsetX;
            this.offsetZ = offsetZ;
            this.offsetY = offsetY;
        }
    }
}
