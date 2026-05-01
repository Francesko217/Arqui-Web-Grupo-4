package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.MeetingPointRequestDTO;
import pe.edu.upc.tutrade.DTOs.MeetingPointResponseDTO;

import java.util.List;

public interface IMeetingPointService {
    MeetingPointResponseDTO create(MeetingPointRequestDTO dto, String email);
    MeetingPointResponseDTO getByTrade(int tradeId, String email);
    MeetingPointResponseDTO update(int id, MeetingPointRequestDTO dto, String email);
    void delete(int id, String email);
    List<MeetingPointResponseDTO> listAll();
}
