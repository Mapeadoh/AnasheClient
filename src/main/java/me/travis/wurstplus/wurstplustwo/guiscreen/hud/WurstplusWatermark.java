package me.travis.wurstplus.wurstplustwo.guiscreen.hud;


import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.pinnables.WurstplusPinnable;


public class WurstplusWatermark extends WurstplusPinnable {
	public WurstplusWatermark() {
		super("Watermark", "Watermark", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String line = ChatFormatting.DARK_AQUA + AnasheClient.WURSTPLUS_NAME + ChatFormatting.RESET + AnasheClient.WURSTPLUS_VERSION;

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}
}