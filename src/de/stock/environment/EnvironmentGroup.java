package de.stock.environment;

import java.util.ArrayList;

/**
 * An {@link de.stock.event.Event event} can influence a single
 * {@link de.stock.tradeable.ITradeable tradeable}, an
 * {@link de.stock.environment.IEnvironment environment} (and therefore the
 * tradeables influenced by the environment) or an environment group which
 * consists of multiple {@link de.stock.environment.IEnvironment environments} <br>
 * <br>
 * The difference between multiple environments and one environment group
 * containing these environments is that all tradeables of the multiple
 * environments are influenced and with the environment group the intersecting
 * tradeables of the environments are influenced only <br>
 * <br>
 * Example:<br> {@link de.stock.environment.IEnvironment Environment} E1 influences
 * {@link de.stock.tradeable.ITradeable tradeable} T1 and T2<br>
 * {@link de.stock.environment.IEnvironment Environment} E2 influences
 * {@link de.stock.tradeable.ITradeable tradeable} T2 and T3<br>
 * <br> {@link de.stock.event.Event Event} A with single environments E1 and E2 will
 * influence T1, T2 and T3<br> {@link de.stock.event.Event Event} B with environment
 * group consisting of E1 and E2 will influence T2 only
 * as T2 is the intersecting tradeable of E1 and E2
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class EnvironmentGroup {

    private String                 name;
    /**
     * Top limit in percent
     */
    private Double                 influenceTopLimit;
    /**
     * Bottom limit in percent
     */
    private Double                 influenceBottomLimit;
    /**
     * Share value rises if environment group's influence is positive
     */
    private boolean                influenceIsPositive;
    private ArrayList<Environment> environments;

    public EnvironmentGroup() {
        environments = new ArrayList<Environment>();
        influenceIsPositive = true;
        influenceBottomLimit = null;
        influenceTopLimit = null;
    }

    public ArrayList<Environment> getEnvironments() {

        return environments;
    }

    public Double getInfluenceBottomLimit() {

        return influenceBottomLimit;
    }

    public Double getInfluenceTopLimit() {

        return influenceTopLimit;
    }

    public String getName() {

        return name;
    }

    /**
     * Returns true if the influence is positive (tradeable value rises)
     * 
     * @return true if positive influence
     */
    public boolean influenceIsPositive() {

        return influenceIsPositive;
    }

    /**
     * Register environment at environment group
     * 
     * @param environment
     *            environment to register
     */
    public void registerEnvironment(final Environment environment) {
        environments.add(environment);
    }

    public void setEnvironments(final ArrayList<Environment> environment) {

        environments = environment;
    }

    public void setGroupName(final String groupName) {

        name = groupName;
    }

    /**
     * Sets the bottom limit (in percent)
     * 
     * @param bottomLimit
     *            represents the bottom limit (in percent)
     */
    public void setInfluenceBottomLimit(Double bottomLimit) {
        if (bottomLimit < 0) {
            bottomLimit = 0.0;
        }

        // Switch top and bottom limit if values are wrongly assigned
        if (influenceTopLimit != null && influenceTopLimit < bottomLimit) {
            influenceBottomLimit = influenceTopLimit;
            influenceTopLimit = bottomLimit;
        } else {
            influenceBottomLimit = bottomLimit;
        }
    }

    /**
     * Sets the influence limit (in percent)
     * 
     * @param bottom
     *            represents the bottom limit (in percent)
     * @param top
     *            represents the top limit (in percent)
     */
    public void setInfluenceLimit(final Double bottom, final Double top) {
        setInfluenceTopLimit(top);
        setInfluenceBottomLimit(bottom);
    }

    /**
     * Set influence of environment group negative (tradeable value falls)
     */
    public void setInfluenceNegative() {

        influenceIsPositive = false;
    }

    /**
     * Set influence of environment group positive (tradeable value rises)
     */
    public void setInfluencePositive() {

        influenceIsPositive = true;
    }

    /**
     * Sets the top limit (in percent)
     * 
     * @param topLimit
     *            represents the top limit (in percent)
     */
    public void setInfluenceTopLimit(Double topLimit) {
        if (topLimit < 0) {
            topLimit = 0.0;
        }

        // Switch top and bottom limit if values are wrongly assigned
        if (influenceBottomLimit != null && influenceBottomLimit > topLimit) {
            influenceTopLimit = influenceBottomLimit;
            influenceBottomLimit = topLimit;
        } else {
            influenceTopLimit = topLimit;
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets sign of environment group influence<br>
     * <br>
     * If {@code influenceIsPositive} is true the tradeable values influenced by
     * this
     * group raise otherwise they fall
     * 
     * @param isPositive
     *            indicates whether the sign is positive or not
     */
    public void setSign(final boolean isPositive) {
        if (isPositive) {
            setInfluencePositive();
        } else {
            setInfluenceNegative();
        }
    }
}
