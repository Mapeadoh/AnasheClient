package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Frames;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {
	public static ArrayList<Frames> frames;

	public ClickGUI(){
		ClickGUI.frames = new ArrayList<Frames>();
		int DevFrameX = 10;
		for (final WurstplusCategory category : WurstplusCategory.values()){
			final Frames devframe = new Frames(category);
			devframe.setX(DevFrameX);
			ClickGUI.frames.add(devframe);
			DevFrameX += devframe.getWidth() + 10;
		}
	}

	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		for (final Frames frames : ClickGUI.frames){
			frames.renderGUIFrame(this.fontRenderer);
			frames.updatePosition(mouseX, mouseY);
			frames.updateMouseWheel();
			for (final Component comp : frames.getComponents()){
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}

	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException{
		for (final Frames frames : ClickGUI.frames){
			if (frames.isWithinHeader(mouseX, mouseY) && mouseButton == 0){
				frames.setDrag(true);
				frames.dragX = mouseX - frames.getX();
				frames.dragY = mouseY - frames.getY();
			}
			if (frames.isWithinHeader(mouseX, mouseY) && mouseButton == 1){
				frames.setOpen(!frames.isOpen());
			}
			if (frames.isOpen() && !frames.getComponents().isEmpty()){
				for (final Component component : frames.getComponents()){
					component.mouseClicked(mouseX, mouseY, mouseButton);
				}
			}
		}
	}

	protected void mouseReleased(final int mouseX, final int mouseY, final int state){
		for (final Frames frames : ClickGUI.frames){
			frames.setDrag(false);
		}
		for (final Frames frames : ClickGUI.frames){
			if (frames.isOpen() && !frames.getComponents().isEmpty()){
				for (final Component component : frames.getComponents()){
					component.mouseReleased(mouseX, mouseY, state);
				}
			}
		}
	}

	protected void keyTyped(final char typedChar, final int keyCode){
		for (final Frames frames : ClickGUI.frames){
			if (frames.isOpen() && !frames.getComponents().isEmpty()){
				for (final Component component : frames.getComponents()){
					component.keyTyped(typedChar, keyCode);
				}
			}
		}
		if (keyCode == 1){
			this.mc.displayGuiScreen(null);
			AnasheClient.get_hack_manager().get_module_with_tag("NewGUI").set_active(false);
			/**
			 * temporal bc make a save settings on unload is a hard work (i think)
			 */
			AnasheClient.get_config_manager().save_settings();
		}
	}

	public boolean doesGuiPauseGame(){
		return false;
	}



	/*public void initGui() {
		if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer && NewClickGUI.INSTANCE.blur.get_value(true)) {
			if (this.mc.entityRenderer.getShaderGroup() != null) {
				this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
			}
			this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		}
	}
	public void onGuiClosed() {
		if (this.mc.entityRenderer.getShaderGroup() != null) {
			this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
		}
	}*/



	public static Frames getFrameByName(String name){
		Frames pa = null;

		for (Frames frames : getFrames()){
			if (name.equalsIgnoreCase(String.valueOf(frames.category))) pa = frames;
		}
		return pa;
	}

	public static ArrayList<Frames> getFrames(){
		return frames;
	}
}
