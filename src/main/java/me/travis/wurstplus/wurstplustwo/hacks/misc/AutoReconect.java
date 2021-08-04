package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.Listener;
import me.zero.alpine.fork.listener.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import me.travis.mapeadoh.clientstuff.postman.JTimer;
import me.travis.mapeadoh.clientstuff.postman.Event.Era;
import me.travis.mapeadoh.clientstuff.postman.PacketEvent;

import static me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus.EVENT_BUS;

public class AutoReconect extends WurstplusHack {
    public AutoReconect() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "AutoReconnect";
        this.tag = "AutoReconnect";
        this.description = "AutoReconnect";
    }

    public WurstplusSetting delay = create ("Delay", "Delay", 5, 1, 20);

    private String lastIp;
    private int lastPort;
    private boolean reconnect;
    private JTimer timer = new JTimer();

    @EventHandler
    private final Listener<PacketEvent.Send> sendPacketEventPre = new Listener<>(event -> {
        if(event.getEra() == Era.PRE) {
            if(event.getPacket() instanceof C00Handshake) {
                final C00Handshake packet = (C00Handshake) event.getPacket();
                if(packet.getRequestedState() == EnumConnectionState.LOGIN) {
                    this.lastIp = packet.ip;
                    this.lastPort = packet.port;
                }
            }
        }
        if(event.getEra() == Era.POST) {
            if (this.lastIp != null && this.lastPort > 0 && this.reconnect) {
                if (this.timer.hasReached((long) delay.get_value(5))) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(null, Minecraft.getMinecraft(), this.lastIp, this.lastPort));
                    this.timer.reset();
                    this.reconnect = false;
                }
            }
        }
    });

    public void enable() {
        EVENT_BUS.subscribe(this);
    }

    public void disable() {
        EVENT_BUS.unsubscribe(this);
    }

}