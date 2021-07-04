package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketRespawn;

public class AutoKit extends WurstplusHack{

    public AutoKit() {

        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "Auto Kit";
        this.tag = "AutoKit";
        this.description = "automatically selects a kit";
}


@EventHandler
private final Listener<WurstplusEventPacket.ReceivePacket> receiveListener = new Listener<>(event -> {
    if (event.get_packet() instanceof SPacketRespawn && mc.player.isDead) {
        new Thread( ()->{
            try {
                Thread.sleep(750);
                mc.player.sendChatMessage("/kit");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
});

@Override
protected void enable(){
    WurstplusEventBus.EVENT_BUS.subscribe(this);
}

@Override
protected void disable() {
    WurstplusEventBus.EVENT_BUS.unsubscribe(this);
    }
}
