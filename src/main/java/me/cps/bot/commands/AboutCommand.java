package me.cps.bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.cps.bot.networkdata.BotNetworkDataHub;
import me.cps.bot.networkdata.NetworkDataBase;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

/**
 * Curious Productions Bot
 * Command - About Command
 *
 * Get information about the network.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class AboutCommand extends Command {

    NetworkDataBase data;

    public AboutCommand() {
        data = BotNetworkDataHub.getInstance().getNetworkDataBase();
        this.name = "about";
        this.aliases = new String[]{"serverinfo", "aboutserver"};
        this.category = new Category("Server");
        this.help = "Get info about the " + data.getNetworkName() + " network!";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(data.getNetworkName() + " Network Info");
        eb.addField("Server IP", data.getNetworkIp(), true);
        eb.addField("Server Website", data.getNetworkIp(), true);
        eb.addField("Players Online", "-1", true); //todo
        eb.setFooter("Bot created by Gabrlella");
        eb.setColor(Color.blue);

        event.reply(eb.build());
    }
}
