package caps123987.listeners;

import caps123987.customblock.CustomBlock;
import caps123987.types.SimpleBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
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

        summonDisplayEntity(block, item.clone(), Math.round(e.getPlayer().getLocation().getYaw() / 90));

        if(item.getItemMeta().getCustomModelData()<500000) {
            block.setType(Material.OBSIDIAN);
        }else{
            summonInteractEntity(block.getLocation().clone().add(.5,0,.5));
        }

        SimpleBlock simBlock = new SimpleBlock(block.getLocation());

        CustomBlock.instance.addBlock(simBlock, item.getItemMeta().getPersistentDataContainer().get(CustomBlock.instance.customBlockKey, PersistentDataType.STRING));

    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if(!e.getBlock().getType().equals(Material.OBSIDIAN)){
            return;
        }


        if(CustomBlock.instance.containsBlock(new SimpleBlock(e.getBlock().getLocation()))){
            e.setDropItems(false);

            CustomBlock.instance.destroyBlock(e.getBlock());
        }
    }

    @EventHandler
    public void onBreakInteract(PlayerInteractEntityEvent e){

        if(!e.getHand().equals(EquipmentSlot.HAND)){
            return;
        }

        if(!(e.getRightClicked() instanceof Interaction interaction)){
            return;
        }


        if(!interaction.getScoreboardTags().contains("customBlockInteraction")){
            return;
        }

        Block block = interaction.getLocation().clone().subtract(.5,0,.5).getBlock();

        if(CustomBlock.instance.containsBlock(new SimpleBlock(block))) {

            CustomBlock.instance.destroyBlock(block);
            interaction.remove();
        }
    }
    private void summonDisplayEntity(Block block, ItemStack item, int rotation){

        ItemDisplay itemDisplay = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5,0.5,0.5), EntityType.ITEM_DISPLAY);
        ItemStack itemPut = item.clone();
        itemPut.setAmount(1);
        itemDisplay.setItemStack(itemPut);
        itemDisplay.setGravity(false);
        itemDisplay.setInvulnerable(true);
        itemDisplay.addScoreboardTag("customBlock");
        itemDisplay.setTransformation(
                new Transformation(new Vector3f(),
                    new AxisAngle4f(),
                    new Vector3f(1.0005f,1.0005f,1.0005f),
                    new AxisAngle4f((float) (getRotation(rotation) * (Math.PI/2)),0,1,0)));
        itemDisplay.setBrightness(new Display.Brightness(block.getLightFromBlocks(),block.getLightFromSky()));
    }

    private void summonInteractEntity(Location loc){
        Interaction interaction = (Interaction) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.INTERACTION);
        interaction.setGravity(false);
        interaction.setInvulnerable(false);
        interaction.getScoreboardTags().add("customBlockInteraction");
    }
    private int getRotation(int rotation){
        return switch (rotation) {
            case 1 -> 0;
            case -1 -> 2;
            case 2, -2 -> 3;
            default -> 1;
        };
    }
}
