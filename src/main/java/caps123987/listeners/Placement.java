package caps123987.listeners;

import caps123987.customblock.CustomBlock;
import caps123987.types.SimpleBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Objects;

public class Placement implements Listener {
    @EventHandler
    public void onPlace(PlayerInteractEvent e){


        if(e.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }

        ItemStack item = e.getItem();

        if(item == null){
            return;
        }

        Block block = e.getClickedBlock().getRelative(e.getBlockFace());

        /*BoundingBox box = BoundingBox.of(block);

        if(!block.getLocation().getNearbyEntities(1,1,1).stream().allMatch(entity -> entity instanceof ItemDisplay)){
            return;
        }*/

        if(!item.getItemMeta().getPersistentDataContainer().has(CustomBlock.instance.customBlockKey, PersistentDataType.STRING)){
            return;
        }

        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            item.setAmount(item.getAmount()-1);
        }

        ItemDisplay itemDisplay = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().add(0.5,0.5,0.5), EntityType.ITEM_DISPLAY);
        ItemStack itemPut = item.clone();
        itemPut.setAmount(1);
        itemDisplay.setItemStack(itemPut);
        itemDisplay.setGravity(false);
        itemDisplay.setInvulnerable(true);
        itemDisplay.addScoreboardTag("customBlock");
        itemDisplay.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(1.0005f,1.0005f,1.0005f),new AxisAngle4f()));
        itemDisplay.setBrightness(new Display.Brightness(block.getLightFromBlocks(),block.getLightFromSky()));

        
        block.setType(Material.OBSIDIAN);

        SimpleBlock simBlock = new SimpleBlock(e.getClickedBlock().getRelative(e.getBlockFace()).getLocation());

        CustomBlock.instance.addBlock(simBlock, item.getItemMeta().getPersistentDataContainer().get(CustomBlock.instance.customBlockKey, PersistentDataType.STRING));

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!e.getBlock().getType().equals(Material.OBSIDIAN)){
            return;
        }

        if(CustomBlock.instance.containsBlock(new SimpleBlock(e.getBlock().getLocation()))){
            e.setDropItems(false);

            ItemDisplay display = e.getBlock().getLocation().add(.5,.5,.5).getNearbyEntitiesByType(ItemDisplay.class, 1)
                    .stream().findFirst().get();

            e.getBlock().getWorld().dropItemNaturally(
                    e.getBlock().getLocation(),
                    display.getItemStack());

            display.remove();

            CustomBlock.instance.removeBlock(new SimpleBlock(e.getBlock().getLocation()));
        }
    }
}
