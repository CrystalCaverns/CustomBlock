package caps123987.customblock;

import caps123987.services.AutoSave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CustomBlock extends JavaPlugin {
    public static CustomBlock instance;
    private AutoSave autoSave;
    @Override
    public void onEnable() {
        instance = this;
        setUpBlocks();

        autoSave = new AutoSave();
        autoSave.start(5);
    }

    public void setUpBlocks(){
        File file = new File(CustomBlock.instance.getDataFolder(),"Networks.yml");

        if(file.exists()) {

            FileConfiguration yaml= YamlConfiguration.loadConfiguration(file);
            List<Location> list = (List<Location>) yaml.get("networks");

        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
            List<Location> list = new CopyOnWriteArrayList<>();

            yaml.set("networks", list);
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
