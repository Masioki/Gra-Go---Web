package GoOnline.domain.Game;

import GoOnline.domain.GameCommandType;
import GoOnline.domain.PawnColor;
import GoOnline.domain.Player;
import GoOnline.dto.GameData;

import javax.persistence.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "id", unique = true)
    private int gameID;
    //TODO - podwójna zależność do sprawdzenia
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private Player owner;
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private Player opponent;
    //mówimy hibernetowi że tej zmiennej ma nie ruszać
    @Transient
    private List<GameObserver> observers;// nie mapowac
    @Transient
    private GameLogic gameLogic;
    @Column(name = "boardSize", nullable = false)
    private int boardSize;
    @Transient
    private String lastMoved;
    @Transient
    private boolean pass;
    @Column(name = "gameStatus", nullable = false)
    private GameStatus gameStatus;
    @Transient
    private List<Move> moves;
    private int movesCount;


    public Game(Player owner, int boardSize) {
        gameID = (int) (Math.random() * 1000);
        this.owner = owner;
        this.boardSize = boardSize;
        observers = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
        pass = false;
        gameStatus = GameStatus.WAITING;
        lastMoved = "bot";
        moves = new LinkedList<>();
    }


    public boolean addPlayer(Player player) {
       /* if (!players.contains(player) && players.size() < 2) {
            players.add(player);
            return true;
        }
        return false;*/
        if (opponent == null) {
            opponent = player;
            gameStatus = GameStatus.IN_PROGRESS;
            return true;
        }
        return false;
    }

    public int getOwnScore(String username) {
        if (username.equals(owner.getUsername())) return gameLogic.getFinalScore(true);
        return gameLogic.getFinalScore(false);
    }

    public int getOpponentScore(String username) {
        if (username.equals(owner.getUsername())) return gameLogic.getFinalScore(false);
        return gameLogic.getFinalScore(true);
    }


    /*
    LOGIKA
     */

    public boolean isPlayerTurn(Player player) {
        //System.out.println(player.getUsername() + " " + lastMoved);
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return !player.getUsername().equals(lastMoved);
    }

    public synchronized List<Move> move(Move move) throws Exception {
        int x = move.getX();
        int y = move.getY();
        Player player = move.getPlayer();
        List<Move> resultMoves = new ArrayList<>();
        Map<Point, GridState> previous = new HashMap<>(gameLogic.getBoard());
        if (gameStatus != GameStatus.FINISHED && isPlayerTurn(player) && gameLogic.placePawn(x, y, player.getUsername().equals(owner.getUsername()))) {
            PawnColor color;
            if (player.getUsername().equals(owner.getUsername())) color = PawnColor.WHITE;
            else color = PawnColor.BLACK;
            move.setNumber(++movesCount);
            move.setColor(color);
            move.setGame(this);
            move.setMoveType(MoveType.MOVE);
            resultMoves.add(move);
            Map<Point, GridState> after = gameLogic.getBoard();
            Map<Point, GridState> changes = new HashMap<>();
            for (Point point : after.keySet()) {
                if (previous.get(point) != after.get(point)) changes.put(point, after.get(point));
            }
            for (Point p : changes.keySet()) {
                Move m = new Move();
                m.setX((int) p.getX());
                m.setY((int) p.getY());
                move.setNumber(++movesCount);
                move.setColor(PawnColor.EMPTY);
                move.setGame(this);
                move.setMoveType(MoveType.MOVE);
                resultMoves.add(m);
            }
            lastMoved = player.getUsername();
            pass = false;
            moves.addAll(resultMoves);
            return resultMoves;
        }
        throw new Exception("Bad move");
    }

    //TODO: dodawanie do moves
    public synchronized boolean pass(Player player) throws Exception {
        if (gameStatus != GameStatus.FINISHED && isPlayerTurn(player)) {
            if (pass) {
                int white = getOwnScore(owner.getUsername());
                int black = getOpponentScore(owner.getUsername());
                if (white == black) signalObservers(0, 0, null, PawnColor.BLACK, GameCommandType.DRAW);
                else if (white > black)
                    signalObservers(0, 0, owner.getUsername(), PawnColor.WHITE, GameCommandType.WIN);
                else {
                    List<Player> players = Arrays.asList(owner, opponent);
                    for (Player p : players) {
                        if (!owner.getUsername().equals(p.getUsername())) {
                            signalObservers(0, 0, p.getUsername(), PawnColor.BLACK, GameCommandType.WIN);
                            break;
                        }
                    }
                }
                gameStatus = GameStatus.FINISHED;
            }
            pass = true;
            signalObservers(0, 0, player.getUsername(), PawnColor.BLACK, GameCommandType.PASS);
            lastMoved = player.getUsername();
            return true;
        }
        return false;
    }

    public synchronized void surrender(Player player) {
        if (gameStatus != GameStatus.FINISHED && (opponent.getUsername().equals(player.getUsername()) || owner.getUsername().equals(player.getUsername()))) {
            Move m = new Move();
            m.setGame(this);
            m.setMoveType(MoveType.SURRENDER);
            m.setPlayer(player);
            m.setNumber(++movesCount);
            moves.add(m);
            gameStatus = GameStatus.FINISHED;
        }
    }

    /* Getters Setters */
    public GameData getGameData() {
        GameData gameData = new GameData();
        gameData.setGameID(gameID);
        gameData.setUsername(owner.getUsername());
        return gameData;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public List<GameObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<GameObserver> observers) {
        this.observers = observers;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getLastMoved() {
        return lastMoved;
    }

    public void setLastMoved(String lastMoved) {
        this.lastMoved = lastMoved;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }
}
