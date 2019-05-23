package net.cheatercodes.extraticks.portallookup.mixins;

import net.cheatercodes.extraticks.portallookup.PortalBlocksProvider;
import net.cheatercodes.extraticks.portallookup.PortalBlocksState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements PortalBlocksProvider {

    PortalBlocksState portalBlocks = null;

    @Inject(method = "save", at = @At("HEAD"))
    private void onSave(CallbackInfo info) {
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onLoad(MinecraftServer minecraftServer_1, Executor executor_1, WorldSaveHandler worldSaveHandler_1,
                        LevelProperties levelProperties_1, DimensionType dimensionType_1, Profiler profiler_1,
                        WorldGenerationProgressListener worldGenerationProgressListener_1, CallbackInfo info) {
        portalBlocks = ((ServerWorld)(Object)this).getPersistentStateManager().getOrCreate(PortalBlocksState::new, "portals");
        System.out.println("Loaded " + portalBlocks.getBlocks().size() + " PortalBlocks");
    }

    public PortalBlocksState getPortalBlocksState() {
        return portalBlocks;
    }
}
