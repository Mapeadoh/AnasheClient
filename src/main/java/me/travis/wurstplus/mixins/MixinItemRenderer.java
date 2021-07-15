package me.travis.wurstplus.mixins;

import me.travis.mapeadoh.clientstuff.gamesense.TransformSideFirstPersonEvent;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
//import com.gamesense.client.module.modules.render.NoRender;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.travis.mapeadoh.clientstuff.wp3.events.RenderItemEvent;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Check ViewModel.class for further credits
 */

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    // idk what is this
    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        WurstplusEventBus.EVENT_BUS.post(event);
    }
    // noeat setting, im retard it isnt fov setting
    @Inject(method = "transformEatFirstPerson", at = @At("HEAD"), cancellable = true)
    public void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        WurstplusEventBus.EVENT_BUS.post(event);

        if (AnasheClient.get_hack_manager().get_module_with_tag("ViewModel").is_active() && AnasheClient.get_setting_manager().get_setting_with_tag("ViewModel", "CancelEatAnim").get_value(true)) {
            callbackInfo.cancel();
        }
    }
    // idk what is it
    @Inject(method = "transformFirstPerson", at = @At("HEAD"))
    public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        WurstplusEventBus.EVENT_BUS.post(event);
    }
    // por si los 2 mixins no andan bien juntos, agrego esto por las dudas
    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (AnasheClient.get_hack_manager().get_module_with_tag("NoRender").is_active() && AnasheClient.get_setting_manager().get_setting_with_tag("NoRender", "Fire").get_value(true)) {
            info.cancel();
        }
    }
        @Redirect(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V"))
        public void transformRedirect(ItemRenderer renderer, EnumHandSide hand, float y) {
            RenderItemEvent event = new RenderItemEvent(0.56F, -0.52F + y * -0.6F, -0.72F, -0.56F, -0.52F + y * -0.6F, -0.72F,
                    0.0, 0.0, 1.0, 0.0,
                    0.0, 0.0, 1.0, 0.0,
                    1.0, 1.0, 1.0,
                    1.0, 1.0, 1.0
            );
            WurstplusEventBus.EVENT_BUS.post(event);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(event.getMainX(), event.getMainY(), event.getMainZ());
                GlStateManager.scale(event.getMainHandScaleX(), event.getMainHandScaleY(), event.getMainHandScaleZ());
                GlStateManager.rotate((float) event.getMainRAngel(), (float) event.getMainRx(), (float) event.getMainRy(), (float) event.getMainRz());
            } else {
                GlStateManager.translate(event.getOffX(), event.getOffY(), event.getOffZ());
                GlStateManager.scale(event.getOffHandScaleX(), event.getOffHandScaleY(), event.getOffHandScaleZ());
                GlStateManager.rotate((float) event.getOffRAngel(), (float) event.getOffRx(), (float) event.getOffRy(), (float) event.getOffRz());
            }
        }
// gs no render, no overlay
    /*@Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
    public void renderOverlays(float partialTicks, CallbackInfo callbackInfo) {
        NoRender noRender = ModuleManager.getModule(NoRender.class);

        if (noRender.isEnabled() && noRender.noOverlay.getValue()) {
            callbackInfo.cancel();
        }
    }*/
}