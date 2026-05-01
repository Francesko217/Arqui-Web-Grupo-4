package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDate;

public class RatingResponseDTO {
    private int idRating;
    private int score;
    private String comment;
    private LocalDate createdAt;
    private UserResponseDTO rater;
    private int ratedUserId;
    private int tradeId;

    public int getIdRating() { return idRating; }
    public void setIdRating(int idRating) { this.idRating = idRating; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public UserResponseDTO getRater() { return rater; }
    public void setRater(UserResponseDTO rater) { this.rater = rater; }

    public int getRatedUserId() { return ratedUserId; }
    public void setRatedUserId(int ratedUserId) { this.ratedUserId = ratedUserId; }

    public int getTradeId() { return tradeId; }
    public void setTradeId(int tradeId) { this.tradeId = tradeId; }
}
