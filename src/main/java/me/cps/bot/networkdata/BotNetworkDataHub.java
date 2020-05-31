package me.cps.bot.networkdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Curious Productions Bot
 * Bot Network Data Hub
 *
 * Gets all network info (mainly for db purposes)
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class BotNetworkDataHub {

    private NetworkDataBase networkDataBase;
    private static BotNetworkDataHub instance;

    public BotNetworkDataHub() {
        instance = this;
        parseNetworkData();
        checkForUpdates(BotComponentType.NETWORKCONFIG, networkDataBase.getConfigVersion());
    }

    public static BotNetworkDataHub getInstance() {
        return instance;
    }

    public NetworkDataBase getNetworkDataBase() {
        return networkDataBase;
    }

    public void parseNetworkData() {
        System.out.println("Started parse of Network Data...");
        try {
            File file = new File("/home/cps/network_data.yaml");
            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            networkDataBase = om.readValue(file, NetworkDataBase.class);
            System.out.println("Parse successful!");
        } catch (Exception e) {
            System.out.println("ERROR: Error parsing network data. Defaults have been applied. Please see error below:");
            e.printStackTrace();
            networkDataBase = new NetworkDataBase("localhost", "cps", "password", "cps", 3306, "Europe/Paris",
                    "localhost", "", 6379,
                    "CPS", "AQUA", "BLUE",
                    "cps.me", "play.cps.me", "CPS AC", "1.0");
        }
    }

    public void checkForUpdates(BotComponentType type, String version) {
        System.out.println("----------------------------------------");
        System.out.println("Checking for updates for " + type);
        try {
            URL url = new URL(type.getVersionUrl());

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ver = in.readLine();
            in.close();

            System.out.println("Latest version is " + ver);
            System.out.println("Our version is " + version);

            if (version.equals(ver)) {
                System.out.println(type.toString() + " is up to date!");
            } else {
                System.out.println("Warning! This " + (type == BotComponentType.NETWORKCONFIG || type == BotComponentType.BOTCONFIG ? "config" : "code") + " is out of date. Please visit " + type.getGithubUrl() + " to get the latest " + (type == BotComponentType.NETWORKCONFIG || type == BotComponentType.BOTCONFIG ? "config file." : "code."));
            }

        } catch (Exception e) {
            System.out.println("Error checking for updates.");
            e.printStackTrace();
        }
        System.out.println("----------------------------------------");
    }


}
