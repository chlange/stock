package de.stock.game;

import java.util.HashMap;

import de.stock.settings.Settings_Game;
import de.stock.settings.Settings_Input;
import de.stock.tradeable.ITradeable;
import de.stock.utils.InputReader;
import de.stock.utils.Printer;

/**
 * This class provides all data and methods that affect the player
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Player {

    private static Player instance = null;

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    private String                    forename;
    private String                    surname;
    /**
     * Money of the player
     */
    private double                    money;
    /**
     * Overall number of bought tradeables
     */
    private long                      boughtTradeables;
    /**
     * The currency symbol
     */
    private String                    currency;
    /**
     * The nationality of the player
     */
    private String                    nationality;

    /**
     * All player owned tradeables with the amount
     */
    private HashMap<ITradeable, Long> tradeables;

    private Player() {
        money = 0;
        boughtTradeables = 0;
        tradeables = new HashMap<ITradeable, Long>();
    }

    /**
     * Adds {@code tradeable} to portfolio<br>
     * <br>
     * Adds {@code amount} to tradeable if its already in the portfolio
     * 
     * @param tradeable
     *            bought tradeable
     * @param amount
     *            amount of bought tradeables
     */
    public void addTradeable(final ITradeable tradeable, Long amount) {

        if (getTradeables().containsKey(tradeable)) {
            final Long currentAmount = getTradeables().get(tradeable);
            amount += currentAmount;
        }
        getTradeables().put(tradeable, amount);

    }

    public void chooseCurrency() {
        Printer.print(Settings_Input.IN_STRING_HEAD, "Please enter your currency");
        currency = InputReader.getFromArray(Settings_Game.AVAILABLE_CURRENCIES);
    }

    public void chooseForename() {
        Printer.print(Settings_Input.IN_STRING_HEAD, "Please enter your forename");
        forename = InputReader.readLine();
    }

    public void chooseNationality() {
        Printer.print(Settings_Input.IN_STRING_HEAD, "Please enter your currency");
        nationality = InputReader.getFromArray(Settings_Game.AVAILABLE_COUNTRIES);
    }

    public void chooseSurname() {
        Printer.print(Settings_Input.IN_STRING_HEAD, "Please enter your surname");
        surname = InputReader.readLine();
    }

    public void decBoughtTradeables(final long value) {
        boughtTradeables -= value;
    }

    public void decMoney(final double money) {
        this.money -= money;
    }

    public long getBoughtTradeables() {
        return boughtTradeables;
    }

    public String getCurrency() {

        return currency;
    }

    public String getForename() {
        return forename;
    }

    public double getMoney() {

        return money;
    }

    public String getNationality() {

        return nationality;
    }

    public String getSurname() {
        return surname;
    }

    public HashMap<ITradeable, Long> getTradeables() {
        return tradeables;
    }

    public void incBoughtTradeables(final long value) {
        boughtTradeables += value;
    }

    public void incMoney(final double money) {

        this.money += money;
    }

    public void setBoughtTradeables(final long boughtTradeables) {
        this.boughtTradeables = boughtTradeables;
    }

    public void setCurrency(final String currency) {

        this.currency = currency;
    }

    public void setForename(final String forename) {
        this.forename = forename;
    }

    public void setMoney(final double value) {

        money = value;
    }

    public void setNationality(final String nationality) {

        this.nationality = nationality;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public void setTradeableMap(final HashMap<ITradeable, Long> tradeableMap) {

        tradeables = tradeableMap;
    }

    public void setTradeables(final HashMap<ITradeable, Long> tradeables) {
        this.tradeables = tradeables;
    }
}
