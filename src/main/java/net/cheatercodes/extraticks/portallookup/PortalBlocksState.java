package net.cheatercodes.extraticks.portallookup;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

public class PortalBlocksState extends PersistentState {

    private LongOpenHashSet blocks = new LongOpenHashSet();

    public PortalBlocksState() {
        super("portals");
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.blocks = new LongOpenHashSet(tag.getLongArray("blocks"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putLongArray("blocks", this.blocks.toLongArray());
        return tag;
    }

    public LongOpenHashSet getBlocks() {
        return blocks;
    }
}
