package me.travis.mapeadoh.clientstuff.xulu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;

public class Wrapper {

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    public static World getWorld() {
        return getMinecraft().world;
    }

}
