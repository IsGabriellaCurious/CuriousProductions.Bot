package me.cps.bot.database;

import me.cps.bot.networkdata.BotNetworkDataHub;
import me.cps.root.punish.PunishData;
import me.cps.root.punish.PunishDuration;
import me.cps.root.punish.PunishType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Curious Productions Bot
 * Database Hub
 *
 * All that MySQL database stuff!
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-31
 */
public class DatabaseHub {

    private static DatabaseHub instance;

    private String host, username, password, database;
    private int port;

    public DatabaseHub() {
        instance = this;
        this.host = BotNetworkDataHub.getInstance().getNetworkDataBase().getMysqlUrl();
        this.username = BotNetworkDataHub.getInstance().getNetworkDataBase().getMysqlUser();
        this.password = BotNetworkDataHub.getInstance().getNetworkDataBase().getMysqlPw();
        this.database = BotNetworkDataHub.getInstance().getNetworkDataBase().getMysqlDb();
        this.port = BotNetworkDataHub.getInstance().getNetworkDataBase().getMysqlPort();

        System.out.println("Starting a quick MySQL connection check, Un momento!");
        try {
            Connection conn = createConnection();

            conn.close();
            System.out.println("Test successful! Connection is closed and we are good to go.");
        } catch (Exception e) {
            System.out.println("ERROR! See the error below:");
            e.printStackTrace();
        }
    }

    public static DatabaseHub getInstance() { return instance; }

    public Connection createConnection() throws SQLException, ClassNotFoundException {

        synchronized (this) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?serverTimezone=" + BotNetworkDataHub.getInstance().getNetworkDataBase().getMysqlTimezone(), this.username, this.password); //connection url
            return conn;
        }
    }

    public String getDurationMessage(long duration) {

        if (duration == -1)
            return "Permanent";

        long remaining = (duration -  System.currentTimeMillis()) / 1000;

        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        while (remaining >= 86400) {
            days++;
            remaining -= 86400;
        }

        while (remaining >= 3600) {
            hours++;
            remaining -= 3600;
        }

        while (remaining >= 60) {
            minutes++;
            remaining -= 60;
        }

        while (remaining >= 1) {
            seconds++;
            remaining -= 1;
        }

        return
                (days != 0 ? days + " days, " : "") +
                        (hours != 0 ? hours + " hours, " : "") +
                        (minutes != 0 ? minutes + " minutes, " : "") +
                        (seconds != 0 ? seconds + " seconds" : "");

    }


    /**
     * @param username The player's username you want to search.
     * @return Key is the type of data, Value is the result of the data (e.g. RANK key could have a HELPER value). Empty means the player doesn't exist. (or error)
     */
    public HashMap<String, String> getPlayerAccountData(String username) {
        HashMap<String, String> result = new HashMap<>();
        String uuid = "";

        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `account` WHERE name=?");
            statement.setString(1, username);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                result.put("id", resultSet.getString("id"));
                result.put("uuid", resultSet.getString("uuid"));
                uuid = resultSet.getString("uuid");
                result.put("rank", resultSet.getString("rank"));
            }

            PreparedStatement punishStatement = connection.prepareStatement("SELECT * FROM `punish.active` WHERE uuid=?");
            punishStatement.setString(1, uuid);
            punishStatement.executeQuery();

            ResultSet punishSet =  punishStatement.getResultSet();

            if (punishSet.next()) {
                if (punishSet.getString("type").equalsIgnoreCase("BAN")) {
                    result.put("type", "Ban");
                    result.put("banned", "true");
                    result.put("reason", punishSet.getString("reason"));
                    result.put("duration", getDurationMessage(punishSet.getLong("duration")));
                } else if (punishSet.getString("type").equalsIgnoreCase("BLACKLIST")) {
                    result.put("type", "Blacklist");
                    result.put("blacklisted", "true");
                    result.put("reason", punishSet.getString("reason"));
                    result.put("duration", getDurationMessage(punishSet.getLong("duration")));
                }
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String nameFromUUID(UUID uuid) {
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT `name` FROM `account` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            String result = null;
            if (resultSet.next())
                result = resultSet.getString("name");
            else
                result = "";

            connection.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID uuidFromName(String name) {
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `account` WHERE name=?");
            statement.setString(1,name);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            UUID result = null;
            if (resultSet.next())
                result = UUID.fromString(resultSet.getString("uuid"));

            connection.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<PunishData> playerPunishments(String player) {
        UUID uuid = uuidFromName(player);
        ArrayList<PunishData> data = new ArrayList<>();

        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.history` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();


            while (rs.next()) {
                PunishData pData = new PunishData();
                pData.setPunishmentId(rs.getString("punishmentId"));
                pData.setPlayer(rs.getString("punisher"));
                pData.setTarget(nameFromUUID(UUID.fromString(rs.getString("uuid"))));
                pData.setReason(rs.getString("reason"));
                pData.setType(PunishType.valueOf(rs.getString("type")));
                pData.setDuration(PunishDuration.valueOf(rs.getString("durationType")));
                pData.setActive(Boolean.parseBoolean(rs.getString("active")));
                pData.setDate(rs.getString("date"));

                pData.setRemovedOn(rs.getString("removedOn"));
                pData.setRemovedBy(rs.getString("removedBy"));
                pData.setRemovedReason(rs.getString("removedReason"));

                data.add(pData);
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
