package de.stock.settings;

/**
 * Provides settings for the {@link de.stock.level.LevelDecorator Level} system
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Settings_Level {

    /**
     * Number of level stages w/o award level
     */
    public static final Integer LEVEL_STAGES      = 9;
    /**
     * First level stage
     */
    public static final Integer LEVEL_STAGE_START = 1;
    /**
     * Last level stage
     */
    public static final Integer LEVEL_STAGE_END   = 9;

    /**
     * Award level specific level stage
     */
    public static final Integer AWARD_LEVEL       = 0;
    /**
     * Main level specific level stage
     */
    public static final Integer MAIN_LEVEL        = 1;

    /**
     * Level stage 1 is the main level stage
     */
    public static final Integer LEVEL_STAGE_1     = 1;
    public static final Integer LEVEL_STAGE_2     = 2;
    public static final Integer LEVEL_STAGE_3     = 3;
    public static final Integer LEVEL_STAGE_4     = 4;
    public static final Integer LEVEL_STAGE_5     = 5;
    public static final Integer LEVEL_STAGE_6     = 6;
    public static final Integer LEVEL_STAGE_7     = 7;
    public static final Integer LEVEL_STAGE_8     = 8;
    public static final Integer LEVEL_STAGE_9     = 9;

    /**
     * Level pack path
     */
    public static String        PATH_LEVELPACKS   = Settings_Game.PATH_RESOURCES + "levels/";
}
