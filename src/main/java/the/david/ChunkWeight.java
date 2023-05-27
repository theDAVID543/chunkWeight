package the.david;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

public final class ChunkWeight extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new eventListener(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("chunkweight")).setExecutor(new commandHandler());
        entityConfigReader.createCustomConfig();
        blockConfigReader.createCustomConfig();
        animalConfigReader.createCustomConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static Entity getEntityByUniqueId(UUID uniqueId){
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getUniqueId().equals(uniqueId))
                        return entity;
                }
            }
        }

        return null;
    }
}
