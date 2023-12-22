package caps123987.listeners;

import caps123987.customblock.CustomBlock;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import caps123987.types.SimpleBlock;

public class Placement implements Listener {
    @EventHandler
    public void onPlace(PlayerInteractEvent e){

        if(!e.getHand().equals(EquipmentSlot.HAND)){
            return;
        }

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }

        ItemStack item = e.getItem();

        if(item == null){
            return;
        }

        if(!item.getItemMeta().hasCustomModelData()){
            return;
        }

        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            item.setAmount(item.getAmount()-1);
        }

        Block block = e.getClickedBlock().getRelative(e.getBlockFace());
        block.setType(Material.OBSIDIAN);

        SimpleBlock simBlock = new SimpleBlock(e.getClickedBlock().getRelative(e.getBlockFace()).getLocation());

        CustomBlock.instance.getBlocks().add(simBlock);

    }
}
