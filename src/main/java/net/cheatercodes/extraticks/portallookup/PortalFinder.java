package net.cheatercodes.extraticks.portallookup;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class PortalFinder {

    public static void checkChunk(World world, ChunkPos chunkPos) {
        WorldChunk chunk = world.method_8497(chunkPos.x, chunkPos.z);

        PortalBlocksState state = ((PortalBlocksProvider)world).getPortalBlocksState();
        LongOpenHashSet blocks = state.getBlocks();

        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                for(int y = 0; y < chunk.getHeight(); y++) {
                    BlockPos pos = chunkPos.toBlockPos(x, y, z);
                    if(chunk.getBlockState(pos).getBlock() == Blocks.NETHER_PORTAL) {
                        blocks.add(pos.asLong());
                    }
                }
            }
        }
    }

    public static BlockPos findPortal(World world, BlockPos origin) {
        int minX = origin.getX() - 128;
        int maxX = origin.getX() + 128;
        int minZ = origin.getZ() - 128;
        int maxZ = origin.getZ() + 128;
        int maxY = world.getEffectiveHeight() - 1;

        PortalBlocksState state = ((PortalBlocksProvider)world).getPortalBlocksState();
        LongOpenHashSet blocks = state.getBlocks();
        LongOpenHashSet chunks = state.getChunks();

        //Load the chunks normally implicitly loaded through portal lookup
        for(int x = -128; x <= 128; x += 16) {
            for (int z = -128; z <= 128; z += 16) {
                ChunkPos chunkPos = new ChunkPos(origin.add(x, 0, z));
                world.method_8497(chunkPos.x, chunkPos.z);

                //If chunk loaded for the first time (with this mod installed)
                if(!chunks.contains(chunkPos.toLong())) {
                    checkChunk(world, chunkPos);
                    chunks.add(chunkPos.toLong());
                }
            }
        }

        double minDistance = Double.MAX_VALUE;
        BlockPos target = null;

        //Only consider portal block with no portal below
        //Priority:
        // Distance
        // LowestX
        // LowestZ
        // HighestY
        for(long l : blocks) {
            BlockPos portal = BlockPos.fromLong(l);

            //Check if the found Block is within range
            if(portal.getX() < minX || portal.getX() > maxX)
                continue;
            if(portal.getY() < 0 || portal.getY() > maxY)
                continue;
            if(portal.getZ() < minZ || portal.getZ() > maxZ)
                continue;

            //Only consider the bottom most portal block of a portal
            if(world.getBlockState(portal.down()).getBlock() == Blocks.NETHER_PORTAL){
                continue;
            }

            double distance = portal.getSquaredDistance(origin);

            //If this is the newest closest portal
            if(distance < minDistance) {
                target = portal;
                minDistance = distance;
            }
            // Usually comparing doubles like that should be avoided,
            // however this is for edge cases to keep the priorities intact
            else if(distance == minDistance) {
                if(portal.getX() < target.getX()) {
                    target = portal;
                    minDistance = distance;
                }
                else if(portal.getZ() < target.getZ()){
                    target = portal;
                    minDistance = distance;
                }
                else if(portal.getY() > target.getY()) {
                    target = portal;
                    minDistance = distance;
                }
            }
        }

        return target;
    }
}
