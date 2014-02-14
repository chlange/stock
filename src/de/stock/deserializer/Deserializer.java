package de.stock.deserializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import de.stock.action.ActionObserver;
import de.stock.environment.EnvironmentHandler;
import de.stock.environment.IEnvironment;
import de.stock.event.Event;
import de.stock.settings.Settings_Deserializer;
import de.stock.settings.Settings_Output;
import de.stock.tradable.ITradable;
import de.stock.tradable.TradableHandler;
import de.stock.utils.Printer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * The parser loads nearly all the content of the game.<br>
 * <br>
 * All environments, events, tradables, ... are serialized in json-formatted
 * strings <br>
 * and saved in files, so content can be injected very fast and clean.<br>
 * You can find the file extensions in
 * {@link de.stock.settings.Settings_Deserializer
 * Settings_Deserializer}<br>
 * <br>
 * The game uses the <a href="http://flexjson.sourceforge.net">Flexjson</a>
 * library as this library is easy to use and provides deep serialization.<br>
 * <br> {@link de.stock.level.LevelPack LevelPacks} are loaded through
 * {@link de.stock.level.LevelPackLoader LevelPackLoader}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Deserializer {

    private static JSONSerializer jsonserializer;

    /**
     * Deserializes all files regarding to {@code type}<br>
     * <br>
     * - Tradables are saved in
     * {@link de.stock.tradable.TradableHandler#getTradables()
     * TradableHandler}<br>
     * - Events are saved in
     * {@link de.stock.action.ActionObserver#getMainEvents() ActionObserver}<br>
     * - Environments are saved in
     * {@link de.stock.environment.EnvironmentHandler#getEnvironments()
     * EnvironmentHandler}<br>
     * 
     * @param type
     *            The type of object (See
     *            {@link de.stock.settings.Settings_Deserializer
     *            Settings_Deserializer}).
     *            Files of this type get deserialized
     * 
     * @return number of deserialized elements
     */
    public static Integer deserialize(final Integer type) {
        ArrayList<File> files = null;
        Integer ret = 0;

        if (type == null) {
            return 0;
        } else if (type == Settings_Deserializer.TYPE_TRADEABLE) {
            files = getFilesOnly(getFilesFromDir(Settings_Deserializer.PATH_TRADEABLES));
        } else if (type == Settings_Deserializer.TYPE_EVENT) {
            files = getFilesOnly(getFilesFromDir(Settings_Deserializer.PATH_EVENTS));
        } else if (type == Settings_Deserializer.TYPE_ENVIRONMENT) {
            files = getFilesOnly(getFilesFromDir(Settings_Deserializer.PATH_ENVIRONMENTS));
        }

        ret = deserializeFiles(type, files);
        return ret;
    }

    /**
     * Creates object(s) from passed String
     * 
     * String gets deep deserialized which means that objects of arrays in the
     * object are created, too
     * 
     * @param string
     *            The string to deserialize
     * @param type
     *            The type of object (See
     *            {@link de.stock.settings.Settings_Deserializer
     *            Settings_Deserializer})
     * @return An instance of the class regarding to type from string on success
     *         else null
     */
    public static Object deserialize(final Integer type, final String string) {
        if (type == Settings_Deserializer.TYPE_TRADEABLE) {
            final JSONDeserializer<ITradable> jsondeserializer = new JSONDeserializer<ITradable>();
            ITradable tradable = null;
            try {
                tradable = jsondeserializer.deserialize(string);
                return tradable;
            }
            catch (final Exception e) {
                Printer.print(Settings_Output.OUT_ERROR, "Deserializer error", 0,
                        "Deserializer error",
                        "Unable to deserialize tradable string starting with the following text:\n"
                                + string.substring(0, 100) + "[...]");
                return null;
            }
        } else if (type == Settings_Deserializer.TYPE_EVENT) {
            final JSONDeserializer<Event> jsondeserializer = new JSONDeserializer<Event>();
            Event event = null;
            try {
                event = jsondeserializer.deserialize(string);
                return event;
            }
            catch (final Exception e) {
                Printer.print(Settings_Output.OUT_ERROR, "Deserializer error", 0,
                        "Deserializer error",
                        "Unable to deserialize event string starting with the following text:\n"
                                + string.substring(0, 100) + "[...]");
                return null;
            }
        } else if (type == Settings_Deserializer.TYPE_ENVIRONMENT) {
            final JSONDeserializer<IEnvironment> jsondeserializer = new JSONDeserializer<IEnvironment>();
            IEnvironment environment = null;
            try {
                environment = jsondeserializer.deserialize(string);
                return environment;
            }
            catch (final Exception e) {
                Printer.print(Settings_Output.OUT_ERROR, "Deserializer error", 0,
                        "Deserializer error",
                        "Unable to deserialize environment string starting with the following text:\n"
                                + string.substring(0, 100) + "[...]");
                return null;
            }
        }

        return null;
    }

    /**
     * Deserialize all {@code files} from {@code type}
     * 
     * @param type
     *            The type of objects
     * @param files
     *            The files to deserialize
     * @return
     *         Non-negative number of deserialized objects upon success
     *         otherwise -1
     */
    private static Integer deserializeFiles(final Integer type, final ArrayList<File> files) {
        if (type == null || files == null) {
            return -1;
        }

        Integer deserializedObjects = 0;
        String fileContent;
        final String extension = (type == Settings_Deserializer.TYPE_EVENT) ? Settings_Deserializer.EXTENSION_EVENTS
                : (type == Settings_Deserializer.TYPE_ENVIRONMENT) ? Settings_Deserializer.EXTENSION_ENVIRONMENTS
                        : (type == Settings_Deserializer.TYPE_TRADEABLE) ? Settings_Deserializer.EXTENSION_TRADEABLES
                                : null;
        if (extension == null) {
            return -1;
        }

        for (final File file : files) {
            if (file.getName().endsWith(extension)) {
                // Read file content
                fileContent = readFile(file);
                if (fileContent != null && fileContent.isEmpty() == false) {
                    // Deserialize file content
                    final Object object = deserialize(type, fileContent);
                    if (object == null) {
                        continue;
                    }

                    // Register object at appropriate handler
                    if (object instanceof Event) {
                        ActionObserver.getInstance().registerEvent((Event) object);
                    } else if (object instanceof IEnvironment) {
                        EnvironmentHandler.getInstance().register((IEnvironment) object);
                    } else if (object instanceof ITradable) {
                        TradableHandler.getInstance().addTradable((ITradable) object);
                    } else {
                        continue;
                    }

                    deserializedObjects++;
                }
            }
        }
        return deserializedObjects;
    }

    /**
     * See {@link de.stock.deserializer.Deserializer#getFilesFromDir(String)}
     * 
     * @param dir
     *            the directory
     * 
     * @return Files and folders from dir on success otherwise null
     */
    public static File[] getFilesFromDir(final File dir) {
        return getFilesFromDir(dir.getPath());
    }

    /**
     * Opens passed directory and returns array of File objects containing files
     * an folders in directory
     * 
     * @param dir
     *            Directory to read from
     * @return Files and folders from dir on success otherwise null
     */
    public static File[] getFilesFromDir(final String dir) {

        if (dir == null) {
            return null;
        }

        final File file = new File(dir);
        if (file.isDirectory() == false) {
            return null;
        }

        final File files[] = file.listFiles();
        return files;
    }

    /**
     * Gets all files from passed File array, even those in folders and returns
     * array of File objects
     * 
     * @return Array of all files on success otherwise null
     */
    public static ArrayList<File> getFilesOnly(final File[] files) {
        if (files == null) {
            return null;
        }

        final ArrayList<File> fileArray = new ArrayList<File>();

        for (final File file : files) {
            if (file.isDirectory()) {
                // Add files in directories recursively
                fileArray.addAll(getFilesOnly(getFilesFromDir(file.getAbsolutePath())));
            } else {
                // Add file
                fileArray.add(file);
            }
        }

        return fileArray;
    }

    /**
     * Reads in passed file and returns String object
     * 
     * @param file
     *            File to read from
     * 
     * @return A String containing the content of the file
     */
    public static String readFile(final File file) {
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            String content = new String();
            while ((str = in.readLine()) != null) {
                content = content.concat(str);
            }
            in.close();
            return content;
        }
        catch (final IOException e) {
            Printer.print(Settings_Output.OUT_ERROR, 0, "Read error", "Error while reading file "
                    + file.getName());
            return null;
        }

    }

    /**
     * Wrapper for {@link de.stock.deserializer.Deserializer#readFile(File)}
     * 
     * @param path
     *            Path to the file
     * 
     * @return A String containing the content of the file
     */
    public static String readFile(final String path) {
        return readFile(new File(path));
    }

    /**
     * (Deep) Serializes {@code object} and returns JSON-formatted string
     * 
     * @param object
     *            The object to serialize
     * @return
     *         a JSON-formatted string
     */
    public static String serialize(final Object object) {
        if (jsonserializer == null) {
            jsonserializer = new JSONSerializer();
        }
        return jsonserializer.prettyPrint(true).deepSerialize(object);
    }
}
