package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.mapeadoh.clientstuff.phobos.RenderUtil;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BurrowESP extends WurstplusHack {

    private static BurrowESP INSTANCE = new BurrowESP();

    public WurstplusSetting range = create("Range","Range", 20, 5, 50);
    public WurstplusSetting self = create("Self", "Self", true);
    public WurstplusSetting text = create("Text", "Text", true);
    public WurstplusSetting rainbow = create("Rainbow", "Rainbow", false);
    public WurstplusSetting red = create("Red", "Red", 0, 0, 255);
    public WurstplusSetting green = create("Green","Green", 255, 0, 255);
    public WurstplusSetting blue = create("Blue","Blue", 0, 0, 255);
    public WurstplusSetting alpha = create("Alpha", "Alpha", 0, 0, 255);
    public WurstplusSetting outlineAlpha = create("OL-Alpha","OL-Alpha", 0, 0, 255);

    private final List<BlockPos> posList = new ArrayList<>();

    public BurrowESP(){
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "BurrowESP";
        this.tag = "BurrowESP";
        this.description = "Burrow ESP from renosense";
    }


    @Override
    public void update(){
        if(rainbow.get_value(true)){
            rainbow();
        }
        posList.clear();
        for (EntityPlayer player : mc.world.playerEntities){
            BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY+0.2), Math.floor(player.posZ));
            if((mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST  ||mc.world.getBlockState(blockPos).getBlock() == Blocks.PISTON || mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST || mc.world.getBlockState(blockPos).getBlock() == Blocks.SAND|| mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= this.range.get_value(1)){

                if (!(blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= 1.5) || this.self.get_value(true)) {
                    posList.add(blockPos);
                }


            }
        }
    }

    @Override
    public void render(WurstplusEventRender event){
        for (BlockPos blockPos : posList){
            String s = mc.player.getName() + " burrowed";
            if (this.text.get_value(true)) {
                WurstplusRenderUtil.drawText(blockPos, s);
            }
            RenderUtil.drawBoxESP(blockPos, new Color(red.get_value(1), green.get_value(1), blue.get_value(1), outlineAlpha.get_value(1)), 1.5F, true, true, alpha.get_value(1));
        }
    }
    public void rainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 1, 1);

        red.set_value((color_rgb_o >> 16) & 0xFF);
        green.set_value((color_rgb_o >> 8) & 0xFF);
        blue.set_value(color_rgb_o & 0xFF);

    }
}