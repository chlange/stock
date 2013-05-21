package de.stock.tradeable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import de.stock.deserializer.Deserializer;
import de.stock.settings.Settings_Deserializer;

/**
 * The tradeable handler stores all registered
 * {@link de.stock.tradeable.Tradeable tradeables}, keeps track of the active
 * (currently played) {@link de.stock.tradeable.Tradeable tradeables} and
 * updates them if they weren't influenced by an {@link de.stock.event.Event
 * Event}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class TradeableHandler {

    private static TradeableHandler instance = null;

    public static TradeableHandler getInstance() {
        if (instance == null) {
            instance = new TradeableHandler();
        }
        return instance;
    }

    /**
     * Saves all tradeables
     */
    private HashSet<ITradeable>                tradeables;
    /**
     * Tradeables with their current value
     */
    private HashMap<ITradeable, Double>        activeTradeables;

    /**
     * Used to check if tradeable value was influenced by an event or needs to
     * be updated<br>
     * See {@link #saveCurrentState()} and {@link #updateUnchangedTradeables()}
     */
    private static HashMap<ITradeable, Double> tempTradeables;

    private TradeableHandler() {
        setTradeables(new HashSet<ITradeable>());
        activeTradeables = new HashMap<ITradeable, Double>();
        tempTradeables = new HashMap<ITradeable, Double>();
    }

    /**
     * Wrapper for {@link #registerActiveTradeable(ITradeable)}
     * 
     * @param tradeable
     *            tradeable to add
     */
    public void addActiveTradeable(final ITradeable tradeable) {
        registerActiveTradeable(tradeable);
    }

    /**
     * Adds tradeable to <b>available</b> tradeables<br>
     * <br>
     * <b>Use {@link #register(ITradeable)} to set tradeable active</b>!
     */
    public void addTradeable(final ITradeable tradeable) {
        tradeables.add(tradeable);
    }

    public HashMap<ITradeable, Double> getActiveTradeables() {
        return activeTradeables;
    }

    public HashMap<ITradeable, Double> getTempTradeables() {
        return tempTradeables;
    }

    public HashSet<ITradeable> getTradeables() {
        return tradeables;
    }

    /**
     * Loads all tradeables from directory
     * {@link de.stock.settings.Settings_Deserializer#PATH_TRADEABLES} and
     * stores
     * them in {@link #getTradeables()}
     * 
     * @return
     *         number of loaded tradeables
     */
    public Integer loadTradeables() {
        return Deserializer.deserialize(Settings_Deserializer.TYPE_TRADEABLE);
    }

    /**
     * Wrapper for {@link #registerActiveTradeable(ITradeable)}
     * 
     * @param tradeable
     *            tradeable to add
     */
    public void register(final ITradeable tradeable) {
        registerActiveTradeable(tradeable);
    }

    /**
     * Adds {@code tradeable} to active {@link de.stock.tradeable.Tradeable
     * tradeables} and initializes it using
     * {@link de.stock.tradeable.Tradeable#initializeValue()}
     * 
     * @param tradeable
     *            tradeable to add to active tradeables
     */
    public void registerActiveTradeable(final ITradeable tradeable) {
        if (getActiveTradeables().containsKey(tradeable)) {
            return;
        }
        getActiveTradeables().put(tradeable, tradeable.initializeValue());
    }

    /**
     * Saves current state of {@link de.stock.tradeable.Tradeable tradeables}<br>
     * <br>
     * Cooperates with {@link #updateUnchangedTradeables()} to update tradeables
     * which weren't influenced by events
     */
    public void saveCurrentState() {
        tempTradeables.clear();
        tempTradeables.putAll(getActiveTradeables());
    }

    public void setActiveTradeables(final HashMap<ITradeable, Double> activeTradeables) {
        this.activeTradeables = activeTradeables;
    }

    public void setTempTradeables(final HashMap<ITradeable, Double> tempTradeables) {
        TradeableHandler.tempTradeables = tempTradeables;
    }

    public void setTradeables(final HashSet<ITradeable> tradeables) {
        this.tradeables = tradeables;
    }

    /**
     * Updates all unchanged {@link de.stock.tradeable.Tradeable tradeables}<br>
     * <br>
     * Compares temporary state of {@link #saveCurrentState()} with current
     * state and updates {@link de.stock.tradeable.Tradeable tradeables} which
     * weren't influenced by events <br>
     */
    public void updateUnchangedTradeables() {
        for (final Entry<ITradeable, Double> activeEntry : getActiveTradeables().entrySet()) {
            if (getTempTradeables().containsKey(activeEntry.getKey())) {
                if (activeEntry.getValue().doubleValue() == getTempTradeables().get(
                        activeEntry.getKey()).doubleValue()) {
                    // Update tradeable if its unchanged in tradeable self
                    activeEntry.getKey().updateValue();
                    // Update tradeable if its unchanged in active tradeables
                    getActiveTradeables()
                            .put(activeEntry.getKey(), activeEntry.getKey().getValue());
                }
            }
        }
    }
}
