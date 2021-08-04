package me.travis.wurstplus.wurstplustwo.hacks.movement;


import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import net.minecraft.network.play.client.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class LongJump extends WurstplusHack {
    public LongJump() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Long Jump";
        this.tag = "LongJump";
        this.description = "jumps longer";
    }

    WurstplusSetting speed = create("Speed", "LGSpeed", 30.0, 1.0, 100.0);
    WurstplusSetting packet = create("Packet", "LGPacket", false);
    WurstplusSetting toggle = create("Toggle", "LGToggle", false);


    private static boolean jumped = false;
    private static boolean boostable = false;



    @Override
    public void update(){
        if (mc.player == null || mc.world == null) return;


        if (jumped)
        {
            if (mc.player.onGround || mc.player.capabilities.isFlying)
            {
                jumped = false;

                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;

                if (packet.get_value(true))
                {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
                }


                if (this.toggle.get_value(true)) {
                    toggle();
                }
                return;
            }

            if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f)) return;
            double yaw = getDirection();
            mc.player.motionX = -Math.sin(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? (speed.get_value(0) / 10f) : 1f));
            mc.player.motionZ = Math.cos(yaw) * (((float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)) * (boostable ? (speed.get_value(0) / 10f) : 1f));



            boostable = false;
            if (this.toggle.get_value(true)) {
                toggle();
            }
        }

        if (!(mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f) && jumped)
        {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {


        if ((mc.player != null && mc.world != null) && event.getEntity() == mc.player && (mc.player.movementInput.moveForward != 0f || mc.player.movementInput.moveStrafe != 0f))
        {
            jumped = true;
            boostable = true;
        }
    }

    private double getDirection()
    {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0f) rotationYaw += 180f;

        float forward = 1f;

        if (mc.player.moveForward < 0f) forward = -0.5f;
        else if (mc.player.moveForward > 0f) forward = 0.5f;

        if (mc.player.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (mc.player.moveStrafing < 0f) rotationYaw += 90f * forward;

        return Math.toRadians(rotationYaw);
    }

}

