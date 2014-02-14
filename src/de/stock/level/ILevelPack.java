package de.stock.level;

import java.util.ArrayList;

import net.xeoh.plugins.base.Plugin;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.tradable.ITradable;

/**
 * The level system knows two types of level packs:<br>
 * <br>
 * - The <b>normal level packs</b> which are grouped in level stages<br>
 * &nbsp;&nbsp;Only one of these can run at the same time!<br>
 * &nbsp;&nbsp;(Level stage 1 and above)<br>
 * <br>
 * - The <b>award level packs</b><br>
 * &nbsp;&nbsp;Multiple of these can run at the same time!<br>
 * &nbsp;&nbsp;(Level stage 0)<br>
 * <br>
 * A level pack - a packet of several {@link de.stock.level.ILevel
 * levels} - which has a name and a description.<br>
 * <br>
 * It may has tradeables which are available and events which can occur over the
 * whole playtime of the level pack.<br>
 * (Every level in a level pack may have specific
 * {@link de.stock.tradable.Tradable tradeables} or
 * {@link de.stock.event.Event events}, too!)<br>
 * <br>
 * The player can choose from these packs at the beginning of the game and if a
 * level stage is passed, too.<br>
 * <br>
 * An example is the (not so serious) level pack "Lemonade" with the following
 * levels:<br>
 * <br>
 * &nbsp;&nbsp;<u>1. Level:</u> Lemonade stand <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Tradeables:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>Lemons and Sugar</i> which are both level pack
 * specific<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Events:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>Rowdies raid your stand -> You lose 50$</i><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>The local store runs out of lemons -> Lemon
 * prices raise</i> <br>
 * <br>
 * &nbsp;&nbsp;<u>2. Level:</u> Lemonade factory<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Tradeables:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>Lemons and Sugar</i> which are both level pack
 * specific and 2. level specific <i>Limes and Saccharin</i><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Events:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>Saccharin declared toxic -> You must pay 5000$
 * for disposal</i><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>You get a new customer -> First contract earns
 * you 15000$</i><br>
 * <br>
 * &nbsp;&nbsp;<u>3. Level:</u> Lemonade concern<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Tradeables:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>Lemons and Sugar</i> which are both level pack
 * specific and 3. level specific <i>Limes, Oranges, Saccharin and Thaumatin</i><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Events:<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>Citrus fruit plague -> All citrus fruit
 * tradeables drop for at least 3 rounds by 10%</i><br>
 * 
 * <br>
 * These level packs are loaded through the
 * {@link de.stock.level.LevelPackLoader LevelPackLoader} <br>
 * <br>
 * A {@link de.stock.level.LevelPack LevelPack} <b>development guide</b> can be
 * found at the {@link de.stock.level.LevelPackLoader LevelPackLoader}<br>
 * <br>
 * (See {@link de.stock.level.ILevel ILevel} for more information on
 * levels)
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public interface ILevelPack extends Plugin {

    /**
     * Add {@code event} to level pack
     * 
     * @param event
     *            event for the whole lifetime of the level pack
     */
    public abstract void addEvent(final Event event);

    public abstract void addFirstLevel(final ILevel myFirstLevel);

    /**
     * Add {@code tradable} to level pack
     * 
     * @param tradable
     *            tradable for the whole lifetime of the level pack
     */
    public abstract void addTradable(final ITradable tradable);

    public abstract String getDescription();

    public abstract ArrayList<MainEvent> getEvents();

    public abstract ArrayList<ILevel> getFirstLevels();

    public abstract Integer getLevelStage();

    public abstract String getName();

    public abstract String getNation();

    /**
     * Get the starting level of the level pack
     * 
     * @return Returns the starting level of the level pack upon success else
     *         null
     */
    public abstract ILevel getStartLevel();

    public abstract ArrayList<ITradable> getTradables();

    public abstract boolean hasOption();

    /**
     * Initializes the level pack - gets called when level pack gets loaded<br>
     * <br>
     * Sets the name, adds the levels, ...<br>
     * <br>
     * See {@link de.stock.level.LevelPackLoader LevelPackLoader} for more
     * information and an example
     */
    public abstract void initialize();

    public abstract boolean isHasOption();

    public abstract boolean isInitialized();

    public abstract boolean isNotInitialized();

    public abstract void setDescription(final String description);

    public abstract void setEvents(final ArrayList<MainEvent> events);

    public abstract void setFirstLevels(final ArrayList<ILevel> firstLevels);

    public abstract void setHasOption(final boolean hasOption);

    public abstract void setInitialized(boolean isInitialized);

    public abstract void setLevelStage(final Integer levelStage);

    public abstract void setName(final String name);

    public abstract void setNation(final String nation);

    public abstract void setTradables(final ArrayList<ITradable> tradables);
}