package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons.*;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.GSColor;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Buttons extends Component {
	public WurstplusHack mod;
	public Frames parent;
	public int offset;
	private boolean isHovered;
	private final ArrayList<Component> subcomponents;
	public boolean open;
	private final int height;
	GSColor fontColor;
	GSColor fontActiveColor;

	private static final ResourceLocation opengui = new ResourceLocation("custom/close.png");
	private static final ResourceLocation closedgui = new ResourceLocation("custom/open.png");
	private static final ResourceLocation oyvey = new ResourceLocation("custom/oyvey.png");
	private static final ResourceLocation future = new ResourceLocation("custom/future.png");

	public Buttons(final WurstplusHack mod, final Frames parent, final int offset) {

		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		int opY = offset + 16;
		if (AnasheClient.get_setting_manager().get_settings_with_hack(mod) != null && !AnasheClient.get_setting_manager().get_settings_with_hack(mod).isEmpty()) {
			for (final WurstplusSetting s : AnasheClient.get_setting_manager().get_settings_with_hack(mod)) {
				switch (s.get_type()) {
					case "combobox":
						this.subcomponents.add(new ModeComponent(s,this, opY));
						break;
					case "button":
						this.subcomponents.add(new BooleanComponent(s,this, opY));
						break;
					case "doubleslider":
						this.subcomponents.add(new DoubleComponent(s, this, opY));
						break;
					case "integerslider":
						this.subcomponents.add(new IntegerComponent(s, this, opY));
						break;
					//case COLOR:
						//this.subcomponents.add(new ColorComponent((Setting.ColorSetting)s, this, opY));
					//break;
					/*case "label":
						this.subcomponents.add(new LabelComponent((this, opY)));
						break;*/
				}
				opY += 16;
			}
		}
		this.subcomponents.add(new KeybindComponent(this, opY));
		this.height=opY+16-offset;
	}

	@Override
	public void setOff(final int newOff) {
		this.offset = newOff;
		int opY = this.offset + 16;
		for (final Component comp : this.subcomponents) {
			comp.setOff(opY);
			//if (comp instanceof ColorComponent) opY+=80;
			opY += 16;
		}
	}

	@Override
	public void renderComponent() {
		// fontcolor
		if(NewClickGUI.INSTANCE.background.in("Custom")){
			fontColor = new GSColor(NewClickGUI.INSTANCE.font_r.get_value(1), NewClickGUI.INSTANCE.font_g.get_value(1), NewClickGUI.INSTANCE.font_b.get_value(1), NewClickGUI.INSTANCE.font_a.get_value(1));
			fontActiveColor = new GSColor(NewClickGUI.INSTANCE.active_font_r.get_value(1), NewClickGUI.INSTANCE.active_font_g.get_value(1), NewClickGUI.INSTANCE.active_font_b.get_value(1), NewClickGUI.INSTANCE.active_font_a.get_value(1));
		}
		if(NewClickGUI.INSTANCE.background.in("OldGUI")){
			fontActiveColor = new GSColor(130, 193, 242, 216);
			fontColor = new GSColor(226, 226, 226, 90);
		}
		// render
		Renderer.drawRectStatic(this.parent.getX(), this.parent.getY() + this.offset + 1, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 16 + this.offset, Renderer.getTransColor(isHovered));
		Renderer.drawRectStatic(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
		FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.mod.get_name(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2 + 2, mod.is_active()?this.fontActiveColor:this.fontColor);
		// icon in the module
		if (this.subcomponents.size() > 1) {
			if (NewClickGUI.INSTANCE.icons.in("Image")) {
				FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.open ? "" : "", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, Renderer.getFontColor());
				if (this.open) {
					//gif texture
					drawOpenRender(this.parent.getX() + this.parent.getWidth() - 13, this.parent.getY() + this.offset + 2 + 2);
				} else {
					//static texture
					drawClosedRender(this.parent.getX() + this.parent.getWidth() - 13, this.parent.getY() + this.offset + 2 + 2);
				}
			}
			else if(NewClickGUI.INSTANCE.icons.in("OyVey")){
				FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.open ? "" : "", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, Renderer.getFontColor());
				drawOyveyIconRender(this.parent.getX() + this.parent.getWidth() - 13, this.parent.getY() + this.offset + 2 + 2);
			}
			else if(NewClickGUI.INSTANCE.icons.in("Future")) {
				FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.open ? "" : "", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, Renderer.getFontColor());
				drawFutureIconRender(this.parent.getX() + this.parent.getWidth() - 13, this.parent.getY() + this.offset + 2 + 2);
			}
			else if(NewClickGUI.INSTANCE.icons.in("Font")) {
				FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.open ? "-" : "+", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, Renderer.getFontColor());
			}


		}
		if (this.open && !this.subcomponents.isEmpty()) {
			for (final Component comp : this.subcomponents) {
				comp.renderComponent();
			}
		}
	}

	@Override
	public int getHeight() {
		if (this.open) {
			return height;
		}
		return 16;
	}

	@Override
	public void updateComponent(final int mouseX, final int mouseY) {
		this.isHovered = this.isMouseOnButton(mouseX, mouseY);
		if (!this.subcomponents.isEmpty()) {
			for (final Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for (final Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
		for (final Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(final char typedChar, final int key) {
		for (final Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}

	public boolean isMouseOnButton(final int x, final int y) {
		return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + this.offset;
	}

	public void drawOpenRender(int x, int y){
		GlStateManager.enableAlpha();
		this.mc.getTextureManager().bindTexture(opengui);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPushMatrix();
		Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,10,10,256,256);
		GL11.glPopMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void drawClosedRender(int x, int y){
		GlStateManager.enableAlpha();
		this.mc.getTextureManager().bindTexture(closedgui);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPushMatrix();
		Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,10,10,256,256);
		GL11.glPopMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void drawOyveyIconRender(int x, int y){
		GlStateManager.enableAlpha();
		this.mc.getTextureManager().bindTexture(oyvey);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPushMatrix();
		Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,10,10,256,256);
		GL11.glPopMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
	}
	public void drawFutureIconRender(int x, int y){
		GlStateManager.enableAlpha();
		this.mc.getTextureManager().bindTexture(future);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glPushMatrix();
		Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,10,10,256,256);
		GL11.glPopMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
	}
}
