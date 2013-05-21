package de.stock.environment.types;

import de.stock.environment.Environment;
import de.stock.environment.IEnvironment;

/**
 * A location is a type of environment and lies at the very top of the
 * environment hierarchy<br>
 * <br>
 * Requirements:<br>
 * - Its a geographical boundary<br>
 * - It has a unique proper name<br>
 * <br>
 * Typical locations are a country like Germany, a confederation like the EU or
 * the Sahara
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Location extends Environment implements IEnvironment {

    public Location() {
        super();
    }
}
