package de.stock.settings;

/**
 * Provides settings for the {@link de.stock.tradeable.Tradeable tradeable}
 * system
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Settings_Tradeable {

    /**
     * Used in {@link de.stock.tradeable.Tradeable#updateValue()
     * Tradeable.updateValue()} to check whether sign is positive or negative<br>
     * <br>
     * Random between 0 and 100 and if value is less or equal than
     * SIGN_NEGATIVE_BOUND sign is negative
     */
    public static final Integer SIGN_NEGATIVE_BOUND = 45;
    /**
     * If a tradeable value falls to or below zero (through
     * {@link de.stock.tradeable.Tradeable#updateValue() updateValue()}) the
     * value gets a
     * little random push within a range of this bottom and this
     * {@link #RESET_TOP_LIMIT top} limit
     */
    public static final Double  RESET_BOTTOM_LIMIT  = 0.1;
    /**
     * If a tradeable value falls to or below zero (through
     * {@link de.stock.tradeable.Tradeable#updateValue() updateValue()}) the
     * value gets a
     * little random push within a range of this top and this
     * {@link #RESET_BOTTOM_LIMIT bottom} limit
     */
    public static final Double  RESET_TOP_LIMIT     = 3.0;
}
