package net.cheatercodes.extraticks.portallookup;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

public class PortalBlocksState extends PersistentState {

    //A set of portal blocks in the world
    private LongOpenHashSet blocks = new LongOpenHashSet();

    //A set of chunks already checked for portals
    private LongOpenHashSet chunks = new LongOpenHashSet();

    public PortalBlocksState() {
        super("portals");
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.blocks = new LongOpenHashSet(tag.getLongArray("blocks"));
        this.chunks = new LongOpenHashSet(tag.getLongArray("chunks"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putLongArray("blocks", this.blocks.toLongArray());
        tag.putLongArray("chunks", this.chunks.toLongArray());
        return tag;
    }

    public LongOpenHashSet getBlocks() {
        return blocks;
    }

    public LongOpenHashSet getChunks() {
        return chunks;
    }
}
