package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

import java.util.Random;

public class AntiAFK extends WurstplusHack {
    public AntiAFK() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "AntiAFKKick";
        this.tag = "AntiAFKKick";
        this.description = "do not kick per afk";
    }
    WurstplusSetting msg = create("ChatMSG", "ChatMSG", "StatsCom", combobox("StatsCom", "GlobalMSG"));
    WurstplusSetting jump = create("Jump", "Jump", true);
    WurstplusSetting hit = create("Hit", "Hit", true);
    WurstplusSetting rotate = create("Rotate", "Rotate", true);
    private final Random random = new Random();

    @Override
    public void update(){
        if (AntiAFK.mc.playerController.getIsHittingBlock()) {
            return;
        }
        if (AntiAFK.mc.player.ticksExisted % 40 == 0 && this.hit.get_value(true)) {
            AntiAFK.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (AntiAFK.mc.player.ticksExisted % 20 == 0 && this.rotate.get_value(true)) {
            AntiAFK.mc.player.rotationYaw = this.random.nextInt(360) - 180;
        }
        if (AntiAFK.mc.player.ticksExisted % 60== 0 && this.msg.in("StatsCom")) {
            AntiAFK.mc.player.sendChatMessage("/stats");
        }
        if (AntiAFK.mc.player.ticksExisted % 60 == 0 && this.msg.in("GlobalMSG")) {
            AntiAFK.mc.player.sendChatMessage("i was afk, dont attack me pls D:");
        }
        if (!this.rotate.get_value(true) && !this.jump.get_value(true) && AntiAFK.mc.player.ticksExisted % 80 == 0) {
            AntiAFK.mc.player.jump();
        }
    }
}
