# Mineral World Mod

一个 Minecraft Fabric mod，将世界方块替换为矿物，保留原版地形/群系/结构形态。

## 功能

- **主世界**：将石头、泥土、沙子等替换为对应矿物
- **下界**：将下界岩、灵魂沙等替换为矿物，保留原版群系特征
- **末地**：将末地石替换为矿物，保留末地城结构

## 方块映射

### 主世界
| 原版方块 | 矿物方块 |
|---------|---------|
| 石头 | 铁矿石 |
| 泥土 | 钻石矿石 |
| 沙子 | 金矿石 |
| 草方块 | 绿宝石矿石 |
| 砂岩 | 铜矿石 |

### 下界
| 原版方块 | 矿物方块 |
|---------|---------|
| 下界岩 | 红石矿石 |
| 灵魂沙 | 青金石矿石 |
| 火山岩 | 黑曜石 |

### 末地
| 原版方块 | 矿物方块 |
|---------|---------|
| 末地石 | 末地石（保留） |
| 黑曜石 | 黑曜石（保留） |

## 安装

1. 安装 [Fabric Loader](https://fabricmc.net/use/installer/)
2. 下载最新版本的 mod jar 文件
3. 将 jar 文件放入 `.minecraft/mods/` 文件夹

## 兼容性

- Minecraft: 1.21.1
- Fabric Loader: 0.16.10
- Yarn Mappings: 1.21.1+build.3

## 开发

### 构建

```bash
./gradlew build
```

### 测试

```bash
./gradlew runClient
./gradlew runServer
```

## 许可证

MIT License
