package GoOnline.services;

import GoOnline.domain.Game.Game;
import GoOnline.domain.Game.GameStatus;
import GoOnline.domain.Game.Move;
import GoOnline.domain.Player;
import GoOnline.dto.GameData;
import GoOnline.dto.MoveDTO;
import GoOnline.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    /**
     * Stworz gre
     *
     * @param name username
     * @return gameID
     */
    public int createGame(String name) {
        Player p = userService.getPlayer(name);
        Game g = new Game(p, 19);
        if (p.getGame() != null) {
            p.getGame().setGameStatus(GameStatus.INTERRUPTED);
            gameRepository.save(p.getGame());
        }
        p.setGame(g);
        return gameRepository.save(g);
    }

    public GameData getGameData(int gameID) {
        Game g = gameRepository.getGame(gameID);
        if (g == null) return new GameData();
        return g.getGameData();
    }

    public List<Move> getGameMoves(int gameID) {
        Game g = gameRepository.getGame(gameID);
        if (g == null) return new ArrayList<>();
        return g.getMoves();
    }

    /**
     * Dolacz do gry
     *
     * @param gameID
     * @param name
     * @return czy sie udalo dolaczyc
     */
    public boolean joinGame(int gameID, String name) {
        Player p = userService.getPlayer(name);
        Game g = gameRepository.getGame(gameID);
        boolean result = g.addPlayer(p);
        if (result) {
            if (gameID != g.getGameID() && p.getGame() != null) {
                p.getGame().setGameStatus(GameStatus.INTERRUPTED);
                gameRepository.save(p.getGame());
            }
            p.setGame(g);
            gameRepository.save(g);
            return true;
        }
        return false;
    }

    public List<GameData> getActiveGames() {
        List<GameData> result = gameRepository.getActiveGames();
        if (result == null) return new ArrayList<>();
        return result;
    }

    public List<MoveDTO> move(int gameID, String username, MoveDTO move) throws Exception {
        Game g = gameRepository.getGame(gameID);
        Player p = userService.getPlayer(username);
        if (g == null || p == null) return new ArrayList<>();
        switch (move.getCommandType()) {
            case "MOVE" -> {
                List<Move> moveList = p.move(move.getX(), move.getY(), g);
                List<MoveDTO> result = new ArrayList<>();
                for (Move m : moveList) result.add(m.getDTO());
                return result;
            }
            case "PASS" -> {
                Move res = p.pass(g);
                return Collections.singletonList(res.getDTO());
            }
            case "SURRENDER" -> p.surrender(g);
        }
        return new ArrayList<>();
    }

    public boolean isWhite(int gameID, String username) {
        Game g = gameRepository.getGame(gameID);
        if (g != null) return g.getOwner().getUsername().equals(username);
        return false;
    }
}
