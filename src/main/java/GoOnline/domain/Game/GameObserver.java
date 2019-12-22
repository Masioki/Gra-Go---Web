package GoOnline.domain.Game;

import Commands.GameCommandType;
import Commands.PawnColor;

public interface GameObserver {
    void action(int x, int y, String username, PawnColor color, GameCommandType type);

}
