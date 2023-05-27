package the.david;


import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.*;

public class eventListener implements Listener {
    public static Map<UUID, Chunk> mobSpawnChunk = new HashMap<>();
    public static Map<UUID, EntityType> uuidToType = new HashMap<>();
    private ArrayList<String> animals = new ArrayList<String>(
            Arrays.asList("ABSTRACTHORSE", "AXOLOTL", "BEE", "CAMEL", "CAT", "CHESTEDHORSE", "CHICKEN", "COW", "DONKEY", "FOX", "FROG", "GOAT", "HOGLIN", "HORSE", "LLAMA", "MULE", "MUSHROOMCOW", "OCELOT", "PANDA", "PARROT", "PIG", "POLARBEAR", "RABBIT", "SHEEP", "SKELETONHORSE", "SNIFFER", "STEERABLE", "STRIDER", "TAMEABLE", "TRADERLLAMA", "TURTLE", "WOLF", "ZOMBIEHORSE")
    );
/*    @EventHandler
    public void onPickExpOrb(PlayerPickupExperienceEvent e){
        if(!Objects.equal(e.getExperienceOrb().getSourceEntityId(),null) && !Objects.equal(e.getExperienceOrb().getSpawnReason().toString(),"EXP_BOTTLE") && !animals.contains(uuidToType.get(e.getExperienceOrb().getSourceEntityId()).toString())){
            if(Objects.equal(entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId())),null)){
                entityConfigReader.setConfig(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId()), 250000 - e.getExperienceOrb().getExperience());
            }else{
                entityConfigReader.setConfig(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId()), entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId())) - e.getExperienceOrb().getExperience());
            }
        }else if(animals.contains(uuidToType.get(e.getExperienceOrb().getSourceEntityId()).toString())){
            if(Objects.equal(animalConfigReader.getChunkWeight(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId())),null)){
                animalConfigReader.setConfig(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId()), 250000 - e.getExperienceOrb().getExperience());
            }else{
                animalConfigReader.setConfig(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId()), animalConfigReader.getChunkWeight(mobSpawnChunk.get(e.getExperienceOrb().getSourceEntityId())) - e.getExperienceOrb().getExperience());
            }
        }
    } */
    @EventHandler
    public void onEntityDie(EntityDeathEvent e){
        if(!animals.contains(e.getEntityType().toString())){
            if(!Objects.equal(entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId())),null) && getRandom(0,250000) >= entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId()))){
                e.setDroppedExp(0);
                return;
            }
        }else{
            if(!Objects.equal(animalConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId())),null) && getRandom(0,250000) >= animalConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId()))){
                e.setDroppedExp(0);
                return;
            }
        }
        if(!Objects.equal(e.getDroppedExp(),0) && !animals.contains(e.getEntity().toString())){
            if(!animals.contains(uuidToType.get(e.getEntity().getUniqueId()).toString())) {
                if(Objects.equal(entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId())), null)) {
                    entityConfigReader.setConfig(mobSpawnChunk.get(e.getEntity().getUniqueId()), 250000 - e.getDroppedExp());
                } else {
                    entityConfigReader.setConfig(mobSpawnChunk.get(e.getEntity().getUniqueId()), entityConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId())) - e.getDroppedExp());
                }
            }else {
                if(Objects.equal(animalConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId())), null)) {
                    animalConfigReader.setConfig(mobSpawnChunk.get(e.getEntity().getUniqueId()), 250000 - e.getDroppedExp());
                } else {
                    animalConfigReader.setConfig(mobSpawnChunk.get(e.getEntity().getUniqueId()), animalConfigReader.getChunkWeight(mobSpawnChunk.get(e.getEntity().getUniqueId())) - e.getDroppedExp());
                }
            }
        }
    }
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
/*        if(!Objects.equal(entityConfigReader.getChunkWeight(e.getLocation().getChunk()),null) && e.getEntityType() != EntityType.ITEM_FRAME && e.getEntityType() != EntityType.GLOW_ITEM_FRAME && e.getEntityType() != EntityType.ARMOR_STAND && !animals.contains(e.getEntityType().toString())){
            if(getRandom(0,250000) >= entityConfigReader.getChunkWeight(e.getLocation().getChunk())){
                e.setCancelled(true);
                return;
            }
        }else if(animals.contains(e.getEntityType().toString())){
            if(!Objects.equal(animalConfigReader.getChunkWeight(e.getLocation().getChunk()),null) && getRandom(0,250000) >= animalConfigReader.getChunkWeight(e.getLocation().getChunk())){
                e.setCancelled(true);
                return;
            }
        } */
        if(e.getLocation().getWorld() == Bukkit.getWorld("world")){
            mobSpawnChunk.put(e.getEntity().getUniqueId(), e.getLocation().getChunk());
            uuidToType.put(e.getEntity().getUniqueId(), e.getEntityType());
        } else if (e.getLocation().getWorld() == Bukkit.getWorld("world_nether")) {

            
        } else if (e.getLocation().getWorld() == Bukkit.getWorld("world_the_end")) {

        }
    }
    @EventHandler
    public void onBreed(EntityBreedEvent e){
        if(animals.contains(e.getEntityType().toString())){
            if(!Objects.equal(animalConfigReader.getChunkWeight(e.getEntity().getLocation().getChunk()),null) && getRandom(0,250000) >= animalConfigReader.getChunkWeight(e.getEntity().getLocation().getChunk())) {
                e.setExperience(0);
            }
        }
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
