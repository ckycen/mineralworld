package mineralworld;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(MineralWorld.MOD_ID)
public class MineralWorld {
    public static final String MOD_ID = "mineralworld";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MineralWorld() {
        LOGGER.info("矿物世界 Mineral World 已加载：三大维度全部替换为原版矿物方块！");
    }
}
