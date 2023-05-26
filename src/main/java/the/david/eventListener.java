package the.david;


import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class eventListener implements Listener {
    public static Map<Chunk,Integer> chunkWeight = new HashMap<>();
    @EventHandler
    public void onPickExpOrb(PlayerPickupExperienceEvent e){
        Bukkit.getLogger().info(ChunkWeight.getEntityByUniqueId(e.getExperienceOrb().getSourceEntityId()).getChunk().toString());
        if(chunkWeight.get(ChunkWeight.getEntityByUniqueId(e.getExperienceOrb().getSourceEntityId()).getChunk()) == null){
            chunkWeight.put(ChunkWeight.getEntityByUniqueId(e.getExperienceOrb().getSourceEntityId()).getChunk(),50000);
        }
    }
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(getRandom(0,50000) >= chunkWeight.get(e.getLocation().getChunk())){
            e.setCancelled(true);
        }
    }
    public int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
}
