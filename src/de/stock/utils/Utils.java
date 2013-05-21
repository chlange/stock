package de.stock.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.stock.event.types.MainEvent;

/**
 * Provides utilities for the game like a {@link #random(Integer,Integer)}
 * method
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public final class Utils {

    /**
     * Get the class name for a depth in call stack
     * 
     * @param depth
     *            depth in the call stack (0 means current class, 1 means
     *            calling class, ...)
     * @return the class name
     */
    public static String getClassName(final int depth) {
        if (depth < 0) {
            return "(index out of range)";
        }

        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int pos = stack.length - depth - 1;

        if (pos >= 0) {
            final String parts[] = stack[pos].getClassName().split("\\.");
            return parts[parts.length - 1];
        } else {
            return "(index out of range)";
        }
    }

    /**
     * Get the method name for a depth in call stack
     * 
     * @param depth
     *            depth in the call stack (0 means current method, 1 means call
     *            method, ...)
     * @return the method name
     */
    public static String getMethodName(final int depth) {
        if (depth < 0) {
            return "(index out of range)";
        }

        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final int pos = stack.length - depth - 1;

        if (pos >= 0) {
            return stack[pos].getMethodName();
        } else {
            return "(index out of range)";
        }
    }

    /**
     * Returns double inbetween range of min -> max
     * 
     * @param min
     *            Lower range bound
     * @param max
     *            Upper range bound
     * @return double value inbetween range
     */
    public static Double random(Double min, Double max) {
        if (min == max) {
            return min;
        }

        final Random rand = new Random();

        if (min > max) {
            final Double tmp = min;
            min = max;
            max = tmp;
        }

        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Returns integer inbetween range of min -> max
     * 
     * @param min
     *            Lower range bound
     * @param max
     *            Upper range bound
     * @return integer value inbetween range
     */
    public static Integer random(Integer min, Integer max) {
        if (min == max) {
            return min;
        }

        if (min > max) {
            final Integer tmp = min;
            min = max;
            max = tmp;
        }

        final Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    /**
     * Shuffles the given list randomly
     * 
     * @param mainEvents
     *            array to shuffle
     */
    public static void shuffle(final ArrayList<MainEvent> mainEvents) {
        Collections.shuffle(mainEvents);
    }

    /**
     * Shuffles the given list randomly
     * 
     * @param list
     *            list to shuffle
     */
    public static void shuffle(final List<Object> list) {
        Collections.shuffle(list);
    }
}
