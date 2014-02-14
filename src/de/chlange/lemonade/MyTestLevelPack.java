package de.chlange.lemonade;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import de.stock.environment.EnvironmentGroup;
import de.stock.environment.types.Area;
import de.stock.environment.types.Location;
import de.stock.event.types.MainEvent;
import de.stock.level.ILevel;
import de.stock.level.LevelPack;
import de.stock.settings.Settings_Level;
import de.stock.tradable.Commodity;

@PluginImplementation
public class MyTestLevelPack extends LevelPack {

    @Override
    public void initialize() {
        // Level pack properties
        setName("Lemonade");
        setDescription("This is my first level pack which is about lemonades!");
        setLevelStage(Settings_Level.MAIN_LEVEL);
        setNation("Germany");

        // Set up environments
        final Location eu = new Location();
        eu.setName("European Union");
        eu.setDescription("Confederation of 27 member states which are located primarily in Europe");

        final Location greece = new Location();
        greece.setName("Greece");
        greece.setDescription("Greece");

        // Add greece to the european union
        eu.linkEnvironment(greece);

        final Area lemonPlantations = new Area();
        lemonPlantations.setName("Lemon plantations");
        lemonPlantations.setDescription("All lemon plantations");

        // Level specific tradable
        final Commodity lemons = new Commodity();
        lemons.setName("Lemons");
        lemons.setDescription("The lemon is a small ellipsoidal yellow fruit");

        lemons.setInitTopBound(15.0);
        lemons.setInitBottomBound(5.0);

        lemons.setInfluenceTopBound(3.0);
        lemons.setInfluenceBottomBound(0.7);

        lemons.setMaxShares(28);
        lemons.setMinShares(14);

        lemons.registerAtEnvironment(lemonPlantations);
        lemons.registerAtEnvironment(greece);

        lemons.initializeShares();
        lemons.initializeValue();

        // Level specific tradable
        final Commodity sugar = new Commodity();
        sugar.setName("Sugar");
        sugar.setDescription("The sugar");

        sugar.setInitTopBound(30.0);
        sugar.setInitBottomBound(13.0);

        sugar.setInfluenceTopBound(4.5);
        sugar.setInfluenceBottomBound(1.4);

        sugar.setMaxShares(55);
        sugar.setMinShares(22);

        sugar.initializeShares();
        sugar.initializeValue();

        // Set up a level
        // You should use your class(es) from step 2!
        final ILevel myLevel = new MyFirstTestLevel();
        myLevel.setDescription("Get 100$ in money to win this level.");

        // Set up a successor level of myLevel which starts if myLevel's goal is
        // reached
        // You should use your class(es) from step 2!
        final ILevel mySecondLevel = new MySecondLevel();
        mySecondLevel.setDescription("Get 500$ in money to win this level.");

        // Link second level with first level
        // The second level is the successor level which gets started if
        // myLevel's goal is reached
        myLevel.addSuccessor(mySecondLevel);

        // Set up an event
        final MainEvent myEvent = new MainEvent();
        myEvent.setPriority(de.stock.utils.Priority.HIGH);

        myEvent.setName("Greece lemon plague");
        myEvent.setDescription("All greece lemons are affected by a plague");

        myEvent.setInfluenceBottomBound(2);
        myEvent.setInfluenceTopBound(5);

        myEvent.setRoundsTopBound(4);
        myEvent.setRoundsBottomBound(1);

        myEvent.setIndexInitTopBound(30);
        myEvent.setIndexInitBottomBound(0);
        myEvent.setIndexMaximum(100);
        myEvent.setExecutionBound(70); // Events starts if this bound is reached

        myEvent.initializeIndex();

        // Set up environment group which influences all lemon
        // plantations in greece
        final EnvironmentGroup greeceLemonPlantations = new EnvironmentGroup();
        greeceLemonPlantations.setName("greeceLemonPlantations");
        greeceLemonPlantations.setInfluenceTopLimit(13.0); // In percent
        greeceLemonPlantations.setInfluenceBottomLimit(7.0); // In percent
        greeceLemonPlantations.setInfluenceNegative(); // Influence is negative
                                                       // -> value of lemons
                                                       // fall!
        greeceLemonPlantations.registerEnvironment(greece);
        greeceLemonPlantations.registerEnvironment(lemonPlantations);

        // Add environment group to event
        myEvent.registerEnvironmentGroup(greeceLemonPlantations);
        // Add event to level pack
        addEvent(myEvent);
        // Add tradable to level pack
        addTradable(lemons);
        // Add tradable to second level
        mySecondLevel.registerTradable(sugar);
        // Add level to level pack
        addFirstLevel(myLevel);
    }

}
