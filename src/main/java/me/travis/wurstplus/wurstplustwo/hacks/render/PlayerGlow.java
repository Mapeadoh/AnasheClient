package me.travis.wurstplus.wurstplustwo.hacks.render;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class PlayerGlow extends WurstplusHack
{
    public PlayerGlow() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "PlayerGlow";
        this.tag = "PlayerGlow";
        this.description = "PlayerGlow";
    }

    @Override
    public void update() {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (!entity.isGlowing()) {
                entity.setGlowing(true);
            }
        }
    }

    public void disable() {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.isGlowing()) {
                entity.setGlowing(false);
            }
        }
        super.disable();
    }
}