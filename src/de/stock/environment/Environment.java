package de.stock.environment;

import java.util.HashSet;

import de.stock.environment.types.Area;
import de.stock.environment.types.Group;
import de.stock.environment.types.Location;
import de.stock.tradeable.ITradeable;

/**
 * See {@link de.stock.environment.IEnvironment IEnvironment} for further
 * information
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Environment implements IEnvironment {

    private String                      name;
    private String                      description;

    /**
     * Current environment influences IEnvironment
     */
    private final HashSet<IEnvironment> linkedEnvironments;
    /**
     * Current environment influences ITradeable
     */
    private final HashSet<ITradeable>   tradeables;

    protected Environment() {
        name = new String("");
        description = new String("");
        linkedEnvironments = new HashSet<IEnvironment>();
        tradeables = new HashSet<ITradeable>();
    }

    @Override
    public String getDescription() {

        return description;
    }

    /**
     * Returns all directly available and linked by other enviroments influenced
     * tradeables
     * 
     * @return Returns all influenced tradeables of current environment
     */
    @Override
    public HashSet<ITradeable> getInfluencedTradeables() {

        final HashSet<ITradeable> affectedTradeables = new HashSet<ITradeable>();

        // Add direct available tradeables to map
        affectedTradeables.addAll(tradeables);
        // Add all affected tradeables of all linked environments recursively
        for (final IEnvironment environment : linkedEnvironments) {
            affectedTradeables.addAll(environment.getInfluencedTradeables());
        }

        return affectedTradeables;
    }

    @Override
    public HashSet<IEnvironment> getLinkedEnvironments() {

        return linkedEnvironments;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public HashSet<ITradeable> getTradeables() {
        return tradeables;
    }

    /**
     * Links environment with percentage Influence<br>
     * <br>
     * Environment can link to environment of same type (class) or one layer
     * above<br>
     * <br>
     * Order from lower to upper:<br>
     * Group ---> Area ---> Location<br>
     * 
     * @param environment
     *            Environment that gets linked
     * @return true if it was allowed to link
     */
    @Override
    public boolean linkEnvironment(final IEnvironment environment) {

        if (environment == this) {
            return false;
        }

        boolean allowed = false;

        // Check if its allowed to link environment
        if (this instanceof Location && (environment instanceof Location)) {
            allowed = true;
        } else if (this instanceof Area
                && (environment instanceof Location || environment instanceof Area)) {
            allowed = true;
        } else if (this instanceof Group
                && (environment instanceof Area || environment instanceof Group)) {
            allowed = true;
        }

        if (allowed) {
            linkedEnvironments.add(environment);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Register tradeable which is part of the environment
     * 
     * @param tradeable
     *            Tradeable that gets linked
     */
    @Override
    public void registerTradeable(final ITradeable tradeable) {
        tradeable.registerAtEnvironment(this);
    }

    @Override
    public void setDescription(final String description) {

        this.description = description;
    }

    @Override
    public void setName(final String name) {

        this.name = name;
    }
}
