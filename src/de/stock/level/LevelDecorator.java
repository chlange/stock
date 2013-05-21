package de.stock.level;

import java.util.ArrayList;

import de.stock.action.Action;
import de.stock.action.IAction;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.tradeable.ITradeable;

/**
 * See {@link de.stock.level.ILevel ILevel} for futher information
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public abstract class LevelDecorator extends Action implements ILevel, IAction {

    protected final ILevel level;

    public LevelDecorator() {
        level = new Level();
    }

    public LevelDecorator(final ILevel level) {
        if (level == null) {
            this.level = new Level();
        } else {
            this.level = level;
        }
    }

    @Override
    public void addEvent(final Event event) {
        level.addEvent(event);
    }

    @Override
    public void addTradeable(final ITradeable tradeable) {
        level.addTradeable(tradeable);
    }

    /**
     * Confer award on player
     */
    @Override
    public abstract void conferAward();

    @Override
    public ArrayList<MainEvent> getEvents() {
        return level.getEvents();
    }

    @Override
    public ILevelPack getLevelPack() {
        return level.getLevelPack();
    }

    @Override
    public Integer getLevelStage() {
        return level.getLevelStage();
    }

    @Override
    public ArrayList<ITradeable> getTradeables() {
        return level.getTradeables();
    }

    /**
     * Returns true if level goal is reached
     * 
     * @return {@code true} if level goal is reached
     */
    @Override
    public abstract boolean hasPassedLevel();

    @Override
    public void registerEvent(final Event event) {
        level.registerEvent(event);
    }

    @Override
    public void registerTradeable(final ITradeable tradeable) {
        level.registerTradeable(tradeable);
    }

    @Override
    public void setEvents(final ArrayList<MainEvent> events) {
        level.setEvents(events);
    }

    @Override
    public void setLevelPack(final ILevelPack levelPack) {
        level.setLevelPack(levelPack);
    }

    @Override
    public void setLevelStage(final Integer levelStage) {
        level.setLevelStage(levelStage);
    }

    @Override
    public void setTradeables(final ArrayList<ITradeable> tradeables) {
        level.setTradeables(tradeables);
    }
}
