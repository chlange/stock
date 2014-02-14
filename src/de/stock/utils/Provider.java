package de.stock.utils;

import java.util.HashMap;
import java.util.HashSet;

import de.stock.game.Player;
import de.stock.tradable.ITradable;
import de.stock.tradable.TradableHandler;

public final class Provider {

    /**
     * Returns all currently active {@link de.stock.tradable.Tradable
     * tradables} and their values
     * 
     * @return a Map of all currently active
     *         {@link de.stock.tradable.Tradable tradables} and their values
     */
    public static HashMap<ITradable, Double> getAllActiveTradables() {
        return TradableHandler.getInstance().getActiveTradables();
    }

    /**
     * Returns all registered - inconclusively running -
     * {@link de.stock.tradable.Tradable tradables}
     * 
     * @return a Set of all registered - inconclusively running -
     *         {@link de.stock.tradable.Tradable tradables}
     */
    public static HashSet<ITradable> getAllTradables() {
        return TradableHandler.getInstance().getTradables();
    }

    /**
     * Returns the active {@link de.stock.game.Player Player} object
     * 
     * @return the active {@link de.stock.game.Player Player} object
     */
    public static Player getPlayer() {
        return Player.getInstance();
    }

    /**
     * Returns the active {@link de.stock.tradable.TradableHandler
     * TradableHandler} object
     * 
     * @return the active {@link de.stock.tradable.TradableHandler
     *         TradableHandler} object
     */
    public static TradableHandler getTradableHandler() {
        return TradableHandler.getInstance();
    }
}
