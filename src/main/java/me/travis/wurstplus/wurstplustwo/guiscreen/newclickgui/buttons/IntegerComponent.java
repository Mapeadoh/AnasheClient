package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons;

import java.math.RoundingMode;
import java.math.BigDecimal;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Buttons;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Renderer;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;

public class IntegerComponent extends Component {
	private boolean hovered;
	private final WurstplusSetting set;
	private final Buttons parent;
	private int offset;
	private int x;
	private int y;
	private boolean dragging;
	private double renderWidth;

	public IntegerComponent(final WurstplusSetting value, final Buttons button, final int offset){
		this.dragging = false;
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent(){
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, Renderer.getTransColor(hovered));
		final int drag = this.set.get_value(1) / this.set.get_max(1) * this.parent.parent.getWidth();
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 16, Renderer.getActiveModulesColor());
		Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
		FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.set.get_name() + " " + ChatFormatting.GRAY + this.set.get_value(1), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
	}

	@Override
	public void setOff(final int newOff){
		this.offset = newOff;
	}

	@Override
	public void updateComponent(final int mouseX, final int mouseY){
		this.hovered = (this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY));
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
		final double diff = Math.min(100, Math.max(0, mouseX - this.x));
		final int min = this.set.get_min(1);
		final int max = this.set.get_max(1);
		//int to double idk this is the original line:
		//this.renderWidth = 100 * (this.set.getValue() - min) / (max - min);
		this.renderWidth = 100 * (this.set.get_value(1) - min) / (max - min);
		if (this.dragging){
			if (diff == 0.0){
				this.set.set_value(this.set.get_min(1));
			}
			else{
				final int newValue = (int)roundToPlace(diff / 100.0 * (max - min) + min, 2);
				this.set.set_value(newValue);
			}
		}
	}

	private static double roundToPlace(final double value, final int places){
		if (places < 0){
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button){
		if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open){
			this.dragging = true;
		}
		if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open){
			this.dragging = true;
		}
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton){
		this.dragging = false;
	}

	public boolean isMouseOnButtonD(final int x, final int y){
		return x > this.x && x < this.x + (this.parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 16;
	}

	public boolean isMouseOnButtonI(final int x, final int y){
		return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 16;
	}
}