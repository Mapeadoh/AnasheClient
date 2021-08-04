package me.travis.wurstplus.wurstplustwo.hacks.dev;

import me.travis.mapeadoh.clientstuff.wp3.events.RenderItemEvent;
import me.travis.mapeadoh.clientstuff.wp3.events.CommitEvent;
import me.travis.mapeadoh.clientstuff.wp3.events.EventPriority;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;


public class WP3ViewModel extends WurstplusHack {
    public WP3ViewModel() {
        super(WurstplusCategory.WURSTPLUS_BETA);

        this.name = "New Viewmodel";
        this.tag = "NewViewmodel";
        this.description = "q hiciste chinchulin";

    }
    WurstplusSetting mainX = create("MainX", "MainX", 1.2d, 0.0d, 6.0d);
    WurstplusSetting mainY = create("MainY", "MainY", -0.95d, -3.0d, 3.0d);
    WurstplusSetting mainZ = create("MainZ", "MainZ", -1.45d, -5.0d, 5.0);

    WurstplusSetting offX = create("OffX", "OffX", -1.2, -6.0, 0.0);
    WurstplusSetting offY = create("OffY", "OffY", -0.95, -3.0, 3.0);
    WurstplusSetting offZ = create("OffZ", "OffZ", -1.45d, -5.0d, 5.0);

    WurstplusSetting mainAngel = create("MainAngle", "MainAngle", 0.0, 0.0, 360.0);

    WurstplusSetting mainRx = create("MainRotX", "MainRotX", 0.0, -1.0, 1.0);
    WurstplusSetting mainRy = create("MainRotY", "MainRotY", 0.0, -1.0, 1.0);
    WurstplusSetting mainRz = create("MainRotZ", "MainRotZ", 0.0, -1.0, 1.0);

    WurstplusSetting offAngel = create("OffAngle", "OffAngle", 0.0, 0.0, 360.0);

    WurstplusSetting offRx = create("OffRotX", "OffRotX", 1.2d, 0.0d, 6.0d);
    WurstplusSetting offRy = create("OffRotY", "OffRotY", -0.95d, -3.0d, 3.0d);
    WurstplusSetting offRz = create("OffRotZ", "OffRotZ", -1.45d, -5.0d, 5.0);

    WurstplusSetting mainScaleX = create("mainScaleX","mainScaleX", 1.0, -5.0, 10.0);
    WurstplusSetting mainScaleY = create("mainScaleY","mainScaleY", 1.0, -5.0, 10.0);
    WurstplusSetting mainScaleZ = create("mainScaleZ","mainScaleZ", 1.0, -5.0, 10.0);
    WurstplusSetting offScaleX = create("offScaleX","OffScaleX", 1.0, -5.0, 10.0);
    WurstplusSetting offScaleY = create("offScaleY","OffScaleY", 1.0, -5.0, 10.0);
    WurstplusSetting offScaleZ = create("offScaleZ","OffScaleZ", 1.0, -5.0, 10.0);
    /*Rewrote
    DoubleSetting mainX = new DoubleSetting("mainX", 1.2, 0.0, 6.0, this);
    DoubleSetting mainY = new DoubleSetting("mainY", -0.95, -3.0, 3.0, this);
    DoubleSetting mainZ = new DoubleSetting("mainZ", -1.45, -5.0, 5.0, this);
    DoubleSetting offX = new DoubleSetting("offX", -1.2, -6.0, 0.0, this);
    DoubleSetting offY = new DoubleSetting("offY", -0.95, -3.0, 3.0, this);
    DoubleSetting offZ = new DoubleSetting("offZ", -1.45, -5.0, 5.0, this);
    DoubleSetting mainAngel = new DoubleSetting("mainAngle", 0.0, 0.0, 360.0, this);
    DoubleSetting mainRx = new DoubleSetting("mainRotationPointX", 0.0, -1.0, 1.0, this);
    DoubleSetting mainRy = new DoubleSetting("mainRotationPointY", 0.0, -1.0, 1.0, this);
    DoubleSetting mainRz = new DoubleSetting("mainRotationPointZ", 0.0, -1.0, 1.0, this);
    DoubleSetting offAngel = new DoubleSetting("offAngle", 0.0, 0.0, 360.0, this);
    DoubleSetting offRx = new DoubleSetting("offRotationPointX", 0.0, -1.0, 1.0, this);
    DoubleSetting offRy = new DoubleSetting("offRotationPointY", 0.0, -1.0, 1.0, this);
    DoubleSetting offRz = new DoubleSetting("offRotationPointZ", 0.0, -1.0, 1.0, this);
    DoubleSetting mainScaleX = new DoubleSetting("mainScaleX", 1.0, -5.0, 10.0, this);
    DoubleSetting mainScaleY = new DoubleSetting("mainScaleY", 1.0, -5.0, 10.0, this);
    DoubleSetting mainScaleZ = new DoubleSetting("mainScaleZ", 1.0, -5.0, 10.0, this);
    DoubleSetting offScaleX = new DoubleSetting("offScaleX", 1.0, -5.0, 10.0, this);
    DoubleSetting offScaleY = new DoubleSetting("offScaleY", 1.0, -5.0, 10.0, this);
    DoubleSetting offScaleZ = new DoubleSetting("offScaleZ", 1.0, -5.0, 10.0, this);*/

    @CommitEvent(priority = EventPriority.LOW)
    public void onItemRender(RenderItemEvent event) {
        event.setMainX(mainX.get_value(1));
        event.setMainY(mainY.get_value(1));
        event.setMainZ(mainZ.get_value(1));

        event.setOffX(offX.get_value(1));
        event.setOffY(offY.get_value(1));
        event.setOffZ(offZ.get_value(1));

        event.setMainRAngel(mainAngel.get_value(1));
        event.setMainRx(mainRx.get_value(1));
        event.setMainRy(mainRy.get_value(1));
        event.setMainRz(mainRz.get_value(1));

        event.setOffRAngel(offAngel.get_value(1));
        event.setOffRx(offRx.get_value(1));
        event.setOffRy(offRy.get_value(1));
        event.setOffRz(offRz.get_value(1));

        event.setMainHandScaleX(mainScaleX.get_value(1));
        event.setMainHandScaleY(mainScaleY.get_value(1));
        event.setMainHandScaleZ(mainScaleZ.get_value(1));

        event.setOffHandScaleX(offScaleX.get_value(1));
        event.setOffHandScaleY(offScaleY.get_value(1));
        event.setOffHandScaleZ(offScaleZ.get_value(1));
    }
}