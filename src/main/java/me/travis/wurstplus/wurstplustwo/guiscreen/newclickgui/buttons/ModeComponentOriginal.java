//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Buttons;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Renderer;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.render.WurstplusDraw;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import net.minecraft.client.gui.Gui;

public class ModeComponentOriginal extends Component {
    private WurstplusSetting op;
    private Buttons parent;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;

    public ModeComponentOriginal(Buttons parent, int offset) {
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }

    public void setOff(int newOff) {
        this.offset = newOff;
    }

    public void renderComponent() {
        WurstplusDraw.draw_rect(this.parent.parent.getX() - 1, this.parent.parent.getY() + this.offset, this.parent.parent.getX(), this.parent.parent.getY() + 15 + this.offset, NewClickGUI.INSTANCE.name_frame_r.get_value(1), NewClickGUI.INSTANCE.name_frame_g.get_value(1), NewClickGUI.INSTANCE.name_frame_b.get_value(1), 255);
        WurstplusDraw.draw_rect(this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() + 1, this.parent.parent.getY() + 15 + this.offset, NewClickGUI.INSTANCE.name_frame_r.get_value(1), NewClickGUI.INSTANCE.name_frame_g.get_value(1), NewClickGUI.INSTANCE.name_frame_b.get_value(1), 255);
        WurstplusDraw.draw_rect(this.parent.parent.getX() - 1, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() + 1, this.parent.parent.getY() + this.offset + 16, NewClickGUI.INSTANCE.name_frame_r.get_value(1), NewClickGUI.INSTANCE.name_frame_g.get_value(1), NewClickGUI.INSTANCE.name_frame_b.get_value(1), 255);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, -13684945);
        WurstplusDraw.draw_rect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 1, this.parent.parent.getY() + this.offset + 15, NewClickGUI.INSTANCE.name_frame_r.get_value(1), NewClickGUI.INSTANCE.name_frame_g.get_value(1), NewClickGUI.INSTANCE.name_frame_b.get_value(1), 255);
        WurstplusDraw.draw_rect(this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() - 1, this.parent.parent.getY() + this.offset + 15, NewClickGUI.INSTANCE.name_frame_r.get_value(1), NewClickGUI.INSTANCE.name_frame_g.get_value(1), NewClickGUI.INSTANCE.name_frame_b.get_value(1), 255);
        FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.op.get_name() + " " + ChatFormatting.GRAY + this.op.get_current_value().toUpperCase(), (int) (this.parent.parent.getX() + 4), (int) (this.parent.parent.getY() + this.offset + 4), Renderer.getFontColor());
    }

    public void updateComponent(int mouseX, int mouseY) {
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            int maxIndex = this.op.get_values().size() - 1;
            ++this.modeIndex;
            if (this.modeIndex > maxIndex) {
                this.modeIndex = 0;
            }

            this.op.set_current_value((String)this.op.get_values().get(this.modeIndex));
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
    }
}
