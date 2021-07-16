package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;


public class NoFall extends WurstplusHack
{
    public NoFall() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "No Fall";
        this.tag = "NoFall";
        this.description = "MrCoffee404";
    }

    WurstplusSetting mode = create("Mode", "Mode", "Packet", combobox("Packet", "Bucket"));
    WurstplusSetting distance = create("BucketDistance", "NoFallDistance", 15, 0, 100);;
    private long last = 0L;
    @EventHandler
    public Listener<WurstplusEventPacket.SendPacket> sendListener = new Listener<WurstplusEventPacket.SendPacket>(event -> {
        if (AnasheClient.get_module_manager().get_module_with_tag("SalEFly").is_active()) {
            return;
        }
        else {
            if (event.get_packet() instanceof CPacketPlayer && mode.in("Packet")) {
                ((CPacketPlayer)event.get_packet()).onGround = true;
            }
            return;
        }
    }, (Predicate<WurstplusEventPacket.SendPacket>[])new Predicate[0]);

    @Override
    public void update() {
        if (AnasheClient.get_module_manager().get_module_with_tag("SalEFly").is_active()) {
            return;
        }
        if (mode.in("Bucket") && mc.player.fallDistance >= this.distance.get_value(1) && !WurstplusEntityUtil.isLiving((Entity)mc.player) && System.currentTimeMillis() - this.last > 100L) {
            final Vec3d posVec = mc.player.getPositionVector();
            final RayTraceResult result = mc.world.rayTraceBlocks(posVec, posVec.add(0.0, -5.329999923706055, 0.0), true, true, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                EnumHand hand = EnumHand.MAIN_HAND;
                if (NoFall.mc.player.getHeldItemOffhand().getItem() == Items.WATER_BUCKET) {
                    hand = EnumHand.OFF_HAND;
                }
                else if (NoFall.mc.player.getHeldItemMainhand().getItem() != Items.WATER_BUCKET) {
                    for (int i = 0; i < 9; ++i) {
                        if (NoFall.mc.player.inventory.getStackInSlot(i).getItem() == Items.WATER_BUCKET) {
                            NoFall.mc.player.inventory.currentItem = i;
                            NoFall.mc.player.rotationPitch = 90.0f;
                            this.last = System.currentTimeMillis();
                            return;
                        }
                    }
                    return;
                }
                NoFall.mc.player.rotationPitch = 90.0f;
                NoFall.mc.playerController.processRightClick((EntityPlayer) NoFall.mc.player, (World) NoFall.mc.world, hand);
                this.last = System.currentTimeMillis();
            }
        }
    }
}
