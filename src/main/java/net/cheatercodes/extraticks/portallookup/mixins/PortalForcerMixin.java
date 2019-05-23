package net.cheatercodes.extraticks.portallookup.mixins;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.cheatercodes.extraticks.portallookup.PortalBlocksProvider;
import net.cheatercodes.extraticks.portallookup.PortalBlocksState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalForcer.class)
public class PortalForcerMixin {

    BlockPos origin;

    @Shadow
    private ServerWorld world;


    @ModifyConstant(method = "getPortal", constant = @Constant(intValue = -128, ordinal = 0))
    private int preventVanillaPortalLookup(int value) {
        System.out.println("Skipping vanilla portal lookup");
        return 129;
    }

    @Inject(method = "getPortal", at = @At("HEAD"))
    private void getOrigin(BlockPos origin, Vec3d vec3d_1, Direction direction_1, double double_1, double double_2, boolean boolean_1, CallbackInfoReturnable<BlockPattern.TeleportTarget> info) {
        System.out.println("Portal lookup for " + origin);
        this.origin = origin;
    }

    @ModifyVariable(method = "getPortal", at = @At(value="JUMP", opcode = Opcodes.IFNONNULL, shift = At.Shift.BEFORE), name = "blockPos_2")
    private BlockPos calcPos(BlockPos target) {
        //If portal already found, skip
        if(target != null) {
            return target;
        }

        //Only consider portal block with no portal below
        //Priority:
        //Distance
        //BiggestX
        //BiggestZ
        //LowestY

        int minX = origin.getX() - 128;
        int maxX = origin.getX() + 128;
        int minZ = origin.getZ() - 128;
        int maxZ = origin.getZ() + 128;
        int maxY = world.getEffectiveHeight() - 1;

        double minDistance = Double.MAX_VALUE;

        PortalBlocksState state = ((PortalBlocksProvider)world).getPortalBlocksState();
        LongOpenHashSet set = state.getBlocks();
        for(long l : set) {
            BlockPos portal = BlockPos.fromLong(l);
            if(world.getBlockState(portal.down()).getBlock() == Blocks.NETHER_PORTAL){
                continue;
            }

            double distance = portal.getSquaredDistance(origin);

            if(distance < minDistance) {
                target = portal;
            }
            // Usually comparing doubles like that should be avoided,
            // however this is for edge cases to keep the priorities intact
            else if(distance == minDistance) {
                if(portal.getX() > target.getX()) {
                    target = portal;
                }
                else if(portal.getZ() > target.getZ()){
                    target = portal;
                }
                else if(portal.getY() < target.getY()) {
                    target = portal;
                }
            }
        }

        return target;
    }
}
