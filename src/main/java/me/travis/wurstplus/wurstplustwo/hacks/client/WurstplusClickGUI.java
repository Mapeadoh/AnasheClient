package me.travis.wurstplus.wurstplustwo.hacks.client;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import java.awt.*;

public class WurstplusClickGUI extends WurstplusHack {
public static WurstplusClickGUI INSTANCE;
	public WurstplusClickGUI() {
		super(WurstplusCategory.WURSTPLUS_CLIENT);

		this.name        = "GUI";
		this.tag         = "GUI";
		this.description = "The main gui";

		set_bind(AnasheClient.WURSTPLUS_KEY_GUI);
		INSTANCE = this;
	}
	public WurstplusSetting customtitle = create("DisplayTitle", "DisplayTitle", true);
	public WurstplusSetting displaytitle = create("DisplayTitleMode", "Display Modes", "AnasheClient", combobox("SinanBackdoor", "NullWare+", "W+2Better"));
	WurstplusSetting label_frame = create("info", "ClickGUIInfoFrame", "Frames");

	WurstplusSetting name_frame_r = create("Name R", "ClickGUINameFrameR", 109, 0, 255);
	WurstplusSetting name_frame_g = create("Name G", "ClickGUINameFrameG", 101, 0, 255);
	WurstplusSetting name_frame_b = create("Name B", "ClickGUINameFrameB", 125, 0, 255);
	WurstplusSetting rainbowframe = create("RainbowFrame", "RainbowFrame", false);

	WurstplusSetting background_frame_r = create("Background R", "ClickGUIBackgroundFrameR", 0, 0, 255);
	WurstplusSetting background_frame_g = create("Background G", "ClickGUIBackgroundFrameG", 0, 0, 255);
	WurstplusSetting background_frame_b = create("Background B", "ClickGUIBackgroundFrameB", 0, 0, 255);
	WurstplusSetting background_frame_a = create("Background A", "ClickGUIBackgroundFrameA", 90, 0, 255);
	WurstplusSetting rainbowback = create("RainbowBackground", "RainbowBack", false);

	WurstplusSetting border_frame_r = create("Border R", "ClickGUIBorderFrameR", 64, 0, 255);
	WurstplusSetting border_frame_g = create("Border G", "ClickGUIBorderFrameG", 0, 0, 255);
	WurstplusSetting border_frame_b = create("Border B", "ClickGUIBorderFrameB", 74, 0, 255);
	WurstplusSetting rainbowborder = create("RainbowBorder", "RainbowBorder", false);

	WurstplusSetting label_widget = create("info", "ClickGUIInfoWidget", "Widgets");

	WurstplusSetting name_widget_r = create("Name R", "ClickGUINameWidgetR", 197, 0, 255);
	WurstplusSetting name_widget_g = create("Name G", "ClickGUINameWidgetG", 197, 0, 255);
	WurstplusSetting name_widget_b = create("Name B", "ClickGUINameWidgetB", 197, 0, 255);
	WurstplusSetting rainbowname = create("RainbowName", "RainbowName", false);

	WurstplusSetting background_widget_r = create("Background R", "ClickGUIBackgroundWidgetR", 80, 0, 255);
	WurstplusSetting background_widget_g = create("Background G", "ClickGUIBackgroundWidgetG", 82, 0, 255);
	WurstplusSetting background_widget_b = create("Background B", "ClickGUIBackgroundWidgetB", 226, 0, 255);
	WurstplusSetting background_widget_a = create("Background A", "ClickGUIBackgroundWidgetA", 197, 0, 255);
	WurstplusSetting rainbowbackwidget = create("RainbowWidBack", "RainbowWidBack", false);

	WurstplusSetting border_widget_r = create("Border R", "ClickGUIBorderWidgetR", 64, 0, 255);
	WurstplusSetting border_widget_g = create("Border G", "ClickGUIBorderWidgetG", 0, 0, 255);
	WurstplusSetting border_widget_b = create("Border B", "ClickGUIBorderWidgetB", 74, 0, 255);
	WurstplusSetting rainbowborderwid = create("RainbowWidBorder", "RainbowWidBorder", false);

	WurstplusSetting sat = create("RainbowSaturation", "NametagSatiation", 0.8, 0, 1);
	WurstplusSetting brightness = create("RainbowBrightness", "NametagBrightness", 0.8, 0, 1);


	@Override
	public void update() {
		// Update frame colors.
		AnasheClient.click_gui.theme_frame_name_r = name_frame_r.get_value(1);
		AnasheClient.click_gui.theme_frame_name_g = name_frame_g.get_value(1);
		AnasheClient.click_gui.theme_frame_name_b = name_frame_b.get_value(1);

		AnasheClient.click_gui.theme_frame_background_r = background_frame_r.get_value(1);
		AnasheClient.click_gui.theme_frame_background_g = background_frame_g.get_value(1);
		AnasheClient.click_gui.theme_frame_background_b = background_frame_b.get_value(1);
		AnasheClient.click_gui.theme_frame_background_a = background_frame_a.get_value(1);

		AnasheClient.click_gui.theme_frame_border_r = border_frame_r.get_value(1);
		AnasheClient.click_gui.theme_frame_border_g = border_frame_g.get_value(1);
		AnasheClient.click_gui.theme_frame_border_b = border_frame_b.get_value(1);

		// Update widget colors.
		AnasheClient.click_gui.theme_widget_name_r = name_widget_r.get_value(1);
		AnasheClient.click_gui.theme_widget_name_g = name_widget_g.get_value(1);
		AnasheClient.click_gui.theme_widget_name_b = name_widget_b.get_value(1);

		AnasheClient.click_gui.theme_widget_background_r = background_widget_r.get_value(1);
		AnasheClient.click_gui.theme_widget_background_g = background_widget_g.get_value(1);
		AnasheClient.click_gui.theme_widget_background_b = background_widget_b.get_value(1);
		AnasheClient.click_gui.theme_widget_background_a = background_widget_a.get_value(1);

		AnasheClient.click_gui.theme_widget_border_r = border_widget_r.get_value(1);
		AnasheClient.click_gui.theme_widget_border_g = border_widget_g.get_value(1);
		AnasheClient.click_gui.theme_widget_border_b = border_widget_b.get_value(1);
		if (rainbowframe.get_value(true)) {
			frame_rainbow();
		}
		if (rainbowback.get_value(true)) {
			background_rainbow();
		}
		if (rainbowborder.get_value(true)) {
			frameborder_rainbow();
		}
		if (rainbowname.get_value(true)) {
			widgetname_rainbow();
		}
		if (rainbowbackwidget.get_value(true)) {
			widgetbackground_rainbow();
		}
		if (rainbowborderwid.get_value(true)) {
			widgetborder_rainbow();
		}
	}

	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			mc.displayGuiScreen(AnasheClient.click_gui);
		}
	}

	@Override
	public void disable() {
		if (mc.world != null && mc.player != null) {
			mc.displayGuiScreen(null);
		}
	}

	//fps killer zone Bv
	public void frame_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_1 = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		name_frame_r.set_value((color_rgb_1 >> 16) & 0xFF);
		name_frame_g.set_value((color_rgb_1 >> 8) & 0xFF);
		name_frame_b.set_value(color_rgb_1 & 0xFF);
	}
	public void background_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_2 = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		background_frame_r.set_value((color_rgb_2 >> 16) & 0xFF);
		background_frame_g.set_value((color_rgb_2 >> 8) & 0xFF);
		background_frame_b.set_value(color_rgb_2 & 0xFF);
	}
	public void frameborder_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		border_frame_r.set_value((color_rgb_o >> 16) & 0xFF);
		border_frame_g.set_value((color_rgb_o >> 8) & 0xFF);
		border_frame_b.set_value(color_rgb_o & 0xFF);
	}
	//widgets
	public void widgetname_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		name_widget_r.set_value((color_rgb_o >> 16) & 0xFF);
		name_widget_g.set_value((color_rgb_o >> 8) & 0xFF);
		name_widget_b.set_value(color_rgb_o & 0xFF);
	}
	public void widgetbackground_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		background_widget_r.set_value((color_rgb_o >> 16) & 0xFF);
		background_widget_g.set_value((color_rgb_o >> 8) & 0xFF);
		background_widget_b.set_value(color_rgb_o & 0xFF);
	}
	public void widgetborder_rainbow() {

		float[] tick_color = {
				(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

		border_widget_r.set_value((color_rgb_o >> 16) & 0xFF);
		border_widget_g.set_value((color_rgb_o >> 8) & 0xFF);
		border_widget_b.set_value(color_rgb_o & 0xFF);
	}
}