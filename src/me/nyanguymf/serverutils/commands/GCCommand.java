/**
 * GCCommand.java 2019-01-10
 * 
 * @author NyanGuyMF
 * 
 * @version 1.0
 */
package me.nyanguymf.serverutils.commands;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import me.nyanguymf.serverutils.ServerUtils;
import me.nyanguymf.serverutils.managers.MessagesManager;
import me.nyanguymf.serverutils.utils.StringUtils;

/**
 * @author nyanguymf
 *
 */
class GCCommand extends Command {
    private static ServerUtils su = ServerUtils.getInstance();

    public GCCommand(String permission, String command) {
        super(permission, command);
    }

    /**
     * Call to the Garbage Collector and
     * considers loaded chunks and entities
     * for each server's world.
     *
     * As response send message with this info
     * to command sender.
     */
    @Override
    public void execute(CommandSender sender, boolean permissionAll) {
        if (!sender.hasPermission(super.permission) && !permissionAll) {
            super.sendNoPermission(sender);
            return;
        }

        callGC(sender);

        List<World> worlds  = su.getServer().getWorlds();
        String[]    message = new String[worlds.size()];
        String      format  = MessagesManager.getInstance().getColoredMessage("world");

        for (int c = 0; c < worlds.size(); c++) {
            World world = worlds.get(c);

            message[c]  = StringUtils.replaceVariables(
                    format, new String[] {
                    world.getName(),
                    String.valueOf(world.getLoadedChunks().length),
                    String.valueOf(world.getEntities().size())
                });
        }

        sender.sendMessage(message);
    }

    /**
     * Ask garbage collector: "Please, respectful GC, collect all
     * the garbage".
     *
     * @param sender CommandSender to send message.
     */
    private static void callGC(CommandSender sender) {
        String message = MessagesManager.getInstance().getColoredMessage("gc");

        System.gc();

        sender.sendMessage(message);
    }
}
