package de.stock.level;

import java.util.ArrayList;

import de.stock.action.Action;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.tradeable.ITradeable;

/**
 * See {@link de.stock.level.ILevel ILevel} for futher information
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Level extends Action implements ILevel {

    /**
     * Set in level pack, too.
     * Shouldn't be set in the level, anyway.
     */
    private Integer               levelStage;
    /**
     * Connection to the current levelpack
     */
    private ILevelPack            levelPack;
    /**
     * Tradeables in this level besides the ones of the
     * {@link de.stock.level.LevelPack level pack}
     */
    private ArrayList<ITradeable> tradeables;
    /**
     * Events in this level besides the ones of the
     * {@link de.stock.level.LevelPack level pack}
     */
    private ArrayList<MainEvent>  events;

    protected Level() {
        tradeables = new ArrayList<ITradeable>();
        events = new ArrayList<MainEvent>();
    }

    /**
     * Wrapper for {@link #registerEvent(Event)}
     * 
     * @param event
     *            the event to add
     */
    @Override
    public void addEvent(final Event event) {
        registerEvent(event);
    }

    /**
     * Wrapper for {@link #registerTradeable(ITradeable)}
     * 
     * @param tradeable
     *            the tradeable to add
     */
    @Override
    public void addTradeable(final ITradeable tradeable) {
        registerTradeable(tradeable);
    }

    /**
     * Confer award on player
     */
    @Override
    public void conferAward() {
    }

    @Override
    public ArrayList<MainEvent> getEvents() {
        return events;
    }

    @Override
    public ILevelPack getLevelPack() {
        return levelPack;
    }

    @Override
    public Integer getLevelStage() {

        return levelStage;
    }

    @Override
    public ArrayList<ITradeable> getTradeables() {
        return tradeables;
    }

    /**
     * Returns true if level goalDescription is reached
     * 
     * @return {@code true} if level goalDescription is reached
     */
    @Override
    public boolean hasPassedLevel() {
        return false;
    }

    @Override
    public void registerEvent(final Event event) {
        // Register tradeable if its not registered already
        if (getEvents().contains(event) == false) {
            getEvents().add((MainEvent) event);
        }
    }

    @Override
    public void registerTradeable(final ITradeable tradeable) {
        // Register tradeable if its not registered already
        if (getTradeables().contains(tradeable) == false) {
            getTradeables().add(tradeable);
        }
    }

    @Override
    public void setEvents(final ArrayList<MainEvent> events) {
        this.events = events;
    }

    @Override
    public void setLevelPack(final ILevelPack levelPack) {
        this.levelPack = levelPack;
    }

    @Override
    public void setLevelStage(final Integer levelStage) {

        this.levelStage = levelStage;
    }

    @Override
    public void setTradeables(final ArrayList<ITradeable> tradeables) {
        this.tradeables = tradeables;
    }
}
