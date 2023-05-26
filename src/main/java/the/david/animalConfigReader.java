package the.david;

import org.bukkit.Chunk;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class animalConfigReader {
    public static Integer getChunkWeight(Chunk chunk) {
        if(dataConfig.getString(String.valueOf(chunk)) == null){
            return null;
        }else{
            return Integer.valueOf(dataConfig.getString(String.valueOf(chunk)));
        }
    }

    public static void setConfig(Chunk chunk, Integer weight) {
        dataConfig.set(String.valueOf(chunk),weight);
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
        dataConfigFile = new File(ChunkWeight.instance.getDataFolder(), "data/animal_chunk_weight.yml");
        if (!dataConfigFile.exists()) {
            dataConfigFile.getParentFile().mkdirs();
            ChunkWeight.instance.saveResource("data/animal_chunk_weight.yml", false);
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

