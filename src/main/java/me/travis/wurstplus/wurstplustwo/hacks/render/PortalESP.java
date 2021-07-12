package me.travis.wurstplus.wurstplustwo.hacks.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.entity.player.EntityPlayer;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.turok.draw.RenderHelp;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PortalESP extends WurstplusHack {
    public PortalESP() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        // Info.
        this.name        = "PortalESP";
        this.tag         = "PortalESP";
        this.description = "a portal esp ._.";
    }

    WurstplusSetting mode = create("Mode", "Mode", "Pretty", combobox("Pretty", "Solid", "Outline"));
    WurstplusSetting r = create("R", "R", 230, 0, 255);
    WurstplusSetting g = create("G", "G", 0, 0, 255);
    WurstplusSetting b = create("B", "B", 0, 0, 255);
    WurstplusSetting a = create("A", "A", 150, 0, 255);
    WurstplusSetting height = create("Height", "Height", 0.2, 0.0, 1.0);
    WurstplusSetting rainbow = create("RainBow", "RainBow", false);
    WurstplusSetting sat = create("Saturation", "Satiation", 0.8, 0, 1);
    WurstplusSetting brightness = create("Brightness", "Brightness", 0.8, 0, 1);
    WurstplusSetting range = create("Range", "Range", 20, 0, 100);

    private int color_alpha;

    List<BlockPos> blocks = new ArrayList<>();

    boolean outline = false;
    boolean solid   = false;

    @Override
    public void update() {
        blocks.clear();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (mc.player.getDistance(player) > range.get_value(1) || mc.player == player) continue;

            for (int x = (int) PortalESP.mc.player.posX - this.range.get_value(1); x <= (int) PortalESP.mc.player.posX + range.get_value(1); ++x) {
                for (int y = (int) PortalESP.mc.player.posY - this.range.get_value(1); y <= (int) PortalESP.mc.player.posY + range.get_value(1); ++y) {
                    int z = (int) Math.max(PortalESP.mc.player.posZ - (double) this.range.get_value(1), 0.0);
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = PortalESP.mc.world.getBlockState(pos).getBlock();
                    if (block instanceof BlockPortal) {
                        this.blocks.add(pos);
                    }
                    ++z;
                }
            }
        }
            if (rainbow.get_value(true)) {
                cycle_rainbow();
            }
        }

        @Override
        public void render (WurstplusEventRender event){

            float off_set_h = (float) height.get_value(1.0);

            for (BlockPos pos : blocks) {

                if (mode.in("Pretty")) {
                    outline = true;
                    solid = true;
                }

                if (mode.in("Solid")) {
                    outline = false;
                    solid = true;
                }

                if (mode.in("Outline")) {
                    outline = true;
                    solid = false;
                }

                if (solid) {
                    RenderHelp.prepare("quads");
                    RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                            pos.getX(), pos.getY(), pos.getZ(),
                            1, off_set_h, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
                            "all"
                    );

                    RenderHelp.release();
                }


                if (outline) {
                    RenderHelp.prepare("lines");
                    RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                            pos.getX(), pos.getY(), pos.getZ(),
                            1, off_set_h, 1,
                            r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
                            "all"
                    );

                    RenderHelp.release();
                }
            }
        }
        public void cycle_rainbow () {

            float[] tick_color = {
                    (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
            };

            int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

            r.set_value((color_rgb_o >> 16) & 0xFF);
            g.set_value((color_rgb_o >> 8) & 0xFF);
            b.set_value(color_rgb_o & 0xFF);
    }
}
