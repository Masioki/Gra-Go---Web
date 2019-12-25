package GoOnline.dto;

public class StompMessageDTO {
    private StompMessageHeader stompMessageHeader;
    private MoveDTO move;

    public StompMessageHeader getStompMessageHeader() {
        return stompMessageHeader;
    }

    public void setStompMessageHeader(StompMessageHeader stompMessageHeader) {
        this.stompMessageHeader = stompMessageHeader;
    }

    public MoveDTO getMove() {
        return move;
    }

    public void setMove(MoveDTO move) {
        this.move = move;
    }
}
