package me.cps.bot;

import net.dv8tion.jda.api.entities.Activity;

/**
 * Curious Productions Bot
 * Bot Data
 *
 * All the bot data.
 * All bot data is set at /home/cps/bot_data.yaml
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public class BotData {

    public BotData(String token, String prefix, String ownerId, Activity.ActivityType activityType, String activityMessage, String activityUrl, String configVersion) {
        this.token = token;
        this.prefix = prefix;
        this.ownerId = ownerId;

        this.activityType = activityType;
        this.activityMessage = activityMessage;
        this.activityUrl = activityUrl;

        this.configVersion = configVersion;
    }

    public BotData() {}

    private String token;
    private String prefix;
    private String ownerId;

    private Activity.ActivityType activityType;
    private String activityMessage;
    private String activityUrl;

    private String configVersion;

    public String getToken() { return token; }
    public String getPrefix() { return prefix; }
    public String getOwnerId() { return ownerId; }
    public Activity.ActivityType getActivityType() { return activityType; }
    public String getActivityMessage() { return activityMessage; }
    public String getActivityUrl() { return activityUrl; }
    public String getConfigVersion() { return configVersion; }
}
