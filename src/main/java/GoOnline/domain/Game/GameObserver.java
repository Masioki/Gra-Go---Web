package GoOnline.domain.Game;


import GoOnline.domain.GameCommandType;
import GoOnline.domain.PawnColor;

public interface GameObserver {
    void action(int x, int y, String username, PawnColor color, GameCommandType type);

}
