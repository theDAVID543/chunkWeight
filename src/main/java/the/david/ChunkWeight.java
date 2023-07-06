package the.david;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

public final class ChunkWeight extends JavaPlugin {
    public static JavaPlugin instance;
    private static Integer oldDay;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new eventListener(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("chunkweight")).setExecutor(new commandHandler());
        entityConfigReader.createCustomConfig();
        blockConfigReader.createCustomConfig();
        animalConfigReader.createCustomConfig();
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                spawnLocConfigReader.saveConfig();
//                for(String key : Objects.requireNonNull(spawnLocConfigReader.getUUIDs())){
//                    Bukkit.getScheduler ().runTaskLater (instance, () -> {
//                        if(!Objects.equals(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)),null)){
//                            Chunk chunk = Objects.requireNonNull(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key))).getChunk();
//                            UUID mob = UUID.fromString(key);
//                            World world = Objects.requireNonNull(spawnLocConfigReader.getSpawnLoc(mob)).getWorld();
//                            if(!Objects.equals(world,null)){
//                                if(world.isChunkLoaded(chunk)){
//                                    if(getMob(mob) == null){
//                                        spawnLocConfigReader.remove(mob);
//                                    }
//                                }
//                            }else{
//                                spawnLocConfigReader.remove(mob);
//                            }
//                        }
//                    }, 1L);
//                }
//            }
//
//        }.runTaskTimer(this,0,20*300);
        new BukkitRunnable() {
            @Override
            public void run() {
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Taipei");
                Date now = Calendar.getInstance(timeZone).getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("u");
                Integer time = Integer.valueOf(simpleDateFormat.format(now));
                if(!Objects.equals(oldDay,null) && !Objects.equals(time,oldDay)){
                    Bukkit.getLogger().info("day changed");
                    entityConfigReader.resetConfig();
                    animalConfigReader.resetConfig();
                    blockConfigReader.resetConfig();
                }
                oldDay = time;
            }

        }.runTaskTimer(this,0,20*60);


//        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
//            for(String key : spawnLocConfigReader.getUUIDs()){
//                if(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getWorld().isChunkLoaded(spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getChunk())){
//                    if(getMob(UUID.fromString(key)) != null){
//                        eventListener.mobSpawnChunk.put(ChunkWeight.getMob(UUID.fromString(key)),spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getChunk());
//                        eventListener.mobSpawnWorld.put(ChunkWeight.getMob(UUID.fromString(key)),spawnLocConfigReader.getSpawnLoc(UUID.fromString(key)).getWorld());
//                    }
//                }
//            }
//        });
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
