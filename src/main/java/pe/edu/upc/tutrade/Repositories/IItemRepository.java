package pe.edu.upc.tutrade.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upc.tutrade.Entities.Item;

import java.util.List;

public interface IItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCategory_IdCategory(int categoryId);
    List<Item> findByStatusItem(int status);
    List<Item> findByUser_IdUser(int userId);

    // HU46: ítems que el usuario recibió (era receiver en un trade ACCEPTED, side=2)
    @Query("SELECT ti.item FROM Trade_item ti WHERE ti.trade.receiver.idUser = :userId AND ti.trade.statusTrade = 'ACCEPTED' AND ti.sideTradeItem = 2")
    List<Item> findItemsReceivedByUser(@Param("userId") int userId);

    // Premium: contar ítems activos (status=1) de un usuario
    long countByUser_IdUserAndStatusItem(int userId, int statusItem);

    // HU03: búsqueda por texto en título o descripción
    List<Item> findByTitleItemContainingIgnoreCaseOrDescriptionItemContainingIgnoreCase(String titulo, String descripcion);
}
