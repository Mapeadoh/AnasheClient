package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventGUIScreen;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiGameOver;

public class DeathDisable extends WurstplusHack {
    public DeathDisable(){
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "DisableOnDeath";
        this.tag = "DisableModulesOnDeathEvent";
        this.description = "jaja ripio jajajajajajaajajaja";
    }
    WurstplusSetting surround = create("Surround", "Surround", true);
    WurstplusSetting selftrap = create("SelfTrap", "SelfTrap", true);
    WurstplusSetting autotrap = create("AutoTrap", "AutoTrap", true);
    WurstplusSetting autocity = create("AutoCity", "AutoCity", true);
    WurstplusSetting holefill = create("HoleFIll", "HoleFill", true);
    WurstplusSetting blink = create("Blink", "Blink", true);
    WurstplusSetting timer = create("Timer", "Timer", true);
    WurstplusSetting freecam = create("Freecam", "Freecam", true);

    @EventHandler
    private final Listener<WurstplusEventGUIScreen> WurstplusEventGUIScreen = new Listener<>(event -> {
        if (event.get_guiscreen() instanceof GuiGameOver) {
            if(surround.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("Surround").set_disable();
            }
            if(selftrap.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("SelfTrap").set_disable();
            }
            if(autotrap.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("Trap").set_disable();
            }
            if(autocity.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("AutoCity").set_disable();
            }
            if(holefill.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("HoleFill").set_disable();
            }
            if(blink.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("Blink").set_disable();
            }
            if(timer.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("Timer").set_disable();
            }
            if(freecam.get_value(true)){
                AnasheClient.get_hack_manager().get_module_with_tag("Freecam").set_disable();
            }
        }
    });
}
