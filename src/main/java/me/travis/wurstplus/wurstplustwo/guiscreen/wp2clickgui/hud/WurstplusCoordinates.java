package me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.render.pinnables.WurstplusPinnable;


public class WurstplusCoordinates extends WurstplusPinnable {
	ChatFormatting dg = ChatFormatting.DARK_GRAY;
	ChatFormatting db = ChatFormatting.DARK_BLUE;
	ChatFormatting dr = ChatFormatting.DARK_RED;

	public WurstplusCoordinates() {
		super("Coordinates", "Coordinates", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = AnasheClient.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String x = AnasheClient.g + "[" + AnasheClient.r + Integer.toString((int) (mc.player.posX)) + AnasheClient.g + "]" + AnasheClient.r;
		String y = AnasheClient.g + "[" + AnasheClient.r + Integer.toString((int) (mc.player.posY)) + AnasheClient.g + "]" + AnasheClient.r;
		String z = AnasheClient.g + "[" + AnasheClient.r + Integer.toString((int) (mc.player.posZ)) + AnasheClient.g + "]" + AnasheClient.r;

		String x_nether = AnasheClient.g + "[" + AnasheClient.r + Long.toString(Math.round(mc.player.dimension != -1 ? (mc.player.posX / 8) : (mc.player.posX * 8))) + AnasheClient.g + "]" + AnasheClient.r;
		String z_nether = AnasheClient.g + "[" + AnasheClient.r + Long.toString(Math.round(mc.player.dimension != -1 ? (mc.player.posZ / 8) : (mc.player.posZ * 8))) + AnasheClient.g + "]" + AnasheClient.r;

		String line = "XYZ " + x + y + z + " XZ " + x_nether + z_nether;

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width"));
		this.set_height(this.get(line, "height") + 2);
	}
}