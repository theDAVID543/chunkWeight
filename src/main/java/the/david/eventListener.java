package the.david;


import com.google.common.base.Objects;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.*;

public class eventListener implements Listener {
    public static Map<Entity, World> mobSpawnWorld = new HashMap<>();
    public static Map<Entity, Chunk> mobSpawnChunk = new HashMap<>();
    public static Map<Entity, EntityType> uuidToType = new HashMap<>();
    private ArrayList<String> animals = new ArrayList<String>(
            Arrays.asList("ABSTRACTHORSE", "AXOLOTL", "BEE", "CAMEL", "CAT", "CHESTEDHORSE", "CHICKEN", "COW", "DONKEY", "FOX", "FROG", "GOAT", "HOGLIN", "HORSE", "LLAMA", "MULE", "MUSHROOMCOW", "OCELOT", "PANDA", "PARROT", "PIG", "POLARBEAR", "RABBIT", "SHEEP", "SKELETONHORSE", "SNIFFER", "STEERABLE", "STRIDER", "TAMEABLE", "TRADERLLAMA", "TURTLE", "WOLF", "ZOMBIEHORSE")
    );
    private final Integer chunkLimit = 750;
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
            if(!Objects.equal(entityConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()),mobSpawnChunk.get(e.getEntity())),null) && getRandom(0,chunkLimit) >= entityConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()),mobSpawnChunk.get(e.getEntity()))){
                e.setDroppedExp(0);
                return;
            }
        }else{
            if(!Objects.equal(animalConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()),mobSpawnChunk.get(e.getEntity())),null) && getRandom(0,chunkLimit) >= animalConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()),mobSpawnChunk.get(e.getEntity()))){
                e.setDroppedExp(0);
                return;
            }
        }
        if(!Objects.equal(e.getDroppedExp(),0)){
            if (!animals.contains(e.getEntityType().toString())) {
                if (Objects.equal(entityConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity())), null)) {
                    entityConfigReader.setConfig(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity()), chunkLimit - e.getDroppedExp());
                } else {
                    entityConfigReader.setConfig(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity()), entityConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity())) - e.getDroppedExp());
                }
            } else {
                if (Objects.equal(animalConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity())), null)) {
                    animalConfigReader.setConfig(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity()), chunkLimit - e.getDroppedExp());
                } else {
                    animalConfigReader.setConfig(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity()), animalConfigReader.getChunkWeight(mobSpawnWorld.get(e.getEntity()), mobSpawnChunk.get(e.getEntity())) - e.getDroppedExp());
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
        mobSpawnChunk.put(e.getEntity(), e.getLocation().getChunk());
        mobSpawnWorld.put(e.getEntity(),e.getEntity().getWorld());
        spawnLocConfigReader.setConfig(e.getEntity().getUniqueId(),e.getEntity().getLocation().x(),e.getEntity().getLocation().z(),e.getEntity().getWorld());
    }
    @EventHandler
    public void onBreed(EntityBreedEvent e){
        if(animals.contains(e.getEntityType().toString())){
            if(!Objects.equal(animalConfigReader.getChunkWeight(e.getEntity().getWorld(), e.getEntity().getLocation().getChunk()),null) && getRandom(0,chunkLimit) >= animalConfigReader.getChunkWeight(e.getEntity().getWorld(), e.getEntity().getLocation().getChunk())) {
                e.setExperience(0);
            }
        }
    }
    @EventHandler
    public void onBlockDropExp(BlockExpEvent e){
        if(!Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getWorld(),e.getBlock().getLocation().getChunk()),null)){
            if(getRandom(0,chunkLimit) >= blockConfigReader.getChunkWeight(e.getBlock().getWorld(),e.getBlock().getLocation().getChunk())){
                e.setExpToDrop(0);
            }
        }
        if(Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getWorld(),e.getBlock().getChunk()),null)){
            blockConfigReader.setConfig(e.getBlock().getWorld(),e.getBlock().getLocation().getChunk(), chunkLimit - e.getExpToDrop());
        }else{
            blockConfigReader.setConfig(e.getBlock().getWorld(),e.getBlock().getLocation().getChunk(), blockConfigReader.getChunkWeight(e.getBlock().getWorld(),e.getBlock().getChunk()) - e.getExpToDrop());
        }
    }
    public int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
    private List<Material> crops = Arrays.asList(Material.WHEAT, Material.COCOA, Material.BEETROOTS, Material.CARROTS, Material.POTATOES, Material.SWEET_BERRY_BUSH);
    @EventHandler
    public void onBlockBreakBlock(BlockBreakBlockEvent e){
        if(crops.contains(e.getBlock().getType())) {
            Ageable crop = (Ageable) e.getBlock().getBlockData();
            if(crop.getAge() == crop.getMaximumAge()){
                if (!Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk()), null)) {
                    if (getRandom(0, chunkLimit) <= blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk())) {
                        ExperienceOrb orb = e.getBlock().getWorld().spawn(e.getBlock().getLocation(), ExperienceOrb.class);
                        orb.setExperience(orb.getExperience() + 3);
                    }else{
                        return;
                    }
                }
                if (Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getChunk()), null)) {
                    blockConfigReader.setConfig(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk(), chunkLimit - 3);
                } else {
                    blockConfigReader.setConfig(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk(), blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getChunk()) - 3);
                }
            }
        }
    }
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if(crops.contains(e.getBlock().getType())) {
            Ageable crop = (Ageable) e.getBlock().getBlockData();
            if(crop.getAge() == crop.getMaximumAge()){
                if (!Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk()), null)) {
                    if (getRandom(0, chunkLimit) <= blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk())) {
                        ExperienceOrb orb = e.getBlock().getWorld().spawn(e.getBlock().getLocation(), ExperienceOrb.class);
                        orb.setExperience(orb.getExperience() + 3);
                    }else{
                        return;
                    }
                }
                if (Objects.equal(blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getChunk()), null)) {
                    blockConfigReader.setConfig(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk(), chunkLimit - 3);
                } else {
                    blockConfigReader.setConfig(e.getBlock().getWorld(), e.getBlock().getLocation().getChunk(), blockConfigReader.getChunkWeight(e.getBlock().getWorld(), e.getBlock().getChunk()) - 3);
                }
            }
        }
    }
}
