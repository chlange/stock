package de.stock.deserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.stock.action.ActionObserver;
import de.stock.environment.Environment;
import de.stock.environment.EnvironmentGroup;
import de.stock.environment.EnvironmentHandler;
import de.stock.environment.types.Location;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.game.Player;
import de.stock.level.LevelDecorator;
import de.stock.settings.Settings_Deserializer;
import de.stock.tradable.Commodity;
import de.stock.tradable.Stock;
import de.stock.tradable.TradableHandler;
import de.stock.utils.Priority;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class DeserializerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File           event;
    private File           environment;
    private File           tradeable;
    private File           nevermindFile;
    private File           nevermindFolder;
    private File           fileInNevermindFolder;

    @Before
    public void setUp() throws Exception {
        event = folder.newFile("Event.evt");
        environment = folder.newFile("Environment.env");
        tradeable = folder.newFile("Tradable.trd");
        nevermindFile = folder.newFile("NVM");
        nevermindFolder = folder.newFolder("nevermindFolder");
        fileInNevermindFolder = folder.newFile("nevermindFolder/file.test");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDeserialize() {
        // Serialize object
        final Event event = new Event();
        event.setName("Success");
        event.setPriority(Priority.HIGH);
        final EnvironmentGroup eg = new EnvironmentGroup();
        eg.setName("Success");
        final Location location = new Location();
        location.setName("Success");
        eg.registerEnvironment(location);
        final Commodity oil = new Commodity();
        location.registerTradable(oil);
        oil.setValue(1.0);
        event.registerEnvironmentGroup(eg);

        final JSONSerializer jsd = new JSONSerializer();
        final String serialized = jsd.deepSerialize(event);

        // Test deserialize from Flexjson
        final JSONDeserializer<Event> js = new JSONDeserializer<Event>();
        Event deserialized = js.deserialize(serialized);
        assertEquals("Success", deserialized.getName());
        assertEquals("Success", deserialized.getEnvironmentGroups().get(0).getName());
        assertEquals("Success", deserialized.getEnvironmentGroups().get(0).getEnvironments().get(0)
                .getName());
        assertFalse(deserialized.getEnvironmentGroups().get(0).getEnvironments().get(0)
                .getTradables().isEmpty());

        // Test parser deserializer
        deserialized = (Event) Deserializer.deserialize(Settings_Deserializer.TYPE_EVENT,
                serialized);
        assertEquals("Success", deserialized.getName());
        assertEquals("Success", deserialized.getEnvironmentGroups().get(0).getName());
        assertEquals("Success", deserialized.getEnvironmentGroups().get(0).getEnvironments().get(0)
                .getName());
        assertFalse(deserialized.getEnvironmentGroups().get(0).getEnvironments().get(0)
                .getTradables().isEmpty());

    }

    @Test
    public void testGetFilesFromDir() {
        final File files[] = Deserializer.getFilesFromDir(folder.getRoot());
        assertEquals("Environment.env", files[0].getName());
        assertEquals("Event.evt", files[1].getName());
        assertEquals("Tradable.trd", files[4].getName());
        assertTrue(files[2].getPath().endsWith("nevermindFolder"));
    }

    @Test
    public void testGetFilesOnly() {
        final ArrayList<File> files = Deserializer.getFilesOnly(Deserializer.getFilesFromDir(folder
                .getRoot()));
        assertTrue(files.contains(event));
        assertTrue(files.contains(environment));
        assertTrue(files.contains(tradeable));
        assertTrue(files.contains(nevermindFile));
        assertFalse(files.contains(nevermindFolder));
        assertTrue(files.contains(fileInNevermindFolder));
    }

    @Test
    /**
     *  Test interconnection of methods (mainly level system with overridden LevelDecorator.hasPassedLevel() method)
     */
    public void testInterconnection() {
        ActionObserver.getInstance().getActiveLevels().clear();
        ActionObserver.getInstance().getActiveEvents().clear();

        final LevelDecorator level = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return (Player.getInstance().getMoney() > 100) ? true : false;
            }
        };

        final Stock stock = new Stock();
        stock.setName("stock");
        stock.setValue(1.0);
        stock.setInitTopBound(0.0);
        stock.setInitBottomBound(0.0);
        final Location loc = new Location();
        loc.registerTradable(stock);
        final EnvironmentGroup eg = new EnvironmentGroup();
        eg.setInfluencePositive();
        eg.setInfluenceLimit(500.0, 500.0);
        eg.registerEnvironment(loc);
        final MainEvent me = new MainEvent();
        me.setName("event");
        me.registerEnvironmentGroup(eg);
        level.registerTradable(stock);
        level.registerEvent(me);
        ActionObserver.getInstance().getActiveEvents().put(me, 1);
        assertEquals(0, ActionObserver.getInstance().getActiveLevels().size());
        ActionObserver.getInstance().registerLevel(level);
        assertEquals(1, ActionObserver.getInstance().getActiveLevels().size());
        assertEquals(new Double(0.0), ActionObserver.getInstance().getActiveLevels().get(0)
                .getTradables().get(0).getValue());
        Player.getInstance().setMoney(0);
        ActionObserver.getInstance().iterateActiveLevels();
        assertEquals(1, ActionObserver.getInstance().getActiveLevels().size());
        Player.getInstance().setMoney(200);
        ActionObserver.getInstance().iterateActiveLevels();
        assertEquals(0, ActionObserver.getInstance().getActiveLevels().size());
        assertEquals(1, ActionObserver.getInstance().getActiveEvents().size());
        TradableHandler.getInstance().getActiveTradables().put(stock, 1.0);
        ActionObserver.getInstance().iterateActiveEvents();
        assertEquals(0, ActionObserver.getInstance().getActiveEvents().size());
        assertEquals(new Double(6.0), stock.getValue());
    }

    @Test
    public void testParse() {
        EnvironmentHandler.getInstance().getEnvironments().clear();
        assertEquals(0, EnvironmentHandler.getInstance().getEnvironments().size());

        // Create files
        final File environmentfile = new File("res/environments/germany.env");
        final File environmentfileWrongname = new File("res/environments/england.evt");
        environmentfile.deleteOnExit();
        environmentfileWrongname.deleteOnExit();
        try {
            environmentfile.createNewFile();
            environmentfileWrongname.createNewFile();
        }
        catch (final IOException e) {
            fail("Unable to create files " + e);
        }
        // Create environment
        final Environment environment = new Location();
        environment.setName("Germany");
        final Stock stock = new Stock();
        stock.setName("Drink");
        environment.registerTradable(stock);
        final Environment environmentWrong = new Location();

        // Serialize environment and save in environment file (and wrong
        // environment file)
        final String serializedEnvironment = Deserializer.serialize(environment);
        final String serializedEnvironmentWrong = Deserializer.serialize(environmentWrong);
        try {
            // Correct file
            final FileWriter envStream = new FileWriter(environmentfile);
            final BufferedWriter outEnv = new BufferedWriter(envStream);
            outEnv.write(serializedEnvironment);
            outEnv.close();
            // Wrong file
            final FileWriter envStreamWrong = new FileWriter(environmentfileWrongname);
            final BufferedWriter outEnvWrong = new BufferedWriter(envStreamWrong);
            outEnvWrong.write(serializedEnvironmentWrong);
            outEnvWrong.close();
        }
        catch (final IOException e) {
            fail("Unable to write to files " + e);
        }

        // Deserialize environment
        Deserializer.deserialize(Settings_Deserializer.TYPE_ENVIRONMENT);

        // Check if correct environment was deserialized
        assertEquals(1, EnvironmentHandler.getInstance().getEnvironments().size());
        assertEquals("Germany", EnvironmentHandler.getInstance().getEnvironments().iterator()
                .next().getName());
        assertEquals(1, EnvironmentHandler.getInstance().getEnvironments().iterator().next()
                .getTradables().size());
        assertEquals("Drink", EnvironmentHandler.getInstance().getEnvironments().iterator().next()
                .getTradables().iterator().next().getName());
    }

    @Test
    public void testReadFile() {
        try {
            BufferedWriter out;
            out = new BufferedWriter(new FileWriter(event));
            out.write("Success");
            out.close();
            out = new BufferedWriter(new FileWriter(environment));
            out.write("Success");
            out.close();
            out = new BufferedWriter(new FileWriter(tradeable));
            out.write("Success");
            out.close();
            assertEquals("Success", Deserializer.readFile(event));
            assertEquals("Success", Deserializer.readFile(environment));
            assertEquals("Success", Deserializer.readFile(tradeable));
        }
        catch (final IOException e) {
            // BLABLA
            e.printStackTrace();
        }

    }
}
