package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Buttons;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Renderer;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;

public class LabelComponent extends Component {
    private boolean hovered;
    private final Buttons parent;
    private WurstplusSetting setting;
    private int offset;
    private int x;
    private int y;

    public LabelComponent(final Buttons button, final int offset){
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void renderComponent(){
            Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + 16 + this.offset, Renderer.getTransColor(hovered));
            Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
            FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.setting.get_name(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
    }


    @Override
    public void setOff(final int newOff){
        this.offset = newOff;
    }

    @Override
    public void updateComponent(final int mouseX, final int mouseY){
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    public boolean isMouseOnButton(final int x, final int y){
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16;
    }
}