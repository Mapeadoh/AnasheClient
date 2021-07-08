package me.travis.wurstplus.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import me.travis.wurstplus.wurstplustwo.hacks.render.Skeleton;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer
{
    Minecraft mc = Minecraft.getMinecraft();
    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo callbackInfo)
    {
        if (mc.world != null && mc.player != null && entityIn instanceof EntityPlayer)
        {
            Skeleton.addEntity((EntityPlayer)entityIn, (ModelPlayer) (Object) this);
        }
    }
}