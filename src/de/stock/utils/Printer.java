package de.stock.utils;

/**
 * Provides methods for the information output
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Printer {

    public static void print(final Integer integer) {
        System.out.print(integer);
    }

    public static void print(final Integer option, final Double value, final String name,
            final String text) {

        // TODO: Switch case for different option
        // switch(option) {...}

        if (option % 0x100 == 0) {
            if (value != null) {
                print(value + ":");
            }
            if (name != null) {
                print(name + ":");
            }
            if (text != null) {
                print(text + ":");
            }
        }
    }

    public static void print(final Integer option, final Integer integer) {
        System.out.print(integer);
    }

    public static void print(final Integer option, final Integer value, final String name,
            final String text) {

        // TODO: Switch case for different option
        // switch(option) {...}

        if (option % 0x100 == 0) {
            if (value != null) {
                print(value + ":");
            }
            if (name != null) {
                print(name + ":");
            }
            if (text != null) {
                print(text + ":");
            }
        }
    }

    public static void print(final Integer option, final String string) {
        System.out.print(string);
    }

    public static void print(final Integer option, final String head, final Integer value,
            final String name, final String text) {

        // TODO: Switch case for different option
        // switch(option) {...}

        if (option % 0x100 == 0) {
            if (head != null) {
                print(head + ":");
            }
            if (value != null) {
                print(value + ":");
            }
            if (name != null) {
                print(name + ":");
            }
            if (text != null) {
                print(text + ":");
            }
        }
    }

    public static void print(final String string) {
        System.out.print(string);
    }

    public static void println(final Integer integer) {
        System.out.println(integer);
    }

    public static void println(final Integer option, final Double value, final String name,
            final String text) {
        // TODO: Switch case for different option
        // switch(option) {...}

        if (option % 0x100 == 0) {
            if (value != null) {
                print(value + ":");
            }
            if (name != null) {
                print(name + ":");
            }
            if (text != null) {
                println(text + ":");
            }
        }
    }

    public static void println(final Integer option, final Integer value, final String name,
            final String text) {
        // TODO: Switch case for different option
        // switch(option) {...}

        if (option % 0x100 == 0) {
            if (value != null) {
                print(value + ":");
            }
            if (name != null) {
                print(name + ":");
            }
            if (text != null) {
                println(text + ":");
            }
        }
    }

    public static void println(final Integer option, final Long value, final String name,
            final String text) {
        // TODO: Switch case for different option
        // switch(option) {...}

        if (option % 0x100 == 0) {
            if (value != null) {
                print(value + ":");
            }
            if (name != null) {
                print(name + ":");
            }
            if (text != null) {
                println(text + ":");
            }
        }
    }

    public static void println(final String string) {
        System.out.println(string);
    }
}
