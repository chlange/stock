package de.stock.tradable;

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
import de.stock.tradable.Commodity;
import de.stock.tradable.Stock;
import de.stock.tradable.Tradable;
import de.stock.tradable.TradableHandler;

public class TradableHandlerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoadTradables() {
        TradableHandler.getInstance().getTradables().clear();
        assertEquals(0, TradableHandler.getInstance().getTradables().size());

        final File tradableFile = new File("res/tradables/tradable.trd");
        final File tradableWrongFile = new File("res/tradables/tradableWrong.tra");
        tradableFile.deleteOnExit();
        tradableWrongFile.deleteOnExit();
        // Create file
        try {
            tradableFile.createNewFile();
            tradableWrongFile.createNewFile();
        }
        catch (final IOException e) {
            fail("Unable to create files " + e);
        }

        // Create tradable
        final Tradable stock = new Stock();
        stock.setName("Drink");

        // Serialize tradable
        final String serializedTradable = Deserializer.serialize(stock);

        // Write serialzied tradable to file
        try {
            final FileWriter trdStream = new FileWriter(tradableFile);
            final BufferedWriter outTrd = new BufferedWriter(trdStream);
            outTrd.write(serializedTradable);
            outTrd.close();
            final FileWriter trdStreamWrong = new FileWriter(tradableWrongFile);
            final BufferedWriter outTrdWrong = new BufferedWriter(trdStreamWrong);
            outTrdWrong.write(serializedTradable);
            outTrdWrong.close();
        }
        catch (final IOException e) {
            fail("Unable to write to files " + e);
        }

        // Deserialize Tradables
        Deserializer.deserialize(Settings_Deserializer.TYPE_TRADEABLE);

        // Check if correct tradable was deserialized
        assertEquals(1, TradableHandler.getInstance().getTradables().size());
        assertEquals("Drink", TradableHandler.getInstance().getTradables().iterator().next()
                .getName());
    }

    @Test
    public void testSaveCurrentState() {
        final Stock stock = new Stock();
        stock.setInfluenceBottomBound(2.0);
        stock.setInfluenceTopBound(2.0);

        TradableHandler.getInstance().getTempTradables().clear();
        assertTrue(TradableHandler.getInstance().getTempTradables().isEmpty());
        TradableHandler.getInstance().getActiveTradables().clear();
        TradableHandler.getInstance().getActiveTradables().put(stock, 10.0);
        TradableHandler.getInstance().saveCurrentState();
        assertFalse(TradableHandler.getInstance().getTempTradables().isEmpty());
    }

    @Test
    public void testUpdateUnchangedTradables() {
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
        germany.registerTradable(stock);
        eg.registerEnvironment(germany);
        final Event event = new MainEvent();
        event.registerEnvironmentGroup(eg);
        ActionObserver.getInstance().getActiveEvents().put(event, 2);
        TradableHandler.getInstance().getActiveTradables().put(stock, 5.0);
        TradableHandler.getInstance().getActiveTradables().put(bond, 5.0);
        assertEquals(new Double(5.0),
                TradableHandler.getInstance().getActiveTradables().get(stock));

        // Save state
        TradableHandler.getInstance().saveCurrentState();
        // Iterate events (change stock by 1%)
        ActionObserver.getInstance().iterateActiveEvents();
        assertEquals(new Double(5.0),
                TradableHandler.getInstance().getActiveTradables().get(stock), 0.05);
        assertEquals(new Double(5.0), TradableHandler.getInstance().getActiveTradables()
                .get(bond));
        // Update unchanged (change bond by 2.0)
        TradableHandler.getInstance().updateUnchangedTradables();
        assertEquals(new Double(5.0), TradableHandler.getInstance().getActiveTradables()
                .get(bond), 2.0);
    }
}
