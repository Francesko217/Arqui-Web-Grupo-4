package pe.edu.upc.tutrade.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="Trade_item")
public class Trade_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTrade_item;

    @Column(name="sideTrade_item",nullable = false)
    private int sideTrade_item;
    @ManyToOne
    @JoinColumn(name="idItem")
    private Item item;

    @ManyToOne
    @JoinColumn(name ="idTrade")
    private Trade trade;

    public Trade_item() {
    }

    public Trade_item(int idTrade_item, int sideTrade_item, Item item, Trade trade) {
        this.idTrade_item = idTrade_item;
        this.sideTrade_item = sideTrade_item;
        this.item = item;
        this.trade = trade;
    }

    public int getIdTrade_item() {
        return idTrade_item;
    }

    public void setIdTrade_item(int idTrade_item) {
        this.idTrade_item = idTrade_item;
    }

    public int getSideTrade_item() {
        return sideTrade_item;
    }

    public void setSideTrade_item(int sideTrade_item) {
        this.sideTrade_item = sideTrade_item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }
}
