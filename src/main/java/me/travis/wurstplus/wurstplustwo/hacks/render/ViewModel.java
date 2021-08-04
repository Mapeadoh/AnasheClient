package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.mapeadoh.clientstuff.gamesense.TransformSideFirstPersonEvent;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel extends WurstplusHack {
    public ViewModel() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name        = "ViewModel";
        this.tag         = "ViewModel";
        this.description = "pasted from gamesense";
    }// works?
    WurstplusSetting mode = create("Mode", "Mode", "Value", combobox("Value",  "Fov", "Both"/*ambos en ingles*/, "ItemFov"));
    WurstplusSetting cancel_eat = create("NoEatAnim", "CancelEatAnim", true);
    WurstplusSetting x_left = create("LeftX", "LeftX", 0.0, -2.0, 2.0);
    WurstplusSetting y_left = create("LeftY", "LeftY", 0.0, -2.0, 2.0);
    WurstplusSetting z_left = create("LeftZ", "LeftZ", 0.0, -2.0, 2.0);

    WurstplusSetting x_right = create("RightX", "RightX", 0.0, -2.0, 2.0);
    WurstplusSetting y_right = create("RightY", "RightY", 0.0, -2.0, 2.0);
    WurstplusSetting z_right = create("RightZ", "RightZ", 0.0, -2.0, 2.0);

    WurstplusSetting itemfov = create("ItemFov", "ItemFov", 130, 70, 200);
    WurstplusSetting normalfov = create("Fov", "Fov", 130, 70, 200);
    private float fov;

    @EventHandler
    private final Listener<TransformSideFirstPersonEvent> eventListener = new Listener<>(event -> {
        if (mode.in("Value") || mode.in("Both")) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(x_right.get_value(1d), y_right.get_value(1d), z_right.get_value(1d));
            } else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
                GlStateManager.translate(x_left.get_value(1d), y_left.get_value(1d), z_left.get_value(1d));
            }
        }
    });

    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier e) {
        if (this.mode.in("ItemFov")) {
            e.setFOV((float)this.itemfov.get_value(1));
        }
    }

    public void enable() {
        WurstplusEventBus.EVENT_BUS.subscribe(this);
        fov = ViewModel.mc.gameSettings.fovSetting;
    }

    public void disable() {
        WurstplusEventBus.EVENT_BUS.unsubscribe(this);
        ViewModel.mc.gameSettings.fovSetting = fov;
    }

    @Override
    public void update() {
        if (ViewModel.mc.world == null) {
            return;
        }
        if (this.mode.in("Fov") || mode.in("Both")) {
            ViewModel.mc.gameSettings.fovSetting = (float)this.normalfov.get_value(1);
        }
    }
}
