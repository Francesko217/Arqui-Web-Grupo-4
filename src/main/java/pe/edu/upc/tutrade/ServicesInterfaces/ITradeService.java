package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.TradeRequestDTO;
import pe.edu.upc.tutrade.DTOs.TradeResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ITradeService {
    TradeResponseDTO create(TradeRequestDTO dto, String email);
    List<TradeResponseDTO> list();
    Optional<TradeResponseDTO> listId(int id, String email);
    List<TradeResponseDTO> listMyTrades(String email);
    TradeResponseDTO accept(int id, String email);
    TradeResponseDTO reject(int id, String email);
    TradeResponseDTO cancel(int id, String email);

    // HU39
    List<TradeResponseDTO> listSentTrades(String email);
    List<TradeResponseDTO> listReceivedTrades(String email);

    // HU21
    List<TradeResponseDTO> listByItem(int itemId, String email);

    // HU16
    List<Object[]> rankingByTradeCount();
}
