package mineralworld.mixins;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Mixin(BushBlock.class)
public abstract class PlantBlockMixin {

	private static final Logger LOGGER = LoggerFactory.getLogger("mineralworld");

	private static final Map<String, long[]> STATS = new HashMap<>();

	@Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
	private void mineralworld$allowOnMineralGround(
		BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		boolean sapling = state.getBlock() instanceof SaplingBlock;
		if (!sapling) return;
		BlockState belowState = world.getBlockState(pos.below());
		Block below = belowState.getBlock();
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(below);
		boolean mineral = id != null && id.getPath().endsWith("_ore");
		boolean dirt = belowState.is(BlockTags.DIRT) || belowState.is(Blocks.FARMLAND);
		String cat;
		if (mineral) cat = "ORE";
		else if (dirt) cat = "DIRT";
		else if (belowState.isAir()) cat = "AIR";
		else if (id != null && (id.getPath().equals("stone") || id.getPath().equals("deepslate"))) cat = "STONE";
		else cat = below.toString();
		long[] st = STATS.computeIfAbsent(cat, k -> new long[2]);
		st[0]++;
		if (mineral || dirt) {
			cir.setReturnValue(true);
			st[1]++;
		} else {
			st[1] += 0;
		}
		if (st[0] % 50 == 1) {
			LOGGER.info("[mineralworld] SAPLING cat={} below={} allowed={} total{} samplePos={}", cat, below, cir.getReturnValue(), st[0], pos);
		}
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			StringBuilder sb = new StringBuilder();
			sb.append("\n[mineralworld] ===== SAPLING STATS (total/allowed) =====");
			STATS.forEach((k, v) -> sb.append("\n  ").append(k).append(": ").append(v[0]).append("/").append(v[1]));
			LOGGER.info(sb.toString());
		}));
	}
}
