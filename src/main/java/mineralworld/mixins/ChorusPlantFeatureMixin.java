package mineralworld.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ChorusPlantFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantFeature.class)
public abstract class ChorusPlantFeatureMixin {

	private static boolean isMineralBase(Block block) {
		if (block == Blocks.END_STONE) return true;
		if (block == Blocks.OBSIDIAN) return true;
		Identifier id = Registries.BLOCK.getId(block);
		return id.getPath().endsWith("_ore");
	}

	@Inject(
		method = "generate",
		at = @At("HEAD"),
		cancellable = true
	)
	private void mineralworld$allowChorusOnMinerals(net.minecraft.world.gen.feature.util.FeatureContext<net.minecraft.world.gen.feature.DefaultFeatureConfig> context,
			CallbackInfoReturnable<Boolean> cir) {
		net.minecraft.world.StructureWorldAccess world = context.getWorld();
		net.minecraft.util.math.BlockPos origin = context.getOrigin();
		if (world.isAir(origin) && isMineralBase(world.getBlockState(origin.down()).getBlock())) {
			net.minecraft.block.ChorusFlowerBlock.generate(world, origin, context.getRandom(), 8);
			cir.setReturnValue(true);
		}
	}
}