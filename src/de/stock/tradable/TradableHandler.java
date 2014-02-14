package de.stock.tradable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import de.stock.deserializer.Deserializer;
import de.stock.settings.Settings_Deserializer;

/**
 * The tradable handler stores all registered
 * {@link de.stock.tradable.Tradable tradables}, keeps track of the active
 * (currently played) {@link de.stock.tradable.Tradable tradables} and
 * updates them if they weren't influenced by an {@link de.stock.event.Event
 * Event}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class TradableHandler {

    private static TradableHandler instance = null;

    public static TradableHandler getInstance() {
        if (instance == null) {
            instance = new TradableHandler();
        }
        return instance;
    }

    /**
     * Saves all tradables
     */
    private HashSet<ITradable>                tradables;
    /**
     * Tradables with their current value
     */
    private HashMap<ITradable, Double>        activeTradables;

    /**
     * Used to check if tradable value was influenced by an event or needs to
     * be updated<br>
     * See {@link #saveCurrentState()} and {@link #updateUnchangedTradables()}
     */
    private static HashMap<ITradable, Double> tempTradables;

    private TradableHandler() {
        setTradables(new HashSet<ITradable>());
        activeTradables = new HashMap<ITradable, Double>();
        tempTradables = new HashMap<ITradable, Double>();
    }

    /**
     * Wrapper for {@link #registerActiveTradable(ITradable)}
     * 
     * @param tradable
     *            tradable to add
     */
    public void addActiveTradable(final ITradable tradable) {
        registerActiveTradable(tradable);
    }

    /**
     * Adds tradable to <b>available</b> tradables<br>
     * <br>
     * <b>Use {@link #register(ITradable)} to set tradable active</b>!
     */
    public void addTradable(final ITradable tradable) {
        tradables.add(tradable);
    }

    public HashMap<ITradable, Double> getActiveTradables() {
        return activeTradables;
    }

    public HashMap<ITradable, Double> getTempTradables() {
        return tempTradables;
    }

    public HashSet<ITradable> getTradables() {
        return tradables;
    }

    /**
     * Loads all tradables from directory
     * {@link de.stock.settings.Settings_Deserializer#PATH_TRADEABLES} and
     * stores
     * them in {@link #getTradables()}
     * 
     * @return
     *         number of loaded tradables
     */
    public Integer loadTradables() {
        return Deserializer.deserialize(Settings_Deserializer.TYPE_TRADEABLE);
    }

    /**
     * Wrapper for {@link #registerActiveTradable(ITradable)}
     * 
     * @param tradable
     *            tradable to add
     */
    public void register(final ITradable tradable) {
        registerActiveTradable(tradable);
    }

    /**
     * Adds {@code tradable} to active {@link de.stock.tradable.Tradable
     * tradables} and initializes it using
     * {@link de.stock.tradable.Tradable#initializeValue()}
     * 
     * @param tradable
     *            tradable to add to active tradables
     */
    public void registerActiveTradable(final ITradable tradable) {
        if (getActiveTradables().containsKey(tradable)) {
            return;
        }
        getActiveTradables().put(tradable, tradable.initializeValue());
    }

    /**
     * Saves current state of {@link de.stock.tradable.Tradable tradables}<br>
     * <br>
     * Cooperates with {@link #updateUnchangedTradables()} to update tradables
     * which weren't influenced by events
     */
    public void saveCurrentState() {
        tempTradables.clear();
        tempTradables.putAll(getActiveTradables());
    }

    public void setActiveTradables(final HashMap<ITradable, Double> activeTradables) {
        this.activeTradables = activeTradables;
    }

    public void setTempTradables(final HashMap<ITradable, Double> tempTradables) {
        TradableHandler.tempTradables = tempTradables;
    }

    public void setTradables(final HashSet<ITradable> tradables) {
        this.tradables = tradables;
    }

    /**
     * Updates all unchanged {@link de.stock.tradable.Tradable tradables}<br>
     * <br>
     * Compares temporary state of {@link #saveCurrentState()} with current
     * state and updates {@link de.stock.tradable.Tradable tradables} which
     * weren't influenced by events <br>
     */
    public void updateUnchangedTradables() {
        for (final Entry<ITradable, Double> activeEntry : getActiveTradables().entrySet()) {
            if (getTempTradables().containsKey(activeEntry.getKey())) {
                if (activeEntry.getValue().doubleValue() == getTempTradables().get(
                        activeEntry.getKey()).doubleValue()) {
                    // Update tradable if its unchanged in tradable self
                    activeEntry.getKey().updateValue();
                    // Update tradable if its unchanged in active tradables
                    getActiveTradables()
                            .put(activeEntry.getKey(), activeEntry.getKey().getValue());
                }
            }
        }
    }
}
