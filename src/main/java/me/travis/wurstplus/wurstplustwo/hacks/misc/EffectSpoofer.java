package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class EffectSpoofer extends WurstplusHack {
	
	WurstplusSetting speed = create("Speed", "CSPSpeed", 1, 0, 5);
    WurstplusSetting str_multiplier = create("Strenght", "Multiplier", 1, 0, 2);
    WurstplusSetting haste = create("Haste", "HSpeed", 1, 0, 5);
	
	public EffectSpoofer() {
        super(WurstplusCategory.WURSTPLUS_MISC);
        this.name = "Effect Spoofer";
        this.tag = "EffectSpoofer";
        this.description = "packet effect";
    }
    @Override
    public void enable() {
        // espid (speed)
        final PotionEffect espid = new PotionEffect(Potion.getPotionById(1), 123456789, speed.get_value(1));
        espid.setPotionDurationMax(true);
        EffectSpoofer.mc.player.addPotionEffect(espid);
        // 'tren' (Strenght)
        final PotionEffect tren = new PotionEffect(Potion.getPotionById(5), 123456789, str_multiplier.get_value(1));
        tren.setPotionDurationMax(true);
        EffectSpoofer.mc.player.addPotionEffect(tren);
        // jeist (haste)
        final PotionEffect jeist = new PotionEffect(Potion.getPotionById(3), 123456789, haste.get_value(1));
        jeist.setPotionDurationMax(true);
        EffectSpoofer.mc.player.addPotionEffect(jeist);
    }
    @Override
    public void disable() {
        EffectSpoofer.mc.player.removePotionEffect(Potion.getPotionById(1));
        EffectSpoofer.mc.player.removePotionEffect(Potion.getPotionById(5));
        EffectSpoofer.mc.player.removePotionEffect(Potion.getPotionById(3));
    }
}