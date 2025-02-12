package me.travis.wurstplus.wurstplustwo.hacks.combat;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import me.zero.alpine.fork.listener.EventHandler;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.Listener;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;

public class FastBow extends WurstplusHack{
    public FastBow() {

        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name = "Fast Bow";
        this.tag = "FastBow";
        this.description = "metralleta bow";
    } //                                  xoffu W+3
    WurstplusSetting ticks = create("Ticks", "release", 1.0d, 1.0d, 25.0d);

    @EventHandler
    public Listener<WurstplusEventPacket.SendPacket> listener;


    @Override
    public void update() {
        Double FinalTimeout = ticks.get_value(1) + 1.15D;
        long timeout = FinalTimeout.longValue();
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && (double) mc.player.getItemInUseMaxCount() >= FinalTimeout) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
            mc.player.stopActiveHand();
        }
    }
}