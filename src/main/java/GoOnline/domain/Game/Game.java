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
    private LinkedList<Move> moves;


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

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public int getOwnScore(String username) {
        if (username.equals(owner.getUsername())) return gameLogic.getFinalScore(true);
        return gameLogic.getFinalScore(false);
    }

    public int getOpponentScore(String username) {
        if (username.equals(owner.getUsername())) return gameLogic.getFinalScore(false);
        return gameLogic.getFinalScore(true);
    }


    public Map<Point, PawnColor> getBoard() {
        Map<Point, GridState> board = gameLogic.getBoard();
        Map<Point, PawnColor> newBoard = new HashMap<>(boardSize * boardSize);
        for (Point p : board.keySet()) {
            switch (board.get(p)) {
                case WHITE -> newBoard.put(p, PawnColor.WHITE);
                case BLACK -> newBoard.put(p, PawnColor.BLACK);
                case EMPTY -> newBoard.put(p, PawnColor.EMPTY);
            }
        }
        return newBoard;
    }

    /*
    WYKONYWAC PO KAZDEJ UDANEJ AKCJI
     */
    private void signalObservers(int x, int y, String username, PawnColor color, GameCommandType type) {
        observers.forEach(o -> o.action(x, y, username, color, type));
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

    public synchronized boolean move(Move move) {
        int x = move.getX();
        int y = move.getY();
        Player player = move.getPlayer();
        Map<Point, GridState> previous = new HashMap<>(gameLogic.getBoard());
        if (gameStatus != GameStatus.FINISHED && isPlayerTurn(player) && gameLogic.placePawn(x, y, player.getUsername().equals(owner.getUsername()))) {
            PawnColor color;
            if (player.getUsername().equals(owner.getUsername())) color = PawnColor.WHITE;
            else color = PawnColor.BLACK;
            signalObservers(x, y, player.getUsername(), color, GameCommandType.MOVE);
            Map<Point, GridState> after = gameLogic.getBoard();
            Map<Point, GridState> changes = new HashMap<>();
            for (Point point : after.keySet()) {
                if (previous.get(point) != after.get(point)) changes.put(point, after.get(point));
            }
            for (Point p : changes.keySet()) {
                switch (changes.get(p)) {
                    case EMPTY -> signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.EMPTY, GameCommandType.MOVE);
                    case WHITE -> signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.WHITE, GameCommandType.MOVE);
                    case BLACK -> signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.BLACK, GameCommandType.MOVE);
                }
            }
            lastMoved = player.getUsername();
            pass = false;
            moves.addLast(move);
            return true;
        }
        return false;
    }

    //TODO: dodawanie do moves
    public synchronized boolean pass(Player player) {
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
            signalObservers(0, 0, player.getUsername(), PawnColor.BLACK, GameCommandType.SURRENDER);
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

    public LinkedList<Move> getMoves() {
        return moves;
    }

    public void setMoves(LinkedList<Move> moves) {
        this.moves = moves;
    }


}
