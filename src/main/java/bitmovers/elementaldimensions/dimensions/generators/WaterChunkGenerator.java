package bitmovers.elementaldimensions.dimensions.generators;

import bitmovers.elementaldimensions.dimensions.generators.tools.WaterTerrainGenerator;
import bitmovers.elementaldimensions.mobs.EntityWaterCreep;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WaterChunkGenerator implements IChunkGenerator {

    private final World worldObj;
    private Random random;

    private List<Biome.SpawnListEntry> mobs = Lists.newArrayList(new Biome.SpawnListEntry(EntityWaterCreep.class, 100, 1, 1));

    private WaterTerrainGenerator terraingen = new WaterTerrainGenerator();

    public WaterChunkGenerator(World worldObj) {
        this.worldObj = worldObj;
        long seed = 0x1fff; // @todo
        this.random = new Random((seed + 516) * 314);
        terraingen.setup(worldObj, random, Blocks.STONE.getDefaultState(), Biomes.PLAINS);
        worldObj.setSeaLevel(WaterTerrainGenerator.SEALEVEL);
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        terraingen.generate(x, z, chunkprimer);

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);

        byte[] biomeArray = chunk.getBiomeArray();
        for (int i = 0; i < biomeArray.length; ++i) {
            biomeArray[i] = (byte) Biome.getIdForBiome(Biomes.PLAINS);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.WATER_CREATURE) {
            return mobs;
        }
        return ImmutableList.of();

    }

    @Nullable
    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }
}
