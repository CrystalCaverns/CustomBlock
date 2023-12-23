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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
    private Map<SimpleBlock, String> blocks = new HashMap<SimpleBlock,String>();
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

            List<SimpleBlock> values = (List<SimpleBlock>) yaml.getList("values");

            if(values == null){
                continue;
            }

            for(SimpleBlock block : values){
                blocks.put(block,name);
            }
        }
    }

    public void saveBlocks(){

        Map<String,List<SimpleBlock>> nameMap = new HashMap<String,List<SimpleBlock>>();


        for(String name : blockTypesReg.getAllNames()){
            nameMap.put(name, new ArrayList<SimpleBlock>());
        }

        for(Map.Entry<SimpleBlock, String> block : blocks.entrySet()){
            nameMap.get(block.getValue()).add(block.getKey());
        }

        for(Map.Entry<String, List<SimpleBlock>> names : nameMap.entrySet()){
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
    public Map<SimpleBlock, String> getBlocks(){
        return blocks;
    }

    public void addBlock(SimpleBlock block, String name){
        blocks.put(block, name);
    }
}
