package me.cps.bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.cps.bot.database.DatabaseHub;
import me.cps.root.punish.PunishData;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Curious Productions Bot
 * Command - Player Punish History Command
 *
 * Gets punish history for a player.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class PunishHistoryCommand extends Command {

    public PunishHistoryCommand() {
        this.name = "history";
        this.aliases = new String[]{"punishhistory", "phistory", "ph"};
        this.category = new Category("Players");
        this.help = "Get a player's punishment history.";
        this.arguments = "<player name>";
        this.cooldown = 10;
        this.cooldownScope = CooldownScope.GUILD;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(":x: Please include a player name!");
            return;
        }

        event.reply("One sec...");
        ArrayList<PunishData> data = DatabaseHub.getInstance().playerPunishments(event.getArgs());
        EmbedBuilder eb = new EmbedBuilder();

        if (DatabaseHub.getInstance().uuidFromName(event.getArgs()) == null) {
            eb.setTitle("Whoops! An error occurred.");
            eb.setColor(Color.red);
            eb.addField("What happen?", "The player you specified has never logged into the server.", false);
            event.reply(eb.build());
            return;
        }

        if (data.isEmpty()) {
            eb.setTitle("Whoops! An error occurred.");
            eb.setColor(Color.red);
            eb.addField("What happen?", "There player you specified has never received a punishment!", false);
            event.reply(eb.build());
        } else {
            StringBuilder message = new StringBuilder();
            for (PunishData pd : data) {
                String temp = "**[" + pd.getType() + "]** `" + pd.getReason() + "` for `" + pd.getDuration().getDisplayName() + "` on `" + pd.getDate() + "`.";
                if (!pd.isActive())
                    temp += " **REMOVED** by `" + pd.getRemovedBy() + "` for `" + pd.getRemovedReason() + "` on `" + pd.getRemovedOn() + "`.";
                else
                    temp += " *Active.*";
                message.append(temp).append("\n");
            }
            event.reply(message.toString());
        }
    }
}
