package de.stock.environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stock.environment.types.Area;
import de.stock.environment.types.Group;
import de.stock.environment.types.Location;
import de.stock.tradable.Commodity;
import de.stock.tradable.ITradable;
import de.stock.tradable.Stock;

public class EnvironmentTest {

    Location  location;
    Location  linkedLocation;

    Area      area;
    Area      linkedArea;

    Group     group;
    Group     linkedGroup;

    Commodity bond;
    Stock     stock;

    @Before
    public void setUp() throws Exception {

        location = new Location();
        linkedLocation = new Location();

        area = new Area();
        linkedArea = new Area();

        group = new Group();
        linkedGroup = new Group();

        bond = new Commodity();
        stock = new Stock();
    }

    @After
    public void tearDown() throws Exception {

        location = new Location();
        linkedLocation = new Location();

        area = new Area();
        linkedArea = new Area();

        group = new Group();
        linkedGroup = new Group();

        bond = new Commodity();
        stock = new Stock();
    }

    @Test
    public void testGetAffectedTradeables() {

        HashSet<ITradable> affectedTradables;

        // Check with one linked environment and one tradable
        linkedLocation.registerTradable(bond);
        assertTrue(location.linkEnvironment(linkedLocation));
        assertTrue(location.getLinkedEnvironments().size() == 1);
        affectedTradables = location.getInfluencedTradables();
        assertTrue(affectedTradables.size() == 1);
        assertTrue(affectedTradables.contains(bond));

        // Reset
        linkedLocation = new Location();

        // Check with two linear linked environments and two tradeables
        linkedArea.registerTradable(bond);
        linkedLocation.registerTradable(stock);
        assertTrue(area.linkEnvironment(linkedArea));
        assertTrue(area.linkEnvironment(linkedLocation));
        assertTrue(area.getLinkedEnvironments().size() == 2);
        affectedTradables = area.getInfluencedTradables();
        assertTrue(affectedTradables.size() == 2);
        assertTrue(affectedTradables.contains(bond));
        assertTrue(affectedTradables.contains(stock));

        // Reset
        area = new Area();
        linkedArea = new Area();
        linkedLocation = new Location();

        // Check with two linear linked environments and two tradeables but both
        // the same
        linkedArea.registerTradable(bond);
        linkedLocation.registerTradable(bond);
        assertTrue(area.linkEnvironment(linkedArea));
        assertTrue(area.linkEnvironment(linkedLocation));
        assertTrue(area.getLinkedEnvironments().size() == 2);
        affectedTradables = area.getInfluencedTradables();
        assertTrue(affectedTradables.size() == 1);
        assertTrue(affectedTradables.contains(bond));

        // Reset
        area = new Area();
        linkedArea = new Area();
        linkedLocation = new Location();

        // Check with two rooted linked environments and two tradeables
        linkedArea.registerTradable(bond);
        linkedLocation.registerTradable(stock);
        assertTrue(area.linkEnvironment(linkedArea));
        assertTrue(linkedArea.linkEnvironment(linkedLocation));
        assertTrue(area.getLinkedEnvironments().size() == 1);
        assertTrue(linkedArea.getLinkedEnvironments().size() == 1);
        affectedTradables = area.getInfluencedTradables();
        assertTrue(affectedTradables.size() == 2);
        assertTrue(affectedTradables.contains(bond));
        assertTrue(affectedTradables.contains(stock));

        // Reset
        area = new Area();
        linkedArea = new Area();
        linkedLocation = new Location();

        // Check with two rooted linked environments and three tradeables but
        // two the same
        linkedArea.registerTradable(bond);
        linkedLocation.registerTradable(bond);
        linkedLocation.registerTradable(stock);
        assertTrue(area.linkEnvironment(linkedArea));
        assertTrue(linkedArea.linkEnvironment(linkedLocation));
        assertTrue(area.getLinkedEnvironments().size() == 1);
        assertTrue(linkedArea.getLinkedEnvironments().size() == 1);
        affectedTradables = area.getInfluencedTradables();
        assertTrue(affectedTradables.size() == 2);
        assertTrue(affectedTradables.contains(bond));
        assertTrue(affectedTradables.contains(stock));
    }

    @Test
    public void testLinkEnvironment() {

        // TEST LOCATION

        // Test without linked environment
        assertTrue(location.getLinkedEnvironments().isEmpty());

        // Test with same environment (not allowed)
        assertFalse(location.linkEnvironment(location));
        assertTrue(location.getLinkedEnvironments().size() == 0);

        // Test with one linked environment
        assertTrue(location.linkEnvironment(linkedLocation));
        assertTrue(location.getLinkedEnvironments().size() == 1);

        // Test with one already linked environment
        assertTrue(location.linkEnvironment(linkedLocation));
        assertTrue(location.getLinkedEnvironments().size() == 1);

        // Test with below layed (one layer) environment (not allowed)
        assertFalse(location.linkEnvironment(area));
        assertTrue(location.getLinkedEnvironments().size() == 1);

        // Test with below layed (two layer) environment (not allowed)
        assertFalse(location.linkEnvironment(group));
        assertTrue(location.getLinkedEnvironments().size() == 1);

        // TEST AREA

        // Test with same environment (not allowed)
        assertFalse(area.linkEnvironment(area));
        assertTrue(area.getLinkedEnvironments().size() == 0);

        // Test with one linked environment
        assertTrue(area.linkEnvironment(linkedArea));
        assertTrue(area.getLinkedEnvironments().size() == 1);

        // Test with above linked environment
        assertTrue(area.linkEnvironment(location));
        assertTrue(area.getLinkedEnvironments().size() == 2);

        // Test with below layed (one layer) environment (not allowed)
        assertFalse(area.linkEnvironment(group));
        assertTrue(area.getLinkedEnvironments().size() == 2);

        // TEST GROUP

        // Test with same environment (not allowed)
        assertFalse(group.linkEnvironment(group));
        assertTrue(group.getLinkedEnvironments().size() == 0);

        // Test with one linked environment
        assertTrue(group.linkEnvironment(linkedGroup));
        assertTrue(group.getLinkedEnvironments().size() == 1);

        // Test with above linked (one layer) environment
        assertTrue(group.linkEnvironment(area));
        assertTrue(group.getLinkedEnvironments().size() == 2);

        // Test with above linked (two layer) environment
        assertFalse(group.linkEnvironment(location));
        assertTrue(group.getLinkedEnvironments().size() == 2);
    }

    @Test
    public void testRegisterTradeable() {

        // Test without registered tradable
        assertTrue(location.getInfluencedTradables().size() == 0);

        // Test with one registered tradable
        location.registerTradable(bond);
        assertTrue(location.getInfluencedTradables().size() == 1);

        // Test with already registered tradable
        location.registerTradable(bond);
        assertTrue(location.getInfluencedTradables().size() == 1);

        // Test with two registered tradable
        location.registerTradable(stock);
        assertTrue(location.getInfluencedTradables().size() == 2);
    }
}
