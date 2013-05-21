package de.stock.settings;

/**
 * Provides settings for the {@link de.stock.event.Event event} system
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Settings_Event {

    /**
     * Stop executing new events if this flag is true
     */
    public static boolean       EVENT_LIMIT_REACHED_ALL  = false;

    /**
     * Number of maximum running events at the same time
     */
    public static final Integer MAX_RUNNING_EVENTS_ALL   = 8;

    /**
     * Number of maximum low-priority running events at the same time
     */
    public static final Integer MAX_RUNNING_EVENTS_LOW   = 4;
    /**
     * Number of maximum mid-priority running events at the same time
     */
    public static final Integer MAX_RUNNING_EVENTS_MID   = 2;
    /**
     * Number of maximum high-priority running events at the same time
     */
    public static final Integer MAX_RUNNING_EVENTS_HIGH  = 2;

    /**
     * Stop executing new events of low priority if this flag is true
     */
    public static boolean       EVENT_LIMIT_REACHED_LOW  = false;
    /**
     * Stop executing new events of mid priority if this flag is true
     */
    public static boolean       EVENT_LIMIT_REACHED_MID  = false;
    /**
     * Stop executing new events of high priority if this flag is true
     */
    public static boolean       EVENT_LIMIT_REACHED_HIGH = false;

    /**
     * Number of overall running events
     */
    public static Integer       RUNNING_EVENTS_ALL       = 0;
    /**
     * Number of low-priority running events
     */
    public static Integer       RUNNING_EVENTS_LOW       = 0;
    /**
     * Number of mid-priority running events
     */
    public static Integer       RUNNING_EVENTS_MID       = 0;
    /**
     * Number of high-priority running events
     */
    public static Integer       RUNNING_EVENTS_HIGH      = 0;

    /**
     * Used in {@link de.stock.event.types.MainEvent#updateIndex()
     * MainEvent.updateIndex()} to check whether sign is positive or negative<br>
     * <br>
     * Random between 0 and 100 and if value is <= SIGN_NEGATIVE_BOUND sign is
     * negative
     */
    public static final Integer SIGN_NEGATIVE_BOUND      = 35;

    /**
     * Used in {@link de.stock.event.types.MainEvent#execute()
     * MainEvent.execute()} to check whether the event should be executed or not<br>
     * <br>
     * If {@link de.stock.event.types.MainEvent#hasExecBoundPassed()
     * MainEvent.hasExecBoundPassed()} returns true the event gets executed if
     * Utils.random(0, 100) is greater than this variable
     */
    public static Integer       EXECUTE_BOUND_RATE       = 35;

}
