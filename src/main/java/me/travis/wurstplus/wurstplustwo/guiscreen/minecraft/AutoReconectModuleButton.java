package me.travis.wurstplus.wurstplustwo.guiscreen.minecraft;

import java.util.concurrent.TimeUnit;

import me.travis.mapeadoh.clientstuff.salhack.AlwaysEnabledModule;
import me.travis.mapeadoh.clientstuff.salhack.SalTimer;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.misc.SalHackAutoReconect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.GuiConnecting;

public class AutoReconectModuleButton extends GuiButton
{

    public AutoReconectModuleButton(int buttonId, int x, int y, String buttonText)
    {
        super(buttonId, x, y, buttonText);

        timer.reset();

        WurstplusHack AutoReconect = Wurstplus.get_module_manager().get_module_with_tag("AutoReconect");
        ReconnectTimer = SalHackAutoReconect.INSTANCE.delay.get_value(1) * 1000f;
    }
    private WurstplusHack Module = Wurstplus.get_module_manager().get_module_with_tag("AutoReconect");;
    private SalTimer timer = new SalTimer();
    private float ReconnectTimer;

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        if (visible)
        {
            if (Module.is_active() && !timer.passed(ReconnectTimer))
                this.displayString = "AutoReconnect (" + TimeUnit.MILLISECONDS.toSeconds(Math.abs((timer.getTime()+(long)ReconnectTimer) - System.currentTimeMillis())) + ")";
            else if (!Module.is_active())
                this.displayString = "AutoReconnect";

            if (timer.passed(ReconnectTimer) && Module.is_active() && AlwaysEnabledModule.LastIP != null && AlwaysEnabledModule.LastPort != -1)
            {
                mc.displayGuiScreen(new GuiConnecting(null, mc, AlwaysEnabledModule.LastIP, AlwaysEnabledModule.LastPort));
            }
        }
    }

    public void Clicked()
    {
        Module.toggle();

        timer.reset();
    }
}