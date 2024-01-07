package caps123987.customblock;

import caps123987.commands.GiveCommand;
import caps123987.commands.GiveComplete;
import caps123987.listeners.Placement;
import caps123987.registers.BlockTypesReg;
import caps123987.registers.RegProcesor;
import caps123987.services.AutoSave;
import caps123987.types.SimpleBlock;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class CustomBlock extends JavaPlugin {
    public static CustomBlock instance;
    private AutoSave autoSave;
    public BlockTypesReg blockTypesReg;
    public RegProcesor regProcesor;
    private File blocksFile;
    private Map<String, String> blocks = new HashMap<String,String>();
    public NamespacedKey customBlockKey = new NamespacedKey(this,"customblock");

    @Override
    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(SimpleBlock.class);

        blockTypesReg = new BlockTypesReg(this);
        regProcesor = new RegProcesor();

        Bukkit.getPluginManager().registerEvents(new Placement(),this);
        Bukkit.getPluginCommand("customblockgive").setExecutor(new GiveCommand());
        Bukkit.getPluginCommand("customblockgive").setTabCompleter(new GiveComplete());

        blocksFile = new File(CustomBlock.instance.getDataFolder(),"blocks");
        if(!blocksFile.exists()){
            blocksFile.mkdir();
        }

        setUpBlocks();

        autoSave = new AutoSave();
        autoSave.start(this,1);

        this.getLogger().info("Loaded: "+blocks.size()+" blocks");

        //lightUpdate = new LightUpdate(this);
    }

    public void setUpBlocks(){
        for(String name : blockTypesReg.getAllNames()){
            File file = new File(blocksFile,name+".yml");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException ignored) {}
            }

            FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

            List<String> values = (List<String>) yaml.getList("values");

            if(values == null){
                continue;
            }

            for(String block : values){
                blocks.put(block,name);
            }
        }
    }

    public void saveBlocks(){

        Map<String,List<String>> nameMap = new HashMap<String,List<String>>();


        for(String name : blockTypesReg.getAllNames()){
            nameMap.put(name, new ArrayList<String>());
        }

        for(Map.Entry<String, String> block : blocks.entrySet()){
            nameMap.get(block.getValue()).add(block.getKey());
        }

        for(Map.Entry<String, List<String>> names : nameMap.entrySet()){
            File file = new File(blocksFile,names.getKey()+".yml");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException ignored) {}
            }

            FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

            yaml.set("values",names.getValue());

            try {
                yaml.save(file);
            } catch (IOException ignored) {}
        }
    }

    public static CustomBlock getInstance(){
        return instance;
    }
    @Override
    public void onDisable() {
        saveBlocks();
    }
    public Map<String, String> getBlocks(){
        return blocks;
    }

    public void addBlock(SimpleBlock block, String name){
        blocks.put(block.toString(), name);
    }

    public boolean containsBlock(SimpleBlock block){
        return blocks.containsKey(block.toString());
    }

    public void removeBlock(SimpleBlock block){
        blocks.remove(block.toString());
    }
    public void destroyBlock(Block block){
        ItemDisplay display = block.getLocation().add(.5,.5,.5).getNearbyEntitiesByType(ItemDisplay.class, 1)
                .stream().findFirst().get();

        block.getWorld().dropItemNaturally(
                block.getLocation(),
                display.getItemStack());

        display.remove();

        CustomBlock.instance.removeBlock(new SimpleBlock(block));
    }

}
