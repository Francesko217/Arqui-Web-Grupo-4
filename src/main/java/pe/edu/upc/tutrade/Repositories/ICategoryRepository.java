package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Category;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
}
