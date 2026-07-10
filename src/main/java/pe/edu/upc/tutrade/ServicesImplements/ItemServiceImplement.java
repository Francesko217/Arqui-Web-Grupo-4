package pe.edu.upc.tutrade.ServicesImplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ItemResponseDTO;
import pe.edu.upc.tutrade.DTOs.ItemRequestDTO;
import pe.edu.upc.tutrade.Entities.Category;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.ICategoryRepository;
import pe.edu.upc.tutrade.Repositories.IItemRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.Config.UserMapper;
import pe.edu.upc.tutrade.ServicesInterfaces.IItemService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImplement implements IItemService {

    @Autowired
    private IItemRepository iR;

    @Autowired
    private IUserRepository uR;

    @Autowired
    private ICategoryRepository cR;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    private ItemResponseDTO toDTO(Item item) {
        ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
        dto.setUser(userMapper.toDTO(item.getUser()));
        return dto;
    }

    private List<ItemResponseDTO> toDTOList(List<Item> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private static void validateItemFields(ItemRequestDTO dto) {
        if (dto.getTitleItem() == null || dto.getTitleItem().isBlank()) {
            throw new RuntimeException("El título del ítem es obligatorio");
        }
        if (dto.getDescriptionItem() == null || dto.getDescriptionItem().isBlank()) {
            throw new RuntimeException("La descripción del ítem es obligatoria");
        }
        if (dto.getConditionItem() < 1 || dto.getConditionItem() > 4) {
            throw new RuntimeException("Condición inválida (debe ser 1=Nuevo, 2=Como nuevo, 3=Buen estado o 4=Usado)");
        }
    }

    @Override
    public Item insert(ItemRequestDTO dto, String email) {
        User owner = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Category category = cR.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!owner.getIs_verifiedUser()) {
            throw new RuntimeException("Tu cuenta no está verificada. Contacta al administrador.");
        }

        if (!owner.getIs_premiumUser()) {
            long activeItems = iR.countByUser_IdUserAndStatusItem(owner.getIdUser(), 1);
            if (activeItems >= 5) {
                throw new RuntimeException("Límite de 5 ítems activos alcanzado. Actualiza a premium para publicar más.");
            }
        }

        validateItemFields(dto);

        Item item = new Item();
        item.setTitleItem(dto.getTitleItem());
        item.setDescriptionItem(dto.getDescriptionItem());
        item.setConditionItem(dto.getConditionItem());
        // Un ítem siempre nace Disponible (1): pausar/activar/marcar-intercambiado
        // tienen sus propios endpoints con sus propias reglas de transición de estado.
        item.setStatusItem(1);
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

        validateItemFields(dto);

        item.setTitleItem(dto.getTitleItem());
        item.setDescriptionItem(dto.getDescriptionItem());
        item.setConditionItem(dto.getConditionItem());
        // El estado (disponible/pausado/intercambiado) NO se toca aquí: tiene sus
        // propios endpoints (pause/activate) con sus propias reglas de transición.
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

    public Page<ItemResponseDTO> listAsDTO(int page, int size) {
        return iR.findAll(PageRequest.of(page, size)).map(this::toDTO);
    }

    public List<ItemResponseDTO> listByCategoryAsDTO(int categoryId) {
        return toDTOList(iR.findByCategory_IdCategory(categoryId));
    }

    public List<ItemResponseDTO> listByStatusAsDTO(int status) {
        return toDTOList(iR.findByStatusItem(status));
    }

    public List<ItemResponseDTO> listByUserAsDTO(int userId) {
        return toDTOList(iR.findByUser_IdUser(userId));
    }

    // HU03: búsqueda por texto
    public List<ItemResponseDTO> searchAsDTO(String q) {
        return toDTOList(iR.findByTitleItemContainingIgnoreCaseOrDescriptionItemContainingIgnoreCase(q, q));
    }

    public Optional<ItemResponseDTO> listIdAsDTO(int id) {
        return iR.findById(id).map(this::toDTO);
    }

    @Override
    public Item pause(int id, String email) {
        Item item = iR.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        User requester = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        if (!isAdmin && item.getUser().getIdUser() != requester.getIdUser()) {
            throw new RuntimeException("No tienes permiso para pausar este item");
        }
        if (item.getStatusItem() != 1) {
            throw new RuntimeException("Solo se pueden pausar ítems disponibles (status=1)");
        }
        item.setStatusItem(2);
        return iR.save(item);
    }

    @Override
    public Item activate(int id, String email) {
        Item item = iR.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        User requester = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        if (!isAdmin && item.getUser().getIdUser() != requester.getIdUser()) {
            throw new RuntimeException("No tienes permiso para activar este item");
        }
        if (item.getStatusItem() != 2) {
            throw new RuntimeException("Solo se pueden activar ítems pausados (status=2)");
        }
        item.setStatusItem(1);
        return iR.save(item);
    }

    @Override
    public List<Item> listReceivedByUser(String email) {
        User user = uR.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return iR.findItemsReceivedByUser(user.getIdUser());
    }

    public List<ItemResponseDTO> listReceivedAsDTO(String email) {
        return toDTOList(listReceivedByUser(email));
    }
}
