package pe.edu.upc.tutrade.DTOs;

import java.time.LocalDate;
import java.util.List;

public class TradeResponseDTO {
    private int idTrade;
    private String statusTrade;
    private LocalDate created_atTrade;
    private LocalDate completed_atTrade;
    private UserResponseDTO proposer;
    private UserResponseDTO receiver;
    private List<ItemResponseDTO> proposerItems;
    private List<ItemResponseDTO> receiverItems;
    private MeetingPointResponseDTO meetingPoint;

    public int getIdTrade() {
        return idTrade;
    }

    public void setIdTrade(int idTrade) {
        this.idTrade = idTrade;
    }

    public String getStatusTrade() {
        return statusTrade;
    }

    public void setStatusTrade(String statusTrade) {
        this.statusTrade = statusTrade;
    }

    public LocalDate getCreated_atTrade() {
        return created_atTrade;
    }

    public void setCreated_atTrade(LocalDate created_atTrade) {
        this.created_atTrade = created_atTrade;
    }

    public LocalDate getCompleted_atTrade() {
        return completed_atTrade;
    }

    public void setCompleted_atTrade(LocalDate completed_atTrade) {
        this.completed_atTrade = completed_atTrade;
    }

    public UserResponseDTO getProposer() {
        return proposer;
    }

    public void setProposer(UserResponseDTO proposer) {
        this.proposer = proposer;
    }

    public UserResponseDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserResponseDTO receiver) {
        this.receiver = receiver;
    }

    public List<ItemResponseDTO> getProposerItems() {
        return proposerItems;
    }

    public void setProposerItems(List<ItemResponseDTO> proposerItems) {
        this.proposerItems = proposerItems;
    }

    public List<ItemResponseDTO> getReceiverItems() {
        return receiverItems;
    }

    public void setReceiverItems(List<ItemResponseDTO> receiverItems) {
        this.receiverItems = receiverItems;
    }

    public MeetingPointResponseDTO getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(MeetingPointResponseDTO meetingPoint) {
        this.meetingPoint = meetingPoint;
    }
}
