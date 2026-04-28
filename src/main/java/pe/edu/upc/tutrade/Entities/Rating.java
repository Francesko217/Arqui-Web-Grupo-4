package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name="Rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRating;

    @Column(name = "rated_idRating",nullable = false)
    private int rated_idRating;

    @Column(name = "scoreRating",nullable = false)
    private int scoreRating;

    @Column(name = "commentRating",length = 200, nullable = false)
    private String commentRating;

    @Column(name="created_atRating",nullable=false)
    private LocalDate created_atRating;
    @ManyToOne
    @JoinColumn(name="idTrade")
    private Trade trade;
    @ManyToOne
    @JoinColumn(name="idUser")
    private User user;

    public Rating() {
    }

    public Rating(int idRating, int rated_idRating, int scoreRating, String commentRating, LocalDate created_atRating, Trade trade, User user) {
        this.idRating = idRating;
        this.rated_idRating = rated_idRating;
        this.scoreRating = scoreRating;
        this.commentRating = commentRating;
        this.created_atRating = created_atRating;
        this.trade = trade;
        this.user = user;
    }

    public int getIdRating() {
        return idRating;
    }

    public void setIdRating(int idRating) {
        this.idRating = idRating;
    }

    public int getRated_idRating() {
        return rated_idRating;
    }

    public void setRated_idRating(int rated_idRating) {
        this.rated_idRating = rated_idRating;
    }

    public int getScoreRating() {
        return scoreRating;
    }

    public void setScoreRating(int scoreRating) {
        this.scoreRating = scoreRating;
    }

    public String getCommentRating() {
        return commentRating;
    }

    public void setCommentRating(String commentRating) {
        this.commentRating = commentRating;
    }

    public LocalDate getCreated_atRating() {
        return created_atRating;
    }

    public void setCreated_atRating(LocalDate created_atRating) {
        this.created_atRating = created_atRating;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
