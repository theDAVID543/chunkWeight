package the.david;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class blockConfigReader {
    public static Integer getChunkWeight(World world, Chunk chunk) {
        if(dataConfig.getString(world + "." + chunk) == null){
            return null;
        }else{
            return Integer.valueOf(dataConfig.getString(world + "." + chunk));
        }
    }

    public static void setConfig(World world, Chunk chunk, Integer weight) {
        dataConfig.set(world + "." + chunk,weight);
        ChunkWeight.instance.saveConfig();
        try {
            dataConfig.save(dataConfigFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static File dataConfigFile;
    private static FileConfiguration dataConfig;

    public static void createCustomConfig() {
        dataConfigFile = new File(ChunkWeight.instance.getDataFolder(), "data/block_chunk_weight.yml");
        if (!dataConfigFile.exists()) {
            dataConfigFile.getParentFile().mkdirs();
            ChunkWeight.instance.saveResource("data/block_chunk_weight.yml", false);
        }

        dataConfig = new YamlConfiguration();
        try {
            dataConfig.load(dataConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        /* User Edit:
            Instead of the above Try/Catch, you can also use
            YamlConfiguration.loadConfiguration(customConfigFile)
        */
    }

}

