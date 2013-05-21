package de.stock.action;

import java.util.ArrayList;

/**
 * Interface for actions<br>
 * <br>
 * The action system knows two types of actions which start and stop without the
 * players influence (they run a number of rounds)<br>
 * <br>
 * - {@link de.stock.event.types.MainEvent MainEvent} can start on his own or by
 * another event.<br>
 * <br>
 * - {@link de.stock.event.types.SubEvent SubEvent} can be started by another
 * event only.<br>
 * <br>
 * and one extraordinary<br>
 * <br>
 * - {@link de.stock.level.LevelPack LevelPack} gets chosen by player at startup
 * and has goals to complete to get to next level.<br>
 * &nbsp;&nbsp;These level packs are seperated into different level stages and
 * if one is finished the next (more difficult) level stage gets played<br>
 * &nbsp;&nbsp;(See {@link de.stock.level.LevelPack LevelPack} for further
 * information)<br>
 * <br>
 * Action developers <b>must</b> instantiate
 * {@link de.stock.event.types.MainEvent MainEvent},
 * {@link de.stock.event.types.SubEvent SubEvent} or
 * {@link de.stock.level.LevelPack LevelPack}<br>
 * They <b>must not</b> instantiate {@link de.stock.action.Action Action}
 * 
 * @author <a href="mailto:Christian_Lange@hotmail.com">chlange</a>
 */
public interface IAction {

    /**
     * Adds successor to action if it wasn't present already
     */
    public void addSuccessor(IAction successor);

    /**
     * Lets player choose from options and returns successor upon success
     * 
     * @return successor of action if available otherwise null
     */
    public IAction chooseOptions();

    public String getDescription();

    public String getName();

    /**
     * Returns the successor of the action
     * 
     * @return successor of action if available otherwise null
     */
    public IAction getSuccessor();

    public ArrayList<IAction> getSuccessors();

    public boolean hasOptions();

    public void hasOptions(boolean hasOptions);

    /**
     * Prints available options
     */
    public void printOptions();

    public void setDescription(String description);

    public void setName(String name);

    public void setSuccessors(ArrayList<IAction> list);
}
