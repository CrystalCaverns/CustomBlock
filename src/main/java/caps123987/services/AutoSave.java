package caps123987.services;

import caps123987.customblock.CustomBlock;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class AutoSave {
    private CustomBlock plugin;
    public void start(CustomBlock plugin, int interval) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(CustomBlock.instance, this::save, 2000L, interval * 60L * 20L);
    }
    public void save() {
        plugin.saveBlocks();
    }

}
