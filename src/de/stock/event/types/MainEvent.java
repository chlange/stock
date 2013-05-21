package de.stock.event.types;

import de.stock.event.Event;
import de.stock.settings.Settings_Event;
import de.stock.utils.Utils;

/**
 * See {@link de.stock.event.Event Event} for information about events<br>
 * <br>
 * The special about a {@link de.stock.event.types.MainEvent MainEvent} is that
 * it can start on his own (or by another event)
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class MainEvent extends Event {

    /**
     * Range top by how many points the value can be influenced<br>
     * <br>
     * Used by {@link #updateIndex()}
     */
    private Integer influenceTopBound;
    /**
     * Range bottom by how many points the value can be influenced<br>
     * <br>
     * Used by {@link #updateIndex()}
     */
    private Integer influenceBottomBound;
    /**
     * Top limit of initial index value<br>
     * <br>
     * Used by {@link #initializeIndex()}
     */
    private Integer indexInitTopBound;
    /**
     * Bottom limit of initial index value<br>
     * <br>
     * Used by {@link #initializeIndex()}
     */
    private Integer indexInitBottomBound;
    /**
     * Current index of probability that event could be executed
     */
    private Integer index;
    /**
     * The index value won't climb above this limit
     */
    private Integer indexMaximum;
    /**
     * The index must pass this bound to be - probably - executed
     */
    private Integer executionBound;

    public MainEvent() {
        influenceBottomBound = new Integer(0);
        influenceTopBound = new Integer(0);
        index = new Integer(0);
        indexMaximum = new Integer(0);
        executionBound = new Integer(0);
    }

    public MainEvent(final Integer affectTopBound, final Integer affectBottomBound,
            final Integer index, final Integer indexMaximum, final Integer executionsBound) {
        influenceBottomBound = affectTopBound;
        influenceTopBound = affectBottomBound;
        this.index = index;
        this.indexMaximum = indexMaximum;
        executionBound = executionsBound;
    }

    /**
     * Returns true if event should be executed
     * 
     * @return {@code true} if event should be executed
     */
    public boolean execute() {
        return (hasExecBoundPassed() && Utils.random(0, 100) >= Settings_Event.EXECUTE_BOUND_RATE) ? true
                : false;
    }

    public Integer getExecutionBound() {
        return executionBound;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getIndexInitBottomBound() {
        return indexInitBottomBound;
    }

    public Integer getIndexInitTopBound() {
        return indexInitTopBound;
    }

    public Integer getIndexMaximum() {
        return indexMaximum;
    }

    public Integer getIndexTopBound() {

        return indexMaximum;
    }

    public Integer getInfluenceBottomBound() {
        return influenceBottomBound;
    }

    public Integer getInfluenceTopBound() {
        return influenceTopBound;
    }

    /**
     * Returns true if execution bound is passed
     * 
     * @return {@code true} if execution bound is passed
     */
    public boolean hasExecBoundPassed() {
        return (getIndex() >= getExecutionBound()) ? true : false;
    }

    /**
     * Initializes the index
     * 
     * @return
     *         the index
     */
    public Integer initializeIndex() {
        final Integer value = Utils.random(getIndexInitBottomBound(), getIndexInitTopBound());
        setIndex(value);
        return value;
    }

    public void setExecutionBound(final Integer executionBound) {
        this.executionBound = executionBound;
    }

    public void setIndex(final Integer index) {
        this.index = index;
    }

    public void setIndexInitBottomBound(final Integer indexInitBottomBound) {
        this.indexInitBottomBound = indexInitBottomBound;
    }

    public void setIndexInitTopBound(final Integer indexInitTopBound) {
        this.indexInitTopBound = indexInitTopBound;
    }

    public void setIndexMaximum(final Integer indexMaximum) {
        this.indexMaximum = indexMaximum;
    }

    public void setIndexTopBound(final Integer indexTopBound) {

        indexMaximum = indexTopBound;
    }

    public void setInfluenceBottomBound(final Integer influenceBottomBound) {
        this.influenceBottomBound = influenceBottomBound;
    }

    public void setInfluenceTopBound(final Integer influenceTopBound) {
        this.influenceTopBound = influenceTopBound;
    }

    /**
     * Updates the index
     */
    public void updateIndex() {

        // Reset index if it reaches 0
        if (getIndex() == 0) {
            initializeIndex();
            return;
        }

        Integer sign;
        Integer value;
        Integer newIndex;

        sign = (Utils.random(0, 100) > Settings_Event.SIGN_NEGATIVE_BOUND) ? 1 : -1;
        value = (Utils.random(getInfluenceBottomBound(), getInfluenceTopBound()));

        newIndex = getIndex() + (value * sign);

        if (newIndex < 0) {
            setIndex(0);
        } else if (getIndexTopBound() != null && newIndex > getIndexTopBound()) {
            setIndex(getIndexTopBound());
        } else {
            setIndex(newIndex);
        }
    }
}
