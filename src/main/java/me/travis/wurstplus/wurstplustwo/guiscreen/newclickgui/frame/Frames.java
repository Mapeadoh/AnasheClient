package me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.frame;

import me.travis.wurstplus.AnasheClient;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.ClickGUI;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.FontUtils;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class Frames {
    public ArrayList<Component> guicomponents;
    public WurstplusCategory category;
    private final int width;
    private final int barHeight;
    private int height;
    public int x;
    public int y;
    public int dragX;
    public int dragY;
    private boolean isDragging;
    public boolean open;
    boolean font;

    public Frames(final WurstplusCategory catg){
        this.guicomponents = new ArrayList<Component>();
        this.category = catg;
        this.open = true;
        this.isDragging = false;
        this.x = 10;
        this.y = 30;
        this.dragX = 0;
        this.width = 100;
        this.barHeight = 16;
        int tY = this.barHeight;

        for (final WurstplusHack mod : AnasheClient.get_module_manager().get_modules_with_category(catg)){
            final Buttons devmodButton = new Buttons(mod, this, tY);
            this.guicomponents.add(devmodButton);
            tY += 16;
        }
        this.refresh();
    }

    public ArrayList<Component> getComponents() {
        return this.guicomponents;
    }

    public int getWidth() {
        return this.width;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(final int newX) {
        this.x = newX;
    }

    public void setY(final int newY) {
        this.y = newY;
    }

    public void renderGUIFrame(final FontRenderer fontRenderer){
        Renderer.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.barHeight, Renderer.getMainColor(), Renderer.getTransColor(false));
        if(font) AnasheClient.fontRenderer.drawStringWithShadow(this.category.name(), (float)(this.x + 2), (float)(this.y + 3), Renderer.getFontColor());
        else FontUtils.drawStringWithShadow(NewClickGUI.INSTANCE.customFont.get_value(true), this.category.get_name(), this.x + 2, this.y + 3, Renderer.getFontColor());
        if (this.open && !this.guicomponents.isEmpty()){
            for (final Component component : this.guicomponents){
                component.renderComponent();
            }
        }
    }

    public void updatePosition(final int mouseX, final int mouseY){
        if (this.isDragging){
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }

    public boolean isWithinHeader(final int x, final int y){
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public void setDrag(final boolean drag){
        this.isDragging = drag;
    }

    public void setOpen(final boolean open){
        this.open = open;
    }

    public boolean isOpen(){
        return this.open;
    }

    public void refresh(){
        int off = this.barHeight;
        for (final Component comp : this.guicomponents){
            comp.setOff(off);
            off += comp.getHeight();
        }
        this.height = off;
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (final Frames frames : ClickGUI.frames) {
            if (scrollWheel < 0) {
                frames.setY(frames.getY() - NewClickGUI.INSTANCE.scrollspeed.get_value(1));
            }
            else if (scrollWheel > 0) {
                frames.setY(frames.getY() + NewClickGUI.INSTANCE.scrollspeed.get_value(1));
            }
        }
    }
}