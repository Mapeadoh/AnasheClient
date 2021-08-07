package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.mapeadoh.clientstuff.phobos.*;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.misc.Freecam;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.Random;

public class PhobosSpeed extends WurstplusHack {

    WurstplusSetting mode = create("Mode", "Mode", "Instant", combobox("Instant", "onGround", "Accel", "Vanilla", "Boost"));
    WurstplusSetting strafeJump = create("Jump", "Jump", false);
    WurstplusSetting noShake = create("NoShake", "NoShake", false);
    WurstplusSetting useTimer = create("Timer", "TImer", false);
    WurstplusSetting zeroSpeed = create("0-Speed", "0--Speed", 0.0, 0.0, 100.0);
    WurstplusSetting speed = create("Speed", "Speed", 10.0, 0.1, 100.0);
    WurstplusSetting blocked = create("Blocked", "Blocked", 10.0, 0.0, 100.0);
    WurstplusSetting unblocked = create("Unblocked", "Unblocked", 10.0, 0.0, 100.0);
    public double startY = 0.0;
    public boolean antiShake = false;
    public double minY = 0.0;
    public boolean changeY = false;
    private double highChainVal = 0.0;
    private double lowChainVal = 0.0;
    private boolean oneTime = false;
    private double bounceHeight = 0.4;
    private float move = 0.26f;
    private int vanillaCounter = 0;
    WurstplusTimer timer = new WurstplusTimer();

    public PhobosSpeed() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Speed";
        this.tag = "PhobosSpeed";
        this.description = "speed from phobos";

    }

    private boolean shouldReturn() {
        return AnasheClient.get_module_manager().getModule(Freecam.class).is_active() || AnasheClient.get_module_manager().getModule(ElytraFly.class).is_active() || AnasheClient.get_module_manager().getModule(WurstplusStrafe.class).is_active() || AnasheClient.get_module_manager().getModule(PacketFly.class).is_active();
    }

    @Override
    public void update() {
        if (this.shouldReturn() || mc.player.isSneaking() || mc.player.isInWater() || mc.player.isInLava()) {
            return;
        }
        switch (this.mode.get_current_value()) {
            case "Boost": {
                this.doBoost();
                break;
            }
            case "Accel": {
                this.doAccel();
                break;
            }
            case "onGround": {
                this.doOnground();
                break;
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (!this.mode.in("Vanilla") || mc.world != null && mc.player != null) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                this.vanillaCounter = this.vanilla() ? ++this.vanillaCounter : 0;
                if (this.vanillaCounter != 4) break;
                this.changeY = true;
                this.minY = mc.player.getEntityBoundingBox().minY + (mc.world.getBlockState(mc.player.getPosition()).getMaterial().blocksMovement() ? -this.blocked.get_value(1.0) / 10.0 : this.unblocked.get_value(1.0) / 10.0) + this.getJumpBoostModifier();
                return;
            }
            case 1: {
                if (this.vanillaCounter == 3) {
                    mc.player.motionX *= this.zeroSpeed.get_value(1.0) / 10.0;
                    mc.player.motionZ *= this.zeroSpeed.get_value(1.0) / 10.0;
                    break;
                }
                if (this.vanillaCounter != 4) break;
                mc.player.motionX /= this.speed.get_value(1.0) / 10.0;
                mc.player.motionZ /= this.speed.get_value(1.0) / 10.0;
                this.vanillaCounter = 2;
            }
        }
    }

    private double getJumpBoostModifier() {
        double boost = 0.0;
        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier();
            boost *= 1.0 + 0.2 * (double) amplifier;
        }
        return boost;
    }

    private boolean vanillaCheck() {
        if (mc.player.onGround) {
            // empty if block
        }
        return false;
    }

    private boolean vanilla() {
        return mc.player.onGround;
    }

    private void doBoost() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (mc.player.onGround) {
            this.startY = mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed(mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving(mc.player) && !mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid(mc.player) && BlockUtil.isBlockBelowEntitySolid(mc.player)) {
            this.oneTime = true;
            this.antiShake = this.noShake.get_value(true) && mc.player.getRidingEntity() == null;
            Random random = new Random();
            boolean rnd = random.nextBoolean();
            if (mc.player.posY >= this.startY + this.bounceHeight) {
                mc.player.motionY = -this.bounceHeight;
                this.lowChainVal += 1.0;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.15f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.2f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.225f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.25f;
                }
                if (this.lowChainVal >= 7.0) {
                    this.move = 0.27895f;
                }
                if (this.useTimer.get_value(true)) {
                    timer.setTimer(1.0f);
                }
            }
            if (mc.player.posY == this.startY) {
                mc.player.motionY = this.bounceHeight;
                this.highChainVal += 1.0;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.325f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.4f;
                }
                if (this.highChainVal >= 6.0) {
                    this.move = 0.43395f;
                }
                if (this.useTimer.get_value(true)) {
                    if (rnd) {
                        timer.setTimer(1.3f);
                    } else {
                        timer.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, mc.player);
        } else {
            if (this.oneTime) {
                mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.antiShake = false;
            this.speedOff();
        }
    }

    private void doAccel() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (mc.player.onGround) {
            this.startY = mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed(mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving(mc.player) && !mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid(mc.player) && BlockUtil.isBlockBelowEntitySolid(mc.player)) {
            this.oneTime = true;
            this.antiShake = this.noShake.get_value(true) && mc.player.getRidingEntity() == null;
            Random random = new Random();
            boolean rnd = random.nextBoolean();
            if (mc.player.posY >= this.startY + this.bounceHeight) {
                mc.player.motionY = -this.bounceHeight;
                this.lowChainVal += 1.0;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.get_value(true)) {
                    timer.setTimer(1.0f);
                }
            }
            if (mc.player.posY == this.startY) {
                mc.player.motionY = this.bounceHeight;
                this.highChainVal += 1.0;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.175f;
                }
                if (this.useTimer.get_value(true)) {
                    if (rnd) {
                        timer.setTimer(1.3f);
                    } else {
                        timer.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, mc.player);
        } else {
            if (this.oneTime) {
                mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }

    private void doOnground() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (mc.player.onGround) {
            this.startY = mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed(mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving(mc.player) && !mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid(mc.player) && BlockUtil.isBlockBelowEntitySolid(mc.player)) {
            this.oneTime = true;
            this.antiShake = this.noShake.get_value(true) && mc.player.getRidingEntity() == null;
            Random random = new Random();
            boolean rnd = random.nextBoolean();
            if (mc.player.posY >= this.startY + this.bounceHeight) {
                mc.player.motionY = -this.bounceHeight;
                this.lowChainVal += 1.0;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.get_value(true)) {
                    timer.setTimer(1.0f);
                }
            }
            if (mc.player.posY == this.startY) {
                mc.player.motionY = this.bounceHeight;
                this.highChainVal += 1.0;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.2f;
                }
                if (this.useTimer.get_value(true)) {
                    if (rnd) {
                        timer.setTimer(1.3f);
                    } else {
                        timer.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, mc.player);
        } else {
            if (this.oneTime) {
                mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }

    @Override
    public void disable() {
        if (this.mode.in("onGround") || this.mode.in("Boost")) {
            mc.player.motionY = -0.1;
        }
        this.changeY = false;
        timer.setTimer(1.0f);
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.antiShake = false;
    }

    @SubscribeEvent
    public void onMode(MoveEvent event) {
        if (!(this.shouldReturn() || event.getStage() != 0 || !this.mode.in("Instant") || mc.world != null && mc.player != null || mc.player.isSneaking() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f)) {
            if (mc.player.onGround && this.strafeJump.get_value(true)) {
                mc.player.motionY = 0.4;
                event.setY(0.4);
            }
            MovementInput movementInput = mc.player.movementInput;
            float moveForward = movementInput.moveForward;
            float moveStrafe = movementInput.moveStrafe;
            float rotationYaw = mc.player.rotationYaw;
            if ((double) moveForward == 0.0 && (double) moveStrafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            } else {
                if ((double) moveForward != 0.0) {
                    if ((double) moveStrafe > 0.0) {
                        rotationYaw += (float) ((double) moveForward > 0.0 ? -45 : 45);
                    } else if ((double) moveStrafe < 0.0) {
                        rotationYaw += (float) ((double) moveForward > 0.0 ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    float f = moveForward == 0.0f ? moveForward : (moveForward = (double) moveForward > 0.0 ? 1.0f : -1.0f);
                }
                moveStrafe = moveStrafe == 0.0f ? moveStrafe : ((double) moveStrafe > 0.0 ? 1.0f : -1.0f);
                event.setX((double) moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + (double) moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ((double) moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - (double) moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }

    private void speedOff() {
        float yaw = (float) Math.toRadians(mc.player.rotationYaw);
        if (BlockUtil.isBlockAboveEntitySolid(mc.player)) {
            if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && mc.player.onGround) {
                mc.player.motionX -= (double) MathUtil.sin(yaw) * 0.15;
                mc.player.motionZ += (double) MathUtil.cos(yaw) * 0.15;
            }
        } else if (mc.player.collidedHorizontally) {
            if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && mc.player.onGround) {
                mc.player.motionX -= (double) MathUtil.sin(yaw) * 0.03;
                mc.player.motionZ += (double) MathUtil.cos(yaw) * 0.03;
            }
        } else if (!BlockUtil.isBlockBelowEntitySolid(mc.player)) {
            if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && mc.player.onGround) {
                mc.player.motionX -= (double) MathUtil.sin(yaw) * 0.03;
                mc.player.motionZ += (double) MathUtil.cos(yaw) * 0.03;
            }
        } else {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
    }
}