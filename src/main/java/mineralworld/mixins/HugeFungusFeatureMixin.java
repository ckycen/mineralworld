package mineralworld.mixins;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(HugeFungusFeature.class)
public abstract class HugeFungusFeatureMixin {

	private static final Logger LOGGER = LoggerFactory.getLogger("mineralworld");

	private static boolean isLava(WorldGenLevel world, int x, int y, int z) {
		return world.getBlockState(new BlockPos(x, y, z)).is(Blocks.LAVA);
	}

	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void mineralworld$noFungusInLava(FeaturePlaceContext<HugeFungusConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
		WorldGenLevel world = context.level();
		BlockPos origin = context.origin();
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();
		int baseY = y - 1;
		for (int dy = -5; dy <= 14; dy++) {
			if (isLava(world, x, y + dy, z)) {
				LOGGER.info("[mineralworld] HUGE_FUNGUS blocked at {} {} {} (lava at y={})", x, y, z, y + dy);
				cir.setReturnValue(false);
				return;
			}
		}
		for (int dy = 0; dy >= -3; dy--) {
			int ly = baseY + dy;
			if (isLava(world, x + 1, ly, z) || isLava(world, x - 1, ly, z)
					|| isLava(world, x, ly, z + 1) || isLava(world, x, ly, z - 1)
					|| isLava(world, x + 1, ly, z + 1) || isLava(world, x - 1, ly, z - 1)
					|| isLava(world, x + 1, ly, z - 1) || isLava(world, x - 1, ly, z + 1)) {
				LOGGER.info("[mineralworld] HUGE_FUNGUS blocked at {} {} {} (lava neighbor at y={})", x, y, z, ly);
				cir.setReturnValue(false);
				return;
			}
		}
	}
}
