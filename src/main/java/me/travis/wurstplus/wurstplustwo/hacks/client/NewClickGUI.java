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
     public WurstplusSetting icons = create("Icons", "Icons", "Font", combobox("Font", "Image"));
    public WurstplusSetting background = create("Background", "Silver", "Black", combobox("Black", "Silver", "Gray"));
    public WurstplusSetting scrollspeed = create("ScrollSpeed", "ScrollSpeed", 10, 1, 20);
    public WurstplusSetting name_frame_r = create("R", "R", 109, 0, 255);
    public WurstplusSetting name_frame_g = create("G", "G", 101, 0, 255);
    public WurstplusSetting name_frame_b = create("B", "B", 125, 0, 255);
    public WurstplusSetting rainbowframe = create("RainbowFrame", "RainbowFrame", false);
    public WurstplusSetting customFont = create("Custom Font", "Custom Font", true);

    public WurstplusSetting opacity = create("Opacity", "Opacity", 200, 50, 255);

    public GSColor guiColor = new GSColor(name_frame_r.get_value(1), name_frame_g.get_value(1), name_frame_b.get_value(1));

    @Override
    public void update() {
        if (rainbowframe.get_value(true)) {
            frame_rainbow();

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
}