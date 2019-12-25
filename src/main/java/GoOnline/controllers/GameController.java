package GoOnline.controllers;

import GoOnline.domain.Game.Game;
import GoOnline.domain.Game.Move;
import GoOnline.domain.Player;
import GoOnline.dto.GameData;
import GoOnline.dto.MoveDTO;
import GoOnline.dto.StompMessageDTO;
import GoOnline.dto.StompMessageHeader;
import GoOnline.services.GameService;
import GoOnline.services.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;

import static GoOnline.dto.StompMessageHeader.ERROR;
import static GoOnline.dto.StompMessageHeader.SUCCESS;

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
        model.addAttribute("ownGames", userService.getPlayerGames(authentication.getName()));
        model.addAttribute("gamesList", gameService.getActiveGames());
        return "lobby";
    }

    @GetMapping("/game/join/{gameID}")
    public String joinGame(@PathVariable("gameID") int gameID, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (!gameService.joinGame(gameID, authentication.getName())) {
            redirectAttributes.addAttribute("gameInProgress");
            return "lobby";
        }
        model.addAttribute("gameData", gameService.getGameData(gameID));
        model.addAttribute("moves", gameService.getGameMoves(gameID));
        return "game";
    }

    @GetMapping("/game/create")
    public String createGame(Model model, Authentication authentication) {
        int gameID = gameService.createGame(authentication.getName());
        model.addAttribute("gameData", gameService.getGameData(gameID));
        model.addAttribute("moves", new ArrayList<MoveDTO>());
        return "game";
    }

    @MessageMapping("/game/{gameID}")
    @SendTo("/topic/game/{gameID}")
    public String sendMessage(@DestinationVariable("gameID") int gameID, @Payload String message, Principal principal) {
        StompMessageDTO mes = gson.fromJson(message, StompMessageDTO.class);
        MoveDTO move = mes.getMove();
        if (!gameService.move(gameID, principal.getName(), dto)) {
            StompMessageDTO ret = new StompMessageDTO();
            ret.setStompMessageHeader(ERROR);
            return gson.toJson(ret);
        } else {
            StompMessageDTO messageDTO = new StompMessageDTO();
            messageDTO.setStompMessageHeader(SUCCESS);
            messageDTO.setMove(move);
            return gson.toJson(messageDTO);
        }
    }

}
