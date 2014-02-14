package de.stock.game;

import java.util.HashMap;

import de.stock.settings.Settings_Game;
import de.stock.settings.Settings_Input;
import de.stock.tradable.ITradable;
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
     * Overall number of bought tradables
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
     * All player owned tradables with the amount
     */
    private HashMap<ITradable, Long> tradables;

    private Player() {
        money = 0;
        boughtTradeables = 0;
        tradables = new HashMap<ITradable, Long>();
    }

    /**
     * Adds {@code tradable} to portfolio<br>
     * <br>
     * Adds {@code amount} to tradable if its already in the portfolio
     * 
     * @param tradable
     *            bought tradable
     * @param amount
     *            amount of bought tradables
     */
    public void addTradable(final ITradable tradable, Long amount) {

        final Long currentAmount = getTradables().get(tradable);
        
        if(currentAmount != null)
        	amount += currentAmount;
        
        getTradables().put(tradable, amount);

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

    public HashMap<ITradable, Long> getTradables() {
        return tradables;
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

    public void setTradableMap(final HashMap<ITradable, Long> tradableMap) {

        tradables = tradableMap;
    }

    public void setTradables(final HashMap<ITradable, Long> tradables) {
        this.tradables = tradables;
    }
}
