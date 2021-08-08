package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Quiver
        extends WurstplusHack {
    WurstplusSetting disable = this.create("Toggle", "Toggle", true);
    private int random_variation;

    public Quiver() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "Quiver";
        this.tag = "Quiver";
        this.description = "Shoots good arrows at you";
    }

    @Override
    public void update() {
        if (Quiver.mc.player.inventory.getCurrentItem().getItem() == Items.BOW) {
            Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(0.0f, -90.0f, true));
            if (Quiver.mc.player.getItemInUseMaxCount() >= this.getBowCharge()) {
                Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Quiver.mc.player.getHorizontalFacing()));
                Quiver.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
        if (this.disable.get_value(true)) {
            this.set_disable();
        }
    }

    private int getBowCharge() {
        if (this.random_variation == 0) {
            this.random_variation = 2;
        }
        return 3 + this.random_variation;
    }
}