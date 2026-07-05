package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ReportRequestDTO;
import pe.edu.upc.tutrade.DTOs.ReportResponseDTO;
import pe.edu.upc.tutrade.Entities.Report;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IReportRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImplement implements IReportService {

    @Autowired
    private IReportRepository reportRepo;

    @Autowired
    private IUserRepository userRepo;

    private static ReportResponseDTO toDTO(Report r) {
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setIdReport(r.getIdReport());
        dto.setReason(r.getReasonReport());
        dto.setDescription(r.getDescriptionReport());
        dto.setStatus(r.getStatusReport());
        dto.setCreatedAt(r.getCreated_atReport());
        dto.setReportedUserId(r.getReported().getIdUser());
        dto.setReporterUserId(r.getReporter().getIdUser());
        return dto;
    }

    @Override
    public ReportResponseDTO create(ReportRequestDTO dto, String reporterEmail) {
        User reporter = userRepo.findByEmailUser(reporterEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User reported = userRepo.findById(dto.getReportedUserId())
                .orElseThrow(() -> new RuntimeException("Usuario reportado no encontrado"));

        if (reporter.getIdUser() == reported.getIdUser()) {
            throw new RuntimeException("No puedes reportarte a ti mismo");
        }
        if (dto.getReason() == null || dto.getReason().isBlank()) {
            throw new RuntimeException("El motivo del reporte es obligatorio");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new RuntimeException("La descripción del reporte es obligatoria");
        }

        Report report = new Report();
        report.setReasonReport(dto.getReason());
        report.setDescriptionReport(dto.getDescription());
        report.setStatusReport("PENDING");
        report.setCreated_atReport(LocalDate.now());
        report.setReporter(reporter);
        report.setReported(reported);

        return toDTO(reportRepo.save(report));
    }

    @Override
    public List<ReportResponseDTO> listAll() {
        return reportRepo.findAll().stream()
                .map(ReportServiceImplement::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDTO> listByReportedUser(int userId) {
        return reportRepo.findByReported_IdUser(userId).stream()
                .map(ReportServiceImplement::toDTO)
                .collect(Collectors.toList());
    }
}
