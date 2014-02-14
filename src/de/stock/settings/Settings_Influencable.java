package de.stock.settings;

import java.util.HashMap;
import java.util.Map.Entry;

import de.stock.game.Player;

/**
 * Provides settings for objects which can be influenced by events and the
 * method {@link #influence(HashMap)} to actually influence the objects
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Settings_Influencable {

    // TODO: Add objects

    /**
     * Influence players money with this variable
     */
    public static final Integer PLAYER_MONEY             = 0x0001;
    /**
     * Influence players number of bought tradables with this variable
     */
    public static final Integer PLAYER_BOUGHT_TRADEABLES = 0x0002;

    /**
     * Influences passed objects
     * 
     * @param influenceObjects
     *            Map of objects to influence with the {@code Long} value
     */
    public static void influence(final HashMap<Integer, Long> influenceObjects) {
        for (final Entry<Integer, Long> entry : influenceObjects.entrySet()) {
            if (entry.getKey() == PLAYER_MONEY) {
                Player.getInstance().incMoney(entry.getValue().longValue());
            } else if (entry.getKey() == PLAYER_BOUGHT_TRADEABLES) {
                Player.getInstance().incBoughtTradables(entry.getValue().longValue());
            }
        }
    }

}
