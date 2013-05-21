package de.stock.settings;

/**
 * Provides settings for the deserializer
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Settings_Deserializer {

    /**
     * Used for deserialzer
     */
    public static final Integer TYPE_TRADEABLE         = 0x0001;
    /**
     * Used for deserialzer
     */
    public static final Integer TYPE_EVENT             = 0x0002;
    /**
     * Used for deserialzer
     */
    public static final Integer TYPE_ENVIRONMENT       = 0x0003;
    /**
     * Used for deserialzer
     */
    public static final Integer TYPE_LEVELPACK         = 0x0004;
    /**
     * Serialized tradeable file extension
     */
    public static final String  EXTENSION_TRADEABLES   = ".trd";
    /**
     * Serialized event file extension
     */
    public static final String  EXTENSION_EVENTS       = ".evt";
    /**
     * Serialized environment file extension
     */
    public static final String  EXTENSION_ENVIRONMENTS = ".env";
    /**
     * Level file extension
     */
    public static final String  EXTENSION_LEVELPACKS   = ".lvl";
    /**
     * Serialized tradeable path
     */
    public static String        PATH_TRADEABLES        = Settings_Game.PATH_RESOURCES
                                                               + "tradeables/";
    /**
     * Serialized event path
     */
    public static String        PATH_EVENTS            = Settings_Game.PATH_RESOURCES + "events/";
    /**
     * Serialized environment path
     */
    public static String        PATH_ENVIRONMENTS      = Settings_Game.PATH_RESOURCES
                                                               + "environments/";
}
