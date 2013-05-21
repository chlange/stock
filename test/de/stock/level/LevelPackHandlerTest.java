package de.stock.level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.stock.settings.Settings_Level;

public class LevelPackHandlerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        if (LevelPackHandler.getInstance().getLevelStageMap() == null) {
            LevelPackHandler.getInstance().setLevelStageMap(
                    new HashMap<Integer, ArrayList<ILevelPack>>());
        }

        for (int i = Settings_Level.AWARD_LEVEL; i <= Settings_Level.LEVEL_STAGE_END; i++) {
            LevelPackHandler.getInstance().getLevelStageMap().put(i, new ArrayList<ILevelPack>());
        }
    }

    @Test
    public void testChooseLevelPack() {
        assertNull(LevelPackHandler.getInstance().chooseLevelPack(Settings_Level.MAIN_LEVEL));
        final ILevelPack myLevelPack = new LevelPack() {

            @Override
            public void initialize() {
                setName("myTestLevelPack");
                setDescription("myTestLevelPackDescription");
                setLevelStage(Settings_Level.MAIN_LEVEL);
            }
        };
        assertTrue(LevelPackHandler.getInstance().register(myLevelPack));
        assertEquals(myLevelPack,
                LevelPackHandler.getInstance().chooseLevelPack(Settings_Level.MAIN_LEVEL));
        // assertTrue(LevelPackHandler.getInstance().register(myLevelPack));
        // assertEquals(myLevelPack,
        // LevelPackHandler.getInstance().chooseLevelPack(Settings_Level.MAIN_LEVEL));
    }

    @Test
    public void testGetLevels() {
        assertNull(LevelPackHandler.getInstance().getLevelPacks(-1));
        assertNull(LevelPackHandler.getInstance().getLevelPacks(null));

        LevelPackHandler.getInstance().getLevelStageMap().put(Settings_Level.LEVEL_STAGE_2, null);
        assertNull(LevelPackHandler.getInstance().getLevelPacks(Settings_Level.LEVEL_STAGE_2));

        assertNotNull(LevelPackHandler.getInstance().getLevelPacks(Settings_Level.MAIN_LEVEL));

        LevelPackHandler.getInstance().setLevelStageMap(null);
        assertNull(LevelPackHandler.getInstance().getLevelPacks(Settings_Level.MAIN_LEVEL));
    }

    @Test
    public void testGetLevelStage() {
        LevelPackHandler.getInstance().setLevelStage(Settings_Level.AWARD_LEVEL);
        assertEquals(Settings_Level.AWARD_LEVEL, LevelPackHandler.getInstance().getLevelStage());
    }

    @Test
    public void testIncLevelStage() {
        LevelPackHandler.getInstance().setLevelStage(Settings_Level.LEVEL_STAGE_1);
        assertEquals(Settings_Level.LEVEL_STAGE_2, LevelPackHandler.getInstance().incLevelStage());
    }

    @Test
    public void testIsLevelStageValid() {
        assertTrue(LevelPackHandler.getInstance().isLevelStageValid(Settings_Level.AWARD_LEVEL));
        assertTrue(LevelPackHandler.getInstance().isLevelStageValid(Settings_Level.MAIN_LEVEL));
        assertTrue(LevelPackHandler.getInstance().isLevelStageValid(Settings_Level.LEVEL_STAGE_9));

        assertFalse(LevelPackHandler.getInstance().isLevelStageValid(-1));
        assertFalse(LevelPackHandler.getInstance().isLevelStageValid(
                Settings_Level.LEVEL_STAGES + 1));
        assertFalse(LevelPackHandler.getInstance().isLevelStageValid(null));
    }

    @Test
    public void testLoadLevelPacks() {
        fail("How to test this?");
    }

    @Test
    public void testRegister() {

        final LevelDecorator eventOne = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return false;
            }

        };

        eventOne.setName("First event");
        eventOne.setDescription("This is the first event");

        final LevelDecorator eventTwo = new LevelDecorator() {

            @Override
            public void conferAward() {
            }

            @Override
            public boolean hasPassedLevel() {
                return false;
            }

        };

        eventTwo.setName("Second event");
        eventTwo.setDescription("This is the second event");

        final LevelPack levelPackMain = new LevelPack("testMain", "levelpack") {

            @Override
            public void initialize() {
            }
        };
        levelPackMain.addFirstLevel(eventOne);

        final LevelPack levelPackAward = new LevelPack("testAward", "levelpack") {

            @Override
            public void initialize() {
            }
        };
        levelPackAward.addFirstLevel(eventTwo);

        assertTrue(LevelPackHandler.getInstance()
                .register(Settings_Level.MAIN_LEVEL, levelPackMain));
        assertTrue(LevelPackHandler.getInstance().register(Settings_Level.AWARD_LEVEL,
                levelPackAward));

        assertFalse(LevelPackHandler.getInstance().register(-1, levelPackMain));
        assertFalse(LevelPackHandler.getInstance().register(null, levelPackAward));

        assertFalse(LevelPackHandler.getInstance().register(Settings_Level.MAIN_LEVEL, null));
        assertFalse(LevelPackHandler.getInstance().register(Settings_Level.AWARD_LEVEL, null));

        assertEquals("testMain",
                LevelPackHandler.getInstance().getLevelPacks(Settings_Level.MAIN_LEVEL).get(0)
                        .getName());
        assertEquals("First event",
                LevelPackHandler.getInstance().getLevelPacks(Settings_Level.MAIN_LEVEL).get(0)
                        .getFirstLevels().get(0).getName());

        assertEquals("testAward",
                LevelPackHandler.getInstance().getLevelPacks(Settings_Level.AWARD_LEVEL).get(0)
                        .getName());
        assertEquals("Second event",
                LevelPackHandler.getInstance().getLevelPacks(Settings_Level.AWARD_LEVEL).get(0)
                        .getFirstLevels().get(0).getName());

        LevelPackHandler.getInstance().getLevelStageMap().put(Settings_Level.LEVEL_STAGE_2, null);
        assertFalse(LevelPackHandler.getInstance().register(Settings_Level.LEVEL_STAGE_2,
                levelPackMain));
    }

    @Test
    public void testSetLevelStageMap() {
        LevelPackHandler.getInstance().setLevelStageMap(null);
        assertEquals(null, LevelPackHandler.getInstance().getLevelStageMap());
    }
}
