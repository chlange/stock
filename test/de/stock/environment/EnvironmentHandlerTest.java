package de.stock.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stock.deserializer.Deserializer;
import de.stock.environment.types.Area;
import de.stock.environment.types.Group;
import de.stock.environment.types.Location;
import de.stock.event.types.MainEvent;
import de.stock.settings.Settings_Deserializer;
import de.stock.tradeable.Commodity;
import de.stock.tradeable.Forex;
import de.stock.tradeable.Stock;
import de.stock.tradeable.TradeableHandler;

public class EnvironmentHandlerTest {

    EnvironmentGroup environmentGroup;
    EnvironmentGroup environmentGroupTwo;
    Location         location;
    Area             area;
    Area             areaTwo;
    Group            group;
    Stock            stock;
    Commodity        bond;
    Forex            forex;
    MainEvent        mainEvent;

    @Before
    public void setUp() throws Exception {
        environmentGroup = new EnvironmentGroup();
        environmentGroupTwo = new EnvironmentGroup();
        location = new Location();
        area = new Area();
        areaTwo = new Area();
        stock = new Stock();
        group = new Group();
        bond = new Commodity();
        forex = new Forex();
        mainEvent = new MainEvent();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInfluenceEnvironment() {

        // Affect stock through location
        final ArrayList<EnvironmentGroup> environmentGroupArray = new ArrayList<EnvironmentGroup>();
        environmentGroupArray.add(environmentGroup);
        environmentGroup.setInfluencePositive();
        environmentGroup.setInfluenceBottomLimit(1.0);
        environmentGroup.setInfluenceTopLimit(1.0);
        environmentGroup.registerEnvironment(location);
        assertTrue(environmentGroup.getEnvironments().size() == 1);
        location.registerTradeable(stock);
        assertTrue(location.getInfluencedTradeables().size() == 1);
        stock.setValue(100.0);
        mainEvent.registerEnvironmentGroup(environmentGroup);
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 100.0);
        EnvironmentHandler.getInstance().influenceEnvironments(environmentGroupArray);
        assertEquals(new Double(101.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
        assertEquals(new Double(101.0), stock.getValue());

        // Reset
        stock.setValue(100.0);
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 100.0);

        // Affect stock and bond through location
        location.registerTradeable(bond);
        TradeableHandler.getInstance().getActiveTradeables().put(bond, 1.0);
        EnvironmentHandler.getInstance().influenceEnvironments(environmentGroupArray);
        assertEquals(new Double(101.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
        assertEquals(new Double(1.01),
                TradeableHandler.getInstance().getActiveTradeables().get(bond));
        assertEquals(new Double(101.0), stock.getValue());
        assertEquals(new Double(1.01), bond.getValue());

        // Reset
        stock.setValue(100.0);
        bond.setValue(1.0);
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 100.0);
        TradeableHandler.getInstance().getActiveTradeables().put(bond, 1.0);

        // Affect stock, bond and forex through location and area
        environmentGroup.registerEnvironment(area);
        area.registerTradeable(forex);
        area.registerTradeable(stock);
        area.registerTradeable(bond);
        location.registerTradeable(forex);
        TradeableHandler.getInstance().getActiveTradeables().put(forex, 200.0);
        EnvironmentHandler.getInstance().influenceEnvironments(environmentGroupArray);
        assertEquals(new Double(101.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
        assertEquals(new Double(1.01),
                TradeableHandler.getInstance().getActiveTradeables().get(bond));
        assertEquals(new Double(202.0),
                TradeableHandler.getInstance().getActiveTradeables().get(forex));
        assertEquals(new Double(101.0), stock.getValue());
        assertEquals(new Double(1.01), bond.getValue());
        assertEquals(new Double(202.0), forex.getValue());

        // Reset
        stock.setValue(100.0);
        bond.setValue(1.0);
        forex.setValue(200.0);
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 100.0);
        TradeableHandler.getInstance().getActiveTradeables().put(bond, 1.0);
        TradeableHandler.getInstance().getActiveTradeables().put(forex, 200.0);

        // Affect no tradeable as these are not intersecting tradeables
        environmentGroup.registerEnvironment(group);
        EnvironmentHandler.getInstance().influenceEnvironments(environmentGroupArray);
        assertEquals(new Double(100.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
        assertEquals(new Double(1.0), TradeableHandler.getInstance().getActiveTradeables()
                .get(bond));
        assertEquals(new Double(200.0),
                TradeableHandler.getInstance().getActiveTradeables().get(forex));
        assertEquals(new Double(100.0), stock.getValue());
        assertEquals(new Double(1.0), bond.getValue());
        assertEquals(new Double(200.0), forex.getValue());

        // Reset
        stock.setValue(100.0);
        bond.setValue(1.0);
        forex.setValue(200.0);
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 100.0);
        TradeableHandler.getInstance().getActiveTradeables().put(bond, 1.0);
        TradeableHandler.getInstance().getActiveTradeables().put(forex, 200.0);
        environmentGroup.getEnvironments().remove(group);

        // Affect stock, bond and forex through location and area
        // and affect forex through areaTwo
        environmentGroupArray.add(environmentGroupTwo);
        areaTwo.registerTradeable(forex);
        environmentGroupTwo.registerEnvironment(areaTwo);
        environmentGroupTwo.setInfluenceBottomLimit(1.0);
        environmentGroupTwo.setInfluenceTopLimit(1.0);
        environmentGroupTwo.setInfluenceNegative();
        EnvironmentHandler.getInstance().influenceEnvironments(environmentGroupArray);
        assertEquals(new Double(101.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
        assertEquals(new Double(1.01),
                TradeableHandler.getInstance().getActiveTradeables().get(bond));
        assertEquals(new Double(199.98),
                TradeableHandler.getInstance().getActiveTradeables().get(forex));
        assertEquals(new Double(101.0), stock.getValue());
        assertEquals(new Double(1.01), bond.getValue());
        assertEquals(new Double(199.98), forex.getValue());
    }

    @Test
    public void testLoadEnvironments() {
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
        environment.registerTradeable(stock);
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
                .getTradeables().size());
        assertEquals("Drink", EnvironmentHandler.getInstance().getEnvironments().iterator().next()
                .getTradeables().iterator().next().getName());
    }
}
