package pe.edu.upc.tutrade.ServicesImplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ItemResponseDTO;
import pe.edu.upc.tutrade.DTOs.MeetingPointResponseDTO;
import pe.edu.upc.tutrade.DTOs.TradeRequestDTO;
import pe.edu.upc.tutrade.DTOs.TradeResponseDTO;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.Entities.MeetingPoint;
import pe.edu.upc.tutrade.Entities.Trade;
import pe.edu.upc.tutrade.Entities.Trade_item;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IItemRepository;
import pe.edu.upc.tutrade.Repositories.IMeetingPointRepository;
import pe.edu.upc.tutrade.Repositories.ITradeItemRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.ITradeService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TradeServiceImplement implements ITradeService {

    @Autowired
    private ITradeRepository tradeRepo;

    @Autowired
    private ITradeItemRepository tradeItemRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IItemRepository itemRepo;

    @Autowired
    private IMeetingPointRepository meetingPointRepo;

    @Autowired
    private ModelMapper modelMapper;

    private MeetingPointResponseDTO toMeetingPointDTO(MeetingPoint mp) {
        MeetingPointResponseDTO dto = new MeetingPointResponseDTO();
        dto.setIdMeetingPoint(mp.getIdMeetingPoint());
        dto.setAddress(mp.getAddressMeetingPoint());
        dto.setLatitude(mp.getLatitudeMeetingPoint());
        dto.setLongitude(mp.getLongitudeMeetingPoint());
        dto.setScheduledAt(mp.getScheduledAtMeetingPoint());
        dto.setTradeId(mp.getTrade().getIdTrade());
        return dto;
    }

    private TradeResponseDTO toDTO(Trade trade) {
        TradeResponseDTO dto = new TradeResponseDTO();
        dto.setIdTrade(trade.getIdTrade());
        dto.setStatusTrade(trade.getStatusTrade());
        dto.setCreated_atTrade(trade.getCreated_atTrade());
        dto.setCompleted_atTrade(trade.getCompleted_atTrade());
        dto.setProposer(modelMapper.map(trade.getProposer(), UserResponseDTO.class));
        dto.setReceiver(modelMapper.map(trade.getReceiver(), UserResponseDTO.class));

        List<Trade_item> tradeItems = tradeItemRepo.findByTrade_IdTrade(trade.getIdTrade());
        dto.setProposerItems(tradeItems.stream()
                .filter(ti -> ti.getSideTradeItem() == 1)
                .map(ti -> modelMapper.map(ti.getItem(), ItemResponseDTO.class))
                .collect(Collectors.toList()));
        dto.setReceiverItems(tradeItems.stream()
                .filter(ti -> ti.getSideTradeItem() == 2)
                .map(ti -> modelMapper.map(ti.getItem(), ItemResponseDTO.class))
                .collect(Collectors.toList()));

        meetingPointRepo.findByTrade_IdTrade(trade.getIdTrade())
                .ifPresent(mp -> dto.setMeetingPoint(toMeetingPointDTO(mp)));

        return dto;
    }

    private void validateItemAvailable(Item item) {
        if (item.getStatusItem() != 1) {
            throw new RuntimeException("El item '" + item.getTitleItem() + "' no está disponible");
        }
        if (tradeItemRepo.existsByItem_IdItemAndTrade_StatusTrade(item.getIdItem(), "PENDING")) {
            throw new RuntimeException("El item '" + item.getTitleItem() + "' ya está en un trueque pendiente");
        }
    }

    @Override
    public TradeResponseDTO create(TradeRequestDTO dto, String email) {
        User proposer = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User receiver = userRepo.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));

        if (proposer.getIdUser() == receiver.getIdUser()) {
            throw new RuntimeException("No puedes proponer un trueque contigo mismo");
        }

        List<Item> proposerItems = dto.getProposerItemIds().stream()
                .map(id -> itemRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Item no encontrado: " + id)))
                .collect(Collectors.toList());

        for (Item item : proposerItems) {
            if (item.getUser().getIdUser() != proposer.getIdUser()) {
                throw new RuntimeException("El item '" + item.getTitleItem() + "' no te pertenece");
            }
            validateItemAvailable(item);
        }

        List<Item> receiverItems = dto.getReceiverItemIds().stream()
                .map(id -> itemRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Item no encontrado: " + id)))
                .collect(Collectors.toList());

        for (Item item : receiverItems) {
            if (item.getUser().getIdUser() != receiver.getIdUser()) {
                throw new RuntimeException("El item '" + item.getTitleItem() + "' no pertenece al receptor");
            }
            validateItemAvailable(item);
        }

        Trade trade = new Trade();
        trade.setProposer(proposer);
        trade.setReceiver(receiver);
        trade.setStatusTrade("PENDING");
        trade.setCreated_atTrade(LocalDate.now());
        trade = tradeRepo.save(trade);

        for (Item item : proposerItems) {
            Trade_item ti = new Trade_item();
            ti.setTrade(trade);
            ti.setItem(item);
            ti.setSideTradeItem(1);
            tradeItemRepo.save(ti);
        }
        for (Item item : receiverItems) {
            Trade_item ti = new Trade_item();
            ti.setTrade(trade);
            ti.setItem(item);
            ti.setSideTradeItem(2);
            tradeItemRepo.save(ti);
        }

        return toDTO(trade);
    }

    @Override
    public List<TradeResponseDTO> list() {
        return tradeRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<TradeResponseDTO> listId(int id, String email) {
        Trade trade = tradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User requester = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        boolean isInvolved = trade.getProposer().getIdUser() == requester.getIdUser() ||
                trade.getReceiver().getIdUser() == requester.getIdUser();

        if (!isAdmin && !isInvolved) {
            throw new RuntimeException("No tienes permiso para ver este trueque");
        }

        return Optional.of(toDTO(trade));
    }

    @Override
    public List<TradeResponseDTO> listMyTrades(String email) {
        User user = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<Integer> seen = new HashSet<>();
        List<Trade> allTrades = new ArrayList<>();
        for (Trade t : tradeRepo.findByProposer_IdUser(user.getIdUser())) {
            if (seen.add(t.getIdTrade())) allTrades.add(t);
        }
        for (Trade t : tradeRepo.findByReceiver_IdUser(user.getIdUser())) {
            if (seen.add(t.getIdTrade())) allTrades.add(t);
        }

        return allTrades.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public TradeResponseDTO accept(int id, String email) {
        Trade trade = tradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User receiver = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (trade.getReceiver().getIdUser() != receiver.getIdUser()) {
            throw new RuntimeException("Solo el receptor puede aceptar el trueque");
        }
        if (!trade.getStatusTrade().equals("PENDING")) {
            throw new RuntimeException("El trueque no está en estado pendiente");
        }

        List<Trade_item> tradeItems = tradeItemRepo.findByTrade_IdTrade(id);
        for (Trade_item ti : tradeItems) {
            Item item = ti.getItem();
            item.setStatusItem(3);
            itemRepo.save(item);
        }

        trade.setStatusTrade("ACCEPTED");
        trade.setCompleted_atTrade(LocalDate.now());
        tradeRepo.save(trade);

        return toDTO(trade);
    }

    @Override
    public TradeResponseDTO reject(int id, String email) {
        Trade trade = tradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User receiver = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (trade.getReceiver().getIdUser() != receiver.getIdUser()) {
            throw new RuntimeException("Solo el receptor puede rechazar el trueque");
        }
        if (!trade.getStatusTrade().equals("PENDING")) {
            throw new RuntimeException("El trueque no está en estado pendiente");
        }

        trade.setStatusTrade("REJECTED");
        tradeRepo.save(trade);

        return toDTO(trade);
    }

    @Override
    public TradeResponseDTO cancel(int id, String email) {
        Trade trade = tradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User requester = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isProposer = trade.getProposer().getIdUser() == requester.getIdUser();
        boolean isReceiver = trade.getReceiver().getIdUser() == requester.getIdUser();
        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");

        if (!isProposer && !isReceiver && !isAdmin) {
            throw new RuntimeException("No tienes permiso para cancelar este trueque");
        }
        if (!trade.getStatusTrade().equals("PENDING")) {
            throw new RuntimeException("El trueque no está en estado pendiente");
        }

        trade.setStatusTrade("CANCELLED");
        tradeRepo.save(trade);

        return toDTO(trade);
    }
}
