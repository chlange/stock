package de.stock.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ActionTest {

    IAction action;

    @Before
    public void setUp() throws Exception {
        action = new Action();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddSuccessor() {
        final IAction testAction = new Action();
        assertEquals(0, action.getSuccessors().size());
        assertNull(action.getSuccessor());
        action.addSuccessor(testAction);

        assertEquals(1, action.getSuccessors().size());
        assertEquals(testAction, action.getSuccessor());
    }

    @Test
    public void testGetSuccessor() {
        final IAction testAction = new Action();
        assertEquals(0, action.getSuccessors().size());
        assertNull(action.getSuccessor());

        action.addSuccessor(testAction);
        assertEquals(1, action.getSuccessors().size());
        assertEquals(testAction, action.getSuccessor());

        // Needs user interaction
        //
        // IAction secondTestAction = new Action();
        // action.addSuccessor(secondTestAction);
        // action.hasOptions(true);
        // assertEquals(2, action.getSuccessors().size());
        // assertEquals(testAction, action.getSuccessor());
    }

}
