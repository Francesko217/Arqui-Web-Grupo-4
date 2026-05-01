package pe.edu.upc.tutrade.DTOs;

import java.util.List;

public class TradeRequestDTO {
    private int receiverId;
    private List<Integer> proposerItemIds;
    private List<Integer> receiverItemIds;

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public List<Integer> getProposerItemIds() {
        return proposerItemIds;
    }

    public void setProposerItemIds(List<Integer> proposerItemIds) {
        this.proposerItemIds = proposerItemIds;
    }

    public List<Integer> getReceiverItemIds() {
        return receiverItemIds;
    }

    public void setReceiverItemIds(List<Integer> receiverItemIds) {
        this.receiverItemIds = receiverItemIds;
    }
}
