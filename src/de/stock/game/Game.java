package de.stock.game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.stock.action.ActionObserver;
import de.stock.environment.EnvironmentHandler;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.level.ILevel;
import de.stock.level.ILevelPack;
import de.stock.level.LevelPackHandler;
import de.stock.settings.Settings_Game;
import de.stock.settings.Settings_Level;
import de.stock.settings.Settings_Output;
import de.stock.settings.Settings_Player;
import de.stock.tradable.ITradable;
import de.stock.tradable.TradableHandler;
import de.stock.utils.InputReader;
import de.stock.utils.Printer;

/**
 * This is the starting point of the game<br>
 * <br>
 * This class coordiantes the load- and runtime of the game<br>
 * <br>
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Game {

    static Integer nrEvents;
    static Integer nrEnvironments;
    static Integer nrTradeables;
    static Integer nrLevelPacks;
    static Integer round;

    /**
     * Load all content (events, tradeables, levelpacks, environments)
     */
    public static void loadContent() {
        nrEvents = ActionObserver.getInstance().loadEvents();
        nrEnvironments = EnvironmentHandler.getInstance().loadEnvironments();
        nrTradeables = TradableHandler.getInstance().loadTradeables();

        // Load events from resource path
        // Network loading possible
        final File pathLevelPacks = new File(Settings_Level.PATH_LEVELPACKS);
        nrLevelPacks = LevelPackHandler.getInstance().loadLevelPacks(pathLevelPacks.toURI());
    }

    public static void main(final String[] args) {

        loadContent();
        if (nrLevelPacks == 0) {
            Printer.print(Settings_Output.OUT_ERROR, "No level packs loaded", 0,
                    "No level packs loaded",
                    "No level packs where loaded. Add some level packs to your game.");
            return;
        }

        // Choose first level pack
        final ILevelPack levelPack = LevelPackHandler.getInstance().chooseLevelPack(
                LevelPackHandler.getInstance().getLevelStage());
        ActionObserver.getInstance().registerLevel(levelPack.getStartLevel());

        for (final MainEvent event : ActionObserver.getInstance().getMainEvents()) {
            event.initializeIndex();
        }
        for (final ITradable tradable : TradableHandler.getInstance().getTradables()) {
            tradable.initializeShares();
            tradable.initializeValue();
        }

        Printer.println(Settings_Output.OUT_MSG, 0, "Please choose a difficulty", "Please choose a difficulty");
        Printer.println(Settings_Output.OUT_OPTION, 0, "1. Difficulty easy", "1. Difficulty easy");
        Printer.println(Settings_Output.OUT_OPTION, 0, "2. Difficulty normal", "2. Difficulty normal");
        Printer.println(Settings_Output.OUT_OPTION, 0, "3. Difficulty hard", "3. Difficulty hard");

        Integer difficulty = InputReader.readInteger();
        boolean difficultySet = false;

        while (difficultySet == false) {
            switch (difficulty) {
                case 1:
                    Player.getInstance().setMoney(Settings_Player.PLAYER_MONEY_EASY);
                    difficultySet = true;
                    break;
                case 2:
                    Player.getInstance().setMoney(Settings_Player.PLAYER_MONEY_NORMAL);
                    difficultySet = true;
                    break;
                case 3:
                    Player.getInstance().setMoney(Settings_Player.PLAYER_MONEY_HARD);
                    difficultySet = true;
                    break;
                default:
                    Printer.println(Settings_Output.OUT_ERROR, 0, "Input out of index", "Please repeat your input");
                    difficulty = InputReader.readInteger();
            }
        }

        Player.getInstance().setCurrency("€");

        // Round loop
        for (;; Settings_Game.ROUND++) {

            // Temporary next round indicator
            // TODO: DELETE
            String nextRoundLine = null;

            System.out.println("ROUND:");
            System.out.println("\t" + Settings_Game.ROUND);

            TradableHandler.getInstance().saveCurrentState();
            ActionObserver.getInstance().iterateActiveEvents();
            ActionObserver.getInstance().iterateMainEvents();
            ActionObserver.getInstance().iterateActiveLevels();
            TradableHandler.getInstance().updateUnchangedTradables();

            showInfo();

            while (nextRoundLine == null || nextRoundLine.startsWith("x") == false) {

                nextRoundLine = InputReader.readLine();
                if (nextRoundLine.equals("i")) {
                    showInfo();
                }

                String bla;
                Integer index;
                Integer amount;
                if (nextRoundLine.startsWith("b")) {
                    // Temporary buy
                    bla = nextRoundLine.substring(1, nextRoundLine.length());
                    index = Integer.parseInt(bla.split("\\.")[0]);
                    amount = Integer.parseInt(bla.split("\\.")[1]);
                    Entry<ITradable, Double> entry = null;

                    final java.util.Iterator<Entry<ITradable, Double>> it = TradableHandler
                            .getInstance().getActiveTradables().entrySet().iterator();
                    for (int i = 0; i < index; i++) {
                        entry = it.next();
                    }
                    if ((entry.getValue() * amount) > Player.getInstance().getMoney()) {
                        Printer.print("\t");
                        Printer.println(Settings_Output.OUT_ERROR, 0, "Insufficient money",
                                "Insufficient money");
                    } else {
                        if (entry.getKey().getShares() - amount >= 0) {
                            Player.getInstance().decMoney(entry.getValue() * amount);
                            Player.getInstance().incBoughtTradeables(amount);
                            Player.getInstance().addTradable(entry.getKey(), amount.longValue());
                            entry.getKey().decShares(amount);
                        } else {
                            Printer.println(Settings_Output.OUT_ERROR, 0, "Insufficient shares",
                                    "Insufficient shares");
                        }
                    }
                } else if (nextRoundLine.startsWith("s")) {
                    // Temporary sell
                    bla = nextRoundLine.substring(1, nextRoundLine.length());
                    index = Integer.parseInt(bla.split("\\.")[0]);
                    amount = Integer.parseInt(bla.split("\\.")[1]);
                    Entry<ITradable, Double> entry = null;

                    final java.util.Iterator<Entry<ITradable, Double>> it = TradableHandler
                            .getInstance().getActiveTradables().entrySet().iterator();
                    for (int i = 0; i < index; i++) {
                        entry = it.next();
                    }

                    Player.getInstance().incMoney(entry.getValue() * amount);
                    Player.getInstance().decBoughtTradeables(amount);
                    Player.getInstance().getTradables().remove(entry.getKey());
                    entry.getKey().incShares(amount);
                }

                ActionObserver.getInstance().iterateActiveLevels();
                showInfo();
            }
        }
    }

    private static void showInfo() {
        System.out.println("YOUR MONEY: ");
        System.out.println("\t" + Player.getInstance().getMoney()
                + Player.getInstance().getCurrency());

        System.out.println("YOUR TRADEABLES: ");
        for (final Entry<ITradable, Long> entry : Player.getInstance().getTradables().entrySet()) {
            Printer.print("\t");
            Printer.println(Settings_Output.OUT_TRADEABLE, entry.getValue(), entry.getKey()
                    .getName(), entry.getKey().getDescription());
        }

        // Print tradable infos
        System.out.println("TRADEABLES: ");
        for (final Entry<ITradable, Double> tradable : TradableHandler.getInstance()
                .getActiveTradables().entrySet()) {
            Printer.print("\t");
            Printer.println(Settings_Output.OUT_TRADEABLE, tradable.getValue(), tradable.getKey()
                    .getName(), "Available shares " + tradable.getKey().getShares());
        }

        Integer levelCount = 1;
        // Print active levels
        System.out.println("ACTIVE LEVELS:");
        for (final ILevel level : ActionObserver.getInstance().getActiveLevels()) {
            Printer.print("\t");
            Printer.println(Settings_Output.OUT_LEVEL, levelCount++, level.getName(),
                    level.getDescription());
        }

        // Print active events
        System.out.println("ACTIVE EVENTS:");
        final HashMap<Event, Integer> events = ActionObserver.getInstance().getActiveEvents();
        for (final Entry<Event, Integer> entry : events.entrySet()) {
            Printer.print("\t");
            Printer.println(Settings_Output.OUT_EVENT, entry.getValue(), entry.getKey().getName(),
                    entry.getKey().getDescription());
        }

        // Print main events
        System.out.println("MAIN EVENTS:");
        final ArrayList<MainEvent> mainevents = ActionObserver.getInstance().getMainEvents();
        for (final MainEvent mainEvent : mainevents) {
            Printer.print("\t");
            Printer.println(Settings_Output.OUT_EVENT, mainEvent.getIndex(), mainEvent.getName(),
                    mainEvent.getDescription());
        }

        System.out.println();
        System.out.println("\tNext round with input of \"x\"");
        System.out
                .println("\tBuy with \"b*.x\" - * is the index of the tradable (starts at 1!), x is the amount");
        System.out
                .println("\tSell with \"s*.x\" - * is the index of the tradable (starts at 1!), x is the amount");
    }
}
