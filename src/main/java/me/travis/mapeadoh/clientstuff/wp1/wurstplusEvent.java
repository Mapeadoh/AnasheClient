package me.travis.mapeadoh.clientstuff.wp1;

import me.zero.alpine.fork.event.type.Cancellable;
import net.minecraft.client.Minecraft;

public class wurstplusEvent extends Cancellable {

    private Era era = Era.PRE;
    private final float partialTicks;

    public wurstplusEvent() {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
    }

    public Era getEra() {
        return era;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public enum Era {
        PRE, PERI, POST
    }

}
