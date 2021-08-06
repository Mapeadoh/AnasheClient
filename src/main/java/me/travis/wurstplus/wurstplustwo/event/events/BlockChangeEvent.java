package me.travis.wurstplus.wurstplustwo.event.events;

import me.travis.wurstplus.wurstplustwo.event.MinecraftEvent;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BlockChangeEvent extends MinecraftEvent {
    private final BlockPos position;
    private final Block block;

    public BlockChangeEvent(BlockPos position, Block block) {
        this.position = position;
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockPos getPosition() {
        return this.position;
    }
}