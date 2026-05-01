package pe.edu.upc.tutrade.ServicesImplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.RatingRequestDTO;
import pe.edu.upc.tutrade.DTOs.RatingResponseDTO;
import pe.edu.upc.tutrade.Entities.Rating;
import pe.edu.upc.tutrade.Entities.Trade;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.IRatingRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IRatingService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImplement implements IRatingService {

    @Autowired
    private IRatingRepository ratingRepo;

    @Autowired
    private ITradeRepository tradeRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    private RatingResponseDTO toDTO(Rating rating) {
        RatingResponseDTO dto = new RatingResponseDTO();
        dto.setIdRating(rating.getIdRating());
        dto.setScore(rating.getScoreRating());
        dto.setComment(rating.getCommentRating());
        dto.setCreatedAt(rating.getCreated_atRating());
        dto.setRater(modelMapper.map(rating.getUser(), pe.edu.upc.tutrade.DTOs.UserResponseDTO.class));
        dto.setRatedUserId(rating.getRated_idRating());
        dto.setTradeId(rating.getTrade().getIdTrade());
        return dto;
    }

    @Override
    public RatingResponseDTO create(RatingRequestDTO dto, String email) {
        User rater = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Trade trade = tradeRepo.findById(dto.getTradeId())
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));

        if (!trade.getStatusTrade().equals("ACCEPTED")) {
            throw new RuntimeException("Solo se puede valorar un trueque completado (ACCEPTED)");
        }

        boolean isProposer = trade.getProposer().getIdUser() == rater.getIdUser();
        boolean isReceiver = trade.getReceiver().getIdUser() == rater.getIdUser();

        if (!isProposer && !isReceiver) {
            throw new RuntimeException("No eres parte de este trueque");
        }

        int expectedRatedId = isProposer
                ? trade.getReceiver().getIdUser()
                : trade.getProposer().getIdUser();

        if (dto.getRatedUserId() != expectedRatedId) {
            throw new RuntimeException("Solo puedes valorar al otro participante del trueque");
        }

        if (ratingRepo.countByRaterAndTradeAndRated(rater.getIdUser(), dto.getTradeId(), dto.getRatedUserId()) > 0) {
            throw new RuntimeException("Ya has valorado a este usuario en este trueque");
        }

        if (dto.getScore() < 1 || dto.getScore() > 5) {
            throw new RuntimeException("La puntuación debe estar entre 1 y 5");
        }

        Rating rating = new Rating();
        rating.setUser(rater);
        rating.setRated_idRating(dto.getRatedUserId());
        rating.setTrade(trade);
        rating.setScoreRating(dto.getScore());
        rating.setCommentRating(dto.getComment());
        rating.setCreated_atRating(LocalDate.now());

        return toDTO(ratingRepo.save(rating));
    }

    @Override
    public List<RatingResponseDTO> listByUser(int userId) {
        return ratingRepo.findByRatedUserId(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RatingResponseDTO> listByTrade(int tradeId, String email) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new RuntimeException("Trueque no encontrado"));
        User requester = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = requester.getRole() != null &&
                requester.getRole().getNameRole().equalsIgnoreCase("ADMIN");
        boolean isInvolved = trade.getProposer().getIdUser() == requester.getIdUser() ||
                trade.getReceiver().getIdUser() == requester.getIdUser();

        if (!isAdmin && !isInvolved) {
            throw new RuntimeException("No tienes permiso para ver las valoraciones de este trueque");
        }

        return ratingRepo.findByTrade_IdTrade(tradeId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RatingResponseDTO> listAll() {
        return ratingRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
}
