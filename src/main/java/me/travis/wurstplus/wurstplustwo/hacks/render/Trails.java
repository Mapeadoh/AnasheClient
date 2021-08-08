package me.travis.wurstplus.wurstplustwo.hacks.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.travis.mapeadoh.clientstuff.noraAndDelux.RenderUtil;
import me.travis.mapeadoh.clientstuff.phobos.UpdateWalkingPlayerEvent;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Trails
        extends WurstplusHack {
    private final WurstplusSetting lineWidth = this.create("LineWidth", "LineWidth", 1.5, 0.1f, 5.0);
    private final WurstplusSetting red = this.create("Red", "Red", 0, 0, 255);
    private final WurstplusSetting green = this.create("Green", "Green", 255, 0, 255);
    private final WurstplusSetting blue = this.create("Blue", "Blue", 0, 0, 255);
    private final WurstplusSetting alpha = this.create("Alpha", "Alpha", 255, 0, 255);
    private final Map<Entity, List<Vec3d>> renderMap = new HashMap<Entity, List<Vec3d>>();

    public Trails() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "Trails";
        this.tag = "Trails";
        this.description = "Draws trails on projectiles";
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        for (final Entity entity : Trails.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityThrowable) && !(entity instanceof EntityArrow)) {
                continue;
            }
            final List<Vec3d> vectors = (this.renderMap.get(entity) != null) ? this.renderMap.get(entity) : new ArrayList<Vec3d>();
            vectors.add(new Vec3d(entity.posX, entity.posY, entity.posZ));
            this.renderMap.put(entity, vectors);
        }
    }

    @Override
    public void render() {
        for (Entity entity : Trails.mc.world.loadedEntityList) {
            if (!this.renderMap.containsKey((Object)entity)) continue;
            GlStateManager.pushMatrix();
            RenderUtil.GLPrePhobos((float)this.lineWidth.get_value(1.5));
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GL11.glColor4f((float)((float)this.red.get_value(0) / 255.0f), (float)((float)this.green.get_value(255) / 255.0f), (float)((float)this.blue.get_value(0) / 255.0f), (float)((float)this.alpha.get_value(255) / 255.0f));
            GL11.glLineWidth((float)((float)this.lineWidth.get_value(1.5)));
            GL11.glBegin((int)1);
            for (int i = 0; i < this.renderMap.get((Object)entity).size() - 1; ++i) {
                GL11.glVertex3d((double)this.renderMap.get((Object)entity).get((int)i).x, (double)this.renderMap.get((Object)entity).get((int)i).y, (double)this.renderMap.get((Object)entity).get((int)i).z);
                GL11.glVertex3d((double)this.renderMap.get((Object)entity).get((int)(i + 1)).x, (double)this.renderMap.get((Object)entity).get((int)(i + 1)).y, (double)this.renderMap.get((Object)entity).get((int)(i + 1)).z);
            }
            GL11.glEnd();
            GlStateManager.resetColor();
            GlStateManager.enableDepth();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            RenderUtil.GlPost();
            GlStateManager.popMatrix();
        }
    }
}