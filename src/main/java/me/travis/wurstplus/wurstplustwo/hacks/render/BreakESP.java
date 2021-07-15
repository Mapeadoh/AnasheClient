/**package me.travis.wurstplus.wurstplustwo.hacks.render;

import com.gamesense.api.event.events.DrawBlockDamageEvent;
import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.setting.values.BooleanSetting;
import com.gamesense.api.setting.values.ColorSetting;
import com.gamesense.api.setting.values.IntegerSetting;
import com.gamesense.api.setting.values.ModeSetting;
import com.gamesense.api.util.render.GSColor;
import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import com.gamesense.api.util.world.GeometryMasks;
import com.gamesense.client.module.Category;
import com.gamesense.client.module.Module;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;


public class BreakESP extends WurstplusHack {
    public BreakESP(){
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "BreakESP";
        this.tag = "BreakESP";
        this.description = "romper e ese pe";

    }

    WurstplusSetting mode = create("Render", "Render", "Both", combobox("Outline", "Fill", "Both"));
    WurstplusSetting lineWidth = create("Width", "Width" 1, 0, 5);
    WurstplusSetting range = create("Range", "Range", 100, 1, 200);
    WurstplusSetting cancelAnimation = create("No Animation", "NoAnim", true);
    WurstplusSetting r = create("R", "CityR", 0, 0, 255);
    WurstplusSetting g = create("G", "CityG", 255, 0, 255);
    WurstplusSetting b = create("B", "CityB", 0, 0, 255);
    WurstplusSetting a = create("A", "CityA", 50, 0, 255);

    boolean outline = false;
    boolean solid   = false;

    public void render(WurstplusEventRender event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null) {

                BlockPos blockPos = destroyBlockProgress.getPosition();

                if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    return;
                }


                if (blockPos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= range.getValue()) {

                    int progress = destroyBlockProgress.getPartialBlockDamage();
                    AxisAlignedBB axisAlignedBB = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);

                    renderESP(axisAlignedBB, progress, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1));
                }
            }
        });
    }

    private void renderESP(AxisAlignedBB axisAlignedBB, int i, int r_value, int g_value, int value, int progress) {

        double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
        double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
        double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
        double progressValX = progress * ((axisAlignedBB.maxX - centerX) / 10);
        double progressValY = progress * ((axisAlignedBB.maxY - centerY) / 10);
        double progressValZ = progress * ((axisAlignedBB.maxZ - centerZ) / 10);

        AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);

        if (mode.in("Pretty")) {
            outline = true;
            solid   = true;
        }

        if (mode.in("Solid")) {
            outline = false;
            solid   = true;
        }

        if (mode.in("Outline")) {
            outline = true;
            solid   = false;
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

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<DrawBlockDamageEvent> drawBlockDamageEventListener = new Listener<>(event -> {
        if (cancelAnimation.getValue()) {
            event.cancel();
        }
    });
}*/