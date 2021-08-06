package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame;

import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.GSColor;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Renderer {

    /**
     * @Author Hoosiers and Lukflug
     * 09/08/20
     * Some functions are from net.minecraft.client.gui.Gui, modified for ClickGui usage
     */

    //no gradient, single color
    public static void drawRectStatic(int leftX, int leftY, int rightX, int rightY, GSColor color){
        Gui.drawRect(leftX,leftY,rightX,rightY,color.getRGB());
    }

    //top down color gradient
    public static void drawRectGradient(int leftX, int leftY, int rightX, int rightY, GSColor startColor, GSColor endColor){
        float s = (float)(startColor.getRGB() >> 24 & 255) / 255.0F;
        float s1 = (float)(startColor.getRGB() >> 16 & 255) / 255.0F;
        float s2 = (float)(startColor.getRGB() >> 8 & 255) / 255.0F;
        float s3 = (float)(startColor.getRGB() & 255) / 255.0F;
        float e1 = (float)(endColor.getRGB() >> 24 & 255) / 255.0F;
        float e2 = (float)(endColor.getRGB() >> 16 & 255) / 255.0F;
        float e3 = (float)(endColor.getRGB() >> 8 & 255) / 255.0F;
        float e4 = (float)(endColor.getRGB() & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(rightX, leftY, 0).color(s1, s2, s3, s).endVertex(); //startColor
        bufferbuilder.pos(leftX, leftY, 0).color(s1, s2, s3, s).endVertex(); //startColor
        bufferbuilder.pos(leftX, rightY, 0).color(e2, e3, e4, e1).endVertex(); //endColor
        bufferbuilder.pos(rightX, rightY, 0).color(e2, e4, e4, e1).endVertex(); //endColor
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static GSColor getMainColor() {
        return NewClickGUI.INSTANCE.guiColor;
    }

    public static GSColor getActiveModulesColor() {
        return NewClickGUI.INSTANCE.ActiveguiColor;
    }

    public static GSColor getTransColor (boolean hovered) {
        GSColor transColor = new GSColor(195, 195, 195, NewClickGUI.INSTANCE.opacity.get_value(1) - 50);

        if (NewClickGUI.INSTANCE.background.in("Black")){
            transColor = new GSColor(0, 0, 0,NewClickGUI.INSTANCE.opacity.get_value(1) - 50);
        }
        else if (NewClickGUI.INSTANCE.background.in("OldGUI")){
            transColor = new GSColor(255, 255, 255, 72);
        }
        else if (NewClickGUI.INSTANCE.background.in("Custom")){
            transColor = new GSColor(NewClickGUI.INSTANCE.name_frame_r.get_value(1), NewClickGUI.INSTANCE.name_frame_g.get_value(1), NewClickGUI.INSTANCE.name_frame_b.get_value(1),NewClickGUI.INSTANCE.opacity.get_value(1) - 50);
        }

        if (hovered) return new GSColor(transColor.darker().darker());
        return transColor;
    }

    public static GSColor getFontColor() {
        return new GSColor(255,255,255);
    }
}