package caps123987.placement;

import caps123987.customblock.CustomBlock;
import caps123987.types.SimpleBlock;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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


        if(e.getPlayer().getBoundingBox().overlaps(BoundingBox.of(block))){
            return;
        }

        if(!item.getItemMeta().getPersistentDataContainer().has(CustomBlock.instance.getCustomBlockKey(), PersistentDataType.STRING)){
            return;
        }

        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            item.setAmount(item.getAmount()-1);
        }

        CustomBlock.instance.getPlacementHandler().placeBlock(item,block, (int) e.getPlayer().getLocation().getYaw());
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
