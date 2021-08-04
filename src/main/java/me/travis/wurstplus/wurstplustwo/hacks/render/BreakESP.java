package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.turok.draw.RenderHelp;
import net.minecraft.init.Blocks;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import java.util.HashMap;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class BreakESP extends WurstplusHack
{
    WurstplusSetting ignoreSelf;
    WurstplusSetting onlyObby;
    WurstplusSetting alpha;
    WurstplusSetting fade;
    WurstplusSetting red;
    WurstplusSetting green;
    WurstplusSetting blue;
    private ConcurrentSet test;
    public ConcurrentSet breaking;
    float inc;
    BlockPos pos;
    public static BreakESP INSTANCE;
    private Map alphaMap;
    private ArrayList options;

    public BreakESP() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.ignoreSelf = this.create("IgnoreSelf", "IgnoreSelf", false);
        this.onlyObby = this.create("OnlyObi", "OnlyObi", true);
        this.alpha = this.create("Alpha", "Alpha", 50, 0, 255);
        this.fade = this.create("Fade", "Fade", false);
        this.red = this.create("Red", "Red", 255, 0, 255);
        this.green = this.create("Green", "Green", 255, 0, 255);
        this.blue = this.create("Blue", "Blue", 255, 0, 255);
        this.test = new ConcurrentSet();
        this.breaking = new ConcurrentSet();
        this.alphaMap = new HashMap();
        this.name = "BreakESP";
        this.tag = "BreakESP";
        this.description = "Render BreakESP";
        this.alphaMap.put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);
    }

    @Override
    public void render(final WurstplusEventRender var1) {
        //qqqqqqqqqqqqqq, joder q sueño mañana lo arreglo
        AtomicInteger var3 = new AtomicInteger();
        BreakESP.mc.renderGlobal.damagedBlocks.forEach((var1x, var2) -> {
            if (var2 != null && (!this.ignoreSelf.get_value(true) || BreakESP.mc.world.getEntityByID((int)var1x) != BreakESP.mc.player) && (!this.onlyObby.get_value(true) || BreakESP.mc.world.getBlockState(var2.getPosition()).getBlock() == Blocks.OBSIDIAN)) {
                var3.set((int) ((this.fade.get_value(true)) ? this.alphaMap.get(var2.getPartialBlockDamage()) : Integer.valueOf(this.alpha.get_value(1))));
                this.render_block(var2.getPosition(), var3.get());
            }
        });
    }

    public void render_block(final BlockPos pos, final int alpha) {
        final BlockPos render_block = pos;
        final float h = 1.0f;
        RenderHelp.prepare("quads");
        RenderHelp.draw_cube(RenderHelp.get_buffer_build(), (float)render_block.getX(), (float)render_block.getY(), (float)render_block.getZ(), 1.0f, h, 1.0f, this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), alpha, "all");
        RenderHelp.release();
    }
}
