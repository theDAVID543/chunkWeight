package the.david;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public final class spawnLocConfigReader {
    public static Location getSpawnLoc(UUID uuid) {
        if(dataConfig.getString(uuid.toString()) == null){
            return null;
        }else{
            World world = Bukkit.getWorld(dataConfig.getString(uuid.toString() + ".world" ));
            double x = Double.parseDouble(dataConfig.getString(uuid + ".x" ));
            double z = Double.parseDouble(dataConfig.getString(uuid + ".z"));
            Location location = new Location(world,x,69d,z);
            return location;
        }
    }
    public static Set<String> getUUIDs() {
        if(dataConfig.getStringList("") == null){
            return null;
        }else{
            return dataConfig.getKeys(false);
        }
    }
    public static void remove(UUID uuid) {
        dataConfig.set(String.valueOf(uuid),null);
    }

    public static void setConfig(UUID uuid, Double x, Double z, World world) {
        dataConfig.set(uuid + ".x",x);
        dataConfig.set(uuid + ".z",z);
        dataConfig.set(uuid + ".world",world.getName());
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
        dataConfigFile = new File(ChunkWeight.instance.getDataFolder(), "data/spawnLoc.yml");
        if (!dataConfigFile.exists()) {
            dataConfigFile.getParentFile().mkdirs();
            ChunkWeight.instance.saveResource("data/spawnLoc.yml", false);
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

