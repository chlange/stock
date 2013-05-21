package de.stock.environment.types;

import de.stock.environment.Environment;
import de.stock.environment.IEnvironment;

/**
 * A group is a type of environment and lies at the very bottom of the
 * environment hierarchy<br>
 * <br>
 * Requirements:<br>
 * - Its not a geographical boundary<br>
 * - Its a part of a location and/or area<br>
 * <br>
 * A typical group is cereal
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public class Group extends Environment implements IEnvironment {

    public Group() {
        super();
    }
}
