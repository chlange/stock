package de.chlange.lemonade;

import de.stock.game.Player;
import de.stock.level.LevelDecorator;
import de.stock.settings.Settings_Output;
import de.stock.utils.Printer;

public class MyFirstTestLevel extends LevelDecorator {

    @Override
    public void conferAward() {
        Printer.println(Settings_Output.OUT_AWARD, 0, "Award earned", "You get 100$ extra money");
        Player.getInstance().incMoney(100);
    }

    @Override
    public boolean hasPassedLevel() {
        return (Player.getInstance().getMoney() >= 100) ? true : false;
    }

}
