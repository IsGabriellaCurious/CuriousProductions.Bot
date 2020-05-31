package me.cps.bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.cps.bot.networkdata.BotNetworkDataHub;

/**
 * Curious Productions Bot
 * Command - Ip Command
 *
 * Gets the IP of the server.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class IpCommand extends Command {

    public IpCommand() {
        this.name = "ip";
        this.aliases = new String[]{"getip"};
        this.category = new Category("Server");
        this.help = "Get the IP adress of the server.";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Here! `" + BotNetworkDataHub.getInstance().getNetworkDataBase().getNetworkIp() + "`");
    }
}
