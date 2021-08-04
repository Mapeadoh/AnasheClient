package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils;

import me.travis.wurstplus.AnasheClient;
import net.minecraft.client.Minecraft;

public class FontUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static float drawStringWithShadow(boolean customFont, String text, int x, int y, GSColor color){
		if(customFont) return AnasheClient.fontRenderer.drawStringWithShadow(text, x, y, color);
		else return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
	}

	public static int getStringWidth(boolean customFont, String str){
		if (customFont) return AnasheClient.fontRenderer.getStringWidth(str);
		else return mc.fontRenderer.getStringWidth(str);
	}

	public static int getFontHeight(boolean customFont){
		if (customFont) return AnasheClient.fontRenderer.getHeight();
		else return mc.fontRenderer.FONT_HEIGHT;
	}
}
