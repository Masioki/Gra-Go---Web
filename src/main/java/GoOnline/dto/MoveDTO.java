package GoOnline.dto;

import GoOnline.domain.GameCommandType;

public class MoveDTO {
    private int x;
    private int y;
    private String commandType;

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getCommandType() {
        return commandType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
