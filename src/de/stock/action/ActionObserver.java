package de.stock.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.stock.deserializer.Deserializer;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.level.ILevel;
import de.stock.level.ILevelPack;
import de.stock.level.LevelDecorator;
import de.stock.level.LevelPackHandler;
import de.stock.settings.Settings_Deserializer;
import de.stock.settings.Settings_Event;
import de.stock.settings.Settings_Output;
import de.stock.tradeable.ITradeable;
import de.stock.tradeable.TradeableHandler;
import de.stock.utils.Printer;
import de.stock.utils.Priority;
import de.stock.utils.Utils;

/**
 * The action observer holds all {@link de.stock.event.types.MainEvent main
 * events}, currently active {@link de.stock.event.Event events} and
 * currently active {@link de.stock.level.Level levels}.<br>
 * <br>
 * It <b>does not</b> hold the {@link de.stock.event.types.SubEvent sub events}
 * as these can be called by other events
 * only<br>
 * <br>
 * The action observer iterates every round over:<br>
 * <br>
 * - all (<u>not running</u>) <b>main events</b> to check if they are ready to
 * be executed and executes them if time has come.<br>
 * <br>
 * - all <b>running events</b> and checks if their days are over.<br>
 * &nbsp;&nbsp;If their days are over it gets a successor event if available,
 * starts this event and removes the old one from the list of running events.<br>
 * <br>
 * - all <b>running levels</b> and checks if their goal is reached<br>
 * &nbsp;&nbsp;If their goal is reached it gives the user the prize/shows the
 * award and gets the next level (from same stage or next stage) if available
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class ActionObserver {

    private static ActionObserver instance = null;

    public static ActionObserver getInstance() {
        if (instance == null) {
            instance = new ActionObserver();
        }
        return instance;
    }

    private ArrayList<MainEvent>    mainEvents;
    private ArrayList<ILevel>       activeLevels;

    /**
     * All active events with their remaining rounds
     */
    private HashMap<Event, Integer> activeEvents;

    public ActionObserver() {
        mainEvents = new ArrayList<MainEvent>();
        activeLevels = new ArrayList<ILevel>();
        activeEvents = new HashMap<Event, Integer>();
    }

    /**
     * Decrease number of running events (overall and priority specific)
     * 
     * @param event
     *            the event which got stopped
     */
    private void decRunningEvents(final Event event) {
        final Priority prio = event.getPriority();
        if (prio == Priority.LOW) {
            Settings_Event.RUNNING_EVENTS_LOW--;
        } else if (prio == Priority.MID) {
            Settings_Event.RUNNING_EVENTS_MID--;
        } else if (prio == Priority.HIGH) {
            Settings_Event.RUNNING_EVENTS_HIGH--;
        }

        Settings_Event.RUNNING_EVENTS_ALL--;
    }

    public HashMap<Event, Integer> getActiveEvents() {
        return activeEvents;
    }

    public ArrayList<ILevel> getActiveLevels() {
        return activeLevels;
    }

    public ArrayList<MainEvent> getMainEvents() {
        return mainEvents;
    }

    /**
     * Increase number of running events (overall and priority specific)
     * 
     * @param event
     *            the event which got started
     */
    private void incRunningEvents(final Event event) {
        final Priority prio = event.getPriority();
        if (prio == Priority.LOW) {
            Settings_Event.RUNNING_EVENTS_LOW++;
        } else if (prio == Priority.MID) {
            Settings_Event.RUNNING_EVENTS_MID++;
        } else if (prio == Priority.HIGH) {
            Settings_Event.RUNNING_EVENTS_HIGH++;
        }

        Settings_Event.RUNNING_EVENTS_ALL++;
    }

    /**
     * Iterates over active events and chooses next if time is reached
     */
    public void iterateActiveEvents() {
        final HashMap<Event, Integer> activeEvents = getActiveEvents();
        // This map replaces the old activeEvents map at the end of this method
        final HashMap<Event, Integer> newActiveEvents = new HashMap<Event, Integer>();

        Integer remainingRounds;

        for (final Entry<Event, Integer> entry : activeEvents.entrySet()) {

            // Influence environment groups of event
            entry.getKey().influenceEnvironments();

            // Remove one round
            entry.setValue(entry.getValue() - 1);
            remainingRounds = entry.getValue();

            if (remainingRounds <= 0) {
                // Event is over

                decRunningEvents(entry.getKey());

                // Get successor
                final Event successor = (Event) entry.getKey().getSuccessor();

                if (successor != null) {

                    // Calculate remaining rounds of successor
                    remainingRounds = Utils.random(successor.getRoundsBottomBound(),
                            successor.getRoundsTopBound());
                    // Add successor to new map
                    newActiveEvents.put(successor, remainingRounds);

                    Printer.println(Settings_Output.OUT_MSG, 0, "Event " + successor.getName()
                            + " started", successor.getDescription());
                }

                // Passed event is removed by not adding it to the
                // newActiveEvents map

            } else {

                // Save event in new map with decreased rounds
                newActiveEvents.put(entry.getKey(), remainingRounds);
            }
        }
        setActiveEvents(newActiveEvents);
    }

    /**
     * Iterates over active levels and chooses next if time is reached<br>
     * <br>
     * Adds/Removes level (and/or level pack) specific content (events and
     * tradeables) from event observer or tradeable handler if level (and/or
     * level pack) got added or removed
     */
    public void iterateActiveLevels() {

        final ArrayList<ILevel> activeLevels = getActiveLevels();
        final ArrayList<ILevel> newActiveLevels = new ArrayList<ILevel>();

        for (final ILevel level : activeLevels) {
            if (level.hasPassedLevel()) {

                Printer.println(Settings_Output.OUT_LEVEL, 0, "Level finished", "You finished the level " + level.getName());
                
                level.conferAward();

                IAction successor = level.getSuccessor();

                // Remove level specific content (events and
                // tradeables) of passed level
                remLvlSpfcContent(level);

                if (successor != null) {

                    // Level pack has next level
                    newActiveLevels.add((LevelDecorator) successor);

                } else {

                    remLvlPackSpfcContent(level);

                    // Get next level pack
                    final ILevelPack levelPack = LevelPackHandler.getInstance().getNextStageLevel();
                    if (levelPack == null) {
                        // Last level stage finished
                        Printer.print(Settings_Output.OUT_INFO, 0, "Last level pack reached",
                                "Last level pack of game reached");
                        continue;
                    }

                    // Get next level from level pack
                    successor = levelPack.getStartLevel();
                    if (successor == null) {
                        Printer.print(Settings_Output.OUT_INFO, 0, "Last level reached",
                                "Last level of " + levelPack.getName() + " reached");
                        continue;
                    }
                    newActiveLevels.add((LevelDecorator) successor);

                    regLvlPackSpfcContent(levelPack);
                }

                regLvlSpfcContent((ILevel) successor);
            } else {
                newActiveLevels.add(level);
            }
        }
        setActiveLevels(newActiveLevels);
    }

    /**
     * Iterates over main event array and executes event if time is reached<br>
     * <br>
     * 
     */
    public void iterateMainEvents() {

        ArrayList<MainEvent> mainEvents;
        Event mainEvent;

        // Stop if overall event limit is reached
        if (Settings_Event.EVENT_LIMIT_REACHED_ALL == true) {
            return;
        }

        mainEvents = getMainEvents();

        // Shuffle every round
        Utils.shuffle(mainEvents);

        for (int i = 0; i < mainEvents.size(); i++) {
            mainEvent = mainEvents.get(i);

            // Skip event if it is already running
            if (activeEvents.containsKey(mainEvent)) {
                continue;
            }

            // Skip event if priority specific limit is reached
            if ((mainEvent).getPriority() == Priority.HIGH
                    && Settings_Event.EVENT_LIMIT_REACHED_HIGH == true) {
                continue;
            } else if ((mainEvent).getPriority() == Priority.MID
                    && Settings_Event.EVENT_LIMIT_REACHED_MID == true) {
                continue;
            } else if ((mainEvent).getPriority() == Priority.LOW
                    && Settings_Event.EVENT_LIMIT_REACHED_LOW == true) {
                continue;
            }

            ((MainEvent) mainEvent).updateIndex();

            // Execute event if time has come
            if (((MainEvent) mainEvent).execute()) {

                // Reset index to avoid multiple executions in a row
                ((MainEvent) mainEvent).initializeIndex();

                final Integer rounds = Utils.random((mainEvent).getRoundsBottomBound(),
                        (mainEvent).getRoundsTopBound());

                // Influence other objects once
                // i.e. reduce player money
                (mainEvent).influenceObjects();

                // Add event to running events
                activeEvents.put(mainEvent, rounds);

                // Increase number of running events (overall and priority
                // specific)
                incRunningEvents(mainEvent);

                setEventLimitFlags();

                Printer.println(Settings_Output.OUT_MSG, 0, "Event " + mainEvent.getName()
                        + " started", mainEvent.getDescription());
            }
        }
    }

    /**
     * Keeps balance in event system Some kind of the heart of this program
     * 
     * TODO: Must be placed somewhere
     */
    public void keepBalance() {

        // TODO: IMPLEMENT! BE CAREFUL, YAW!
    }

    /**
     * Loads all events from directory
     * {@link de.stock.settings.Settings_Deserializer#PATH_EVENTS} and stores
     * them in {@link #getMainEvents()}
     * 
     * @return
     *         number of loaded events
     */
    public Integer loadEvents() {
        return Deserializer.deserialize(Settings_Deserializer.TYPE_EVENT);
    }

    /**
     * Registers event at event observer
     * 
     * @param action
     *            Event to register
     */
    public void register(final IAction action) {

        if (action instanceof MainEvent) {
            registerEvent((MainEvent) action);
        } else if (action instanceof LevelDecorator) {
            registerLevel((LevelDecorator) action);
        }
    }

    /**
     * Registers main event at event observer
     * 
     * @param event
     *            Event to register
     */
    public void registerEvent(final Event event) {
        if (event instanceof MainEvent) {
            ((MainEvent) event).initializeIndex();
        }

        if (mainEvents.contains(event)) {
            return;
        }

        mainEvents.add((MainEvent) event);
    }

    /**
     * Registers level at event observer
     * 
     * @param level
     *            level to register
     */
    public void registerLevel(final ILevel level) {

        activeLevels.add(level);

        // Add all (not yet added) events of level
        if (level.getEvents() != null) {
            for (final Event event : level.getEvents()) {
                if (mainEvents.contains(event) == false) {
                    mainEvents.add((MainEvent) event);
                }
            }
        }

        // Register all tradeables
        if (level.getTradeables() != null) {
            for (final ITradeable tradeable : level.getTradeables()) {
                TradeableHandler.getInstance().register(tradeable);
            }
        }
    }

    /**
     * Register level pack specifc content (events and tradeables)
     * 
     * @param levelPack
     *            the level pack to get the content from
     */
    private void regLvlPackSpfcContent(final ILevelPack levelPack) {
        // Register level pack specific events
        if (levelPack.getEvents() != null) {
            for (final MainEvent event : levelPack.getEvents()) {
                ActionObserver.getInstance().registerEvent(event);
            }
        }

        if (levelPack.getTradeables() != null) {
            // Register level pack specific tradeables to active tradeables
            for (final ITradeable tradeable : levelPack.getTradeables()) {
                TradeableHandler.getInstance().registerActiveTradeable(tradeable);
            }
        }
    }

    /**
     * Register level specifc content (events and tradeables)
     * 
     * @param level
     *            the level to get the content from
     */
    private void regLvlSpfcContent(final ILevel level) {
        // Register level specific events
        if (level.getEvents() != null) {
            for (final MainEvent event : level.getEvents()) {
                registerEvent(event);
            }
        }

        // Register level specific tradeables
        if (level.getTradeables() != null) {
            for (final ITradeable tradeable : level.getTradeables()) {
                TradeableHandler.getInstance().register(tradeable);
            }
        }
    }

    /**
     * Remove level pack specific content (events and tradeables)
     * 
     * @param level
     *            the level to get the level pack from
     */
    private void remLvlPackSpfcContent(final ILevel level) {
        // Remove level pack specific events
        // (Running events will run till their end)
        if (level.getLevelPack() != null && level.getLevelPack().getEvents() != null) {
            getMainEvents().removeAll(level.getLevelPack().getEvents());
        }

        // Remove level pack specific tradeables
        // TODO: If player owns one or more stocks of a tradeable that will be deleted ask him if he wants to sell or keep them!
        if (level.getLevelPack() != null && level.getLevelPack().getTradeables() != null) {
            for (final ITradeable tradeable : level.getLevelPack().getTradeables()) {
                /**
                 * Pseudo - Not tested:
                 * 
                 * if(Player.getTradeables().containsKey(tradeable)) {
                 *     Ask player to sell or keep tradeables
                 * }
                 * 
                 */
                TradeableHandler.getInstance().getActiveTradeables().remove(tradeable);
            }
        }
    }

    /**
     * Remove level specific content (events and tradeables)
     * 
     * @param level
     *            the level to get the content from
     */
    private void remLvlSpfcContent(final ILevel level) {
        // Remove level specific events of passed level
        // (Running events will run till their end)
        if (level.getEvents() != null) {
            getMainEvents().removeAll(level.getEvents());
        }

        // Remove level specific tradeables of passed level
        // TODO: If player owns one or more stocks of a tradeable that will be deleted ask him if he wants to sell or keep them!
        if (level.getTradeables() != null) {
            for (final ITradeable tradeable : level.getTradeables()) {
                /**
                 * Pseudo - Not tested:
                 * 
                 * if(Player.getTradeables().containsKey(tradeable)) {
                 *     Ask player to sell or keep tradeables
                 * }
                 * 
                 */
                TradeableHandler.getInstance().getActiveTradeables().remove(tradeable);
            }
        }
    }

    public void setActiveEvents(final HashMap<Event, Integer> activeEvents) {
        this.activeEvents = activeEvents;
    }

    public void setActiveLevels(final ArrayList<ILevel> newActiveLevels) {
        activeLevels = newActiveLevels;
    }

    /**
     * Set event limit flags
     */
    private void setEventLimitFlags() {
        // Set priority specific limit flags
        Settings_Event.EVENT_LIMIT_REACHED_LOW = (Settings_Event.RUNNING_EVENTS_LOW >= Settings_Event.MAX_RUNNING_EVENTS_LOW) ? true
                : false;

        Settings_Event.EVENT_LIMIT_REACHED_MID = (Settings_Event.RUNNING_EVENTS_MID >= Settings_Event.MAX_RUNNING_EVENTS_MID) ? true
                : false;

        Settings_Event.EVENT_LIMIT_REACHED_HIGH = (Settings_Event.RUNNING_EVENTS_HIGH >= Settings_Event.MAX_RUNNING_EVENTS_HIGH) ? true
                : false;

        // Set overall limit flags
        Settings_Event.EVENT_LIMIT_REACHED_ALL = (Settings_Event.RUNNING_EVENTS_ALL >= Settings_Event.MAX_RUNNING_EVENTS_ALL) ? true
                : false;
    }

    public void setMainEvents(final ArrayList<MainEvent> arrayList) {
        mainEvents = arrayList;
    }
}
