package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventEntityRemoved;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.chat.WurstplusAutoEz;
import me.travis.wurstplus.wurstplustwo.util.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NewAC3 extends WurstplusHack {
    WurstplusSetting debug = this.create("Debug", "CaDebug", false);
    WurstplusSetting place_crystal = this.create("Place", "CaPlace", true);
    WurstplusSetting break_crystal = this.create("Break", "CaBreak", true);
    WurstplusSetting break_trys = this.create("Break Attempts", "CaBreakAttempts", 2, 1, 6);
    WurstplusSetting anti_weakness = this.create("Anti-Weakness", "CaAntiWeakness", true);
    WurstplusSetting enemyRange = this.create("Enemy Range", "CaEnemyRange", 9.0, 5.0, 15.0);
    WurstplusSetting hit_range = this.create("Hit Range", "CaHitRange", 5.2f, 1.0, 6.0);
    WurstplusSetting place_range = this.create("Place Range", "CaPlaceRange", 5.2f, 1.0, 6.0);
    WurstplusSetting hit_range_wall = this.create("Range Wall", "CaRangeWall", 4.0, 1.0, 6.0);
    WurstplusSetting wallPlaceRange = this.create("Place Wall Range", "CaPlaceWallRange", 4.0, 0.0, 6.0);
    WurstplusSetting place_delay = this.create("Place Delay", "CaPlaceDelay", 0, 0, 10);
    WurstplusSetting break_delay = this.create("Break Delay", "CaBreakDelay", 2, 0, 10);
    WurstplusSetting min_player_place = this.create("Min Enemy Place", "CaMinEnemyPlace", 8, 0, 20);
    WurstplusSetting min_player_break = this.create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
    WurstplusSetting max_self_damage = this.create("Max Self Damage", "CaMaxSelfDamage", 6, 0, 20);
    WurstplusSetting rotate_mode = this.create("Rotate", "CaRotateMode", "Off", this.combobox("Off", "Old", "Const", "Good"));
    WurstplusSetting raytrace = this.create("Raytrace", "CaRaytrace", false);
    WurstplusSetting sound = create("NoExplodeSound", "NoExplodeSound", false);
    WurstplusSetting switch_mode = this.create("Switch", "CaSwitchMode", "Swap", this.combobox("Swap", "Silent", "Off"));
    WurstplusSetting anti_suicide = this.create("Anti Suicide", "CaAntiSuicide", true);
    WurstplusSetting client_side = this.create("Client Side", "CaClientSide", false);
    WurstplusSetting predict = this.create("Predict", "CaPredict", false);
    WurstplusSetting jumpy_mode = this.create("Jumpy Mode", "CaJumpyMode", false);
    WurstplusSetting anti_stuck = this.create("Anti Stuck", "CaAntiStuck", false);
    WurstplusSetting antiStuckTries = this.create("Anti Stuck Tries", "CaAntiStuckTries", 5, 1, 15);
    WurstplusSetting endcrystal = this.create("1.13 Mode", "CaThirteen", false);
    WurstplusSetting faceplace_mode = this.create("Tabbott Mode", "CaTabbottMode", true);
    WurstplusSetting faceplace_mode_damage = this.create("T Health", "CaTabbottModeHealth", 8, 0, 36);
    WurstplusSetting fuck_armor_mode = this.create("Armor Destroy", "CaArmorDestory", true);
    WurstplusSetting fuck_armor_mode_precent = this.create("Armor %", "CaArmorPercent", 25, 0, 100);
    WurstplusSetting stop_while_mining = this.create("Stop While Mining", "CaStopWhileMining", false);
    WurstplusSetting faceplace_check = this.create("No Sword FP", "CaJumpyFaceMode", false);
    WurstplusSetting swing = this.create("Swing", "CaSwing", "Mainhand", this.combobox("Mainhand", "Offhand", "Both", "None"));
    WurstplusSetting render_mode = this.create("Render", "CaRenderMode", "Pretty", this.combobox("Pretty", "Solid", "Outline", "None"));
    WurstplusSetting old_render = this.create("Old Render", "CaOldRender", false);
    WurstplusSetting future_render = this.create("Future Render", "CaFutureRender", false);
    WurstplusSetting top_block = this.create("Top Block", "CaTopBlock", false);
    WurstplusSetting r = this.create("R", "CaR", 255, 0, 255);
    WurstplusSetting g = this.create("G", "CaG", 255, 0, 255);
    WurstplusSetting b = this.create("B", "CaB", 255, 0, 255);
    WurstplusSetting a = this.create("A", "CaA", 100, 0, 255);
    WurstplusSetting a_out = this.create("Outline A", "CaOutlineA", 255, 0, 255);
    WurstplusSetting rainbow_mode = this.create("Rainbow", "CaRainbow", false);
    WurstplusSetting sat = this.create("Satiation", "CaSatiation", 0.8, 0.0, 1.0);
    WurstplusSetting brightness = this.create("Brightness", "CaBrightness", 0.8, 0.0, 1.0);
    WurstplusSetting height = this.create("Height", "CaHeight", 1.0, 0.0, 1.0);
    WurstplusSetting render_damage = this.create("Render Damage", "RenderDamage", true);
    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals = new ConcurrentHashMap();
    private final List<BlockPos> placePosList = new CopyOnWriteArrayList<BlockPos>();
    private final WurstplusTimer remove_visual_timer = new WurstplusTimer();
    private final WurstplusTimer chain_timer = new WurstplusTimer();
    private EntityPlayer autoez_target = null;
    private String detail_name = null;
    private int detail_hp = 0;
    private BlockPos render_block_init;
    private BlockPos render_block_old;
    private double render_damage_value;
    private float yaw;
    private float pitch;
    private boolean already_attacking = false;
    private boolean is_rotating;
    private boolean did_anything;
    private boolean outline;
    private boolean solid;
    private int place_timeout;
    private int break_timeout;
    private int break_delay_counter;
    private int place_delay_counter;
    private int prev_slot;
    private int chain_step = 0;
    CPacketPlayerTryUseItemOnBlock p2;
    SPacketSoundEffect packet;
    final Iterator<Entity> iterator = null;
    Entity e;
    SPacketSpawnObject spawnPacket;
    CPacketUseEntity useEntity;
    @EventHandler
    private Listener<WurstplusEventEntityRemoved> on_entity_removed = new Listener<WurstplusEventEntityRemoved>(event -> {
        if (event.get_entity() instanceof EntityEnderCrystal) {
            this.attacked_crystals.remove((Object)event.get_entity());
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<WurstplusEventPacket.SendPacket> send_listener = new Listener<WurstplusEventPacket.SendPacket>(event -> {
        CPacketPlayer p;
        if (event.get_packet() instanceof CPacketPlayer && this.is_rotating && this.rotate_mode.in("Old")) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Rotating");
            }
            p = (CPacketPlayer)event.get_packet();
            p.yaw = this.yaw;
            p.pitch = this.pitch;
            this.is_rotating = false;
        }
        if (event.get_packet() instanceof CPacketPlayerTryUseItemOnBlock && this.is_rotating && this.rotate_mode.in("Old")) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Rotating");
            }
            p2 = (CPacketPlayerTryUseItemOnBlock)event.get_packet();
            p2.facingX = (float)this.render_block_init.getX();
            p2.facingY = (float)this.render_block_init.getY();
            p2.facingZ = (float)this.render_block_init.getZ();
            this.is_rotating = false;
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<WurstplusEventMotionUpdate> on_movement = new Listener<WurstplusEventMotionUpdate>(event -> {
        if (event.stage == 0 && (this.rotate_mode.in("Good") || this.rotate_mode.in("Const"))) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("updating rotation");
            }
            WurstplusPosManager.updatePosition();
            WurstplusRotationUtil.updateRotations();
            this.do_ca();
        }
        if (event.stage == 1 && (this.rotate_mode.in("Good") || this.rotate_mode.in("Const"))) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("resetting rotation");
            }
            WurstplusPosManager.restorePosition();
            WurstplusRotationUtil.restoreRotations();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<WurstplusEventPacket.ReceivePacket> receive_listener = new Listener<WurstplusEventPacket.ReceivePacket>(event -> {
        if(sound.get_value(true)){//idk, no explode sound
            if (event.get_packet() instanceof SPacketSoundEffect) {
                packet = (SPacketSoundEffect)event.get_packet();
                if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    NewAC3.mc.world.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        e = iterator.next();
                        if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                            NewAC3.mc.world.removeEntityFromWorld(e.getEntityId());
                        }
                    }
                }
            }
            if (event.get_packet() instanceof SPacketSpawnObject) {
                spawnPacket = (SPacketSpawnObject)event.get_packet();
                if (spawnPacket.getType() == 51) {
                    if (this.placePosList.contains(new BlockPos(spawnPacket.getX(), spawnPacket.getY(), spawnPacket.getZ()).down())) {
                        useEntity = new CPacketUseEntity();
                        useEntity.action = CPacketUseEntity.Action.ATTACK;
                        useEntity.entityId = spawnPacket.getEntityID();
                        NewAC3.mc.getConnection().sendPacket((Packet)useEntity);
                    }
                }
            }
        } else {
            if (event.get_packet() instanceof SPacketSoundEffect) {
                final SPacketSoundEffect packet = (SPacketSoundEffect) event.get_packet();

                if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity e : mc.world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal) {
                            if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                                e.setDead();
                            }
                        }
                    }
                }
            }
        }

        return;
    }, new Predicate[0]);

    public NewAC3() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "CachooxAC";
        this.tag = "CachooxAC";
        this.description = "a";
    }

    public void do_ca() {
        this.did_anything = false;
        if (NewAC3.mc.player == null || NewAC3.mc.world == null) {
            return;
        }
        if (this.rainbow_mode.get_value(true)) {
            this.cycle_rainbow();
        }
        if (this.remove_visual_timer.passed(1000L)) {
            this.remove_visual_timer.reset();
            this.attacked_crystals.clear();
        }
        if (this.check_pause()) {
            return;
        }
        if (this.place_crystal.get_value(true) && this.place_delay_counter > this.place_timeout) {
            this.place_crystal();
        }
        if (this.break_crystal.get_value(true) && this.break_delay_counter > this.break_timeout) {
            this.break_crystal();
        }
        if (!this.did_anything) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            this.autoez_target = null;
            this.is_rotating = false;
        }
        if (this.autoez_target != null) {
            WurstplusAutoEz.add_target(this.autoez_target.getName());
            this.detail_name = this.autoez_target.getName();
            this.detail_hp = Math.round(this.autoez_target.getHealth() + this.autoez_target.getAbsorptionAmount());
        }
        if (this.chain_timer.passed(1000L)) {
            this.chain_timer.reset();
            this.chain_step = 0;
        }
        this.render_block_old = this.render_block_init;
        ++this.break_delay_counter;
        ++this.place_delay_counter;
    }

    @Override
    public void update() {
        if (this.rotate_mode.in("Off") || this.rotate_mode.in("Old")) {
            this.do_ca();
        }
    }

    public void cycle_rainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], this.sat.get_value(1), this.brightness.get_value(1));
        this.r.set_value(color_rgb_o >> 16 & 0xFF);
        this.g.set_value(color_rgb_o >> 8 & 0xFF);
        this.b.set_value(color_rgb_o & 0xFF);
    }

    public EntityEnderCrystal get_best_crystal() {
        double best_damage = 0.0;
        double maximum_damage_self = this.max_self_damage.get_value(1);
        double best_distance = 0.0;
        EntityEnderCrystal best_crystal = null;
        for (Entity c : NewAC3.mc.world.loadedEntityList) {
            if (!(c instanceof EntityEnderCrystal)) continue;
            EntityEnderCrystal crystal = (EntityEnderCrystal)c;
            if (NewAC3.mc.player.getDistance((Entity)crystal) > (float)(!NewAC3.mc.player.canEntityBeSeen((Entity)crystal) ? this.hit_range_wall.get_value(1) : this.hit_range.get_value(1)) || !NewAC3.mc.player.canEntityBeSeen((Entity)crystal) && this.raytrace.get_value(true) || this.attacked_crystals.containsKey((Object)crystal) && this.attacked_crystals.get((Object)crystal) > this.antiStuckTries.get_value(1) && this.anti_stuck.get_value(true)) continue;
            for (EntityPlayer player : NewAC3.mc.world.playerEntities) {
                double self_damage;
                double minimum_damage;
                if (player == NewAC3.mc.player || WurstplusFriendUtil.isFriend(player.getName()) || player.getDistance((Entity)NewAC3.mc.player) >= (float)this.enemyRange.get_value(1) || player.isDead || player.getHealth() <= 0.0f) continue;
                BlockPos player_pos = new BlockPos(player.posX, player.posY, player.posZ);
                boolean no_place = this.faceplace_check.get_value(true) && NewAC3.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double d = minimum_damage = player.getHealth() < (float)this.faceplace_mode_damage.get_value(1) && this.faceplace_mode.get_value(true) && !no_place || this.get_armor_fucker(player) && !no_place || this.predict.get_value(true) && player.onGround && !NewAC3.mc.world.getBlockState(player_pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) ? 2.0 : (double)this.min_player_break.get_value(1);
                double target_damage = CachooxCAUtils.calculateDamage(crystal, (Entity)player);
                if (target_damage < minimum_damage || (self_damage = (double)CachooxCAUtils.calculateDamage(crystal, (Entity)NewAC3.mc.player)) > maximum_damage_self || this.anti_suicide.get_value(true) && (double)(NewAC3.mc.player.getHealth() + NewAC3.mc.player.getAbsorptionAmount()) - self_damage <= 0.5 || !(target_damage > best_damage) || this.jumpy_mode.get_value(true)) continue;
                best_damage = target_damage;
                best_crystal = crystal;
            }
            if (!this.jumpy_mode.get_value(true) || !(NewAC3.mc.player.getDistanceSq((Entity)crystal) > best_distance)) continue;
            best_distance = NewAC3.mc.player.getDistanceSq((Entity)crystal);
            best_crystal = crystal;
        }
        return best_crystal;
    }

    public BlockPos get_best_block() {
        double best_damage = 0.0;
        double maximum_damage_self = this.max_self_damage.get_value(1);
        BlockPos best_block = null;
        List<BlockPos> blocks = CachooxCAUtils.possiblePlacePositions(this.place_range.get_value(1), this.endcrystal.get_value(true));
        for (EntityPlayer target : NewAC3.mc.world.playerEntities) {
            if (WurstplusFriendUtil.isFriend(target.getName())) continue;
            for (BlockPos block : blocks) {
                double self_damage;
                double minimum_damage;
                if (target == NewAC3.mc.player || target.getDistance((Entity)NewAC3.mc.player) >= (float)this.enemyRange.get_value(1) || !WurstplusBlockUtil.rayTracePlaceCheck(block, this.raytrace.get_value(true)) || !WurstplusBlockUtil.canSeeBlock(block) && NewAC3.mc.player.getDistance((double)block.getX(), (double)block.getY(), (double)block.getZ()) > (double)this.wallPlaceRange.get_value(1) || target.isDead || target.getHealth() <= 0.0f) continue;
                boolean no_place = this.faceplace_check.get_value(true) && NewAC3.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double d = minimum_damage = target.getHealth() < (float)this.faceplace_mode_damage.get_value(1) && this.faceplace_mode.get_value(true) && !no_place || this.get_armor_fucker(target) && !no_place ? 2.0 : (double)this.min_player_place.get_value(1);
                double target_damage = CachooxCAUtils.calculateDamage((double)block.getX() + 0.5, (double)block.getY() + 1.0, (double)block.getZ() + 0.5, (Entity)target);
                if (target_damage < minimum_damage || (self_damage = (double)CachooxCAUtils.calculateDamage((double)block.getX() + 0.5, (double)block.getY() + 1.0, (double)block.getZ() + 0.5, (Entity)NewAC3.mc.player)) > maximum_damage_self || this.anti_suicide.get_value(true) && (double)(NewAC3.mc.player.getHealth() + NewAC3.mc.player.getAbsorptionAmount()) - self_damage <= 0.5 || !(target_damage > best_damage)) continue;
                best_damage = target_damage;
                best_block = block;
                this.autoez_target = target;
            }
        }
        blocks.clear();
        this.render_damage_value = best_damage;
        this.render_block_init = best_block;
        return best_block;
    }

    public void place_crystal() {
        BlockPos target_block = this.get_best_block();
        if (target_block == null) {
            return;
        }
        this.place_delay_counter = 0;
        this.already_attacking = false;
        boolean offhand_check = false;
        this.prev_slot = NewAC3.mc.player.inventory.currentItem;
        int crystal_slot = this.find_crystals_hotbar();
        if (NewAC3.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (NewAC3.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
                if (crystal_slot == -1) {
                    return;
                }
                if (this.switch_mode.in("Swap")) {
                    NewAC3.mc.player.inventory.currentItem = crystal_slot;
                    return;
                }
                if (this.switch_mode.in("Silent")) {
                    NewAC3.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(crystal_slot));
                } else if (this.switch_mode.in("Off")) {
                    return;
                }
            }
        } else {
            offhand_check = true;
        }
        if (this.debug.get_value(true)) {
            WurstplusMessageUtil.send_client_message("placing");
        }
        ++this.chain_step;
        this.did_anything = true;
        this.rotate_to_pos(target_block);
        this.chain_timer.reset();
        WurstplusBlockUtil.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        if (this.switch_mode.in("Silent")) {
            NewAC3.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.prev_slot));
        }
    }

    public boolean get_armor_fucker(EntityPlayer p) {
        for (ItemStack stack : p.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) {
                return true;
            }
            float armor_percent = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
            if (!this.fuck_armor_mode.get_value(true) || !((float)this.fuck_armor_mode_precent.get_value(1) >= armor_percent)) continue;
            return true;
        }
        return false;
    }

    public void break_crystal() {
        EntityEnderCrystal crystal = this.get_best_crystal();
        if (crystal == null) {
            return;
        }
        if (this.anti_weakness.get_value(true) && NewAC3.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            boolean should_weakness = true;
            if (NewAC3.mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(NewAC3.mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                should_weakness = false;
            }
            if (should_weakness) {
                if (!this.already_attacking) {
                    this.already_attacking = true;
                }
                int new_slot = -1;
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = NewAC3.mc.player.inventory.getStackInSlot(i);
                    if (!(stack.getItem() instanceof ItemSword) && !(stack.getItem() instanceof ItemTool)) continue;
                    new_slot = i;
                    NewAC3.mc.playerController.updateController();
                    break;
                }
                if (new_slot != -1) {
                    NewAC3.mc.player.inventory.currentItem = new_slot;
                }
            }
        }
        this.did_anything = true;
        this.rotate_to((Entity)crystal);
        for (int i = 0; i < this.break_trys.get_value(1); ++i) {
            WurstplusEntityUtil.attackEntity((Entity)crystal, true, this.swing);
        }
        this.add_attacked_crystal(crystal);
        if (this.client_side.get_value(true)) {
            NewAC3.mc.world.removeEntityFromWorld(crystal.getEntityId());
        }
        this.break_delay_counter = 0;
    }

    public boolean check_pause() {
        if (this.find_crystals_hotbar() == -1 && NewAC3.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            return true;
        }
        if (this.stop_while_mining.get_value(true) && NewAC3.mc.gameSettings.keyBindAttack.isKeyDown() && NewAC3.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        if (AnasheClient.get_hack_manager().get_module_with_tag("Surround").is_active()) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        if (AnasheClient.get_hack_manager().get_module_with_tag("HoleFill").is_active()) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        if (AnasheClient.get_hack_manager().get_module_with_tag("Trap").is_active()) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        return false;
    }

    private int find_crystals_hotbar() {
        for (int i = 0; i < 9; ++i) {
            if (NewAC3.mc.player.inventory.getStackInSlot(i).getItem() != Items.END_CRYSTAL) continue;
            return i;
        }
        return -1;
    }

    private void add_attacked_crystal(EntityEnderCrystal crystal) {
        if (this.attacked_crystals.containsKey((Object)crystal)) {
            int value = this.attacked_crystals.get((Object)crystal);
            this.attacked_crystals.put(crystal, value + 1);
        } else {
            this.attacked_crystals.put(crystal, 1);
        }
    }

    public void rotate_to_pos(BlockPos pos) {
        float[] angle;
        float[] arrf = angle = this.rotate_mode.in("Const") ? WurstplusMathUtil.calcAngle(NewAC3.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() + 0.5f), (double)((float)pos.getZ() + 0.5f))) : WurstplusMathUtil.calcAngle(NewAC3.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() - 0.5f), (double)((float)pos.getZ() + 0.5f)));
        if (this.rotate_mode.in("Off")) {
            this.is_rotating = false;
        }
        if (this.rotate_mode.in("Good") || this.rotate_mode.in("Const")) {
            WurstplusRotationUtil.setPlayerRotations(angle[0], angle[1]);
        }
        if (this.rotate_mode.in("Old")) {
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.is_rotating = true;
        }
    }

    public void rotate_to(Entity entity) {
        float[] angle = WurstplusMathUtil.calcAngle(NewAC3.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        if (this.rotate_mode.in("Off")) {
            this.is_rotating = false;
        }
        if (this.rotate_mode.in("Good")) {
            WurstplusRotationUtil.setPlayerRotations(angle[0], angle[1]);
        }
        if (this.rotate_mode.in("Old") || this.rotate_mode.in("Cont")) {
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.is_rotating = true;
        }
    }

    @Override
    public void render(WurstplusEventRender event) {
        if (this.render_block_init == null) {
            return;
        }
        if (this.render_mode.in("None")) {
            return;
        }
        if (this.render_mode.in("Pretty")) {
            this.outline = true;
            this.solid = true;
        }
        if (this.render_mode.in("Solid")) {
            this.outline = false;
            this.solid = true;
        }
        if (this.render_mode.in("Outline")) {
            this.outline = true;
            this.solid = false;
        }
        this.render_block(this.render_block_init);
        if (this.future_render.get_value(true) && this.render_block_old != null) {
            this.render_block(this.render_block_old);
        }
        if (this.render_damage.get_value(true)) {
            WurstplusRenderUtil.drawText(this.render_block_init, (Math.floor(this.render_damage_value) == this.render_damage_value ? Integer.valueOf((int)this.render_damage_value) : String.format("%.1f", this.render_damage_value)) + "");
        }
    }

    public void render_block(BlockPos pos) {
        BlockPos render_block = this.top_block.get_value(true) ? pos.up() : pos;
        float h = (float)this.height.get_value(1.0);
        if (this.solid) {
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(), render_block.getX(), render_block.getY(), render_block.getZ(), 1.0f, h, 1.0f, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a.get_value(1), "all");
            RenderHelp.release();
        }
        if (this.outline) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), render_block.getX(), render_block.getY(), render_block.getZ(), 1.0f, h, 1.0f, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a_out.get_value(1), "all");
            RenderHelp.release();
        }
    }

    @Override
    public void enable() {
        this.place_timeout = this.place_delay.get_value(1);
        this.break_timeout = this.break_delay.get_value(1);
        this.is_rotating = false;
        this.autoez_target = null;
        this.remove_visual_timer.reset();
        this.detail_name = null;
        this.detail_hp = 20;
    }

    @Override
    public void disable() {
        this.render_block_init = null;
        this.autoez_target = null;
    }

    @Override
    public String array_detail() {
        return this.detail_name != null ? this.detail_name + " | " + this.detail_hp : "None";
    }
}

