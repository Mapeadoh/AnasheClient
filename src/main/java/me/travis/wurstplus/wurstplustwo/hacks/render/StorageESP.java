package me.travis.wurstplus.wurstplustwo.hacks.render;


import net.minecraft.tileentity.*;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.turok.draw.RenderHelp;

public class StorageESP extends WurstplusHack {
    public StorageESP() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        // Info.
        this.name        = "Storage ESP";
        this.tag         = "StorageESP";
        this.description = "Is able to see storages in world";
    }

    WurstplusSetting shulkeresp = create("ShulkerESP", "ShulkerESP", "ShulkerColor", combobox("ShulkerColor", "RGBMode"));
    WurstplusSetting shu_r = create("ShulkerR", "ShulkerR", 230, 0, 255);
    WurstplusSetting shu_g = create("ShulkerG", "ShulkerG", 0, 0, 255);
    WurstplusSetting shu_b = create("ShulkerB", "ShulkerB", 0, 0, 255);

    WurstplusSetting echestesp = create("EChestESP", "EChestESP", true);
    WurstplusSetting ec_r = create("EChestR", "ECR", 204, 0, 255);
    WurstplusSetting ec_g = create("EChestG", "ECG", 0, 0, 255);
    WurstplusSetting ec_b = create("EChestB", "ECB", 255, 0, 255);

    WurstplusSetting chestesp = create("ChestESP", "ChestESP", true);
    WurstplusSetting ch_r = create("ChestR", "ChestR", 153, 0, 255);
    WurstplusSetting ch_g = create("ChestG", "ChestG", 102, 0, 255);
    WurstplusSetting ch_b = create("ChestB", "ChestB", 0, 0, 255);

    WurstplusSetting otheresp = create("OtherESP", "OtherESP", true);
    WurstplusSetting ot_r = create("OtherR", "OtR", 153, 0, 255);
    WurstplusSetting ot_g = create("OtherG", "OtG", 102, 0, 255);
    WurstplusSetting ot_b = create("OtherB", "OtB", 0, 0, 255);
    WurstplusSetting ot_a = create("Outline A", "StorageESPOutlineA", 150, 0, 255);
    WurstplusSetting a = create("Solid A", "StorageESPSolidA", 150, 0, 255);

    private int color_alpha;

    @Override
    public void render(WurstplusEventRender event) {
        color_alpha = a.get_value(1);

        for (TileEntity tiles : mc.world.loadedTileEntityList) {
            if (tiles instanceof TileEntityShulkerBox) {
                final TileEntityShulkerBox shulker = (TileEntityShulkerBox) tiles;

                int hex = (255 << 24) | shulker.getColor().getColorValue() & 0xFFFFFFFF;

                if (shulkeresp.in("RGBMode")) {
                    draw(tiles, shu_r.get_value(1), shu_g.get_value(1), shu_b.get_value(1));
                } else {
                    draw(tiles, (hex & 0xFF0000) >> 16, (hex & 0xFF00) >> 8, (hex & 0xFF));
                }
            }

            if (tiles instanceof TileEntityEnderChest) {
                if (echestesp.get_value(true)) {
                    draw(tiles, ec_r.get_value(1), ec_g.get_value(1), ec_b.get_value(1));
                }
            }

            if (tiles instanceof TileEntityChest) {
                if (chestesp.get_value(true)) {
                    draw(tiles, ch_r.get_value(1), ch_g.get_value(1), ch_b.get_value(1));
                }
            }

            if (tiles instanceof TileEntityDispenser           ||
                    tiles instanceof TileEntityDropper         ||
                    tiles instanceof TileEntityHopper          ||
                    tiles instanceof TileEntityFurnace         ||
                    tiles instanceof TileEntityMobSpawner      ||
                    tiles instanceof TileEntityEnchantmentTable||
                    tiles instanceof TileEntityBed             ||
                    tiles instanceof TileEntityBrewingStand) {
                if (otheresp.get_value(true)) {
                    draw(tiles, ot_r.get_value(1), ot_g.get_value(1), ot_b.get_value(1));
                }
            }
        }
    }

    public void draw(TileEntity tile_entity, int r, int g, int b) {
        // Solid.
        RenderHelp.prepare("quads");
        RenderHelp.draw_cube(tile_entity.getPos(), r, g, b, a.get_value(1), "all");
        RenderHelp.release();

        // Outline.
        RenderHelp.prepare("lines");
        RenderHelp.draw_cube_line(tile_entity.getPos(), r, g, b, ot_a.get_value(1), "all");
        RenderHelp.release();
    }
}
