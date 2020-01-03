package GoOnline.domain;

import GoOnline.domain.Game.*;

import java.util.*;
import java.awt.*;
import java.util.List;

import static GoOnline.domain.Game.GridState.BLACK;
import static GoOnline.domain.Game.GridState.EMPTY;

public class Bot extends Player {

    private Map<Point, GridState> board;
    private Game game;

    public Bot(int boardSize, Game game) {
        this.game = game;
        setUsername("bot");
        //username = "bot";
        board = new HashMap<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.put(new Point(i, j), EMPTY);
            }
        }
        Runnable r = () -> {
            while (game.getGameStatus() == GameStatus.IN_PROGRESS) {
                if (game.isPlayerTurn(this)) doMove();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    //TODO
    public void action(int x, int y, String username, GridState color, GameCommandType type) {
        if (type == GameCommandType.MOVE) board.replace(new Point(x, y), color);
    }

    private void doMove() {
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
            moveToRandom();
            return;
        }

        int x = (int) Math.round(bestPoint.getX());
        int y = (int) Math.round(bestPoint.getY());

        try {
            move(x, y, game);
        } catch (Exception e) {
            moveToRandom();
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

    private void moveToRandom() {
        List<Point> emptyPlaceList = new ArrayList<>();
        for (Point p : board.keySet()) {
            if (board.get(p) == EMPTY) emptyPlaceList.add(p);
        }
        boolean done = false;
        while (emptyPlaceList.size() != 0) {
            int index = (int) (Math.random() * (emptyPlaceList.size() - 1));
            try {
                List<Move> m = move((int) emptyPlaceList.get(index).getX(), (int) emptyPlaceList.get(index).getY(), game);
                done = true;
                break;
            } catch (Exception e) {
                emptyPlaceList.remove(index);
            }

        }
        if (!done) try {
            pass(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
