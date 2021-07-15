package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.mapeadoh.clientstuff.gamesense.TransformSideFirstPersonEvent;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class ViewModel extends WurstplusHack {
    public ViewModel() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name        = "ViewModel";
        this.tag         = "ViewModel";
        this.description = "works?";
    }// works?
    WurstplusSetting mode = create("Mode", "Mode", "Value", combobox("Value", "Fov", "Both"/*ambos en ingles*/));
    WurstplusSetting cancel_eat = create("NoEatAnim", "CancelEatAnim", true);
    WurstplusSetting x_left = create("LeftX", "LeftX", 0.0, -2.0, 2.0);
    WurstplusSetting y_left = create("LeftY", "LeftY", 0.0, -2.0, 2.0);
    WurstplusSetting z_left = create("LeftZ", "LeftZ", 0.0, -2.0, 2.0);

    WurstplusSetting x_right = create("RightX", "RightX", 0.0, -2.0, 2.0);
    WurstplusSetting y_right = create("RightY", "RightY", 0.0, -2.0, 2.0);
    WurstplusSetting z_right = create("RightZ", "RightZ", 0.0, -2.0, 2.0);

    WurstplusSetting fov = create("ItemFov", "ItemFov", 130, 70, 200);
    /* original code lololololololol
    ModeSetting type = registerMode("Type", Arrays.asList("Value", "FOV", "Both"), "Value");
    public BooleanSetting cancelEating = registerBoolean("No Eat", false);
    DoubleSetting xLeft = registerDouble("Left X", 0.0, -2.0, 2.0);
    DoubleSetting yLeft = registerDouble("Left Y", 0.2, -2.0, 2.0);
    DoubleSetting zLeft = registerDouble("Left Z", -1.2, -2.0, 2.0);
    DoubleSetting xRight = registerDouble("Right X", 0.0, -2.0, 2.0);
    DoubleSetting yRight = registerDouble("Right Y", 0.2, -2.0, 2.0);
    DoubleSetting zRight = registerDouble("Right Z", -1.2, -2.0, 2.0);
    DoubleSetting fov = registerDouble("Item FOV", 130, 70, 200);
    */

    @EventHandler
    private final Listener<TransformSideFirstPersonEvent> eventListener = new Listener<>(event -> {
        if (mode.in("Value") || mode.in("Both")) {
            if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(x_right.get_value(1d), y_right.get_value(1d), z_right.get_value(1d));
            } else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
                GlStateManager.translate(x_left.get_value(1d), y_left.get_value(1d), z_left.get_value(1d));
            }
        }
    });

    @EventHandler
    private final Listener<EntityViewRenderEvent.FOVModifier> fovModifierListener = new Listener<>(event -> {
        if (mode.in("FOV") || mode.in("Both")) {
            event.setFOV(fov.get_value(1));
        }
    });
    public void enable(){
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }
    public void disable(){
        WurstplusEventBus.EVENT_BUS.unsubscribe(this);
    }
}