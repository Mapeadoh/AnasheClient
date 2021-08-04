package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.travis.mapeadoh.clientstuff.phobos.Util;
import me.travis.mapeadoh.clientstuff.phobos.MathUtil;
import me.travis.mapeadoh.clientstuff.phobos.BlockUtil;
import me.travis.mapeadoh.clientstuff.phobos.PacketEvent;

public class GodModule extends WurstplusHack {
    public GodModule(){
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "GodModule";
        this.tag = "GodModule";
        this.description = "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";


    }

    WurstplusSetting rotations = create ("Spoofs", "Spoofs",1, 1, 20);
    WurstplusSetting rotate = create ("Rotate", "Rotate",false);
    WurstplusSetting render = create ("Render", "Render",false);
    WurstplusSetting antiIllegal = create ("AntiIllegal","AntiIllegal" ,true);
    WurstplusSetting checkPos = create ("CheckPos", "CheckPos",true);
    WurstplusSetting oneDot15 = create ("1.15", "1.15",false);
    WurstplusSetting entitycheck = create ("EntityCheck","EntityCheck" ,false);
    WurstplusSetting attacks = create ("Attacks", "Attacks",1, 1, 10);
    WurstplusSetting delay = create ("Delay", "Delay",0, 0, 50);

    float yaw = 0.0f;
    float pitch = 0.0f;
    boolean rotating;
    int rotationPacketsSpoofed;
    int highestID = -100000;

    @Override
    public void toggle() {
        this.resetFields();
        if (GodModule.mc.world != null) {
            this.updateEntityID();
        }
    }

    @Override
    public void update() {
        if (this.render.get_value(false)) {
            for (Entity entity : GodModule.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal)) continue;
                entity.setCustomNameTag(String.valueOf(((EntityEnderCrystal) entity).getEntityId()));
                entity.setAlwaysRenderNameTag(true);
            }
        }
    }

    @Override
    public void enable() {
        this.resetFields();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (GodModule.mc.player.getHeldItem(packet.hand).getItem() instanceof ItemEndCrystal) {
                if (this.checkPos.get_value(true) && !BlockUtil.canPlaceCrystal(packet.position, this.entitycheck.get_value(false), this.oneDot15.get_value(false)) || this.checkPlayers()) {
                    return;
                }
                this.updateEntityID();
                for (int i = 1; i < this.attacks.get_value(1); ++i) {
                    this.attackID(packet.position, this.highestID + i);
                }
            }
        }
        if (event.getStage() == 0 && this.rotating && this.rotate.get_value(false) && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.get_value(1)) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
    }

    private void attackID(BlockPos pos, int id) {
        Entity entity = GodModule.mc.world.getEntityByID(id);
        if (entity == null || entity instanceof EntityEnderCrystal) {
            AttackThread attackThread = new AttackThread(id, pos, this.delay.get_value(0), this);
            attackThread.start();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            this.checkID(((SPacketSpawnObject) event.getPacket()).getEntityID());
        } else if (event.getPacket() instanceof SPacketSpawnExperienceOrb) {
            this.checkID(((SPacketSpawnExperienceOrb) event.getPacket()).getEntityID());
        } else if (event.getPacket() instanceof SPacketSpawnPlayer) {
            this.checkID(((SPacketSpawnPlayer) event.getPacket()).getEntityID());
        } else if (event.getPacket() instanceof SPacketSpawnGlobalEntity) {
            this.checkID(((SPacketSpawnGlobalEntity) event.getPacket()).getEntityId());
        } else if (event.getPacket() instanceof SPacketSpawnPainting) {
            this.checkID(((SPacketSpawnPainting) event.getPacket()).getEntityID());
        } else if (event.getPacket() instanceof SPacketSpawnMob) {
            this.checkID(((SPacketSpawnMob) event.getPacket()).getEntityID());
        }
    }

    private void checkID(int id) {
        if (id > this.highestID) {
            this.highestID = id;
        }
    }

    public void updateEntityID() {
        for (Entity entity : GodModule.mc.world.loadedEntityList) {
            if (entity.getEntityId() <= this.highestID) continue;
            this.highestID = entity.getEntityId();
        }
    }

    private boolean checkPlayers() {
        if (this.antiIllegal.get_value(true)) {
            for (EntityPlayer player : GodModule.mc.world.playerEntities) {
                if (!this.checkItem(player.getHeldItemMainhand()) && !this.checkItem(player.getHeldItemOffhand()))
                    continue;
                return false;
            }
        }
        return true;
    }

    private boolean checkItem(ItemStack stack) {
        return stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemExpBottle || stack.getItem() == Items.STRING;
    }

    public void rotateTo(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(GodModule.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(pos));
        this.yaw = angle[0];
        this.pitch = angle[1];
        this.rotating = true;
    }

    private void resetFields() {
        this.rotating = false;
        this.highestID = -1000000;
    }

    public static class AttackThread
            extends Thread {
        private final BlockPos pos;
        private final int id;
        private final int delay;
        private final GodModule godModule;

        public AttackThread(int idIn, BlockPos posIn, int delayIn, GodModule godModuleIn) {
            this.id = idIn;
            this.pos = posIn;
            this.delay = delayIn;
            this.godModule = godModuleIn;
        }

        @Override
        public void run() {
            try {
                this.wait(this.delay);
                CPacketUseEntity attack = new CPacketUseEntity();
                attack.entityId = this.id;
                attack.action = CPacketUseEntity.Action.ATTACK;
                this.godModule.rotateTo(this.pos.up());
                Util.mc.player.connection.sendPacket(attack);
                Util.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}