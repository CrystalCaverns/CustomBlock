package caps123987.services;

import caps123987.customblock.CustomBlock;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class AutoSave {

    public void start(CustomBlock plugin, int interval) {
        Bukkit.getScheduler().runTaskTimer(CustomBlock.instance, this::save, 2000L, interval * 60L * 20L);

    }
    public void save() {


        if(MeStorage.saveNets()) {
            MeStorage.logger().log(Level.INFO,"MeNets saved");
        }else {
            MeStorage.logger().log(Level.WARNING, ChatColor.RED+"MeNets NOT saved");
        }
        if(MeStorage.saveDisks()) {
            MeStorage.logger().log(Level.INFO,"MeDisks saved");
        }else {
            MeStorage.logger().log(Level.WARNING,ChatColor.RED+"MeDisks NOT saved");
        }
    }

}
