package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Report;

import java.util.List;

public interface IReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByReported_IdUser(int userId);
    List<Report> findByReporter_IdUser(int userId);
}
