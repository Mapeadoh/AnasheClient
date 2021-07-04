package me.travis.wurstplus.wurstplustwo.hacks.render;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.Entity;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class NoSandRender extends WurstplusHack
{
    public NoSandRender() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "No Sand Render";
        this.tag = "NoSandRender";
        this.description = "allows you to drop FPS of other players but not you";
    }
    
    @Override
    public void update() {
        for (final Entity e : NoSandRender.mc.world.loadedEntityList) {
            if (e instanceof EntityFallingBlock) {
                NoSandRender.mc.world.removeEntity(e);
            }
        }
    }
}