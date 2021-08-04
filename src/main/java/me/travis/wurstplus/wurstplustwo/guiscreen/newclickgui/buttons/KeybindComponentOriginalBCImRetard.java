//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Buttons;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Component;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame.Renderer;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.render.WurstplusDraw;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class KeybindComponentOriginalBCImRetard extends Component {
    private boolean isBinding;
    private Buttons parent;
    private int offset;
    private int x;
    private int y;
    private String points;
    private float tick;

    public KeybindComponentOriginalBCImRetard(Buttons parent, int offset) {
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.points = ".";
        this.tick = 0.0F;
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
        if (this.isBinding) {
            this.tick += 0.5F;
            FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), "Press a Key" + ChatFormatting.GRAY + " " + this.points, (int)(this.parent.parent.getX() + 4), (int)(this.parent.parent.getY() + this.offset + 4), Renderer.getFontColor());
        } else {
            FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), "Bind" + ChatFormatting.GRAY + " " + this.parent.mod.get_bind("string"), (int)(this.parent.parent.getX() + 4), (int)(this.parent.parent.getY() + this.offset + 4), Renderer.getFontColor());
        }

        if (this.isBinding) {
            if (this.tick >= 15.0F) {
                this.points = "..";
            }

            if (this.tick >= 30.0F) {
                this.points = "...";
            }

            if (this.tick >= 45.0F) {
                this.points = ".";
                this.tick = 0.0F;
            }
        }

    }

    public void updateComponent(int mouseX, int mouseY) {
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.isBinding = !this.isBinding;
        }

    }

    public void keyTyped(char typedChar, int key) {
        if (this.isBinding) {
            if (Keyboard.isKeyDown(211)) {
                this.parent.mod.set_bind(0);
                this.isBinding = false;
            } else if (Keyboard.isKeyDown(14)) {
                this.parent.mod.set_bind(0);
                this.isBinding = false;
            } else {
                this.parent.mod.set_bind(key);
                this.isBinding = false;
            }
        }

    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 100 && y > this.y && y < this.y + 15;
    }
}
