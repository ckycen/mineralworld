package mineralworld.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlantBlock.class)
public abstract class PlantBlockMixin {

	private static final Logger LOGGER = LoggerFactory.getLogger("mineralworld");

	private static final Map<String, long[]> STATS = new HashMap<>();

	@Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
	private void mineralworld$allowOnMineralGround(
		BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		boolean sapling = state.getBlock() instanceof SaplingBlock;
		if (!sapling) return;
		BlockState belowState = world.getBlockState(pos.down());
		Block below = belowState.getBlock();
		Identifier id = Registries.BLOCK.getId(below);
		boolean mineral = id != null && id.getPath().endsWith("_ore");
		boolean dirt = belowState.isIn(BlockTags.DIRT) || belowState.isOf(Blocks.FARMLAND);
		String cat;
		if (mineral) cat = "ORE";
		else if (dirt) cat = "DIRT";
		else if (belowState.isAir()) cat = "AIR";
		else if (id != null && (id.getPath().equals("stone") || id.getPath().equals("deepslate"))) cat = "STONE";
		else cat = below.toString();
		long[] st = STATS.computeIfAbsent(cat, k -> new long[2]);
		st[0]++; // total
		if (mineral || dirt) {
			cir.setReturnValue(true);
			st[1]++; // allowed
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