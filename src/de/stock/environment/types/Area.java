package de.stock.environment.types;

import de.stock.environment.Environment;
import de.stock.environment.IEnvironment;

/**
 * An area is a type of environment and lies in the middle of the environment
 * hierarchy<br>
 * <br>
 * Requirements:<br>
 * - Its a geographical boundary<br>
 * - It must not have a unique proper name<br>
 * <br>
 * Typical areas are a forest or a desert
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Area extends Environment implements IEnvironment {

    public Area() {
        super();
    }
}
