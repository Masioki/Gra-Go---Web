package GoOnline.domain;


import GoOnline.domain.Game.Game;
import GoOnline.domain.Game.Move;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Entity
@Table(name = "player")
public class Player implements UserDetails {

    @ManyToMany
    private List<Game> games;//TODO

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
    @Column(name = "password", nullable = false)
    private String password;

    //nasza zależność
    //TODO - cascade
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<Move> moves;

    public List<Move> move(int x, int y, Game game) throws Exception {
        if (game == null) return new ArrayList<>();
        Move m = new Move();
        m.setX(x);
        m.setY(y);
        m.setPlayer(this);
        return game.move(m);
    }

    public Move pass(Game game) throws Exception{
        if (game == null) throw new Exception("no available game");
        return game.pass(this);
    }

    public void surrender(Game game) {
        if (game != null)
            game.surrender(this);
    }

    public Point getScore(Game game) {
        int own = game.getOwnScore(username);
        int opponent = game.getOpponentScore(username);
        return new Point(own, opponent);
    }

    public Map<Point, PawnColor> getCurrentGameBoard(Game game) {
        if (game == null) return new HashMap<>();
        return null;//return game.getBoard();
    }

    /* getters setters */

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

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
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
