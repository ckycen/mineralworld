package mineralworld;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineralWorld implements ModInitializer {
	public static final String MOD_ID = "mineralworld";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("矿物世界 Mineral World 已加载：三大维度全部替换为原版矿物方块！");
	}
}