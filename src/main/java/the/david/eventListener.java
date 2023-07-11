package the.david;


import com.google.common.base.Objects;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class eventListener implements Listener {
    public static Map<Entity, World> mobSpawnWorld = new HashMap<>();
    public static Map<Entity, Chunk> mobSpawnChunk = new HashMap<>();
    public static Map<Entity, EntityType> uuidToType = new HashMap<>();

    private final NamespacedKey keyX = new NamespacedKey(ChunkWeight.instance, "spawnChunkX");
    private final NamespacedKey keyZ = new NamespacedKey(ChunkWeight.instance, "spawnChunkZ");
    private final NamespacedKey keyWorld = new NamespacedKey(ChunkWeight.instance, "spawnWorld");
    private final NamespacedKey keyUsedChest = new NamespacedKey(ChunkWeight.instance, "usedChest");

    private ArrayList<String> animals = new ArrayList<String>(
            Arrays.asList("ABSTRACTHORSE", "AXOLOTL", "BEE", "CAMEL", "CAT", "CHESTEDHORSE", "CHICKEN", "COW", "DONKEY", "FOX", "FROG", "GOAT", "HOGLIN", "HORSE", "LLAMA", "MULE", "MUSHROOMCOW", "OCELOT", "PANDA", "PARROT", "PIG", "POLARBEAR", "RABBIT", "SHEEP", "SKELETONHORSE", "SNIFFER", "STEERABLE", "STRIDER", "TAMEABLE", "TRADERLLAMA", "TURTLE", "WOLF", "ZOMBIEHORSE")
    );
    private final Integer chunkLimit = 500;
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
    public void onPlaceBlock(BlockPlaceEvent e){
        Block block = e.getBlock();
        if (block.getState() instanceof Chest) {
            Chest chest;
            chest = (Chest) block.getState();
            chest.getPersistentDataContainer().set(keyUsedChest, PersistentDataType.BOOLEAN, true);
            chest.update();
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.hasBlock() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && java.util.Objects.equals(e.getClickedBlock().getType(), Material.CHEST)){
            Chest chest;
            chest = (Chest) e.getClickedBlock().getState();
            boolean empty = true;
            for (ItemStack s : chest.getBlockInventory()) {
                if (s != null) {
                    empty = false;
                    break;
                }
            }
            if(!empty && !Objects.equal(chest.getPersistentDataContainer().get(keyUsedChest, PersistentDataType.BOOLEAN),true)){
                if (!Objects.equal(blockConfigReader.getChunkWeight(e.getClickedBlock().getWorld(), e.getClickedBlock().getLocation().getChunk()), null)) {
                    if (getRandom(0, chunkLimit) <= blockConfigReader.getChunkWeight(e.getClickedBlock().getWorld(), e.getClickedBlock().getLocation().getChunk())) {
                        ExperienceOrb orb = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation(), ExperienceOrb.class);
                        orb.setExperience(orb.getExperience() + 75);
                    }else{
                        return;
                    }
                }
                if (Objects.equal(blockConfigReader.getChunkWeight(e.getClickedBlock().getWorld(), e.getClickedBlock().getChunk()), null)) {
                    blockConfigReader.setConfig(e.getClickedBlock().getWorld(), e.getClickedBlock().getLocation().getChunk(), chunkLimit - 75);
                } else {
                    blockConfigReader.setConfig(e.getClickedBlock().getWorld(), e.getClickedBlock().getLocation().getChunk(), blockConfigReader.getChunkWeight(e.getClickedBlock().getWorld(), e.getClickedBlock().getChunk()) - 75);
                }
            }
            chest.getPersistentDataContainer().set(keyUsedChest, PersistentDataType.BOOLEAN, true);
            chest.update();
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDie(EntityDeathEvent e){
        if(!Objects.equal(e.getDroppedExp(),0)){
            if(!e.getEntity().getPersistentDataContainer().has(keyWorld) || !e.getEntity().getPersistentDataContainer().has(keyX) || !e.getEntity().getPersistentDataContainer().has(keyZ)){
                return;
            }
            World spawnWorld = Bukkit.getWorld(e.getEntity().getPersistentDataContainer().get(keyWorld, PersistentDataType.STRING));
            Chunk spawnChunk = spawnWorld.getChunkAt(e.getEntity().getPersistentDataContainer().get(keyX, PersistentDataType.INTEGER),e.getEntity().getPersistentDataContainer().get(keyZ, PersistentDataType.INTEGER));

            if(!animals.contains(e.getEntityType().toString())){
                if(!Objects.equal(entityConfigReader.getChunkWeight(spawnWorld,spawnChunk),null) && getRandom(0,chunkLimit) >= entityConfigReader.getChunkWeight(spawnWorld,spawnChunk)){
                    e.setDroppedExp(0);
                    return;
                }
            }else{
                if(!Objects.equal(animalConfigReader.getChunkWeight(spawnWorld,spawnChunk),null) && getRandom(0,chunkLimit) >= animalConfigReader.getChunkWeight(spawnWorld,spawnChunk)){
                    e.setDroppedExp(0);
                    return;
                }
            }
            if (!animals.contains(e.getEntityType().toString())) {
                if (Objects.equal(entityConfigReader.getChunkWeight(spawnWorld, spawnChunk), null)) {
                    entityConfigReader.setConfig(spawnWorld, spawnChunk, chunkLimit - e.getDroppedExp());
                } else {
                    entityConfigReader.setConfig(spawnWorld, spawnChunk, entityConfigReader.getChunkWeight(spawnWorld, spawnChunk) - e.getDroppedExp());
                }
            } else {
                if (Objects.equal(animalConfigReader.getChunkWeight(spawnWorld, spawnChunk), null)) {
                    animalConfigReader.setConfig(spawnWorld, spawnChunk, chunkLimit - e.getDroppedExp());
                } else {
                    animalConfigReader.setConfig(spawnWorld, spawnChunk, animalConfigReader.getChunkWeight(spawnWorld, spawnChunk) - e.getDroppedExp());
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
        e.getEntity().getPersistentDataContainer().set(keyX, PersistentDataType.INTEGER, e.getLocation().getChunk().getX());
        e.getEntity().getPersistentDataContainer().set(keyZ, PersistentDataType.INTEGER, e.getLocation().getChunk().getZ());
        e.getEntity().getPersistentDataContainer().set(keyWorld, PersistentDataType.STRING, e.getLocation().getWorld().getName());
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
    private Map<Location, Boolean> fertilized = new HashMap<>();
    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent e){
        fertilized.put(e.getBlock().getLocation(), true);
    }
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreakBlock(BlockBreakBlockEvent e){
        if(!java.util.Objects.equals(fertilized.get(e.getBlock().getLocation()),true)){
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
        fertilized.remove(e.getBlock().getLocation());
    }
    @EventHandler(ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent e){
        if(!java.util.Objects.equals(fertilized.get(e.getBlock().getLocation()),true)){
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
        fertilized.remove(e.getBlock().getLocation());
    }
}
