
package me.travis.mapeadoh.clientstuff.phobos;

import net.minecraft.client.Minecraft;

public interface Util {
    Minecraft mc = Minecraft.getMinecraft();
    boolean nullCheck = (mc.player == null || mc.world == null);
}