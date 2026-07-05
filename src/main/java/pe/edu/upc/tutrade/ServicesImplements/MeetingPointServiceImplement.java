package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.MeetingPointRequestDTO;
import pe.edu.upc.tutrade.DTOs.MeetingPointResponseDTO;
import pe.edu.upc.tutrade.Entities.MeetingPoint;
import pe.edu.upc.tutrade.Entities.Trade;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IMeetingPointRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IMeetingPointService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingPointServiceImplement implements IMeetingPointService {

    @Autowired
    private IMeetingPointRepository meetingPointRepo;

    @Autowired
    private ITradeRepository tradeRepo;

    @Autowired
    private IUserRepository userRepo;

    private MeetingPointResponseDTO toDTO(MeetingPoint mp) {
        MeetingPointResponseDTO dto = new MeetingPointResponseDTO();
        dto.setIdMeetingPoint(mp.getIdMeetingPoint());
        dto.setAddress(mp.getAddressMeetingPoint());
        dto.setLatitude(mp.getLatitudeMeetingPoint());
        dto.setLongitude(mp.getLongitudeMeetingPoint());
        dto.setScheduledAt(mp.getScheduledAtMeetingPoint());
        dto.setTradeId(mp.getTrade().getIdTrade());
        return dto;
    }

    private static void validateMeetingPointFields(MeetingPointRequestDTO dto) {
        if (dto.getAddress() == null || dto.getAddress().isBlank()) {
            throw new RuntimeException("La dirección del punto de encuentro es obligatoria");
        }
        if (dto.getLatitude() < -90 || dto.getLatitude() > 90) {
            throw new RuntimeException("Latitud inválida (debe estar entre -90 y 90)");
        }
        if (dto.getLongitude() < -180 || dto.getLongitude() > 180) {
            throw new RuntimeException("Longitud inválida (debe estar entre -180 y 180)");
        }
        if (dto.getScheduledAt() == null) {
            throw new RuntimeException("La fecha y hora del encuentro son obligatorias");
        }
        if (dto.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La fecha del encuentro no puede ser en el pasado");
        }
    }

    private void validateParticipant(Trade trade, User user) {
        boolean isProposer = trade.getProposer().getIdUser() == user.getIdUser();
        boolean isReceiver = trade.getReceiver().getIdUser() == user.getIdUser();
        if (!isProposer && !isReceiver) {
            throw new RuntimeException("No eres parte de este trueque");
        }
    }

    @Override
    public MeetingPointResponseDTO create(MeetingPointRequestDTO dto, String email) {
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Trade trade = tradeRepo.findById(dto.getTradeId())
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));

        validateParticipant(trade, caller);

        if (!trade.getStatusTrade().equals("PENDING") && !trade.getStatusTrade().equals("ACCEPTED")) {
            throw new RuntimeException("Solo se puede proponer un punto de encuentro en un trueque pendiente o aceptado");
        }

        if (meetingPointRepo.existsByTrade_IdTrade(dto.getTradeId())) {
            throw new RuntimeException("Este trueque ya tiene un punto de encuentro. Usa PUT para modificarlo");
        }

        validateMeetingPointFields(dto);

        MeetingPoint mp = new MeetingPoint();
        mp.setTrade(trade);
        mp.setAddressMeetingPoint(dto.getAddress());
        mp.setLatitudeMeetingPoint(dto.getLatitude());
        mp.setLongitudeMeetingPoint(dto.getLongitude());
        mp.setScheduledAtMeetingPoint(dto.getScheduledAt());

        return toDTO(meetingPointRepo.save(mp));
    }

    @Override
    public MeetingPointResponseDTO getByTrade(int tradeId, String email) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = caller.getRole() != null &&
                caller.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        boolean isParticipant = trade.getProposer().getIdUser() == caller.getIdUser() ||
                trade.getReceiver().getIdUser() == caller.getIdUser();

        if (!isAdmin && !isParticipant) {
            throw new RuntimeException("No tienes permiso para ver este punto de encuentro");
        }

        return meetingPointRepo.findByTrade_IdTrade(tradeId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Este trueque no tiene un punto de encuentro"));
    }

    @Override
    public MeetingPointResponseDTO update(int id, MeetingPointRequestDTO dto, String email) {
        MeetingPoint mp = meetingPointRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de encuentro no encontrado"));
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Trade trade = mp.getTrade();
        validateParticipant(trade, caller);

        if (!trade.getStatusTrade().equals("PENDING") && !trade.getStatusTrade().equals("ACCEPTED")) {
            throw new RuntimeException("Solo se puede modificar el punto de encuentro de un trueque pendiente o aceptado");
        }

        validateMeetingPointFields(dto);

        mp.setAddressMeetingPoint(dto.getAddress());
        mp.setLatitudeMeetingPoint(dto.getLatitude());
        mp.setLongitudeMeetingPoint(dto.getLongitude());
        mp.setScheduledAtMeetingPoint(dto.getScheduledAt());

        return toDTO(meetingPointRepo.save(mp));
    }

    @Override
    public void delete(int id, String email) {
        MeetingPoint mp = meetingPointRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Punto de encuentro no encontrado"));
        User caller = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Trade trade = mp.getTrade();

        boolean isAdmin = caller.getRole() != null &&
                caller.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        boolean isProposer = trade.getProposer().getIdUser() == caller.getIdUser();
        boolean isReceiver = trade.getReceiver().getIdUser() == caller.getIdUser();

        if (!isAdmin && !isProposer && !isReceiver) {
            throw new RuntimeException("No tienes permiso para eliminar este punto de encuentro");
        }

        if (trade.getStatusTrade().equals("ACCEPTED")) {
            throw new RuntimeException("No se puede eliminar el punto de encuentro de un trueque completado");
        }

        meetingPointRepo.deleteById(id);
    }

    @Override
    public List<MeetingPointResponseDTO> listAll() {
        return meetingPointRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
}
