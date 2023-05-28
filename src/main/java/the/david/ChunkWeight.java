package the.david;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        spawnLocConfigReader.createCustomConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                for(String key : spawnLocConfigReader.getUUIDs()){
                    if(getMob(UUID.fromString(key)) == null){
                        spawnLocConfigReader.remove(UUID.fromString(key));
                    }
                }
                //your action to do every minute
            }
        }.runTaskTimer(this,0,20*60);
    }
    public Entity getMob(UUID id) {
        for(World w : getServer().getWorlds())
        {
            for(Entity e : w.getEntities())
            {
                if(!e.getUniqueId().equals(id)) continue;
                return e;
            }
        }
        return null;
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
