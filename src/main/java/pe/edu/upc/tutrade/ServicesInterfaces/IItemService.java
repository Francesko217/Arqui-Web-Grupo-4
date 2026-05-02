package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.ItemRequestDTO;
import pe.edu.upc.tutrade.Entities.Item;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    Item insert(ItemRequestDTO dto, String email);
    List<Item> list();
    List<Item> listByCategory(int categoryId);
    List<Item> listByStatus(int status);
    List<Item> listByUser(int userId);
    Optional<Item> listId(int id);
    Item update(int id, ItemRequestDTO dto, String email);
    void delete(int id, String email);

    // HU15
    Item pause(int id, String email);
    Item activate(int id, String email);

    // HU46
    List<Item> listReceivedByUser(String email);
}
