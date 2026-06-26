package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.ReportRequestDTO;
import pe.edu.upc.tutrade.DTOs.ReportResponseDTO;

import java.util.List;

public interface IReportService {
    ReportResponseDTO create(ReportRequestDTO dto, String reporterEmail);
    List<ReportResponseDTO> listAll();
    List<ReportResponseDTO> listByReportedUser(int userId);
}
