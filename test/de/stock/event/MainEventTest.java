package de.stock.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.stock.event.types.MainEvent;

public class MainEventTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    MainEvent event;

    @Before
    public void setUp() throws Exception {
        event = new MainEvent();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHasExecutionBoundPassed() {
        event.setExecutionBound(1);
        event.setIndex(0);
        assertFalse(event.hasExecBoundPassed());

        event.setExecutionBound(0);
        event.setIndex(0);
        assertTrue(event.hasExecBoundPassed());

        event.setExecutionBound(0);
        event.setIndex(1);
        assertTrue(event.hasExecBoundPassed());
    }

    @Test
    public void testUpdateValue() {
        event.setIndexMaximum(100);

        event.setInfluenceBottomBound(0);
        event.setInfluenceTopBound(0);
        event.setIndexInitBottomBound(0);
        event.setIndexInitTopBound(0);
        event.setIndex(0);
        assertEquals(new Integer(0), event.getIndex());

        event.updateIndex();
        assertEquals(new Integer(0), event.getIndex());

        event.setInfluenceBottomBound(1);
        event.setInfluenceTopBound(1);
        event.updateIndex();
        assertEquals(new Integer(1), event.getIndex(), 1);

        event.setIndex(5);
        event.setInfluenceBottomBound(0);
        event.setInfluenceTopBound(2);
        event.updateIndex();
        assertEquals(new Integer(5), event.getIndex(), 2);
    }

    // @Test
    // public void testExecute() {
    // event.setIndex(80);
    // event.setExecutionBound(90);
    // assertFalse(event.execute());
    //
    // event.setIndex(80);
    // event.setExecutionBound(81);
    // assertFalse(event.execute());
    //
    // event.setIndex(82);
    // event.setExecutionBound(81);
    // assertTrue(event.execute());
    // }
}
