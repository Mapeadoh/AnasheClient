package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.client.event.InputUpdateEvent;

public class NoSlow extends WurstplusHack {
    public NoSlow() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "No Slow";
        this.tag = "NoSlow";
        this.description = "";
    }
    WurstplusSetting webs = create("IgnoreWeb", "IgnoreWeb", true);
    WurstplusSetting webspeed = create("WebSpeed", "WebSpeed", 2,0,5);

    @EventHandler
    private Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
    });
    public void update(){
        if (this.webs.get_value(true) && NoSlow.mc.player.isInWeb) {
            NoSlow.mc.player.motionX *= webspeed.get_value(1);
            NoSlow.mc.player.motionZ *= webspeed.get_value(1);
        }
    }
}
