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
                    if(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getWorld().isChunkLoaded(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getChunk())){
                        if(getMob(UUID.fromString(key)) == null){
                            spawnLocConfigReader.remove(UUID.fromString(key));
                            Bukkit.getLogger().info("removed " + UUID.fromString(key));

                        }
                    }
                }
            }

        }.runTaskTimer(this,0,20*60);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            for(String key : spawnLocConfigReader.getUUIDs()){
                if(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getWorld().isChunkLoaded(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getChunk())){
                    if(getMob(UUID.fromString(key)) != null){
                        eventListener.mobSpawnChunk.put(ChunkWeight.getMob(UUID.fromString(key)),spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getChunk());
                        eventListener.mobSpawnWorld.put(ChunkWeight.getMob(UUID.fromString(key)),spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getWorld());
                        Bukkit.getLogger().info("added " + UUID.fromString(key));
                    }
                }
            }
        });
//        for(String key : spawnLocConfigReader.getUUIDs()){
//            if(!java.util.Objects.equals(ChunkWeight.getMob(UUID.fromString(key)),null)){
//                eventListener.mobSpawnChunk.put(ChunkWeight.getMob(UUID.fromString(key)),spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getChunk());
//                eventListener.mobSpawnWorld.put(ChunkWeight.getMob(UUID.fromString(key)),spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getWorld());
//                Bukkit.getLogger().info("added " + UUID.fromString(key));
//            }
//        }
    }
    public static Entity getMob(UUID id) {
        for(World w : Bukkit.getServer().getWorlds())
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
