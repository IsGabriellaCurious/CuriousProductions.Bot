package me.cps.bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.cps.bot.database.DatabaseHub;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.HashMap;

/**
 * Curious Productions Bot
 * Command - Player Info Command
 *
 * Gets info about a player.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class PlayerInfoCommand extends Command {

    public PlayerInfoCommand() {
        this.name = "search";
        this.aliases = new String[]{"playerinfo", "searchplayer"};
        this.arguments = "<player name>";
        this.category = new Category("Players");
        this.help = "Get information about a player.";
        this.guildOnly = true;
        this.cooldown = 10; //you can change this if you desire
        this.cooldownScope = CooldownScope.GUILD;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(":x: Please include a player name!");
            return;
        }

        event.reply("One sec...");
        HashMap<String, String> data = DatabaseHub.getInstance().getPlayerAccountData(event.getArgs());
        EmbedBuilder eb = new EmbedBuilder();

        if (DatabaseHub.getInstance().uuidFromName(event.getArgs()) == null) {
            eb.setTitle("Whoops! An error occurred.");
            eb.setColor(Color.red);
            eb.addField("What happen?", "The player you specified has never logged into the server.", false);
            return;
        }

        if (data.isEmpty()) {
            eb.setTitle("Whoops! An error occurred.");
            eb.setColor(Color.red);
            eb.addField("What happen?", "There was an internal error. Please contact the server administrator.", false);
        } else {
            eb.setTitle("Player Data for " + event.getArgs());
            eb.setColor(Color.green);
            eb.addField("Player ID", data.get("id"), false);
            eb.addField("UUID", data.get("uuid"), false);
            eb.addField("Rank", data.get("rank"), false);

            boolean isPunished = data.containsKey("banned") || data.containsKey("blacklisted");
            eb.addField("Currently banned?", (isPunished ? "✅" : "❌"), false);
            if (isPunished) {
                eb.addField("Type", data.get("type"), false);
                eb.addField("Reason", data.get("reason"), false);
                eb.addField("Duration", data.get("duration"), false);
            }
        }
        event.reply(eb.build());

    }
}
