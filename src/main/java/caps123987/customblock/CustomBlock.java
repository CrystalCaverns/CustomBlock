package caps123987.customblock;

import caps123987.services.AutoSave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CustomBlock extends JavaPlugin {
    public static CustomBlock instance;
    private AutoSave autoSave;
    @Override
    public void onEnable() {
        instance = this;
        setUpBlocks();

        autoSave = new AutoSave();
        autoSave.start(this,5);
    }

    public void setUpBlocks(){
        File file = new File(CustomBlock.instance.getDataFolder(),"Networks.yml");

        if(file.exists()) {

            FileConfiguration yaml= YamlConfiguration.loadConfiguration(file);
            Set<Location> list = (Set<Location>) yaml.get("blocks");

        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
            Set<Location> list = new HashSet<>();

            list.add(new Location(Bukkit.getWorld("world"),0,0,0));
            list.add(new Location(Bukkit.getWorld("world"),0,0,5));

            yaml.set("blocks", list);
            try {
                yaml.save(file);
            } catch (IOException e) {
                Bukkit.broadcastMessage(e.toString());
            }
        }
    }

    public void saveBlocks(){

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
