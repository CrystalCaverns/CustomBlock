package caps123987.customblock;

import caps123987.listeners.Placement;
import caps123987.services.AutoSave;
import caps123987.types.SimpleBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CustomBlock extends JavaPlugin {
    public static CustomBlock instance;
    private AutoSave autoSave;
    private File blocksFile;
    private Set<SimpleBlock> blocks = new HashSet<>();
    @Override
    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(SimpleBlock.class);

        Bukkit.getPluginManager().registerEvents(new Placement(),this);

        blocksFile = new File(CustomBlock.instance.getDataFolder(),"blocks.yml");
        setUpBlocks();

        autoSave = new AutoSave();
        autoSave.start(this,1);
    }

    public void setUpBlocks(){

        List<SimpleBlock> list;

        if(blocksFile.exists()) {

            FileConfiguration yaml= YamlConfiguration.loadConfiguration(blocksFile);
            list = (List<SimpleBlock>) yaml.get("blocks");



        }else {
            try {
                blocksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileConfiguration yaml = YamlConfiguration.loadConfiguration(blocksFile);
            list = new ArrayList<>();

            yaml.set("blocks", list);
            try {
                yaml.save(blocksFile);
            } catch (IOException e) {
                Bukkit.broadcastMessage(e.toString());
            }
        }

        if(list!=null) {
            blocks.addAll(list);
        }else {
            blocks = new HashSet<>();
        }
    }

    public void saveBlocks(){
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(blocksFile);
        yaml.set("blocks",blocks.stream().toList());
        try {
            yaml.save(blocksFile);
            this.getLogger().info("Blocks saved");
        } catch (IOException e) {
            e.printStackTrace();
            this.getLogger().warning("Blocks not saved");
        }
    }

    @Override
    public void onDisable() {
        saveBlocks();
    }
    public Set<SimpleBlock> getBlocks(){
        return blocks;
    }
    public void setBlocks(Set<SimpleBlock> blocks){
        this.blocks = blocks;
    }
}
