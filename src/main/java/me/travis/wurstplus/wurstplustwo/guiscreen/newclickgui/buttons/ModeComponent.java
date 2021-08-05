package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Buttons;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Renderer;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;

public class ModeComponent extends Component {
	private boolean hovered;
	private final Buttons parent;
	private WurstplusSetting set;
	private int offset;
	private int x;
	private int y;
	private int modeIndex;
	
	public ModeComponent(final WurstplusSetting set, final Buttons button, final int offset){
		this.parent = button;
		this.set = set;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}
	
	/*@Override
	public void setOff(final int newOff){
		this.offset = newOff;
	}
	
	*/
	public void setOff(int newOff) {
		this.offset = newOff;
	}

	@Override
	public void renderComponent() {
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, Renderer.getTransColor(hovered));
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
		FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.set.get_name() + ": " + ChatFormatting.GRAY + this.set.get_current_value(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
	}

	public void updateComponent(int mouseX, int mouseY) {
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = this.set.get_values().size() - 1;
			++this.modeIndex;
			if (this.modeIndex > maxIndex) {
				this.modeIndex = 0;
			}

			this.set.set_current_value((String)this.set.get_values().get(this.modeIndex));
		}

	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
	}
}
