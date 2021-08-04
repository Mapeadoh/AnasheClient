package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons;

import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Buttons;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Renderer;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;

public class KeybindComponent extends Component {
	private boolean hovered;
	private boolean binding;
	private final Buttons parent;
	private int offset;
	private int x;
	private int y;
	
	public KeybindComponent(final Buttons button, final int offset){
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(final int newOff){
		this.offset = newOff;
	}
	
	@Override
	public void renderComponent(){
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, Renderer.getTransColor(true));
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 15, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, Renderer.getTransColor(true));
		if (this.binding) {
			FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), "Press a Key" + ChatFormatting.GRAY + " ", (int)(this.parent.parent.getX() + 4), (int)(this.parent.parent.getY() + this.offset + 4), Renderer.getFontColor());
		} else {
			FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), "Bind" + ChatFormatting.GRAY + " " + this.parent.mod.get_bind("string"), (int)(this.parent.parent.getX() + 4), (int)(this.parent.parent.getY() + this.offset + 4), Renderer.getFontColor());
		}
	}

	public void updateComponent(int mouseX, int mouseY) {
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.binding = !this.binding;
		}

	}

	public void keyTyped(char typedChar, int key) {
		if (this.binding) {
			if (Keyboard.isKeyDown(211)) {
				this.parent.mod.set_bind(0);
				this.binding = false;
			} else if (Keyboard.isKeyDown(14)) {
				this.parent.mod.set_bind(0);
				this.binding = false;
			} else {
				this.parent.mod.set_bind(key);
				this.binding = false;
			}
		}

	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
	}
}
