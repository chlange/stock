package de.stock.environment;

import java.util.ArrayList;
import java.util.HashSet;

import de.stock.deserializer.Deserializer;
import de.stock.settings.Settings_Deserializer;
import de.stock.tradable.ITradable;
import de.stock.tradable.TradableHandler;
import de.stock.utils.Utils;

/**
 * The environment handler holds all environments as a centralized storage for
 * those and is the single point of contact for events to influence tradeables
 * with {@link #influenceEnvironments(ArrayList)}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class EnvironmentHandler {

    /**
     * List of all environments
     */
    private HashSet<IEnvironment>     environments;
    private static EnvironmentHandler instance = null;

    public static EnvironmentHandler getInstance() {

        if (instance == null) {
            instance = new EnvironmentHandler();
        }
        return instance;
    }

    private EnvironmentHandler() {
        environments = new HashSet<IEnvironment>();
    }

    public HashSet<IEnvironment> getEnvironments() {
        return environments;
    }

    /**
     * Influences all tradeables linked to all environmentGroups<br>
     * <br>
     * If a tradable is found more than once in a environmentGroup it gets
     * affected only once!<br>
     * If a tradable is found in _X_ different environment groups it gets
     * affected _X_ times!
     * 
     * @param environmentGroups
     *            All environmentGroups to influence
     */
    public void influenceEnvironments(final ArrayList<EnvironmentGroup> environmentGroups) {

        if (environmentGroups == null) {
            return;
        }

        Integer i = 0;

        // List of all tradeables which are influenced
        ArrayList<ITradable> finalTradables = new ArrayList<ITradable>();
        // Temporary storage for all tradeables
        ArrayList<ArrayList<ITradable>> tradableArray = new ArrayList<ArrayList<ITradable>>();

        // Iterate over each group
        for (final EnvironmentGroup environmentGroup : environmentGroups) {
            finalTradables = new ArrayList<ITradable>();
            tradableArray = new ArrayList<ArrayList<ITradable>>();
            i = 0;

            // Iterate over each environment and save affected tradeables
            for (final Environment environment : environmentGroup.getEnvironments()) {

                // Get all affected tradeables from current environment
                final HashSet<ITradable> tempTradables = environment.getInfluencedTradables();

                // Create new tradable array and add tradeables
                tradableArray.add(i, new ArrayList<ITradable>());
                tradableArray.get(i).addAll(tempTradables);

                i++;
            }

            // Get all intersecting tradeables of all environments in
            // current environment group
            if (tradableArray.isEmpty() == false) {

                finalTradables.addAll(tradableArray.get(0));

                for (final ArrayList<ITradable> tradeablelist : tradableArray) {
                    finalTradables.retainAll(tradeablelist);
                }
            }

            // Calculate and set new value of tradable
            for (final ITradable tradable : finalTradables) {
                if (TradableHandler.getInstance().getActiveTradables().containsKey(tradable)) {
                    updateTradable(environmentGroup, tradable);
                }
            }
        }
    }

    /**
     * Loads all environments from directory
     * {@link de.stock.settings.Settings_Deserializer#PATH_ENVIRONMENTS} and
     * stores them in {@link #getEnvironments()}
     * 
     * @return
     *         number of loaded environments
     */
    public Integer loadEnvironments() {
        return Deserializer.deserialize(Settings_Deserializer.TYPE_ENVIRONMENT);
    }

    /**
     * Register environment at EnvironmentHandler
     * 
     * @param environment
     *            Environment to register
     * 
     * @return {@code true} if environment wasn't already registered
     */
    public boolean register(final IEnvironment environment) {
        return environments.add(environment);
    }

    public void setEnvironments(final HashSet<IEnvironment> environments) {
        this.environments = environments;
    }

    /**
     * Updates {@code tradable} value with properties of
     * {@code environemntGroup}
     * 
     * @param environmentGroup
     *            method gets properties from this object
     * @param tradable
     *            the tradable to influence
     */
    private void updateTradable(final EnvironmentGroup environmentGroup, final ITradable tradable) {
        // Get current value of tradable
        final Double currentValue = TradableHandler.getInstance().getActiveTradables()
                .get(tradable);
        // Get percentage
        final Double percentage = Utils.random(environmentGroup.getInfluenceBottomLimit(),
                environmentGroup.getInfluenceTopLimit());
        // Get sign
        final Integer sign = (environmentGroup.influenceIsPositive()) ? 1 : -1;
        // Calculate new value
        Double newValue = (currentValue / 100 * percentage) * sign;
        newValue += currentValue;
        // Update
        TradableHandler.getInstance().getActiveTradables().put(tradable, newValue);
        tradable.updateValue(newValue);
    }
}
