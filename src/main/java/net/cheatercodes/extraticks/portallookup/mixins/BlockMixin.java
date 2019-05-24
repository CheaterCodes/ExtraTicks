package net.cheatercodes.extraticks.portallookup.mixins;

import net.cheatercodes.extraticks.portallookup.PortalBlocksProvider;
import net.cheatercodes.extraticks.portallookup.PortalBlocksState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "onBlockAdded", at = @At("HEAD"))
    public void onAnyBlockAdded(BlockState new_state, World world, BlockPos pos, BlockState same_as_new_state, boolean boolean_1, CallbackInfo info) {
        if(new_state.getBlock() == Blocks.NETHER_PORTAL) {
            //Update the PortalBlockState with the new PortalBlock
            PortalBlocksState state = ((PortalBlocksProvider)world).getPortalBlocksState();
            state.getBlocks().add(pos.asLong());
            state.markDirty();
        }
    }

    @Inject(method = "onBlockRemoved", at = @At("HEAD"))
    public void onAnyBlockRemoved(BlockState old_block, World world, BlockPos pos, BlockState probably_same_as_old_block, boolean boolean_1, CallbackInfo info) {
        if(old_block.getBlock() == Blocks.NETHER_PORTAL) {
            //Remove the PortalBlock from the PortalBlockState
            PortalBlocksState state = ((PortalBlocksProvider)world).getPortalBlocksState();
            state.getBlocks().remove(pos.asLong());
            state.markDirty();
        }
    }
}
