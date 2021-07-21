package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class PerspectiveMod extends WurstplusHack
{
    public static PerspectiveMod INSTANCE;
    public float cameraPitch;
    public float cameraYaw;

    public PerspectiveMod() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "360Perspective";
        this.tag = "PerspectiveMod";
        this.description = "pasted from floppahack again";
        PerspectiveMod.INSTANCE = this;
    }

    @Override
    public void disable() {
        if (PerspectiveMod.mc.player != null && PerspectiveMod.mc.gameSettings.thirdPersonView == 1) {
            PerspectiveMod.mc.gameSettings.thirdPersonView = 0;
        }
    }

    @Override
    public void update() {
        if (PerspectiveMod.mc.player != null && PerspectiveMod.mc.gameSettings.thirdPersonView != 1) {
            PerspectiveMod.mc.gameSettings.thirdPersonView = 1;
        }
    }

    @SubscribeEvent
    public void onKey(final InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(this.get_keybind())) {
            this.cameraPitch = PerspectiveMod.mc.player.rotationPitch;
            this.cameraYaw = PerspectiveMod.mc.player.rotationYaw;
            PerspectiveMod.mc.gameSettings.thirdPersonView = (this.is_active() ? 1 : 0);
        }
    }

    @SubscribeEvent
    public void cameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        if (this.is_active()) {
            event.setPitch(this.cameraPitch);
            event.setYaw(this.cameraYaw);
        }
    }
}