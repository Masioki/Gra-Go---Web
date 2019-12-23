package GoOnline.domain;


import Commands.PawnColor;
import GoOnline.domain.Game.Game;
import GoOnline.domain.Game.Move;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.awt.*;
import java.util.*;
@Entity
@Table(name = "player")
public class Player implements UserDetails {

    @OneToOne
    private Game game;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable= false)
    private String password;

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    //nasza zależność
    //TODO - cascade
   @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<Move> moves;
    public boolean move(int x, int y) {
        if (game == null) return false;
        //TODO - trzeba poprawić
        //return game.move(x, y, this);
        return false;
    }

    public boolean pass() {
        if (game == null) return false;
        return game.pass(this);
    }

    public void surrender() {
        if (game != null)
            game.surrender(this);
    }

    public Point getScore() {
        int own = game.getOwnScore(username);
        int opponent = game.getOpponentScore(username);
        return new Point(own, opponent);
    }

    public Map<Point, PawnColor> getCurrentGameBoard() {
        if (game == null) return new HashMap<>();
        return game.getBoard();
    }

    /* getters setters */

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        if (this.game != null) this.game.surrender(this);
        this.game = game;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
