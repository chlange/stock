package de.stock.event;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.stock.action.ActionObserver;
import de.stock.action.IAction;
import de.stock.environment.types.Location;
import de.stock.tradeable.Stock;
import de.stock.tradeable.TradeableHandler;

public class EventTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    Event event;

    @Before
    public void setUp() throws Exception {
        event = new Event();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetSuccessor() {

        assertEquals(null, event.getSuccessor());

        final ArrayList<IAction> successors = new ArrayList<IAction>();
        final Event successor = new Event();
        successor.setName("Success");
        successor.hasOptions(false);

        event.setSuccessors(successors);
        assertEquals(null, event.getSuccessor());

        successors.add(successor);
        assertEquals("Success", event.getSuccessor().getName());
    }

    @Test
    public void testInfluenceEnvironments() {
        final Location location = new Location();
        final Stock stock = new Stock();
        stock.setValue(1.0);
        stock.setInitBottomBound(1.0);
        stock.setInitTopBound(1.0);
        location.registerTradeable(stock);

        event = new Event();
        event.registerEnvironment(location, true, 1.0, 1.0);
        TradeableHandler.getInstance().register(stock);
        ActionObserver.getInstance().getActiveEvents().put(event, 1);
        assertEquals(new Double(1.0),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
        ActionObserver.getInstance().iterateActiveEvents();
        assertEquals(new Double(1.01),
                TradeableHandler.getInstance().getActiveTradeables().get(stock));
    }
}
