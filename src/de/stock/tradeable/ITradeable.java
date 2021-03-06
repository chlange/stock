package de.stock.tradeable;

import java.util.HashSet;

import de.stock.environment.IEnvironment;

/**
 * Interface for tradeables<br>
 * <br>
 * Tradeables, which are stored in the
 * {@link de.stock.tradeable.TradeableHandler TradeableHandler}, consist of
 * several parts:<br>
 * <br>
 * - The <b>value</b></b><br>
 * &nbsp;&nbsp;Every tradeable has a value at what cost the player can buy one<br>
 * &nbsp;&nbsp;The value gets influenced by {@link de.stock.event.Event events}
 * respectively {@link de.stock.environment.Environment environments}.<br>
 * &nbsp;&nbsp;If a tradeable wasn't influenced by an event the
 * {@link de.stock.tradeable.TradeableHandler TradeableHandler}
 * {@link de.stock.tradeable.TradeableHandler#updateUnchangedTradeables()
 * updates} the value manually<br>
 * &nbsp;&nbsp;using a random value in between the
 * {@link #getInfluenceBottomBound() influencable bottom bound} and the
 * {@link #getInfluenceTopBound() influencable top bound} of the tradeable every
 * round.<br>
 * <br>
 * - The <b>shares</b></b><br>
 * &nbsp;&nbsp;Every tradeable has a number of minimum and maximum available
 * shares.<br>
 * &nbsp;&nbsp;The number of shares is initialized with
 * {@link #initializeShares()}<br>
 * &nbsp;&nbsp;This number varies if the player buys or sells a tradeable.<br>
 * &nbsp;&nbsp;(It should change randomly without the players influence, too)<br>
 * <br>
 * - The <b>environment</b></b><br>
 * &nbsp;&nbsp;Every tradeable is part of one or more environments.<br>
 * &nbsp;&nbsp;Example: Wheat is part of the
 * {@link de.stock.environment.types.Group Group} cereal<br>
 * <br>
 * 
 * A tradeable is from type {@link de.stock.tradeable.Stock Stock},
 * {@link de.stock.tradeable.Commodity Commodity},
 * {@link de.stock.tradeable.Forex Forex} or {@link de.stock.tradeable.Derivate
 * Derivate} and a developer <b>must</b> instantiate from these classes.<br>
 * He <b>must not</b> instantiate the {@link de.stock.tradeable.Tradeable
 * Tradeable} class!<br>
 * <br>
 * Its the key element to earn money in this game.<br>
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public interface ITradeable {

    public void decShares(Integer value);

    public String getDescription();

    public Double getInfluenceBottomBound();

    public Double getInfluenceTopBound();

    public Double getInitBottomBound();

    public Double getInitTopBound();

    public Integer getMaxShares();

    public Integer getMinShares();

    public String getName();

    public HashSet<IEnvironment> getPartOf();

    public Integer getShares();

    public Double getValue();

    public void incShares(Integer value);

    /**
     * Initialize number of shares using {@link #getMaxShares() number of
     * maximum} and {@link #getMinShares() number of minimum shares}
     * 
     * @return Returns the number of shares
     */
    public Integer initializeShares();

    /**
     * Initializes value of tradeable using {@link #getInitBottomBound() init
     * bottom bound} and {@link #getInitTopBound() top bound}
     * 
     * @return Returns the initial value
     */
    public Double initializeValue();

    /**
     * See {@link #registerAtEnvironment(IEnvironment)}
     * 
     * @param environment
     *            tradeable gets registered at this environment
     */
    public void linkToEnvironment(IEnvironment environment);

    /**
     * Register tradeable at {@link de.stock.environment.Environment
     * environment}
     * 
     * @param environment
     *            tradeable gets registered at this environment
     */
    public void registerAtEnvironment(IEnvironment environment);

    public void setDescription(String description);

    public void setInfluenceBottomBound(Double influenceBottomBound);

    public void setInfluenceTopBound(Double influenceTopBound);

    public void setInitBottomBound(Double initBottomBound);

    public void setInitTopBound(Double initTopBound);

    public void setMaxShares(Integer maxShares);

    public void setMinShares(Integer minShares);

    public void setName(String name);

    public void setPartOf(HashSet<IEnvironment> partOf);

    public void setShares(Integer shares);

    public void setValue(Double value);

    /**
     * Updates value of tradeable according to random<br>
     * value in between {@link #getInfluenceBottomBound()} and
     * {@link #setInfluenceTopBound(Double)}<br>
     * The sign of the value is random, too. (See
     * {@link de.stock.settings.Settings_Tradeable SIGN_NEGATIVE_BOUND})
     * 
     * @return new value of tradeable
     */
    public Double updateValue();

    /**
     * Sets value to {@code value}
     * 
     * @param value
     *            new value
     */
    public void updateValue(Double value);
}