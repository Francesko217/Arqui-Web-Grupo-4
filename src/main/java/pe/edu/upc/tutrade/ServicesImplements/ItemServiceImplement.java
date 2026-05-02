package pe.edu.upc.tutrade.ServicesImplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ItemResponseDTO;
import pe.edu.upc.tutrade.DTOs.ItemRequestDTO;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.Category;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.Entities.Profile;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.ICategoryRepository;
import pe.edu.upc.tutrade.Repositories.IItemRepository;
import pe.edu.upc.tutrade.Repositories.IProfileRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IItemService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private IProfileRepository profileRepo;

    @Autowired
    private ModelMapper modelMapper;

    // Builds a profileId→ProfileResponseDTO map from a list of items in one query.
    private Map<Integer, Profile> profileMapFor(List<Item> items) {
        Set<Integer> userIds = items.stream()
                .map(i -> i.getUser().getIdUser())
                .collect(Collectors.toSet());
        return profileRepo.findAllByUser_IdUserIn(userIds).stream()
                .collect(Collectors.toMap(p -> p.getUser().getIdUser(), p -> p));
    }

    private ItemResponseDTO toDTO(Item item, Map<Integer, Profile> profileMap) {
        ItemResponseDTO dto = modelMapper.map(item, ItemResponseDTO.class);
        UserResponseDTO userDTO = modelMapper.map(item.getUser(), UserResponseDTO.class);
        Profile profile = profileMap.get(item.getUser().getIdUser());
        if (profile != null) {
            userDTO.setProfile(ProfileServiceImplement.toDTO(profile));
        }
        dto.setUser(userDTO);
        return dto;
    }

    private List<ItemResponseDTO> toDTOList(List<Item> items) {
        Map<Integer, Profile> profileMap = profileMapFor(items);
        return items.stream()
                .map(item -> toDTO(item, profileMap))
                .collect(Collectors.toList());
    }

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

    public List<ItemResponseDTO> listAsDTO() {
        return toDTOList(iR.findAll());
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

    public Optional<ItemResponseDTO> listIdAsDTO(int id) {
        return iR.findById(id).map(item -> {
            Map<Integer, Profile> profileMap = profileMapFor(List.of(item));
            return toDTO(item, profileMap);
        });
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
