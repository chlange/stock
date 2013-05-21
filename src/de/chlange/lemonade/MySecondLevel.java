package de.chlange.lemonade;

import de.stock.game.Player;
import de.stock.level.LevelDecorator;

public class MySecondLevel extends LevelDecorator {

    @Override
    public void conferAward() {
        // No award
    }

    @Override
    public boolean hasPassedLevel() {
        return (Player.getInstance().getMoney() >= 500) ? true : false;
    }
}
