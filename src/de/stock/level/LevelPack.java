package de.stock.level;

import java.util.ArrayList;

import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.settings.Settings_Output;
import de.stock.tradeable.ITradeable;
import de.stock.utils.InputReader;
import de.stock.utils.Printer;
import de.stock.utils.Utils;

/**
 * See {@link de.stock.level.ILevelPack ILevelPack} for futher information
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public abstract class LevelPack implements ILevelPack {

    private String                name;
    private String                description;
    private Integer               levelStage;

    /**
     * In which nation does this level pack play
     */
    private String                nation;

    /**
     * Available first levels
     */
    private ArrayList<ILevel>     firstLevels;

    /**
     * Indicates whether the player can choose the first level (true) or not
     * (false)
     */
    private boolean               hasOption;

    /**
     * Tradeables in this level pack (each level may have specific tradeables,
     * too)
     */
    private ArrayList<ITradeable> tradeables;

    /**
     * Events in this level pack (each level may have specific events, too)
     */
    private ArrayList<MainEvent>  events;

    /**
     * Indicates whether this level pack is initialized or not
     */
    private boolean               isInitialized = false;

    public LevelPack() {
        firstLevels = new ArrayList<ILevel>();
        tradeables = new ArrayList<ITradeable>();
        events = new ArrayList<MainEvent>();
    }

    public LevelPack(final String name, final String description) {
        this.name = name;
        this.description = description;
        firstLevels = new ArrayList<ILevel>();
        tradeables = new ArrayList<ITradeable>();
        events = new ArrayList<MainEvent>();
    }

    /**
     * Add {@code event} to level pack
     * 
     * @param event
     *            event for the whole lifetime of the level pack
     */
    @Override
    public void addEvent(final Event event) {
        if (events.contains(event)) {
            return;
        }

        events.add((MainEvent) event);
    }

    @Override
    public void addFirstLevel(final ILevel myFirstLevel) {
        if (myFirstLevel != null) {

            // Set level stage in level or get level stage from level
            // It's the responsibility of the level developer to use just one
            // level stage!
            if (levelStage != null) {
                myFirstLevel.setLevelStage(levelStage);
            } else if (myFirstLevel.getLevelStage() != null) {
                setLevelStage(myFirstLevel.getLevelStage());
            }

            // Connect level with level pack
            myFirstLevel.setLevelPack(this);
            getFirstLevels().add(myFirstLevel);
        }
    }

    @Override
    /**
     * Add {@code tradeable} to level pack<br>
     * <br>
     * This {@code tradeable} is active for the whole lifetime of the level pack
     * 
     * @param tradeable
     *            tradeable for the whole lifetime of the level pack
     */
    public void addTradeable(final ITradeable tradeable) {
        if (tradeables.contains(tradeable)) {
            return;
        }

        tradeables.add(tradeable);
    }

    /**
     * Lets player choose from first levels and returns first level upon success
     * 
     * @return first level upon success otherwise null
     */
    private ILevel chooseLevel() {
        if (getFirstLevels() == null || getFirstLevels().size() == 0) {
            return null;
        }

        if (getFirstLevels().size() == 1) {
            return getFirstLevels().get(0);
        }

        Integer poll = null;
        printLevels();

        while (poll == null || poll < 0 || poll >= getFirstLevels().size()) {
            poll = InputReader.readInteger();
            if (poll < 0 || poll >= getFirstLevels().size()) {
                Printer.print(Settings_Output.OUT_ERROR, "Option Error", 0, "Option Error",
                        "Option out of index");
                continue;
            }
        }

        return getFirstLevels().get(poll);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ArrayList<MainEvent> getEvents() {
        return events;
    }

    @Override
    public ArrayList<ILevel> getFirstLevels() {
        return firstLevels;
    }

    @Override
    public Integer getLevelStage() {
        return levelStage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNation() {
        return nation;
    }

    /**
     * Get the starting level of the level pack
     * 
     * @return Returns the starting level of the level pack upon success else
     *         null
     */
    @Override
    public ILevel getStartLevel() {
        if (firstLevels == null) {
            return null;
        } else if (firstLevels.size() == 1) {
            return firstLevels.get(0);
        } else {
            if (hasOption()) {
                return chooseLevel();
            } else {
                return firstLevels.get(Utils.random(0, firstLevels.size() - 1));
            }
        }
    }

    @Override
    public ArrayList<ITradeable> getTradeables() {
        return tradeables;
    }

    @Override
    public boolean hasOption() {
        return hasOption;
    }

    @Override
    public abstract void initialize();

    @Override
    public boolean isHasOption() {
        return hasOption;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public boolean isNotInitialized() {
        return !isInitialized;
    }

    /**
     * Prints available first levels
     */
    private void printLevels() {
        if (getFirstLevels() == null || getFirstLevels().size() == 0) {
            return;
        }

        Printer.print(Settings_Output.OUT_OPTION_HEAD, "Please choose an option");

        Integer i = 0;
        for (final ILevel action : getFirstLevels()) {
            Printer.print(Settings_Output.OUT_OPTION, i, action.getName(), action.getDescription());
            i++;
        }
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public void setEvents(final ArrayList<MainEvent> events) {
        this.events = events;
    }

    @Override
    public void setFirstLevels(final ArrayList<ILevel> firstLevels) {
        this.firstLevels = firstLevels;
    }

    @Override
    public void setHasOption(final boolean hasOption) {
        this.hasOption = hasOption;
    }

    @Override
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    @Override
    public void setLevelStage(final Integer levelStage) {
        this.levelStage = levelStage;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setNation(final String nation) {
        this.nation = nation;
    }

    @Override
    public void setTradeables(final ArrayList<ITradeable> tradeables) {
        this.tradeables = tradeables;
    }
}
