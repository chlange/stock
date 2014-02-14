package de.stock.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stock.deserializer.Deserializer;
import de.stock.environment.EnvironmentGroup;
import de.stock.environment.types.Location;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.game.Player;
import de.stock.level.ILevel;
import de.stock.level.ILevelPack;
import de.stock.level.LevelDecorator;
import de.stock.level.LevelPack;
import de.stock.settings.Settings_Deserializer;
import de.stock.settings.Settings_Event;
import de.stock.settings.Settings_Influencable;
import de.stock.tradable.Commodity;
import de.stock.tradable.ITradable;
import de.stock.tradable.Stock;
import de.stock.tradable.TradableHandler;
import de.stock.utils.Priority;
import de.stock.utils.Provider;

public class ActionObserverTest {

    ActionObserver ao;

    @Before
    public void setUp() throws Exception {

        ao = ActionObserver.getInstance();
    }

    @After
    public void tearDown() throws Exception {

        ao.setMainEvents(new ArrayList<MainEvent>());
    }

    @Test
    public void testIterateActiveEvents() {

        // Prepare active events map
        HashMap<Event, Integer> activeEvents = new HashMap<Event, Integer>();
        MainEvent mainEvent = new MainEvent();
        MainEvent sucEvent = new MainEvent();
        ArrayList<IAction> successors = new ArrayList<IAction>();
        successors.add(sucEvent);
        mainEvent.setSuccessors(successors);

        // Test with one event that has 2 days left
        // Action: Just decrease days by 1
        activeEvents.put(mainEvent, 2);
        ao.setActiveEvents(activeEvents);
        ao.iterateActiveEvents();
        assertTrue(ao.getActiveEvents().size() == 1);
        // Test if main event has 1 day left
        assertTrue(ao.getActiveEvents().values().contains(new Integer(1)));

        // Reset and prepare for next test
        activeEvents = new HashMap<Event, Integer>();
        mainEvent = new MainEvent();
        sucEvent = new MainEvent();
        sucEvent.setRoundsBottomBound(3);
        sucEvent.setRoundsTopBound(3);
        successors = new ArrayList<IAction>();
        successors.add(sucEvent);
        mainEvent.setSuccessors(successors);

        // Test with one event that has 1 day left (and a successor)
        // Action: Get successor event (sucEvent) that has 3 days left
        activeEvents.put(mainEvent, 1);
        ao.setActiveEvents(activeEvents);
        ao.iterateActiveEvents();
        assertEquals(1, ao.getActiveEvents().size());
        // Test if successor event has 3 days left
        assertTrue(ao.getActiveEvents().values().contains(new Integer(3)));

        // Reset and prepare for next test
        activeEvents = new HashMap<Event, Integer>();
        mainEvent = new MainEvent();
        MainEvent mainEventTwo = new MainEvent();
        sucEvent = new MainEvent();
        sucEvent.setRoundsBottomBound(3);
        sucEvent.setRoundsTopBound(3);
        successors = new ArrayList<IAction>();
        successors.add(sucEvent);
        mainEvent.setSuccessors(successors);

        // Test with two events
        // One has 1 day left (and a successor), the other has 2 days left
        // Actions: Get successor event (sucEvent) that has 3 days left for
        // first event
        // Decrease days of other event
        activeEvents.put(mainEvent, 1);
        activeEvents.put(mainEventTwo, 2);
        ao.setActiveEvents(activeEvents);
        ao.iterateActiveEvents();
        assertTrue(ao.getActiveEvents().size() == 2);
        // Test if successor event has 3 days left
        assertTrue(ao.getActiveEvents().values().contains(new Integer(3)));
        // Test if days of other event was decerased
        assertTrue(ao.getActiveEvents().values().contains(new Integer(1)));

        // Reset and prepare for next test
        activeEvents = new HashMap<Event, Integer>();
        mainEvent = new MainEvent();
        mainEventTwo = new MainEvent();

        // Test with two events
        // One has 1 day left (and no successor), the other has 2 days left
        // Actions: Remove first event as it has no successor
        // Decrease days of other event
        activeEvents.put(mainEvent, 1);
        activeEvents.put(mainEventTwo, 2);
        ao.setActiveEvents(activeEvents);
        ao.iterateActiveEvents();
        assertTrue(ao.getActiveEvents().size() == 1);
        // Test if days of other event was decerased
        assertTrue(ao.getActiveEvents().values().contains(new Integer(1)));
    }

    @Test
    public void testIterateActiveLevels() {

        ao.setActiveLevels(new ArrayList<ILevel>());
        ILevel level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };
        final ILevel levelTwo = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };

        level.setName("first level");
        levelTwo.setName("next level");

        final ArrayList<IAction> list = new ArrayList<IAction>();

        // Level not passed
        Provider.getPlayer().setMoney(0);
        assertFalse(level.hasPassedLevel());
        ao.iterateActiveLevels();

        // Level not passed but registered at event observer
        ao.registerLevel(level);
        Provider.getPlayer().setMoney(0);
        assertFalse(level.hasPassedLevel());
        ao.iterateActiveLevels();

        // Level passed
        Provider.getPlayer().setMoney(1500);
        assertTrue(level.hasPassedLevel());
        ao.iterateActiveLevels();

        // Level passed and next level available
        list.add(levelTwo);
        ((Action) level).setSuccessors(list);
        assertTrue(level.hasPassedLevel());
        assertTrue(ao.getActiveLevels().size() == 0);
        ao.getActiveLevels().add(0, level);
        assertTrue(ao.getActiveLevels().size() == 1);
        ao.iterateActiveLevels();
        assertTrue(ao.getActiveLevels().size() == 1);

        // Level pack passed and next level not available
        level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };
        assertTrue(level.hasPassedLevel());
        ao.setActiveLevels(new ArrayList<ILevel>());
        ao.getActiveLevels().add(level);
        ao.iterateActiveLevels();
        assertTrue(ao.getActiveLevels().size() == 0);

        // Test level specific events removal if level is passed
        level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };
        MainEvent event = new MainEvent();
        level.setEvents(new ArrayList<MainEvent>());
        level.getEvents().add(event);
        ao.setMainEvents(new ArrayList<MainEvent>());
        ao.getMainEvents().add(event);
        ao.setActiveLevels(new ArrayList<ILevel>());
        ao.registerLevel(level);
        Provider.getPlayer().setMoney(1500);
        assertTrue(level.hasPassedLevel());
        assertEquals(1, ao.getActiveLevels().size());
        assertEquals(1, ao.getMainEvents().size());
        ao.iterateActiveLevels();
        assertEquals(0, ao.getActiveLevels().size());
        assertEquals(0, ao.getMainEvents().size());

        // Test level pack specific events removal if level is passed
        ILevelPack levelPack = new LevelPack("my", "levelPack") {

            @Override
            public void initialize() {
            }
        };
        level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };
        event = new MainEvent();
        final MainEvent eventTwo = new MainEvent();
        levelPack.addFirstLevel(level);
        levelPack.setEvents(new ArrayList<MainEvent>());
        levelPack.getEvents().add(eventTwo);
        level.setEvents(new ArrayList<MainEvent>());
        level.getEvents().add(event);
        ao.setMainEvents(new ArrayList<MainEvent>());
        ao.getMainEvents().add(event);
        ao.getMainEvents().add(eventTwo);
        ao.setActiveLevels(new ArrayList<ILevel>());
        ao.registerLevel(level);
        Provider.getPlayer().setMoney(1500);
        assertTrue(level.hasPassedLevel());
        assertEquals(1, ao.getActiveLevels().size());
        assertEquals(2, ao.getMainEvents().size());
        ao.iterateActiveLevels();
        assertEquals(0, ao.getActiveLevels().size());
        assertEquals(0, ao.getMainEvents().size());

        // Test level specific tradable removal if level is passed
        level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };
        Stock stock = new Stock();
        level.setTradables(new ArrayList<ITradable>());
        level.getTradables().add(stock);
        TradableHandler.getInstance().setActiveTradables(new HashMap<ITradable, Double>());
        TradableHandler.getInstance().getActiveTradables().put(stock, 0.0);
        ao.setActiveLevels(new ArrayList<ILevel>());
        ao.registerLevel(level);
        Provider.getPlayer().setMoney(1500);
        assertTrue(level.hasPassedLevel());
        assertEquals(1, ao.getActiveLevels().size());
        assertEquals(1, TradableHandler.getInstance().getActiveTradables().size());
        ao.iterateActiveLevels();
        assertEquals(0, ao.getActiveLevels().size());
        assertEquals(0, TradableHandler.getInstance().getActiveTradables().size());

        // Test level pack specific tradable removal if level is passed
        levelPack = new LevelPack("my", "levelPack") {

            @Override
            public void initialize() {
            }
        };
        level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Provider.getPlayer().getMoney() > 1000) ? true : false;
            }
        };
        stock = new Stock();
        final Commodity bond = new Commodity();
        levelPack.setTradables(new ArrayList<ITradable>());
        levelPack.getTradables().add(bond);
        level.setLevelPack(levelPack);
        level.setTradables(new ArrayList<ITradable>());
        level.getTradables().add(stock);
        TradableHandler.getInstance().setActiveTradables(new HashMap<ITradable, Double>());
        TradableHandler.getInstance().getActiveTradables().put(stock, 0.0);
        TradableHandler.getInstance().getActiveTradables().put(bond, 0.0);
        ao.setActiveLevels(new ArrayList<ILevel>());
        ao.registerLevel(level);
        Provider.getPlayer().setMoney(1500);
        assertTrue(level.hasPassedLevel());
        assertEquals(1, ao.getActiveLevels().size());
        assertEquals(2, TradableHandler.getInstance().getActiveTradables().size());
        ao.iterateActiveLevels();
        assertEquals(0, ao.getActiveLevels().size());
        assertEquals(0, TradableHandler.getInstance().getActiveTradables().size());
    }

    @Test
    public void testIterateMainEvents() {

        final HashMap<Event, Integer> activeEvents = new HashMap<Event, Integer>();
        ao.setActiveEvents(activeEvents);
        assertTrue(ao.getMainEvents().size() == 0);
        final MainEvent mainEvent = new MainEvent();
        final MainEvent sucEvent = new MainEvent();
        final ArrayList<IAction> list = new ArrayList<IAction>();
        ao.registerEvent(mainEvent);
        assertTrue(ao.getMainEvents().size() == 1);
        assertTrue(ao.getActiveEvents().size() == 0);

        mainEvent.setIndex(100);
        mainEvent.setExecutionBound(0);
        mainEvent.hasOptions(false);
        mainEvent.setPriority(Priority.LOW);
        Settings_Event.EVENT_LIMIT_REACHED_ALL = false;
        Settings_Event.EXECUTE_BOUND_RATE = 0;
        ao.iterateMainEvents();
        assertTrue(ao.getActiveEvents().size() == 1);

        sucEvent.setPriority(Priority.HIGH);
        list.add(sucEvent);
        mainEvent.setSuccessors(list);

        mainEvent.setIndex(100);
        mainEvent.setExecutionBound(0);
        mainEvent.hasOptions(false);

        // Test with reached limit
        ao.getActiveEvents().clear();
        Settings_Event.EVENT_LIMIT_REACHED_ALL = true;
        Settings_Event.EXECUTE_BOUND_RATE = 0;
        ao.iterateMainEvents();
        assertTrue(ao.getActiveEvents().size() == 0);

        // Test with priority specific reached limit
        ao.getActiveEvents().clear();
        Settings_Event.EVENT_LIMIT_REACHED_ALL = false;
        Settings_Event.EVENT_LIMIT_REACHED_LOW = true;
        Settings_Event.EXECUTE_BOUND_RATE = 0;
        ao.iterateMainEvents();
        assertTrue(ao.getActiveEvents().size() == 0);

        // Test with priority specific reached limit (but with successor
        // priority)
        ao.getActiveEvents().clear();
        Settings_Event.EVENT_LIMIT_REACHED_ALL = false;
        Settings_Event.EVENT_LIMIT_REACHED_HIGH = true;
        Settings_Event.EXECUTE_BOUND_RATE = 0;
        ao.iterateMainEvents();
        assertTrue(ao.getActiveEvents().size() == 0);

        // Test without reached limit and test influence of players money, too
        ao.getActiveEvents().clear();
        Settings_Event.EVENT_LIMIT_REACHED_ALL = false;
        Settings_Event.EVENT_LIMIT_REACHED_LOW = false;
        Settings_Event.EXECUTE_BOUND_RATE = 0;
        mainEvent.setInfluenceObjects(new HashMap<Integer, Long>());
        mainEvent.getInfluenceObjects().put(Settings_Influencable.PLAYER_MONEY, (long) 100);
        sucEvent.influenceObjects();
        Player.getInstance().setMoney(0);
        assertEquals(0, Player.getInstance().getMoney(), 0.0);
        ao.iterateMainEvents();
        assertTrue(ao.getActiveEvents().size() == 1);
        assertEquals(100, Player.getInstance().getMoney(), 0.0);
    }

    @Test
    public void testLoadEvents() {
        ActionObserver.getInstance().getMainEvents().clear();
        assertEquals(0, ActionObserver.getInstance().getMainEvents().size());

        // Create files
        final File eventfile = new File("res/events/event.evt");
        final File eventfileWrongname = new File("res/events/event.evn");
        eventfile.deleteOnExit();
        eventfileWrongname.deleteOnExit();
        try {
            eventfile.createNewFile();
            eventfileWrongname.createNewFile();
        }
        catch (final IOException e) {
            fail("Unable to create files " + e);
        }

        // Create event with linked environment and tradable
        final Event event = new MainEvent();
        event.setName("Event");
        final EnvironmentGroup eg = new EnvironmentGroup();
        eg.setName("Environment group");
        final Location location = new Location();
        location.setName("Germany");
        final Stock stock = new Stock();
        stock.setName("Drink");
        location.registerTradable(stock);
        eg.registerEnvironment(location);
        event.registerEnvironmentGroup(eg);

        // Serialize event and save in event file (and wrong event file)
        final String serializedEvent = Deserializer.serialize(event);
        event.setName("EventWrong");
        final String serializedEventWrong = Deserializer.serialize(event);
        try {
            // Correct file
            final FileWriter evStream = new FileWriter(eventfile);
            final BufferedWriter outEv = new BufferedWriter(evStream);
            outEv.write(serializedEvent);
            outEv.close();
            // Wrong file
            final FileWriter evWrongStream = new FileWriter(eventfileWrongname);
            final BufferedWriter outEvWrong = new BufferedWriter(evWrongStream);
            outEvWrong.write(serializedEventWrong);
            outEvWrong.close();
        }
        catch (final IOException e) {
            fail("Unable to write to files " + e);
        }

        // Deserialize events
        Deserializer.deserialize(Settings_Deserializer.TYPE_EVENT);

        // Check if correct event was deserialized
        assertEquals(1, ActionObserver.getInstance().getMainEvents().size());
        assertEquals("Event", ActionObserver.getInstance().getMainEvents().get(0).getName());
        assertEquals(1, ActionObserver.getInstance().getMainEvents().get(0).getEnvironmentGroups()
                .size());
        assertEquals(1, ActionObserver.getInstance().getMainEvents().get(0).getEnvironmentGroups()
                .get(0).getEnvironments().size());
        assertEquals("Germany", ActionObserver.getInstance().getMainEvents().get(0)
                .getEnvironmentGroups().get(0).getEnvironments().get(0).getName());
    }

    @Test
    public void testRegisterEvent() {

        final MainEvent mainEvent = new MainEvent();
        mainEvent.setName("SUCCESS");

        ao.registerEvent(mainEvent);
        assertEquals("SUCCESS", ao.getMainEvents().get(0).getName());
    }
}