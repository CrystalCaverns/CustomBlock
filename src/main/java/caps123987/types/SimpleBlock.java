package caps123987.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SimpleBlock implements ConfigurationSerializable {
    private String world;
    private int x;
    private int y;
    private int z;

    public SimpleBlock(Location location){
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public SimpleBlock(Block block){
        this.world = block.getWorld().getName();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
    }
    public SimpleBlock(World world, int x, int y, int z) {
        this.world = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SimpleBlock(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SimpleBlock() {
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ(){
        return z;
    }
    public Location getLocation(){
        return new Location(getWorld(),x,y,z);
    }
    public Block getBlock(){
        return getWorld().getBlockAt(getLocation());
    }

    @Override
    public String toString() {
        return "world=" + world+ ";x=" + x + ";y=" + y + ";z=" + z;
    }

    public static SimpleBlock fromString(String string){
        String[] args = string.split(";");
        String world = args[0].split("=")[1];
        int x = Integer.parseInt(args[1].split("=")[1]);
        int y = Integer.parseInt(args[2].split("=")[1]);
        int z = Integer.parseInt(args[3].split("=")[1]);
        return new SimpleBlock(world,x,y,z);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("world", world);
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        return map;
    }

    public static SimpleBlock deserialize(Map<String, Object> args) {
        String world = (String) args.get("world");
        int x = (int) args.get("x");
        int y = (int) args.get("y");
        int z = (int) args.get("z");
        return new SimpleBlock(world, x, y, z);
    }
}
