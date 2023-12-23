package caps123987.commands;

import caps123987.customblock.CustomBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 2){
            return true;
        }

        if(!CustomBlock.getInstance().blockTypesReg.getRegister().containsKey(args[0])){
            sender.sendMessage("Block type not found");
            return true;
        }

        ItemStack item = CustomBlock.getInstance().blockTypesReg.getItem(args[0]);

        ((Player) sender).getInventory().addItem(item);

        return true;
    }
}
