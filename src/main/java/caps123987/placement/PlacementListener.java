package caps123987.placement;

import caps123987.customblock.CustomBlock;
import caps123987.types.SimpleBlock;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Objects;

public class PlacementListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e){

        if(e.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        ItemStack item = e.getItemInHand();

        Block block = e.getBlock();

        if(!item.getItemMeta().getPersistentDataContainer().has(CustomBlock.instance.getCustomBlockKey(), PersistentDataType.STRING)){
            return;
        }

        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            item.setAmount(item.getAmount()-1);
        }

        if(block.getState().getBlockData() instanceof Directional){
            CustomBlock.instance.getPlacementHandler().placeBlock(item,block,((Directional)block.getState().getBlockData()).getFacing());
        }else {
            CustomBlock.instance.getPlacementHandler().placeBlock(item,block);
        }


    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if(!e.getBlock().getType().equals(Material.OBSIDIAN)){
            return;
        }

        if(CustomBlock.instance.getPlacementHandler().destroyBlock(e.getBlock())){
            e.setDropItems(false);
        }
    }

    @EventHandler
    public void onBreakInteract(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Interaction interaction)){
            return;
        }
        if(!(e.getDamager() instanceof Player player)){
            return;
        }
        if(!interaction.getScoreboardTags().contains("customBlockInteraction")){
            return;
        }

        Block block = interaction.getLocation().clone().subtract(.5,0,.5).getBlock();

        CustomBlock.instance.getPlacementHandler().destroyBlock(block, interaction);
    }
}
