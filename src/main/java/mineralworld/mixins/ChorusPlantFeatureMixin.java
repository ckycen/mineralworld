package mineralworld.mixins;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantFeature.class)
public abstract class ChorusPlantFeatureMixin {

	private static boolean isMineralBase(Block block) {
		if (block == Blocks.END_STONE) return true;
		if (block == Blocks.OBSIDIAN) return true;
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		return id.getPath().endsWith("_ore");
	}

	@Inject(
		method = "place",
		at = @At("HEAD"),
		cancellable = true
	)
	private void mineralworld$allowChorusOnMinerals(FeaturePlaceContext<NoneFeatureConfiguration> context,
			CallbackInfoReturnable<Boolean> cir) {
		net.minecraft.world.level.WorldGenLevel world = context.level();
		net.minecraft.core.BlockPos origin = context.origin();
		if (world.isEmptyBlock(origin) && isMineralBase(world.getBlockState(origin.below()).getBlock())) {
			ChorusFlowerBlock.generatePlant(world, origin, context.random(), 8);
			cir.setReturnValue(true);
		}
	}
}
