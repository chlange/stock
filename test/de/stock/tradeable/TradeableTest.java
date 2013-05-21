package de.stock.tradeable;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stock.settings.Settings_Tradeable;

public class TradeableTest {

    ITradeable tradeable = new Tradeable();

    @Before
    public void setUp() throws Exception {
        tradeable.setValue(0.0);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInitializeValue() {
        tradeable.setInitBottomBound(5.0);
        tradeable.setInitTopBound(5.0);
        tradeable.initializeValue();
        assertEquals(new Double(5.0), tradeable.getValue());

        tradeable.setInitBottomBound(3.0);
        tradeable.setInitTopBound(3.0);
        tradeable.initializeValue();
        assertEquals(new Double(3.0), tradeable.getValue());
    }

    @Test
    public void testUpdateValueDouble() {
        tradeable.setInfluenceTopBound(1.0);
        tradeable.setInfluenceBottomBound(1.0);
        tradeable.updateValue();
        assertEquals(0.0, tradeable.getValue(), Settings_Tradeable.RESET_TOP_LIMIT);
    }
}
