package me.travis.wurstplus.wurstplustwo.hacks.client;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.GSColor;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import java.awt.*;

public class NewClickGUI extends WurstplusHack {
    public static NewClickGUI INSTANCE;
    public NewClickGUI() {
        super(WurstplusCategory.WURSTPLUS_CLIENT);

        this.name        = "NewGUI";
        this.tag         = "NewGUI";
        this.description = "The (new) main gui";

        set_bind(AnasheClient.WURSTPLUS_KEY_GUI);
        INSTANCE = this;
    }
    //public WurstplusSetting blur = create("Blur", "Blur", true);
     public WurstplusSetting icons = create("Icons", "Icons", "OyVey", combobox("Font", "Image", "OyVey", "Future"));
    public WurstplusSetting background = create("Theme", "Theme", "Custom", combobox("Black", "OldGUI", "Gray", "Custom"));
    public WurstplusSetting scrollspeed = create("ScrollSpeed", "ScrollSpeed", 10, 1, 20);
    public WurstplusSetting name_frame_r = create("CustomR", "CustomR", 0, 0, 255);
    public WurstplusSetting name_frame_g = create("CustomG", "CustomG", 0, 0, 255);
    public WurstplusSetting name_frame_b = create("CustomB", "CustomB", 0, 0, 255);
    public WurstplusSetting rainbowframe = create("RainbowFrame", "RainbowFrame", false);
    public WurstplusSetting font_r = create("CustomFontR", "CustomFontR", 109, 0, 255);
    public WurstplusSetting font_g = create("CustomFontG", "CustomFontG", 101, 0, 255);
    public WurstplusSetting font_b = create("CustomFontB", "CustomFontB", 125, 0, 255);
    public WurstplusSetting font_a = create("CustomFontA", "CustomFontA", 90, 0, 255);
    public WurstplusSetting fontrainbow1 = create("RainbowFont", "RainbowFont2", false);
    public WurstplusSetting active_font_r = create("ActiveFontR", "CustomFontR", 109, 0, 255);
    public WurstplusSetting active_font_g = create("ActiveFontG", "CustomFontG", 101, 0, 255);
    public WurstplusSetting active_font_b = create("ActiveFontB", "CustomFontB", 125, 0, 255);
    public WurstplusSetting active_font_a = create("ActiveFontA", "CustomFontA", 90, 0, 255);
    public WurstplusSetting fontrainbow = create("RainbowFont", "RainbowFont", false);
    public WurstplusSetting customFont = create("Custom Font", "Custom Font", true);
    //public WurstplusSetting customfonts = create("Fonts:", "Fonts", "Verdana", combobox("Verdana", "Arial"));

    public WurstplusSetting opacity = create("Opacity", "Opacity", 200, 50, 255);

    public GSColor guiColor = new GSColor(name_frame_r.get_value(1), name_frame_g.get_value(1), name_frame_b.get_value(1));

    public GSColor ActiveguiColor = new GSColor(active_font_r.get_value(1), active_font_g.get_value(1), active_font_b.get_value(1));

    @Override
    public void update() {
        if (rainbowframe.get_value(true)) {
            frame_rainbow();

        }

        if (fontrainbow1.get_value(true)) {
            font_rainbow();

        }
        if(fontrainbow.get_value(true)){
            font_rainbow2();
        }
    }

    @Override
    public void enable() {
        if (mc.world != null && mc.player != null) {
            mc.displayGuiScreen(AnasheClient.new_click_gui);
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

        int color_rgb_1 = Color.HSBtoRGB(tick_color[0], 1, 1);

        name_frame_r.set_value((color_rgb_1 >> 16) & 0xFF);
        name_frame_g.set_value((color_rgb_1 >> 8) & 0xFF);
        name_frame_b.set_value(color_rgb_1 & 0xFF);
    }
    public void font_rainbow() {

        float[] tick_color2 = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_2 = Color.HSBtoRGB(tick_color2[0], 1, 1);

        font_r.set_value((color_rgb_2 >> 16) & 0xFF);
        font_g.set_value((color_rgb_2 >> 8) & 0xFF);
        font_b.set_value(color_rgb_2 & 0xFF);
    }
    public void font_rainbow2() {

        float[] tick_color3 = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_3 = Color.HSBtoRGB(tick_color3[0], 1, 1);

        active_font_r.set_value((color_rgb_3 >> 16) & 0xFF);
        active_font_g.set_value((color_rgb_3 >> 8) & 0xFF);
        active_font_b.set_value(color_rgb_3 & 0xFF);
    }
}