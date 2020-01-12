package GoOnline.domain;

import GoOnline.domain.Game.Game;
import GoOnline.domain.Game.GridState;
import GoOnline.domain.Game.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static GoOnline.domain.Game.GridState.BLACK;
import static GoOnline.domain.Game.GridState.EMPTY;

public class Bot extends Player {

    private Map<Point, GridState> board;
    private Game game;

    public Bot(int boardSize, Game game) {
        this.game = game;
        System.out.println("Zobaczmy czy ID się zgadza: " + game.getGameID());
        setUsername("bot");
        //username = "bot";
        board = new HashMap<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.put(new Point(i, j), EMPTY);
            }
        }
        //TODO - zmieniony kod
        /*Runnable r = () -> {
            while (game.getGameStatus() == GameStatus.IN_PROGRESS) {
                if (game.isPlayerTurn(this)) doMove();
            }
        };
        Thread t = new Thread(r);
        t.start();*/
    }

    //TODO
    public void action(int x, int y, String username, GridState color, GameCommandType type) {
        if (type == GameCommandType.MOVE) board.replace(new Point(x, y), color);
    }

    //TODO - zmieniony kod
    public List<Move> doMove() {
        System.out.println("Bot wykonuje ruch");
        int max = 1;
        Point bestPoint = new Point(0, 0);

        for (Point p : board.keySet()) {
            if (board.get(p) == GridState.EMPTY && countPointBreaths(board, p) >= 2) {
                List<Point> list = new ArrayList<>();
                list.add(p);
                int temp = countGroupSize(p, list);
                if (temp > max) {
                    max = temp;
                    bestPoint = p;
                }
            }
        }
        if (max == 1) {
            return moveToRandom();
        }

        int x = (int) Math.round(bestPoint.getX());
        int y = (int) Math.round(bestPoint.getY());

        try {
            return move(x, y);
        } catch (Exception e) {
            return moveToRandom();
        }
    }

    private int countPointBreaths(Map<Point, GridState> map, Point point) {
        int breaths = 4;
        int x = (int) point.getX();
        int y = (int) point.getY();

        for (int i = 0; i < 3; i += 2)
            if (map.get(new Point(x, y - 1 + i)) != EMPTY) breaths--;

        for (int j = 0; j < 3; j += 2)
            if (map.get(new Point(x - 1 + j, y)) != EMPTY) breaths--;

        return breaths;
    }

    //obliczamy rozmiar grupy rekurencyjnie
    private int countGroupSize(Point point, List<Point> checked) {
        int sum = 1;
        checked.add(point);
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX(), (int) point.getY() + i - 1);
            if (!checked.contains(temp) && board.get(temp) == BLACK) {
                sum += countGroupSize(temp, checked);
            }
        }
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX() + i - 1, (int) point.getY());
            if (!checked.contains(temp) && board.get(temp) == BLACK) {
                sum += countGroupSize(temp, checked);
            }
        }
        return sum;
    }

    //TODO - zmieniony kod
    private List<Move> moveToRandom() {
        System.out.println("Bot wykonuje randomowy ruch");
        List<Point> emptyPlaceList = new ArrayList<>();
        //lista którą powinna nam dać game
        List<Move> m = null;
        for (Point p : board.keySet()) {
            if (board.get(p) == EMPTY) emptyPlaceList.add(p);
        }
        boolean done = false;
        while (emptyPlaceList.size() != 0) {
            int index = (int) (Math.random() * (emptyPlaceList.size() - 1));
            try {
                System.out.println("jesteśmy tu");
                System.out.println((int) emptyPlaceList.get(index).getX() + "  " + (int) emptyPlaceList.get(index).getY());
                m = move((int) emptyPlaceList.get(index).getX(), (int) emptyPlaceList.get(index).getY());
                done = true;
                break;
            } catch (Exception e) {
                System.out.println("nie tak miało być");
                emptyPlaceList.remove(index);
            }

        }
        if (!done) try {
            System.out.println("nie tak miało być");
            pass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }
}
