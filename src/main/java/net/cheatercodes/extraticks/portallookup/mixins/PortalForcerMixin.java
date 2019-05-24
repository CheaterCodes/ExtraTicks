package net.cheatercodes.extraticks.portallookup.mixins;

import net.cheatercodes.extraticks.portallookup.PortalFinder;
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

    //Inject at the portal lookup loop and prevent it's execution
    @ModifyConstant(method = "getPortal", constant = @Constant(intValue = -128, ordinal = 0))
    private int preventVanillaPortalLookup(int value) {
        return 129;
    }

    //Inject at the HEAD of the getPortal method to obtain the center of the search
    @Inject(method = "getPortal", at = @At("HEAD"))
    private void getOrigin(BlockPos origin, Vec3d vec3d_1, Direction direction_1, double double_1, double double_2, boolean boolean_1, CallbackInfoReturnable<BlockPattern.TeleportTarget> info) {
        this.origin = origin;
    }

    //Inject before the null-check to lookup the portal
    @ModifyVariable(method = "getPortal", at = @At(value="JUMP", opcode = Opcodes.IFNONNULL, shift = At.Shift.BEFORE), name = "blockPos_2")
    private BlockPos calcPos(BlockPos target) {
        //If portal already found, skip
        if(target != null) {
            return target;
        }

        return PortalFinder.findPortal(world, origin);
    }
}
