package me.travis.wurstplus.mixins;

import me.travis.mapeadoh.clientstuff.gamesense.TransformSideFirstPersonEvent;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    // idk what is this
    @Inject(method = "transformSideFirstPerson", at = @At("HEAD"))
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        WurstplusEventBus.EVENT_BUS.post(event);
    }
    // noeat setting
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
    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (AnasheClient.get_hack_manager().get_module_with_tag("NoRender").is_active() && AnasheClient.get_setting_manager().get_setting_with_tag("NoRender", "Fire").get_value(true)) {
            info.cancel();
        }
    }
}