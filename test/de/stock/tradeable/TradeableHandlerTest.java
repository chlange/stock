package de.stock.tradeable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stock.action.ActionObserver;
import de.stock.deserializer.Deserializer;
import de.stock.environment.EnvironmentGroup;
import de.stock.environment.types.Location;
import de.stock.event.Event;
import de.stock.event.types.MainEvent;
import de.stock.settings.Settings_Deserializer;

public class TradeableHandlerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoadTradeables() {
        TradeableHandler.getInstance().getTradeables().clear();
        assertEquals(0, TradeableHandler.getInstance().getTradeables().size());

        final File tradeableFile = new File("res/tradeables/tradeable.trd");
        final File tradeableWrongFile = new File("res/tradeables/tradeableWrong.tra");
        tradeableFile.deleteOnExit();
        tradeableWrongFile.deleteOnExit();
        // Create file
        try {
            tradeableFile.createNewFile();
            tradeableWrongFile.createNewFile();
        }
        catch (final IOException e) {
            fail("Unable to create files " + e);
        }

        // Create tradeable
        final Tradeable stock = new Stock();
        stock.setName("Drink");

        // Serialize tradeable
        final String serializedTradeable = Deserializer.serialize(stock);

        // Write serialzied tradeable to file
        try {
            final FileWriter trdStream = new FileWriter(tradeableFile);
            final BufferedWriter outTrd = new BufferedWriter(trdStream);
            outTrd.write(serializedTradeable);
            outTrd.close();
            final FileWriter trdStreamWrong = new FileWriter(tradeableWrongFile);
            final BufferedWriter outTrdWrong = new BufferedWriter(trdStreamWrong);
            outTrdWrong.write(serializedTradeable);
            outTrdWrong.close();
        }
        catch (final IOException e) {
            fail("Unable to write to files " + e);
        }

        // Deserialize Tradeables
        Deserializer.deserialize(Settings_Deserializer.TYPE_TRADEABLE);

        // Check if correct tradeable was deserialized
        assertEquals(1, TradeableHandler.getInstance().getTradeables().size());
        assertEquals("Drink", TradeableHandler.getInstance().getTradeables().iterator().next()
                .getName());
    }

    @Test
    public void testSaveCurrentState() {
        final Stock stock = new Stock();
        stock.setInfluenceBottomBound(2.0);
        stock.setInfluenceTopBound(2.0);

        TradeableHandler.getInstance().getTempTradeables().clear();
        assertTrue(TradeableHandler.getInstance().getTempTradeables().isEmpty());
        TradeableHandler.getInstance().getActiveTradeables().clear();
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 10.0);
        TradeableHandler.getInstance().saveCurrentState();
        assertFalse(TradeableHandler.getInstance().getTempTradeables().isEmpty());
    }

    @Test
    public void testUpdateUnchangedTradeables() {
        final Stock stock = new Stock();
        stock.setName("Stock");
        stock.setValue(5.0);
        stock.setInfluenceBottomBound(2.0);
        stock.setInfluenceTopBound(2.0);
        final Commodity bond = new Commodity();
        bond.setName("Bond");
        bond.setValue(5.0);
        bond.setInfluenceBottomBound(2.0);
        bond.setInfluenceTopBound(2.0);

        final EnvironmentGroup eg = new EnvironmentGroup();
        eg.setInfluenceLimit(1.0, 1.0);
        final Location germany = new Location();
        germany.registerTradeable(stock);
        eg.registerEnvironment(germany);
        final Event event = new MainEvent();
        event.registerEnvironmentGroup(eg);
        ActionObserver.getInstance().getActiveEvents().put(event, 2);
        TradeableHandler.getInstance().getActiveTradeables().put(stock, 5.0);
        TradeableHandler.getInstance().getActiveTradeables().put(bond, 5.0);
        assertEquals(new Double(5.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));

        // Save state
        TradeableHandler.getInstance().saveCurrentState();
        // Iterate events (change stock by 1%)
        ActionObserver.getInstance().iterateActiveEvents();
        assertEquals(new Double(5.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock), 0.05);
        assertEquals(new Double(5.0), TradeableHandler.getInstance().getActiveTradeables()
                .get(bond));
        // Update unchanged (change bond by 2.0)
        TradeableHandler.getInstance().updateUnchangedTradeables();
        assertEquals(new Double(5.0), TradeableHandler.getInstance().getActiveTradeables()
                .get(bond), 2.0);
    }
}
