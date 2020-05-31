package me.cps.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.cps.bot.commands.PlayerInfoCommand;
import me.cps.bot.database.DatabaseHub;
import me.cps.bot.networkdata.BotComponentType;
import me.cps.bot.networkdata.BotNetworkDataHub;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.File;

/**
 * Curious Productions Bot
 * Bot Main
 *
 * The main class for the bot.
 * All bot data is set at /home/cps/bot_data.yaml
 *
 * @author  Gabriella Hotten
 * @version 1.0-beta
 * @since   2020-05-31
 */
public class CPSBot {

    private static BotData botData;
    private static String version = "1.0-beta";
    private static BotNetworkDataHub networkDataHub;

    public static void main(String[] args) throws LoginException, InterruptedException {
        registerConfigs();
        DatabaseHub databaseHub = new DatabaseHub();
        JDA jda = null;
        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(botData.getOwnerId());
        client.setPrefix(botData.getPrefix());
        client.setActivity((Activity.of(botData.getActivityType(), botData.getActivityMessage(), (botData.getActivityUrl().equals("null") ? null : botData.getActivityUrl()))));
        client.addCommands(
                new PlayerInfoCommand()
        );

        JDABuilder builder = JDABuilder.createDefault(botData.getToken());
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(waiter, client.build());
        jda = builder.build();

        jda.awaitReady();
        networkDataHub.checkForUpdates(BotComponentType.BOTCORE, version);
        System.out.println("Bot for " + networkDataHub.getNetworkDataBase().getNetworkName() + " by Gabriella Hotten. Version " + version);
    }

    public static void registerConfigs() {
        networkDataHub = new BotNetworkDataHub();
        System.out.println("Parsing bot data...");
        try {
            File file = new File("/home/cps/bot_data.yaml");
            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            botData = om.readValue(file, BotData.class);

            System.out.println("Success!");
        } catch (Exception e) {
            System.out.println("Error! Please see below:");
            e.printStackTrace();
        }
        networkDataHub.checkForUpdates(BotComponentType.BOTCONFIG, botData.getConfigVersion());
    }

    public BotData getBotData() { return botData; }
}
