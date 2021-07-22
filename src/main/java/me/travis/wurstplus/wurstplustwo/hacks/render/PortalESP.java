package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.ArrayList;
import java.util.List;
import me.travis.mapeadoh.clientstuff.floppa.RenderUtil;
import net.minecraft.util.math.BlockPos;
import java.awt.Color;

public class PortalESP extends WurstplusHack
{
    WurstplusSetting range = create("Range", "Range", 50, 10, 100);
    public WurstplusSetting colorr = create("ColorR", "ColorR", 200, 0, 255);
    public WurstplusSetting colorg = create("ColorG", "ColorG", 0, 0, 255);
    public WurstplusSetting colorb = create("ColorB", "ColorB", 255, 0, 255);
    public WurstplusSetting colora = create("ColorA", "ColorA", 150, 0, 255);
    WurstplusSetting rainbow = create("Rainbow", "Rainbow", false);
    WurstplusSetting rainbow_sat = create("Saturation", "Saturation", 1.0, 0.1, 1.0);

    public Color blockColor;

    public PortalESP() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "PortalESP";
        this.tag = "PortalESP";
        this.description = "From floppa";
    }

    public void render() {
        if (PortalESP.mc.player != null && PortalESP.mc.world != null) {
            for (final BlockPos pos : this.getSphere(mc.getRenderViewEntity().getPosition(), range.get_value(1), range.get_value(1), false, true, 0)) {
                RenderUtil.drawBlockESP(pos, this.blockColor.getRGB(), 2.0f);
            }
        }
    }

    public List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = sphere ? (cy + r) : ((float)(cy + h));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        if (!l.equals(new BlockPos(PortalESP.mc.player.posX, PortalESP.mc.player.posY, PortalESP.mc.player.posZ))) {
                            if (this.getBlock(l) == Blocks.PORTAL || getBlock(l) == Blocks.END_PORTAL) {
                                circleblocks.add(l);
                                this.blockColor = new Color(colorr.get_value(1), colorg.get_value(1), colorb.get_value(1), colora.get_value(1));
                            }
                        }
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }

    public Block getBlock(final BlockPos pos) {
        final IBlockState ibs = PortalESP.mc.world.getBlockState(pos);
        return ibs.getBlock();
    }

    public void update(){
        if (rainbow.get_value(true)) {
            cycle_rainbow();
        }
    }
    public void cycle_rainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], rainbow_sat.get_value(1), 1);

        colorr.set_value((color_rgb_o >> 16) & 0xFF);
        colorg.set_value((color_rgb_o >> 8) & 0xFF);
        colorb.set_value(color_rgb_o & 0xFF);

    }
}
