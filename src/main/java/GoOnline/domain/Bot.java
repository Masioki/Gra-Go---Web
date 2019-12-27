package GoOnline.domain;

import GoOnline.domain.Game.Game;
import GoOnline.domain.Game.GameObserver;
import GoOnline.domain.Game.GameStatus;

import java.util.*;
import java.awt.*;
import java.util.List;

public class Bot extends Player implements GameObserver {

    private Map<Point, PawnColor> board;
    private Game game;

    public Bot(int boardSize, Game game) {
        this.game = game;
        setUsername("bot");
        //username = "bot";
        board = new HashMap<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.put(new Point(i, j), PawnColor.EMPTY);
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

    @Override
    public void action(int x, int y, String username, PawnColor color, GameCommandType type) {
        if (type == GameCommandType.MOVE) board.replace(new Point(x, y), color);
    }

    private void doMove() {
        int max = 1;
        Point bestPoint = new Point(0, 0);

        for (Point p : board.keySet()) {
            if (board.get(p) == PawnColor.EMPTY && countPointBreaths(board, p) >= 2) {
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
        boolean result = move(x, y, game);
        if (!result) {
            moveToRandom();
        }
    }

    private int countPointBreaths(Map<Point, PawnColor> map, Point point) {
        int breaths = 4;
        int x = (int) point.getX();
        int y = (int) point.getY();

        for (int i = 0; i < 3; i += 2)
            if (map.get(new Point(x, y - 1 + i)) != PawnColor.EMPTY) breaths--;

        for (int j = 0; j < 3; j += 2)
            if (map.get(new Point(x - 1 + j, y)) != PawnColor.EMPTY) breaths--;

        return breaths;
    }

    //obliczamy rozmiar grupy rekurencyjnie
    private int countGroupSize(Point point, List<Point> checked) {
        int sum = 1;
        checked.add(point);
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX(), (int) point.getY() + i - 1);
            if (!checked.contains(temp) && board.get(temp) == PawnColor.BLACK) {
                sum += countGroupSize(temp, checked);
            }
        }
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX() + i - 1, (int) point.getY());
            if (!checked.contains(temp) && board.get(temp) == PawnColor.BLACK) {
                sum += countGroupSize(temp, checked);
            }
        }
        return sum;
    }

    private void moveToRandom() {
        List<Point> emptyPlaceList = new ArrayList<>();
        for (Point p : board.keySet()) {
            if (board.get(p) == PawnColor.EMPTY) emptyPlaceList.add(p);
        }

        boolean done = false;
        while (emptyPlaceList.size() != 0) {
            int index = (int) (Math.random() * (emptyPlaceList.size() - 1));
            if (move((int) emptyPlaceList.get(index).getX(), (int) emptyPlaceList.get(index).getY(), game)) {
                done = true;
                break;
            }
            emptyPlaceList.remove(index);
        }
        if (!done) pass(game);

    }
}
