package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.Entities.Category;
import pe.edu.upc.tutrade.Repositories.ICategoryRepository;
import pe.edu.upc.tutrade.Repositories.IItemRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.ICategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplement implements ICategoryService {

    @Autowired
    private ICategoryRepository cR;

    @Autowired
    private IItemRepository iR;

    private static void validateName(Category category) {
        if (category.getNameCategory() == null || category.getNameCategory().isBlank()) {
            throw new RuntimeException("El nombre de la categoría es obligatorio");
        }
    }

    @Override
    public Category insert(Category category) {
        validateName(category);
        return cR.save(category);
    }

    @Override
    public List<Category> list() {
        return cR.findAll();
    }

    @Override
    public Optional<Category> listId(int id) {
        return cR.findById(id);
    }

    @Override
    public Category update(int id, Category category) {
        Category existente = cR.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        validateName(category);
        existente.setNameCategory(category.getNameCategory());
        existente.setParent_idCategory(category.getParent_idCategory());
        return cR.save(existente);
    }

    @Override
    public void delete(int id) {
        if (!cR.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        if (!iR.findByCategory_IdCategory(id).isEmpty()) {
            throw new RuntimeException("No se puede eliminar: hay ítems asociados a esta categoría");
        }
        cR.deleteById(id);
    }
}
