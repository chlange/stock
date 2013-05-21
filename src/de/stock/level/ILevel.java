package de.stock.level;

import java.util.ArrayList;

import de.stock.action.IAction;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.tradeable.ITradeable;

/**
 * A level, which is part of a {@link de.stock.level.LevelPack LevelPack},
 * consists of several parts:<br>
 * <br>
 * - The <b>level stage</b><br>
 * &nbsp;&nbsp;The player starts at the first level stage and if this level
 * stage is passed he gets in the next stage if available.<br>
 * &nbsp;&nbsp;He chooses <b>one</b> level pack at the beginning of a level
 * stage.<br>
 * &nbsp;&nbsp;If a level is of type <i>normal or award level</i> is indicated
 * through the level stage, see {@link de.stock.settings.Settings_Level
 * Settings_Level}<br>
 * <br>
 * - The <b>successor</b><br>
 * &nbsp;&nbsp;The next level(s) in a level pack<br>
 * &nbsp;&nbsp;A level pack doesn't need to follow a linear order, it can has
 * branches, too.<br>
 * &nbsp;&nbsp;<i>If the method getSuccessor() returns null the level pack is
 * over</i><br>
 * <br>
 * - The <b>events</b><br>
 * &nbsp;&nbsp;A level may have specific events, which are removed (<i>but not
 * stopped if running</i>) from event observer if a level is passed and added if
 * it gets started<br>
 * <br>
 * - The <b>tradeables</b><br>
 * &nbsp;&nbsp;A level may have specific tradeables, which are removed from
 * tradeable handler if a level is passed and added if it gets started<br>
 * <br>
 * - The <b>goal</b><br>
 * &nbsp;&nbsp;The goal to reach to get in the next level<br>
 * <br>
 * - The <b>prize</b><br>
 * &nbsp;&nbsp;The prize the player gets if he passes the level (if available)<br>
 * <br>
 * - The <b>award</b><br>
 * &nbsp;&nbsp;The award the player gets if he passes the level (if available)<br>
 * <br>
 * A {@link de.stock.level.LevelPack LevelPack} <b>development guide</b> can be
 * found at the {@link de.stock.level.LevelPackLoader LevelPackLoader}<br>
 * <br>
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public interface ILevel extends IAction {

    /**
     * Wrapper for {@link #registerEvent(Event)}
     * 
     * @param event
     *            the event to add
     */
    public abstract void addEvent(Event event);

    /**
     * Wrapper for {@link #registerTradeable(ITradeable)}
     * 
     * @param tradeable
     *            the tradeable to add
     */
    public abstract void addTradeable(ITradeable tradeable);

    /**
     * Confer award on player
     */
    public abstract void conferAward();

    public abstract ArrayList<MainEvent> getEvents();

    public abstract ILevelPack getLevelPack();

    public abstract Integer getLevelStage();

    public abstract ArrayList<ITradeable> getTradeables();

    /**
     * Returns true if level goal is reached
     * 
     * @return {@code true} if level goal is reached
     */
    public abstract boolean hasPassedLevel();

    public abstract void registerEvent(Event event);

    public abstract void registerTradeable(ITradeable tradeable);

    public abstract void setEvents(final ArrayList<MainEvent> events);

    public abstract void setLevelPack(final ILevelPack levelPack);

    public abstract void setLevelStage(final Integer levelStage);

    public abstract void setTradeables(final ArrayList<ITradeable> tradeables);
}