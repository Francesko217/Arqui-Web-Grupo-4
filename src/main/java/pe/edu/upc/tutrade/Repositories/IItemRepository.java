package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.tutrade.Entities.Item;

import java.util.List;

public interface IItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCategory_IdCategory(int categoryId);
    List<Item> findByStatusItem(int status);
    List<Item> findByUser_IdUser(int userId);
}
