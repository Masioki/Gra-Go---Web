package GoOnline;


import GoOnline.database.DatabaseService;
import GoOnline.domain.Game.Game;
import GoOnline.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.tags.EditorAwareTag;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

