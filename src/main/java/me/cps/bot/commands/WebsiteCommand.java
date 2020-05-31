package me.cps.bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.cps.bot.networkdata.BotNetworkDataHub;

/**
 * Curious Productions Bot
 * Command - Website Command
 *
 * Gets the network's website.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class WebsiteCommand extends Command {

    public WebsiteCommand() {
        this.name = "website";
        this.aliases = new String[]{"site", "web", "webadress"};
        this.category = new Category("Server");
        this.help = "Get the website of the server.";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Here! " + BotNetworkDataHub.getInstance().getNetworkDataBase().getNetworkWebsite());
    }
}
