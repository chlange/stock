package de.stock.environment;

import java.util.HashSet;

import de.stock.tradable.ITradable;

/**
 * Interface for environments<br>
 * <br>
 * The environment system is seperated into a three element hierarchy<br>
 * <br>
 * At the very top of the hierarchy is the
 * {@link de.stock.environment.types.Location Location}<br>
 * in the middle is the {@link de.stock.environment.types.Area Area}<br>
 * and at the very bottom is the {@link de.stock.environment.types.Group Group}<br>
 * (Read class headers of those for further information)<br>
 * <br>
 * A environment can link to another environment if it is a part or member of it
 * but it must be of the same type or one layer below in the hierarchy<br>
 * (See {@link #linkEnvironment(IEnvironment)})<br>
 * <br>
 * Environment developers <b>must</b> instantiate
 * {@link de.stock.environment.types.Area Area},
 * {@link de.stock.environment.types.Group Group} or
 * {@link de.stock.environment.types.Location Location}<br>
 * They <b>must not</b> instantiate {@link de.stock.environment.Environment
 * Environment}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public interface IEnvironment {

    public String getDescription();

    /**
     * Adds all directly available and linked by other enviroments affected
     * tradeables recursively
     * 
     * @return Returns all affected tradeables of current environment
     */
    public HashSet<ITradable> getInfluencedTradables();

    public HashSet<IEnvironment> getLinkedEnvironments();

    public String getName();

    public HashSet<ITradable> getTradables();

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
     * @return Returns true if it was allowed to link
     */
    public boolean linkEnvironment(IEnvironment environment);

    /**
     * Register tradable which is part of the environment
     * 
     * @param tradable
     *            Tradable that gets linked
     */
    public void registerTradable(ITradable tradable);

    public void setDescription(String description);

    public void setName(String name);

}