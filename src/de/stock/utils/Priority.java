package de.stock.utils;

/**
 * Priorities for events<br>
 * <br>
 * This classification is required to achieve a balance in the game<br>
 * <br>
 * A limited number of events of each priority are allowed to run at the same
 * time only and<br>
 * a defined maximum number of events are allowed to run at the same time<br>
 * (See {@link de.stock.settings.Settings_Event Settings_Event})
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public enum Priority {
    LOW, MID, HIGH
}
