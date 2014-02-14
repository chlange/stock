package de.stock.tradable;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stock.settings.Settings_Tradable;
import de.stock.tradable.ITradable;
import de.stock.tradable.Tradable;

public class TradableTest {

    ITradable tradable = new Tradable();

    @Before
    public void setUp() throws Exception {
        tradable.setValue(0.0);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInitializeValue() {
        tradable.setInitBottomBound(5.0);
        tradable.setInitTopBound(5.0);
        tradable.initializeValue();
        assertEquals(new Double(5.0), tradable.getValue());

        tradable.setInitBottomBound(3.0);
        tradable.setInitTopBound(3.0);
        tradable.initializeValue();
        assertEquals(new Double(3.0), tradable.getValue());
    }

    @Test
    public void testUpdateValueDouble() {
        tradable.setInfluenceTopBound(1.0);
        tradable.setInfluenceBottomBound(1.0);
        tradable.updateValue();
        assertEquals(0.0, tradable.getValue(), Settings_Tradable.RESET_TOP_LIMIT);
    }
}
