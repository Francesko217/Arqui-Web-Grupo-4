package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ItemRequestDTO;
import pe.edu.upc.tutrade.Entities.Category;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.ICategoryRepository;
import pe.edu.upc.tutrade.Repositories.IItemRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IItemService;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImplement implements IItemService {

    @Autowired
    private IItemRepository iR;

    @Autowired
    private IUserRepository uR;

    @Autowired
    private ICategoryRepository cR;

    @Override
    public Item insert(ItemRequestDTO dto, String email) {
        User owner = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Category category = cR.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Item item = new Item();
        item.setTitleItem(dto.getTitleItem());
        item.setDescriptionItem(dto.getDescriptionItem());
        item.setConditionItem(dto.getConditionItem());
        item.setStatusItem(dto.getStatusItem());
        item.setUser(owner);
        item.setCategory(category);
        return iR.save(item);
    }

    @Override
    public List<Item> list() {
        return iR.findAll();
    }

    @Override
    public List<Item> listByCategory(int categoryId) {
        return iR.findByCategory_IdCategory(categoryId);
    }

    @Override
    public List<Item> listByStatus(int status) {
        return iR.findByStatusItem(status);
    }

    @Override
    public List<Item> listByUser(int userId) {
        return iR.findByUser_IdUser(userId);
    }

    @Override
    public Optional<Item> listId(int id) {
        return iR.findById(id);
    }

    @Override
    public Item update(int id, ItemRequestDTO dto, String email) {
        Item item = iR.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        User requester = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");

        if (!isAdmin && item.getUser().getIdUser() != requester.getIdUser()) {
            throw new RuntimeException("No tienes permiso para modificar este item");
        }

        Category category = cR.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        item.setTitleItem(dto.getTitleItem());
        item.setDescriptionItem(dto.getDescriptionItem());
        item.setConditionItem(dto.getConditionItem());
        item.setStatusItem(dto.getStatusItem());
        item.setCategory(category);
        return iR.save(item);
    }

    @Override
    public void delete(int id, String email) {
        Item item = iR.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        User requester = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");

        if (!isAdmin && item.getUser().getIdUser() != requester.getIdUser()) {
            throw new RuntimeException("No tienes permiso para eliminar este item");
        }

        iR.deleteById(id);
    }
}
