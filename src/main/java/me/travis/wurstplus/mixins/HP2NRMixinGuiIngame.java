package me.travis.wurstplus.mixins;

import me.travis.wurstplus.AnasheClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngame.class})
public class HP2NRMixinGuiIngame
        extends Gui {
    @Inject(method={"renderPortal"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPortalHook(float n, ScaledResolution scaledResolution, CallbackInfo info) {
        if (AnasheClient.get_hack_manager().get_module_with_tag("NoRender").is_active() && AnasheClient.get_setting_manager().get_setting_with_tag("NoRender", "PortalOverlay").get_value(true)) {
            info.cancel();
        }
    }

    @Inject(method={"renderPumpkinOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo info) {
        if (AnasheClient.get_hack_manager().get_module_with_tag("NoRender").is_active() && AnasheClient.get_setting_manager().get_setting_with_tag("NoRender", "Pumpkin").get_value(true)) {
            info.cancel();
        }
    }

    @Inject(method={"renderPotionEffects"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo info) {
        if (AnasheClient.get_hack_manager().get_module_with_tag("NoRender").is_active() && AnasheClient.get_setting_manager().get_setting_with_tag("NoRender", "PotionIcons").get_value(true)) {
            info.cancel();
        }
    }
}

