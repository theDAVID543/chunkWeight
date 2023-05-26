package the.david;


import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.google.common.base.Objects;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class eventListener implements Listener {
    public static Map<UUID, Chunk> mobSpawnChunk = new HashMap<>();
    @EventHandler
    public void onPickExpOrb(PlayerPickupExperienceEvent e){
        if(!Objects.equal(e.getExperienceOrb().getSourceEntityId(),null)){
            if(Objects.equal(entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId())),null)){
                entityConfigReader.setConfig(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId()), 250000 - e.getExperienceOrb().getExperience());
            }else{
                entityConfigReader.setConfig(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId()), entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId())) - e.getExperienceOrb().getExperience());
            }
        }
    }
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(!Objects.equal(entityConfigReader.getChunkWeight(e.getLocation().getChunk()),null)){
            if(getRandom(0,250000) >= entityConfigReader.getChunkWeight(e.getLocation().getChunk())){
                e.setCancelled(true);
                return;
            }
        }
        mobSpawnChunk.put(e.getEntity().getUniqueId(), e.getLocation().getChunk());
    }
    @EventHandler
    public void onBlockDropExp(BlockExpEvent e){
        if(!Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getLocation().getChunk()),null)){
            if(getRandom(0,250000) >= blockConfigReader.getChunkWeight(e.getBlock().getLocation().getChunk())){
                e.setExpToDrop(0);
            }
        }
        if(Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getChunk()),null)){
            blockConfigReader.setConfig(e.getBlock().getLocation().getChunk(), 250000 - e.getExpToDrop());
        }else{
            blockConfigReader.setConfig(e.getBlock().getLocation().getChunk(), blockConfigReader.getChunkWeight(e.getBlock().getChunk()) - e.getExpToDrop());
        }
    }
    public int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
}
