package de.stock.utils;

import java.util.HashMap;
import java.util.HashSet;

import de.stock.game.Player;
import de.stock.tradeable.ITradeable;
import de.stock.tradeable.TradeableHandler;

public final class Provider {

    /**
     * Returns all currently active {@link de.stock.tradeable.Tradeable
     * tradeables} and their values
     * 
     * @return a Map of all currently active
     *         {@link de.stock.tradeable.Tradeable tradeables} and their values
     */
    public static HashMap<ITradeable, Double> getAllActiveTradeables() {
        return TradeableHandler.getInstance().getActiveTradeables();
    }

    /**
     * Returns all registered - inconclusively running -
     * {@link de.stock.tradeable.Tradeable tradeables}
     * 
     * @return a Set of all registered - inconclusively running -
     *         {@link de.stock.tradeable.Tradeable tradeables}
     */
    public static HashSet<ITradeable> getAllTradeables() {
        return TradeableHandler.getInstance().getTradeables();
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
     * Returns the active {@link de.stock.tradeable.TradeableHandler
     * TradeableHandler} object
     * 
     * @return the active {@link de.stock.tradeable.TradeableHandler
     *         TradeableHandler} object
     */
    public static TradeableHandler getTradeableHandler() {
        return TradeableHandler.getInstance();
    }
}
