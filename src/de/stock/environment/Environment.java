package de.stock.environment;

import java.util.HashSet;

import de.stock.environment.types.Area;
import de.stock.environment.types.Group;
import de.stock.environment.types.Location;
import de.stock.tradable.ITradable;

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
     * Current environment influences ITradable
     */
    private final HashSet<ITradable>   tradables;

    protected Environment() {
        name = new String("");
        description = new String("");
        linkedEnvironments = new HashSet<IEnvironment>();
        tradables = new HashSet<ITradable>();
    }

    @Override
    public String getDescription() {

        return description;
    }

    /**
     * Returns all directly available and linked by other enviroments influenced
     * tradables
     * 
     * @return Returns all influenced tradables of current environment
     */
    @Override
    public HashSet<ITradable> getInfluencedTradables() {

        final HashSet<ITradable> affectedTradables = new HashSet<ITradable>();

        // Add direct available tradables to map
        affectedTradables.addAll(tradables);
        // Add all affected tradables of all linked environments recursively
        for (final IEnvironment environment : linkedEnvironments) {
            affectedTradables.addAll(environment.getInfluencedTradables());
        }

        return affectedTradables;
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
    public HashSet<ITradable> getTradables() {
        return tradables;
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
     * Register tradable which is part of the environment
     * 
     * @param tradable
     *            Tradable that gets linked
     */
    @Override
    public void registerTradable(final ITradable tradable) {
        tradable.registerAtEnvironment(this);
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
