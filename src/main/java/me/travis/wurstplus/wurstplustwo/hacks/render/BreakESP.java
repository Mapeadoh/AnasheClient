package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.travis.mapeadoh.clientstuff.phobos.BlockBreakingEvent;

public class BreakESP extends WurstplusHack {

    private final Map<BlockPos, Integer> breakingProgressMap = new HashMap<BlockPos, Integer>();

    public BreakESP(){
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "BreakESP";
        this.tag = "BreakESP";
        this.description = "Shows block breaking progress";
    }

    @SubscribeEvent
    public void onBlockBreak(BlockBreakingEvent event) {
        this.breakingProgressMap.put(event.pos, event.breakStage);
    }

    @Override
    public void render(WurstplusEventRender event) {
    }

    public enum Mode {
        BAR,
        ALPHA,
        WIDTH

    }
}