package de.stock.level;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import de.stock.action.ActionObserver;
import de.stock.event.types.MainEvent;
import de.stock.settings.Settings_Level;
import de.stock.settings.Settings_Output;
import de.stock.tradeable.ITradeable;
import de.stock.tradeable.TradeableHandler;
import de.stock.utils.InputReader;
import de.stock.utils.Printer;

/**
 * The level pack handler holds all level packs in their specific level stage
 * (See {@link de.stock.level.LevelPack LevelPack} and
 * {@link de.stock.settings.Settings_Level Settings_Level}) <br>
 * and is the single point of contact for the
 * {@link de.stock.action.ActionObserver ActionObserver} if a level pack of
 * a level stage<br>
 * is needed through the method {@link #chooseLevelPack(Integer)}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class LevelPackHandler {

    private static LevelPackHandler instance = null;

    public static LevelPackHandler getInstance() {
        if (instance == null) {
            instance = new LevelPackHandler();
        }
        return instance;
    }

    private Integer                         levelStage;

    HashMap<Integer, ArrayList<ILevelPack>> levelStageMap;

    private LevelPackHandler() {
        setLevelStage(Settings_Level.MAIN_LEVEL);
        levelStageMap = new HashMap<Integer, ArrayList<ILevelPack>>();
        // Instanciate lists for award and normal levels
        for (int i = Settings_Level.AWARD_LEVEL; i <= Settings_Level.LEVEL_STAGE_END; i++) {
            levelStageMap.put(i, new ArrayList<ILevelPack>());
        }
    }

    /**
     * If this method gets called the player can choose a level pack from the
     * {@code levelStage}<br>
     * <br>
     * The level pack specific content (events, tradeables, ...) gets
     * registered, too. <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return chosen level pack upon success otherwise null
     */
    public ILevelPack chooseLevelPack(final Integer levelStage) {
        if (isLevelStageNotValid(levelStage) || getLevelPacks(levelStage) == null) {
            return null;
        }

        if (getLevelPacks(levelStage).size() == 0) {
            return null;
        } else if (getLevelPacks(levelStage).size() == 1) {
            regLvlPckSpfcContent(getLevelPacks(levelStage).get(0));
            return getLevelPacks(levelStage).get(0);
        }

        Integer poll = null;

        Printer.print(Settings_Output.OUT_OPTION_HEAD, "Please choose a level pack to play");
        printLevelPacks(levelStage);

        while (poll == null || poll < 0 || poll >= getLevelPacks(levelStage).size()) {
            poll = InputReader.readInteger();
            if (poll < 0 || poll >= getLevelPacks(levelStage).size()) {
                Printer.print(Settings_Output.OUT_ERROR, "Option Error", 0, "Option Error",
                        "Level Pack out of index");
                continue;
            }
        }

        final ILevelPack levelPack = getLevelPacks(levelStage).get(poll);

        regLvlPckSpfcContent(levelPack);

        return levelPack;
    }

    /**
     * Returns array of level packs of {@code levelStage} upon success otherwise
     * null<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return array of level packs of {@code levelStage} upon success otherwise
     *         null
     */
    public ArrayList<ILevelPack> getLevelPacks(final Integer levelStage) {
        if (isLevelStageNotValid(levelStage) || levelStageMap == null) {
            return null;
        }

        return getLevelStageMap().get(levelStage);
    }

    public Integer getLevelStage() {
        return levelStage;
    }

    public HashMap<Integer, ArrayList<ILevelPack>> getLevelStageMap() {
        return levelStageMap;
    }

    /**
     * Returns level pack of next level stage upon success or null if last stage
     * is finished<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return level pack of next level stage upon success or null if last stage
     *         is finished
     */
    public ILevelPack getNextStageLevel() {
        if (incLevelStage() > Settings_Level.LEVEL_STAGES) {
            return null;
        }

        return chooseLevelPack(getLevelStage());
    }

    /**
     * Increases level stage by one<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return increased level stage
     */
    public Integer incLevelStage() {
        return ++levelStage;
    }

    /**
     * Returns true if {@code levelStage} isn't valid<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return {@code true} if {@code levelStage} isn't valid
     */
    public boolean isLevelStageNotValid(final Integer levelStage) {
        if (levelStage == null || levelStage < 0 || levelStage > Settings_Level.LEVEL_STAGE_END) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if {@code levelStage} is valid<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return {@code true} if {@code levelStage} is valid
     */
    public boolean isLevelStageValid(final Integer levelStage) {
        return !isLevelStageNotValid(levelStage);
    }

    /**
     * Loads and initializes all level packs from {@code uri} through the
     * {@link de.stock.level.LevelPackLoader
     * LevelPackLoader} and registers them at their specific level stage<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @param uri
     *            the uri to load the level packs (mainly .jar's) from
     * @return
     *         Non-negative number of registered level packs or -1 on error
     */
    public Integer loadLevelPacks(final URI uri) {
        final ArrayList<ILevelPack> loadedLevels = LevelPackLoader.loadLevelPacks(uri);
        if (loadedLevels == null) {
            return -1;
        }

        Integer count = 0;
        for (final ILevelPack levelPack : loadedLevels) {

            if (levelPack.isNotInitialized()) {
                levelPack.initialize();
                levelPack.setInitialized(true);
            }

            if (register(levelPack.getLevelStage(), levelPack) == true) {
                count++;
            }
        }
        return count;
    }

    /**
     * Prints all level packs from {@code levelStage}<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     */
    public void printLevelPacks(final Integer levelStage) {
        if (isLevelStageNotValid(levelStage) || getLevelPacks(levelStage) == null) {
            return;
        }

        Integer i = 0;

        for (final ILevelPack levelPack : getLevelPacks(levelStage)) {
            Printer.print(Settings_Output.OUT_OPTION, i, levelPack.getName(),
                    levelPack.getDescription());
            i++;
        }
    }

    /**
     * Register {@code levelPack} at levelStage specified in {@code levelPack}<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return {@code true} upon success
     */
    public boolean register(final ILevelPack levelPack) {
        if (levelPack.isNotInitialized()) {
            levelPack.initialize();
            levelPack.setInitialized(true);
        }

        return register(levelPack.getLevelStage(), levelPack);
    }

    /**
     * Register {@code levelPack} at {@code levelStage}<br>
     * <br>
     * (See {@link de.stock.settings.Settings_Level Settings_Level} for level
     * stage information)
     * 
     * @return {@code true} upon success
     */
    public boolean register(final Integer levelStage, final ILevelPack levelPack) {
        if (isLevelStageNotValid(levelStage) || levelPack == null
                || getLevelPacks(levelStage) == null) {
            return false;
        }

        if (levelPack.isNotInitialized()) {
            levelPack.initialize();
            levelPack.setInitialized(true);
        }

        // Set level stage in level pack if its unset or differs from passed
        // level stage
        if (levelPack.getLevelStage() == null || levelPack.getLevelStage() != levelStage) {
            levelPack.setLevelStage(levelStage);
        }

        return getLevelPacks(levelStage).add(levelPack);
    }

    /**
     * Register level pack specifc content (events and tradeables)
     * 
     * @param levelPack
     *            the level pack to get the content from
     */
    private void regLvlPckSpfcContent(final ILevelPack levelPack) {
        // Register level pack specific events
        if (levelPack.getEvents() != null) {
            for (final MainEvent event : levelPack.getEvents()) {
                ActionObserver.getInstance().registerEvent(event);
            }
        }

        if (levelPack.getTradeables() != null) {
            // Register level pack specific tradeables to active tradeables
            for (final ITradeable tradeable : levelPack.getTradeables()) {
                TradeableHandler.getInstance().registerActiveTradeable(tradeable);
            }
        }
    }

    public void setLevelStage(final Integer levelStage) {
        this.levelStage = levelStage;
    }

    public void setLevelStageMap(final HashMap<Integer, ArrayList<ILevelPack>> hashMap) {
        levelStageMap = hashMap;
    }
}
