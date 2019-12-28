package GoOnline.controllers;

import GoOnline.dto.MoveDTO;
import GoOnline.services.GameService;
import GoOnline.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;


@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private Gson gson;


    @GetMapping("/game/lobby")
    public String getLobby(Model model, Authentication authentication) {
        //model.addAttribute("ownGames", userService.getPlayerGames(authentication.getName()));
        //model.addAttribute("gamesList", gameService.getActiveGames());
        return "lobbyPage";
    }


    @GetMapping("/game/join/{gameID}")
    public String joinGame(@PathVariable("gameID") int gameID, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (!gameService.joinGame(gameID, authentication.getName())) {
            redirectAttributes.addAttribute("gameInProgress");
            return "lobbyPage";
        }
        model.addAttribute("gameData", gameService.getGameData(gameID));
        model.addAttribute("moves", gson.toJson(gameService.getGameMoves(gameID)));
        return "gamePage";
    }


    @GetMapping("/game/create")
    public String createGame(Model model, Authentication authentication) {
        int gameID = gameService.createGame(authentication.getName());
        model.addAttribute("gameData", gameService.getGameData(gameID));
        model.addAttribute("moves", gson.toJson(new ArrayList<MoveDTO>()));
        return "gamePage";
    }


    @MessageMapping("/game/{gameID}")
    @SendTo("/topic/game/{gameID}")
    public String sendMessage(@DestinationVariable("gameID") int gameID, @Payload String message, Principal principal) {
        MoveDTO move = gson.fromJson(message, MoveDTO.class);
        if (move != null && gameService.move(gameID, principal.getName(), move)) {
            move.setUsername(principal.getName());
            move.setWhite(gameService.isWhite(gameID, principal.getName()));
            return gson.toJson(move);
        } else return "ERROR";
    }

}
