package de.stock.level;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

/**
 * The level pack loader loads all levels through the
 * {@link #loadLevelPacks(URI)} method.<br>
 * <br>
 * It uses the <a href="http://code.google.com/p/jspf/">Java Simple Plugin
 * Framework</a> to create and load level packs.<br>
 * <br>
 * To <b>create a level pack</b> you have to follow these steps:<br>
 * <br>
 * &nbsp;&nbsp;<b>1.</b> Create your level pack and the components in a package
 * with the following naming convention to avoid conflicts with other level
 * packs from other developers<br>
 * <br>
 * <i>{country code}.{surname}.{level pack name}</i><br>
 * <br>
 * For example de.lange.lemonade<br>
 * <br>
 * &nbsp;&nbsp;<b>2.</b> Create one or more classes which extend the class
 * {@link de.stock.level.LevelDecorator} -> This is a level<br>
 * <br>
 * &nbsp;&nbsp;<b>3.</b> Implement the
 * {@link de.stock.level.ILevel#conferAward() conferAward()} and
 * {@link de.stock.level.ILevel#hasPassedLevel() hasPassedLevel()} method<br>
 * <br>
 * <i>conferAward()</i> may be used to give the player his prize for example
 * some money<br>
 * <br>
 * 
 * <pre>
 * {@code
 * Example for conferAward()
 * {
 *  Player.getInstance.incMoney(100);
 * }
 * </pre>
 * 
 * <i>hasPassedLevel()</i> indicates whether the level goal is reached or not<br>
 * <br>
 * 
 * <pre>
 * Example for hasPassedLevel()
 * {
 *  return (Player.getInstance.getMoney() > 100) ? true : false;
 * }
 * </pre>
 * 
 * &nbsp;&nbsp;<b>4.</b> Create a class which extends the
 * class {@link de.stock.level.LevelPack LevelPack} -> This is the level pack<br>
 * <br>
 * Use the Annotation <i>@PluginImplementation</i> for the class and import
 * <i>net.xeoh.plugins.base.annotations.PluginImplementation</i><br>
 * <br>
 * This must be done cause this game uses the <a
 * href="http://code.google.com/p/jspf/">Java Simple Plugin Framework</a><br>
 * <br>
 * &nbsp;&nbsp;<b>5.</b> Implement the
 * {@link de.stock.level.ILevelPack#initialize() initialize()} method of the
 * class<br>
 * <br>
 * <i>initialize()</i> gets called when the level pack gets
 * {@link de.stock.level.LevelPackHandler#register(ILevelPack) regis}
 * {@link de.stock.level.LevelPackHandler#register(Integer, ILevelPack) tered}
 * at the {@link de.stock.level.LevelPackHandler LevelPackHandler}<br>
 * This method should set the name, the description, .... everything.<br>
 * <br>
 * 
 * <pre>
 * Large Example for initialize()
 * {
 *   // Level pack properties
 *   setName("Lemonade");
 *   setDescription("This is my first level pack which is about lemonades!");
 *   setLevelStage(Settings_Level.MAIN_LEVEL);
 *   setNation("Germany");
 * 
 *   // Set up environments
 *   Location eu = new Location();
 *   eu.setName("European Union");
 *   eu.setDescription("Confederation of 27 member states which are located primarily in Europe");
 * 
 *   Location greece = new Location();
 *   greece.setName("Greece");
 *   greece.setDescription("Greece");
 * 
 *   // Add greece to the european union
 *   eu.linkEnvironment(greece);
 * 
 *   Area lemonPlantations = new Area();
 *   lemonPlantations.setName("Lemon plantations");
 *   lemonPlantations.setDescription("All lemon plantations");
 * 
 *   // Level specific tradeable
 *   Commodity lemons = new Commodity();
 *   lemons.setName("Lemons");
 *   lemons.setDescription("The lemon is a small ellipsoidal yellow fruit");
 * 
 *   lemons.setInitTopBound(15.0);
 *   lemons.setInitBottomBound(5.0);
 * 
 *   lemons.setInfluenceTopBound(3.0);
 *   lemons.setInfluenceBottomBound(0.7);
 * 
 *   lemons.setMaxShares(1200);
 *   lemons.setMinShares(500);
 * 
 *   lemons.registerAtEnvironment(lemonPlantations);
 *   lemons.registerAtEnvironment(greece);
 * 
 *   // Set up a level
 *   // You should use your class(es) from step 2!
 *   ILevel myLevel = new LevelDecorator() {
 * 
 *       // @Override
 *       public void conferAward() {
 *           Printer.println(Settings_Output.OUT_AWARD, 0, "Award earned", "You get 100$ extra money");
 *           Player.getInstance().incMoney(100);
 *       }
 * 
 *       // @Override
 *       public boolean hasPassedLevel() {
 *           return (Player.getInstance().getMoney() >= 100) ? true : false;
 *       }
 *   };
 *   myLevel.setDescription("Get 100$ in money to win this level.");
 * 
 *   // Set up a successor level of myLevel which starts if myLevel's goal is reached
 *   // You should use your class(es) from step 2!
 *   ILevel mySecondLevel = new LevelDecorator() {
 * 
 *       // @Override
 *       public void conferAward() {
 *          // No award
 *       }
 * 
 *       // @Override
 *       public boolean hasPassedLevel() {
 *           return (Player.getInstance().getMoney() >= 500) ? true : false;
 *       }
 *   };
 *   mySecondLevel.setDescription("Get 500$ in money to win this level.");
 * 
 *   // Link second level with first level
 *   // The second level is the successor level which gets started if myLevel's goal is reached
 *   myLevel.addSuccessor(mySecondLevel);
 * 
 *   // Set up an event
 *   MainEvent myEvent = new MainEvent();
 *   myEvent.setPriority(de.stock.utils.Priority.HIGH);
 *   
 *   myEvent.setName("Greece lemon plague");
 *   myEvent.setDescription("All greece lemons are affected by a plague");
 *   
 *   myEvent.setInfluenceBottomBound(2);
 *   myEvent.setInfluenceTopBound(5);
 *   
 *   myEvent.setRoundsTopBound(4);
 *   myEvent.setRoundsBottomBound(1);
 * 
 *   myEvent.setIndexInitTopBound(30);
 *   myEvent.setIndexInitBottomBound(0);
 *   myEvent.setIndexMaximum(100);
 *   myEvent.setExecutionBound(70); // Events starts if this bound is reached
 * 
 *   // Set up environment group which influences all lemon
 *   // plantations in greece
 *   EnvironmentGroup greeceLemonPlantations = new EnvironmentGroup();
 *   greeceLemonPlantations.setName("greeceLemonPlantations");
 *   greeceLemonPlantations.setInfluenceTopLimit(15.0); // In percent
 *   greeceLemonPlantations.setInfluenceBottomLimit(7.0); // In percent
 *   greeceLemonPlantations.setInfluenceNegative(); // Influence is negative -> value of lemons fall!
 *   greeceLemonPlantations.registerEnvironment(greece);
 *   greeceLemonPlantations.registerEnvironment(lemonPlantations);
 * 
 *   // Add environment group to event
 *   myEvent.registerEnvironmentGroup(greeceLemonPlantations);
 *   // Add event to level
 *   myLevel.registerEvent(myEvent);
 *   // Add tradeable to level
 *   myLevel.registerTradeable(lemons);
 *   // Add level to level pack
 *   addFirstLevel(myLevel);
 * }
 * </pre>
 * 
 * &nbsp;&nbsp;<b>6.</b> Create a .jar archive of your package<br>
 * <br>
 * This .jar archive will be loaded with {@link #loadLevelPacks(URI)} from local
 * system or the network<br>
 * <br>
 * <b>If your level pack won't be loaded, check if you set
 * <i>@PluginImplementation</i> from step 4!</b><br>
 * <br>
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class LevelPackLoader {

    private static PluginManager pm = null;

    /**
     * Loads all level packs from {@code uri}
     * 
     * @return
     *         an array containing all loaded level packs upon success else null
     */
    public static ArrayList<ILevelPack> loadLevelPacks(final URI uri) {

        if (pm == null) {
            pm = PluginManagerFactory.createPluginManager();
        }

        pm.addPluginsFrom(uri);
        final PluginManagerUtil pmu = new PluginManagerUtil(pm);
        final Collection<ILevelPack> lpCol = pmu.getPlugins(ILevelPack.class);
        return (lpCol != null) ? new ArrayList<ILevelPack>(lpCol) : null;
    }
}
