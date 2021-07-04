package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.util.function.Predicate;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class ManualQuiver extends WurstplusHack
{


    @EventHandler
    public Listener<WurstplusEventPacket.SendPacket> listener;


    public ManualQuiver() {

        super(WurstplusCategory.WURSTPLUS_EXPLOIT);

        this.name = "Manual Quiver";
        this.tag = "ManualQuiver";
        this.description = "quiver but manual";



        this.listener = new Listener<WurstplusEventPacket.SendPacket>(event -> {
            if (event.get_packet() instanceof CPacketPlayerTryUseItem && this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
                this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.mc.player.rotationYaw, -90.0f, this.mc.player.onGround));
            }
        }, (Predicate<WurstplusEventPacket.SendPacket>[])new Predicate[0]);
    }
}
