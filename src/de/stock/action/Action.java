package de.stock.action;

import java.util.ArrayList;

import de.stock.settings.Settings_Output;
import de.stock.utils.InputReader;
import de.stock.utils.Printer;
import de.stock.utils.Utils;

/**
 * Base class of actions<br>
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
public class Action implements IAction {

    private String             name;
    private String             description;
    /**
     * One of the successors gets called (randomly if hasOptions is false or
     * player chosen if hasOptions if true) if the action is over
     */
    private ArrayList<IAction> successors;
    /**
     * Indicates whether the successors are options the player can choose from
     * or not
     */
    private boolean            hasOptions;

    protected Action() {
        name = new String("");
        description = new String("");
        successors = new ArrayList<IAction>();
        hasOptions = false;
    }

    /**
     * Adds successor to action if it wasn't present already
     */
    @Override
    public void addSuccessor(final IAction successor) {
        if (getSuccessors() == null) {
            setSuccessors(new ArrayList<IAction>());
        }

        if (getSuccessors().contains(successor) == false) {
            getSuccessors().add(successor);
        }
    }

    @Override
    /**
     *  Lets player choose from options and returns successor upon success
     *  
     *  @return
     *  successor of action if available otherwise null
     */
    public IAction chooseOptions() {

        if (getSuccessors() == null || getSuccessors().size() == 0) {
            return null;
        }

        if (getSuccessors().size() == 1) {
            return getSuccessors().get(0);
        }

        Integer poll = null;
        printOptions();

        while (poll == null || poll < 0 || poll >= getSuccessors().size()) {
            poll = InputReader.readInteger();
            if (poll < 0 || poll >= getSuccessors().size()) {
                Printer.print(Settings_Output.OUT_ERROR, "Option Error", 0, "Option Error",
                        "Option out of index");
                continue;
            }
        }

        return getSuccessors().get(poll);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    /**
     *  Returns the successor of the action
     *
     * @return
     *  successor of action if available otherwise null
     */
    public IAction getSuccessor() {
        IAction successor = null;

        if (successors == null || getSuccessors().size() == 0) {
            return null;
        }

        if (hasOptions()) {
            printOptions();
            successor = chooseOptions();
        } else {
            // Get random successor
            final Integer pos = Utils.random(0, getSuccessors().size() - 1);
            successor = getSuccessors().get(pos);
        }

        return successor;
    }

    @Override
    public ArrayList<IAction> getSuccessors() {
        return successors;
    }

    @Override
    public boolean hasOptions() {
        return hasOptions;
    }

    @Override
    public void hasOptions(final boolean hasOptions) {
        this.hasOptions = hasOptions;
    }

    @Override
    /**
     * Prints available options
     */
    public void printOptions() {
        if (getSuccessors() == null || getSuccessors().size() == 0) {
            return;
        }

        Printer.print(Settings_Output.OUT_OPTION_HEAD, "Please choose an option");

        Integer i = 0;
        for (final IAction action : getSuccessors()) {
            Printer.print(Settings_Output.OUT_OPTION, i, action.getName(), action.getDescription());
            i++;
        }
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setSuccessors(final ArrayList<IAction> list) {
        successors = list;
    }
}
