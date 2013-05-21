package de.stock.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.stock.settings.Settings_Output;

/**
 * Provides methods for the information input
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class InputReader {

    // Returns chosen position in array
    public static String getFromArray(final ArrayList<String> array) {
        Printer.print(Settings_Output.OUT_OPTION_HEAD, "Please choose from available elements");
        Integer i = 0;
        for (final String string : array) {
            Printer.print(Settings_Output.OUT_OPTION, i, null, string);
            i++;
        }

        Integer poll = null;
        while (poll == null) {
            poll = readInteger();
            if (poll < 0 || poll >= array.size()) {
                Printer.print(Settings_Output.OUT_ERROR, "Invalid element chosen", -1,
                        "Invalid element chosen", "Invalid element chosen");
                poll = null;
            }
        }

        return array.get(poll);
    }

    public static Integer readInteger() {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Integer input;
        String line = null;

        try {
            while (line == null || line.equals("") || line.isEmpty()) {
                line = br.readLine();
            }

            input = Integer.parseInt(line);
            return input;
        }
        catch (final IOException ioe) {
            Printer.print(Settings_Output.OUT_ERROR, "Read Error", null, "Read Error",
                    "Unable to convert input from string to integer");
            return null;
        }
    }

    public static String readLine() {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            return br.readLine();
        }
        catch (final IOException ioe) {
            Printer.print(Settings_Output.OUT_ERROR, "Read Error", null, "Read Error",
                    "Unable to convert input form string to integer");
            return null;
        }
    }
}
