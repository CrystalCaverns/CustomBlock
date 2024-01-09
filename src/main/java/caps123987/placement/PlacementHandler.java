package caps123987.placement;

import caps123987.customblock.CustomBlock;
import caps123987.types.SimpleBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Optional;

public class PlacementHandler {
    public void placeBlock(ItemStack item, Block block){
        this.placeBlock(item,block,0);
    }
    public void placeBlock(String item, Block block){
        this.placeBlock(CustomBlock.instance.getBlockTypesReg().getItem(item), block, 0);
    }
    public void placeBlock(String item, Block block, int rotation){
        this.placeBlock(
                CustomBlock.instance.getBlockTypesReg().getItem(item),
                block,
                rotation);
    }
    public void placeBlock(ItemStack item, Block block, int rotation){

        summonDisplayEntity(block, item.clone(), Math.round(rotation / 90));

        if(item.getItemMeta().getCustomModelData()<500000) {
            block.setType(Material.OBSIDIAN);
        }else{
            summonInteractEntity(block.getLocation().clone().add(.5,0,.5));
        }

        SimpleBlock simBlock = new SimpleBlock(block.getLocation());

        CustomBlock.instance.addBlock(simBlock, item.getItemMeta().getPersistentDataContainer().get(CustomBlock.instance.getCustomBlockKey(), PersistentDataType.STRING));

    }

    public void summonDisplayEntity(Block block, ItemStack item, int rotation){

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

    public void summonInteractEntity(Location loc){
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

    public boolean destroyBlock(Block block){
        if(CustomBlock.instance.containsBlock(new SimpleBlock(block.getLocation()))){
            Optional<ItemDisplay> displayOptional = block.getLocation().add(.5,.5,.5).getNearbyEntitiesByType(ItemDisplay.class, 1)
                    .stream().findFirst();

            if(displayOptional.isEmpty()){
                return false;
            }

            ItemDisplay display = displayOptional.get();

            block.getWorld().dropItemNaturally(
                    block.getLocation(),
                    Objects.requireNonNull(display.getItemStack()));

            display.remove();

            CustomBlock.instance.removeBlock(new SimpleBlock(block));
            return true;
        }else {
            return false;
        }
    }

    public boolean destroyBlock(Block block, Interaction interaction){
        if(CustomBlock.instance.containsBlock(new SimpleBlock(block.getLocation()))){
            ItemDisplay display = block.getLocation().add(.5,.5,.5).getNearbyEntitiesByType(ItemDisplay.class, 1)
                    .stream().findFirst().get();



            block.getWorld().dropItemNaturally(
                    block.getLocation(),
                    Objects.requireNonNull(display.getItemStack()));

            display.remove();

            CustomBlock.instance.removeBlock(new SimpleBlock(block));

            interaction.remove();
            return true;
        }else {
            return false;
        }
    }
}
