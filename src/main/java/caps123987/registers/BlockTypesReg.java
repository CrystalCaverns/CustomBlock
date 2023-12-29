package caps123987.registers;

import caps123987.customblock.CustomBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockTypesReg {
    private final CustomBlock plugin;
    private final File regFile = new File(CustomBlock.instance.getDataFolder(),"blockTypeList.yml");
    private final File customFile = new File(CustomBlock.instance.getDataFolder(),"customModelData.yml");
    private Map<String,Integer> register = new HashMap<String,Integer>();
    private List<String> allNames = new ArrayList<String>();
    public BlockTypesReg(CustomBlock plugin) {
        this.plugin = plugin;
        setUpReg();
    }

    private void setUpReg(){

        List<String> list;

        if(regFile.exists()) {

            FileConfiguration yaml= YamlConfiguration.loadConfiguration(regFile);
            list = (List<String>) yaml.getStringList("regBlocks");

        }else {
            try {
                regFile.createNewFile();
            } catch (IOException ignored) {}

            FileConfiguration yaml = YamlConfiguration.loadConfiguration(regFile);
            list = new ArrayList<String>();
            list.add("testItem");

            yaml.set("regBlocks", list);
            try {
                yaml.save(regFile);
            } catch (IOException e) {
                plugin.getLogger().warning("Could not save blockTypeList.yml");
            }
        }

        allNames.addAll(list);

        if(!customFile.exists()){
            try {
                customFile.createNewFile();
            } catch (IOException ignored) {}
        }

        FileConfiguration yaml = YamlConfiguration.loadConfiguration(customFile);

        for(String name : list){
            if(!yaml.contains(name)){
                yaml.set(name, 0);
            }
            register.put(name, yaml.getInt(name));
        }
        try {
            yaml.save(customFile);
        } catch (IOException ignored) {}



    }

    public ItemStack getItem(String name){
        if(!register.containsKey(name)){
            return new ItemStack(Material.GRASS_BLOCK);
        }
        ItemStack item = new ItemStack(Material.IRON_INGOT);

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(plugin.customBlockKey, PersistentDataType.STRING, name);
        meta.setCustomModelData(register.get(name));
        meta.displayName(Component.text(name));

        item.setItemMeta(meta);

        return item;
    }

    public Map<String, Integer> getRegister() {
        return register;
    }
    public List<String> getAllNames(){
        return allNames;
    }
}
