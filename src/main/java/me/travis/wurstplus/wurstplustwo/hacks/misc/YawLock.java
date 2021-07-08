package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.mapeadoh.clientstuff.salhack.MinecraftEvent;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;

public class YawLock extends WurstplusHack {
    public YawLock() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "LookLock";
        this.tag = "YawLock";
        this.description = "locks ur yaw and pitch for afk elytrafly";
    }
    WurstplusSetting yawlockboolean = create("YawLock", "YawLock", false);
    WurstplusSetting yawlock = create("Yaw", "Yaw", 0f, 0f, 365f);
    WurstplusSetting pitchlockboolean = create("PitchLock", "PitchLock", false);
    WurstplusSetting pitchlock = create("Pitch", "Pitch", 0f, 0f, 365f);
    WurstplusSetting coordmode = create("CoordLock", "CoordLock", false);
    WurstplusSetting cardinal = create("Cords", "Coords", 0d, 0d, 365d);
    private float yaw;
    private float pitch;

    @Override
    public void enable() {
        super.enable();

        if (mc.player != null)
        {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
        }
    }

    @EventHandler
    private Listener<WurstplusEventMotionUpdate> OnPlayerUpdate = new Listener<>(p_Event ->{
         if (p_Event.getStage() != MinecraftEvent.Stage.Pre)
            return;

            Entity l_Entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

            if (this.yawlockboolean.get_value(true))
                mc.player.rotationYaw = yawlock.get_value(1);

            if (this.pitchlockboolean.get_value(true))
                mc.player.rotationPitch = pitchlock.get_value(1);

            if (coordmode.get_value(true))
                l_Entity.rotationYaw = Math.round((l_Entity.rotationYaw + 1.0f) / 45.0f) * 45.0f;
        });
}