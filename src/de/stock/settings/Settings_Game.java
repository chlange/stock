package de.stock.settings;

import java.util.ArrayList;

/**
 * Provides settings for the game
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Settings_Game {

    /**
     * All available countries player can choose from
     */
    public static final ArrayList<String> AVAILABLE_COUNTRIES  = new ArrayList<String>();
    /**
     * All available currencies player can choose from
     */
    public static final ArrayList<String> AVAILABLE_CURRENCIES = new ArrayList<String>();
    /**
     * Version of the game
     */
    public static final double            VERSION              = 0.2;
    /**
     * Resource path
     */
    public static String                  PATH_RESOURCES       = "res/";
    /**
     * The current round
     */
    public static Integer                 ROUND                = 1;
}
