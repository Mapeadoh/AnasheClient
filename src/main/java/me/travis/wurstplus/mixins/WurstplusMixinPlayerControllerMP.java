package me.travis.wurstplus.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventBlock;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventDamageBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.travis.wurstplus.AnasheClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class WurstplusMixinPlayerControllerMP
{
	@Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
	private float onPlayerDamageBlockSpeed(final IBlockState state, final EntityPlayer player, final World world, final BlockPos pos) {
		return state.getPlayerRelativeBlockHardness(player, world, pos) * (AnasheClient.get_event_handler().get_tick_rate() / 20.0f);
	}

	@Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
	public void onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable<Boolean> info) {
		final WurstplusEventDamageBlock event_packet = new WurstplusEventDamageBlock(posBlock, directionFacing);
		WurstplusEventBus.EVENT_BUS.post(event_packet);
		if (event_packet.isCancelled()) {
			info.setReturnValue(false);
			info.cancel();
		}
		final WurstplusEventBlock event = new WurstplusEventBlock(4, posBlock, directionFacing);
		WurstplusEventBus.EVENT_BUS.post(event);
	}

	@Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
	private void clickBlockHook(final BlockPos pos, final EnumFacing face, final CallbackInfoReturnable<Boolean> info) {
		final WurstplusEventBlock event = new WurstplusEventBlock(3, pos, face);
		WurstplusEventBus.EVENT_BUS.post(event);
	}
}