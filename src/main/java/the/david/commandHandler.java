package the.david;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class commandHandler implements CommandExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            return false;
        }
        if(Objects.equals(animalConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()),null)){

        }
        commandSender.sendMessage(Component.text("此Chunk [ " + ((Player) commandSender).getWorld().getName() + " : (" + ((Player) commandSender).getChunk().getX() + ","+ ((Player) commandSender).getChunk().getZ() + ") ] 的權重: ").color(TextColor.color(170,170,170)));
        if(!Objects.equals(animalConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()),null)){
            commandSender.sendMessage(Component.text("  動物: ").color(TextColor.color(255, 170, 0)).append(Component.text(animalConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()).toString())));
        }else{
            commandSender.sendMessage(Component.text("  動物: ").color(TextColor.color(255, 170, 0)).append(Component.text("12500")));
        }
        if(!Objects.equals(entityConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()),null)){
            commandSender.sendMessage(Component.text("  其他生物: ").color(TextColor.color(255, 170, 0)).append(Component.text(entityConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()).toString())));
        }else{
            commandSender.sendMessage(Component.text("  其他生物: ").color(TextColor.color(255, 170, 0)).append(Component.text("12500")));
        }
        if(!Objects.equals(blockConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()),null)){
            commandSender.sendMessage(Component.text("  方塊: ").color(TextColor.color(255, 170, 0)).append(Component.text(blockConfigReader.getChunkWeight(((Player) commandSender).getWorld(),((Player) commandSender).getChunk()).toString())));
        }else{
            commandSender.sendMessage(Component.text("  方塊: ").color(TextColor.color(255, 170, 0)).append(Component.text("12500")));
        }
        return true;
    }
}
