package de.stock.event;

import java.util.ArrayList;
import java.util.HashMap;

import de.stock.action.Action;
import de.stock.environment.Environment;
import de.stock.environment.EnvironmentGroup;
import de.stock.environment.EnvironmentHandler;
import de.stock.settings.Settings_Influencable;
import de.stock.utils.Priority;

/**
 * Base class of events<br>
 * <br>
 * The event system knows two real types of events which start and stop without
 * the players influence (they run a number of rounds)<br>
 * <br>
 * - {@link de.stock.event.types.MainEvent MainEvent} can start on his own or by
 * another event.<br>
 * <br>
 * - {@link de.stock.event.types.SubEvent SubEvent} can be started by another
 * event only.<br>
 * <br>
 * (These events can influence {@link de.stock.environment.IEnvironment
 * environments}, {@link de.stock.tradable.ITradable tradables} (through
 * {@link de.stock.environment.EnvironmentGroup environment groups}) and
 * {@link de.stock.settings.Settings_Influencable other objects})<br>
 * <br>
 * Event developers <b>must</b> instantiate
 * {@link de.stock.event.types.MainEvent MainEvent} or
 * {@link de.stock.event.types.SubEvent SubEvent}<br>
 * They <b>must not</b> instantiate this class
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Event extends Action {

    /**
     * The priority of this event<br>
     * <br>
     * See {@link de.stock.utils.Priority Priority} and
     * {@link de.stock.settings.Settings_Event Settings_Event} for more
     * information on priority
     */
    private Priority                    priority;
    /**
     * Top limit how many rounds this event stays<br>
     * <br>
     * Used by {@link de.stock.action.ActionObserver#iterateMainEvents()} if a
     * new event starts to calculate remaining rounds (in connection with
     * {@link #roundsBottomBound})
     */
    private Integer                     roundsTopBound;
    /**
     * Bottom limit how many rounds this event stays<br>
     * <br>
     * Used by {@link de.stock.action.ActionObserver#iterateMainEvents()} if a
     * new event starts to calculate remaining rounds (in connection with
     * {@link #roundsTopBound})
     */
    private Integer                     roundsBottomBound;
    /**
     * Environment groups that get influenced by this event
     */
    private ArrayList<EnvironmentGroup> environmentGroups;
    /**
     * Influence objects, which are specified in
     * {@link de.stock.settings.Settings_Influencable}, by {@code Long} value
     * once the event starts<br>
     * <br>
     * <b>Example:</b>
     * The entry &lt;Settings_Influencable.PLAYER_MONEY, -100>
     * will substract 100 from players money
     */
    private HashMap<Integer, Long>      influencedObjects;

    public Event() {
        roundsBottomBound = new Integer(0);
        roundsTopBound = new Integer(0);
        environmentGroups = new ArrayList<EnvironmentGroup>();
        influencedObjects = new HashMap<Integer, Long>();
    }

    public ArrayList<EnvironmentGroup> getEnvironmentGroups() {
        return environmentGroups;
    }

    public HashMap<Integer, Long> getInfluenceObjects() {
        return influencedObjects;
    }

    public Priority getPriority() {
        return priority;
    }

    public Integer getRoundsBottomBound() {
        return roundsBottomBound;
    }

    public Integer getRoundsTopBound() {
        return roundsTopBound;
    }

    /**
     * Passes influenced environments to EnviromentHandler
     */
    public void influenceEnvironments() {
        EnvironmentHandler.getInstance().influenceEnvironments(environmentGroups);
    }

    /**
     * Influences objects specified in influenceObjects Map
     */
    public void influenceObjects() {
        Settings_Influencable.influence(getInfluenceObjects());
    }

    /**
     * Register a single environment<br>
     * <br>
     * The tradables of this environment get influenced by a random percentage
     * between {@code percentageBottomLimit} and {@code percentageTopLimit}<br>
     * <br>
     * (Gets internally saved as a environment group)<br>
     * 
     * @param environment
     *            Environment to register
     * @param isPositive
     *            Indicates whether the environment lets the tradables raise
     *            (true) or fall (false)
     * @param percentageBottomLimit
     *            Bottom limit of influence in percent
     * @param percentageTopLimit
     *            Top limit of influence in percent
     */
    public void registerEnvironment(final Environment environment, final boolean isPositive,
            final Double percentageBottomLimit, final Double percentageTopLimit) {
        final EnvironmentGroup environmentGroup = new EnvironmentGroup();

        environmentGroup.registerEnvironment(environment);
        environmentGroup.setInfluenceLimit(percentageBottomLimit, percentageTopLimit);
        environmentGroup.setSign(isPositive);

        registerEnvironmentGroup(environmentGroup);
    }

    public void registerEnvironmentGroup(final EnvironmentGroup environmentGroup) {
        environmentGroups.add(environmentGroup);
    }

    public void setEnvironmentGroups(final ArrayList<EnvironmentGroup> environmentGroups) {
        this.environmentGroups = environmentGroups;
    }

    public void setInfluenceObjects(final HashMap<Integer, Long> influenceObjects) {
        influencedObjects = influenceObjects;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public void setRoundsBottomBound(final Integer roundsBottomBound) {
        this.roundsBottomBound = roundsBottomBound;
    }

    public void setRoundsTopBound(final Integer roundsTopBound) {
        this.roundsTopBound = roundsTopBound;
    }
}
