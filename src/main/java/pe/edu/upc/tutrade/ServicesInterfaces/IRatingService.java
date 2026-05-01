package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.RatingRequestDTO;
import pe.edu.upc.tutrade.DTOs.RatingResponseDTO;

import java.util.List;

public interface IRatingService {
    RatingResponseDTO create(RatingRequestDTO dto, String email);
    List<RatingResponseDTO> listByUser(int userId);
    List<RatingResponseDTO> listByTrade(int tradeId, String email);
    List<RatingResponseDTO> listAll();
}
