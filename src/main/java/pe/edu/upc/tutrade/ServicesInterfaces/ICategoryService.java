package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.Entities.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Category insert(Category category);
    List<Category> list();
    Optional<Category> listId(int id);
    Category update(int id, Category category);
    void delete(int id);
}
