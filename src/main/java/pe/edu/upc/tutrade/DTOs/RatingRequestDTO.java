package pe.edu.upc.tutrade.DTOs;

public class RatingRequestDTO {
    private int tradeId;
    private int ratedUserId;
    private int score;
    private String comment;

    public int getTradeId() { return tradeId; }
    public void setTradeId(int tradeId) { this.tradeId = tradeId; }

    public int getRatedUserId() { return ratedUserId; }
    public void setRatedUserId(int ratedUserId) { this.ratedUserId = ratedUserId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
