//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.util.ArrayList;
import java.util.Iterator;

import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusBlockInteractHelper;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class AntiCrystal extends WurstplusHack {
    int index;
    WurstplusSetting switch_mode = this.create("Mode", "Mode", "Normal", this.combobox(new String[]{"Normal", "Ghost", "None"}));
    WurstplusSetting max_self_damage = this.create("Max Self Damage", "MaxSelfDamage", 6, 0, 20);
    WurstplusSetting required_health = this.create("Required Health", "RequiredHealth", 12.0D, 1.0D, 36.0D);
    WurstplusSetting delay = this.create("Delay", "Delay", 2, 1, 20);
    WurstplusSetting range = this.create("Range", "Range", 4, 0, 10);
    WurstplusSetting rotate = this.create("Rotate", "Rotate", true);

    public AntiCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "Anti Crystal";
        this.tag = "AntiCrystal";
        this.description = "Places a pressure plate below crystals to remove crystal damage";
        this.index = 0;
    }

    public void update() {
        if (this.index > 2000) {
            this.index = 0;
        }

        if (this.find_in_hotbar() == -1) {
            WurstplusMessageUtil.send_client_message("No mats");
            this.set_disable();
        }

        if (!(mc.player.getHealth() + mc.player.getAbsorptionAmount() > (float)this.required_health.get_value(1))) {
            Iterator var1 = this.getExclusions().iterator();

            while(var1.hasNext()) {
                EntityEnderCrystal cry = (EntityEnderCrystal)var1.next();
                if (this.index % this.delay.get_value(1) == 0) {
                    if (this.switch_mode.in("Normal")) {
                        mc.player.inventory.currentItem = this.find_in_hotbar();
                    } else if (this.switch_mode.in("Ghost")) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(this.find_in_hotbar()));
                    }

                    if (mc.player.inventory.currentItem == this.find_in_hotbar()) {
                        WurstplusBlockInteractHelper.placeBlock(cry.getPosition(), this.rotate.get_value(true));
                        return;
                    }
                }
            }

        }
    }

    public ArrayList<EntityEnderCrystal> getCrystals() {
        ArrayList<EntityEnderCrystal> crystals = new ArrayList();
        Iterator var2 = mc.world.getLoadedEntityList().iterator();

        while(var2.hasNext()) {
            Entity ent = (Entity)var2.next();
            if (ent instanceof EntityEnderCrystal) {
                crystals.add((EntityEnderCrystal)ent);
            }
        }

        return crystals;
    }

    public ArrayList<EntityEnderCrystal> getInRange() {
        ArrayList<EntityEnderCrystal> inRange = new ArrayList();
        Iterator var2 = this.getCrystals().iterator();

        while(var2.hasNext()) {
            EntityEnderCrystal crystal = (EntityEnderCrystal)var2.next();
            if (mc.player.getDistance(crystal) <= (float)this.range.get_value(1)) {
                inRange.add(crystal);
            }
        }

        return inRange;
    }

    public ArrayList<EntityEnderCrystal> getExclusions() {
        ArrayList<EntityEnderCrystal> returnOutput = new ArrayList();
        Iterator var2 = this.getInRange().iterator();

        while(var2.hasNext()) {
            EntityEnderCrystal crystal = (EntityEnderCrystal)var2.next();
            if (mc.world.getBlockState(crystal.getPosition()).getBlock() != Blocks.WOODEN_PRESSURE_PLATE && mc.world.getBlockState(crystal.getPosition()).getBlock() != Blocks.STONE_PRESSURE_PLATE && mc.world.getBlockState(crystal.getPosition()).getBlock() != Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE && mc.world.getBlockState(crystal.getPosition()).getBlock() != Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                double self_damage = (double) WurstplusCrystalUtil.calculateDamage(crystal, mc.player);
                if (!(self_damage < (double)this.max_self_damage.get_value(1))) {
                    returnOutput.add(crystal);
                }
            }
        }

        return returnOutput;
    }

    private int find_in_hotbar() {
        for(int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockPressurePlate) {
                    return i;
                }
            }
        }

        return -1;
    }
}
