package de.stock.tradable;

import java.util.HashSet;

import de.stock.environment.IEnvironment;
import de.stock.settings.Settings_Tradable;
import de.stock.utils.Utils;

/**
 * See {@link de.stock.tradable.ITradable ITradable} for further information
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Tradable implements ITradable {

    private String                name;
    private String                description;
    private Double                value;
    /**
     * Range for value initialization
     */
    private Double                initTopBound;
    private Double                initBottomBound;
    /**
     * Range for every (not influenced by events) round update
     */
    private Double                influenceTopBound;
    private Double                influenceBottomBound;
    /**
     * Maximum number of shares
     */
    private Integer               maxShares;
    /**
     * Minimum number of shares
     */
    private Integer               minShares;
    /**
     * Available number of shares of this tradable
     */
    private Integer               shares;
    /**
     * Tradable is part of the Environment
     */
    private HashSet<IEnvironment> partOf;

    protected Tradable() {
        name = new String("");
        description = new String("");
        value = new Double(0);
        initTopBound = new Double(0);
        initBottomBound = new Double(0);
        influenceBottomBound = new Double(0);
        influenceTopBound = new Double(0);
        maxShares = new Integer(0);
        shares = new Integer(0);
        partOf = new HashSet<IEnvironment>();
    }

    @Override
    public void decShares(final Integer value) {
        shares -= value;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public Double getInfluenceBottomBound() {
        return influenceBottomBound;
    }

    @Override
    public Double getInfluenceTopBound() {
        return influenceTopBound;
    }

    @Override
    public Double getInitBottomBound() {

        return initBottomBound;
    }

    @Override
    public Double getInitTopBound() {

        return initTopBound;
    }

    @Override
    public Integer getMaxShares() {
        return maxShares;
    }

    @Override
    public Integer getMinShares() {
        return minShares;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public HashSet<IEnvironment> getPartOf() {

        return partOf;
    }

    @Override
    public Integer getShares() {
        return shares;
    }

    @Override
    public Double getValue() {

        return value;
    }

    @Override
    public void incShares(final Integer value) {
        shares += value;
    }

    /**
     * Initialize number of shares using {@link #getMaxShares() number of
     * maximum} and {@link #getMinShares() number of minimum shares}
     * 
     * @return Returns the number of shares
     */
    @Override
    public Integer initializeShares() {
        final Integer value = Utils.random(getMinShares(), getMaxShares());
        setShares(value);
        return value;
    }

    /**
     * Initializes value of tradable using {@link #getInitBottomBound()} and
     * {@link #getInitTopBound()}
     * 
     * @return Returns the initial value
     */
    @Override
    public Double initializeValue() {
        final Double value = Utils.random(initBottomBound, initTopBound);
        setValue(value);
        return value;
    }

    @Override
    /**
     * See {@link #registerAtEnvironment(IEnvironment)}
     * 
     * @param environment
     *            tradable gets registered at this environment
     */
    public void linkToEnvironment(final IEnvironment environment) {
        registerAtEnvironment(environment);
    }

    @Override
    /**
     * Register tradable at {@link de.stock.environment.Environment
     * environment}
     * 
     * @param environment
     *            tradable gets registered at this environment
     */
    public void registerAtEnvironment(final IEnvironment environment) {
        // Register at environment
        environment.getTradables().add(this);
        // Save environment in object
        getPartOf().add(environment);
    }

    @Override
    public void setDescription(final String description) {

        this.description = description;
    }

    @Override
    public void setInfluenceBottomBound(final Double influenceBottomBound) {
        this.influenceBottomBound = influenceBottomBound;
    }

    @Override
    public void setInfluenceTopBound(final Double influenceTopBound) {
        this.influenceTopBound = influenceTopBound;
    }

    @Override
    public void setInitBottomBound(final Double initBottomBound) {

        this.initBottomBound = initBottomBound;
    }

    @Override
    public void setInitTopBound(final Double initTopBound) {

        this.initTopBound = initTopBound;
    }

    @Override
    public void setMaxShares(final Integer maxShares) {
        this.maxShares = maxShares;
    }

    @Override
    public void setMinShares(final Integer minShares) {
        this.minShares = minShares;
    }

    @Override
    public void setName(final String name) {

        this.name = name;
    }

    @Override
    public void setPartOf(final HashSet<IEnvironment> partOf) {

        this.partOf = partOf;
    }

    @Override
    public void setShares(final Integer shares) {
        this.shares = shares;
    }

    @Override
    public void setValue(final Double value) {

        this.value = value;
    }

    /**
     * Updates value (double, not percent) of tradable according to random<br>
     * value in between {@link #getInfluenceBottomBound()} and
     * {@link #setInfluenceTopBound(Double)}<br>
     * The sign of the value is random, too.
     * {@link de.stock.settings.Settings_Tradable#SIGN_NEGATIVE_BOUND}
     * 
     * @return new value of tradable
     */
    @Override
    public Double updateValue() {

        final Integer sign = (Utils.random(0, 100) > Settings_Tradable.SIGN_NEGATIVE_BOUND) ? 1
                : -1;
        Double newValue = Utils.random(getInfluenceBottomBound(), getInfluenceTopBound()) * sign;
        newValue = getValue().doubleValue() + newValue;
        if (newValue <= 0) {
            newValue = Utils.random(Settings_Tradable.RESET_BOTTOM_LIMIT,
                    Settings_Tradable.RESET_TOP_LIMIT);
        }

        setValue(newValue);

        return newValue;
    }

    /**
     * Sets value to {@code value}
     * 
     * @param value
     *            new value
     */
    @Override
    public void updateValue(final Double value) {

        setValue(value);
    }
}
