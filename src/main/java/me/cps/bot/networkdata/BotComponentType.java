package me.cps.bot.networkdata;

/**
 * Curious Productions Bot
 * Bot Network Data Hub - Bot Component Type Enum
 *
 * Enum that holds a list of all bot components.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-31
 */
public enum BotComponentType {

    NETWORKCONFIG("https://pastebin.com/raw/0NYhDm8D", "https://github.com/IsGabriellaCurious/CuriousProductions.Root"),
    BOTCORE("https://pastebin.com/raw/zXsQZ6qN", "https://github.com/IsGabriellaCurious/CuriousProductions.Bot"),
    BOTCONFIG("https://pastebin.com/raw/jz7f9WCc", "https://github.com/IsGabriellaCurious/CuriousProductions.Bot");

    private String versionUrl;
    private String githubUrl;

    private BotComponentType(String versionUrl, String githubUrl) {
        this.versionUrl = versionUrl;
        this.githubUrl = githubUrl;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

}
