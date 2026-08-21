package mineralworld.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {

	private static final Logger LOGGER = LoggerFactory.getLogger("mineralworld");

	private static final Map<Block, Block> TRUNK_MAP = new HashMap<>();
	private static final Map<Block, Block> LEAF_MAP = new HashMap<>();
	private static final java.util.concurrent.atomic.AtomicLong countTrunk = new java.util.concurrent.atomic.AtomicLong();
	private static final java.util.concurrent.atomic.AtomicLong countLeaf = new java.util.concurrent.atomic.AtomicLong();
	private static final java.util.concurrent.atomic.AtomicLong countTotal = new java.util.concurrent.atomic.AtomicLong();

	static {
		TRUNK_MAP.put(Blocks.OAK_LOG, Blocks.EMERALD_ORE);
		TRUNK_MAP.put(Blocks.BIRCH_LOG, Blocks.IRON_ORE);
		TRUNK_MAP.put(Blocks.SPRUCE_LOG, Blocks.DIAMOND_ORE);
		TRUNK_MAP.put(Blocks.DARK_OAK_LOG, Blocks.COAL_ORE);
		TRUNK_MAP.put(Blocks.ACACIA_LOG, Blocks.GOLD_ORE);
		TRUNK_MAP.put(Blocks.JUNGLE_LOG, Blocks.EMERALD_ORE);
		TRUNK_MAP.put(Blocks.CHERRY_LOG, Blocks.REDSTONE_ORE);
		TRUNK_MAP.put(Blocks.MANGROVE_LOG, Blocks.COPPER_ORE);
		TRUNK_MAP.put(Blocks.MANGROVE_ROOTS, Blocks.COPPER_ORE);
		TRUNK_MAP.put(Blocks.CRIMSON_STEM, Blocks.REDSTONE_ORE);
		TRUNK_MAP.put(Blocks.WARPED_STEM, Blocks.LAPIS_ORE);

		LEAF_MAP.put(Blocks.OAK_LEAVES, Blocks.EMERALD_BLOCK);
		LEAF_MAP.put(Blocks.BIRCH_LEAVES, Blocks.IRON_BLOCK);
		LEAF_MAP.put(Blocks.SPRUCE_LEAVES, Blocks.DIAMOND_BLOCK);
		LEAF_MAP.put(Blocks.DARK_OAK_LEAVES, Blocks.COAL_BLOCK);
		LEAF_MAP.put(Blocks.ACACIA_LEAVES, Blocks.GOLD_BLOCK);
		LEAF_MAP.put(Blocks.JUNGLE_LEAVES, Blocks.EMERALD_BLOCK);
		LEAF_MAP.put(Blocks.CHERRY_LEAVES, Blocks.REDSTONE_BLOCK);
		LEAF_MAP.put(Blocks.MANGROVE_LEAVES, Blocks.COPPER_BLOCK);
		LEAF_MAP.put(Blocks.AZALEA_LEAVES, Blocks.EMERALD_BLOCK);
		LEAF_MAP.put(Blocks.FLOWERING_AZALEA_LEAVES, Blocks.EMERALD_BLOCK);
		LEAF_MAP.put(Blocks.NETHER_WART_BLOCK, Blocks.REDSTONE_BLOCK);
		LEAF_MAP.put(Blocks.WARPED_WART_BLOCK, Blocks.LAPIS_BLOCK);
	}

	@ModifyVariable(
		method = "setBlockState",
		at = @At("HEAD"),
		ordinal = 0,
		argsOnly = true
	)
	private BlockState mineralworld$replaceBlockState(BlockState state, ModifiableWorld world, BlockPos pos) {
		countTotal.incrementAndGet();
		Block block = state.getBlock();
		Block replacement = TRUNK_MAP.get(block);
		if (replacement == null) {
			replacement = LEAF_MAP.get(block);
		}
		if (replacement != null) {
			if (state.getBlock().getTranslationKey().contains("log") || state.getBlock().getTranslationKey().contains("stem")) {
				countTrunk.incrementAndGet();
			} else {
				countLeaf.incrementAndGet();
			}
			return replacement.getDefaultState();
		}
		return state;
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
			LOGGER.info("\n[mineralworld] ===== TREEFEATURE STATS: setBlockStateCalled={} trunkReplaced={} leafReplaced={} =====", countTotal.get(), countTrunk.get(), countLeaf.get())
		));
	}
}
