package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.Config.ClimatiqClient;
import pe.edu.upc.tutrade.Config.EnvironmentalImpactFactors;
import pe.edu.upc.tutrade.DTOs.ImpactResponseDTO;
import pe.edu.upc.tutrade.DTOs.ImpactResponseDTO.CategoryImpactDTO;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.Entities.Trade;
import pe.edu.upc.tutrade.Entities.Trade_item;
import pe.edu.upc.tutrade.Entities.User;
import pe.edu.upc.tutrade.Repositories.ITradeItemRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.Repositories.IUserRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IImpactService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImpactServiceImplement implements IImpactService {

    private static final String ACCEPTED = "ACCEPTED";
    private static final int SIDE_PROPOSER = 1;
    private static final int SIDE_RECEIVER = 2;

    @Autowired
    private ITradeRepository tradeRepo;

    @Autowired
    private ITradeItemRepository tradeItemRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private EnvironmentalImpactFactors fallback;

    @Autowired
    private ClimatiqClient climatiq;

    @Override
    public ImpactResponseDTO getUserImpact(String email) {
        User user = userRepo.findByEmailUser(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Trade> asProposer =
                tradeRepo.findByProposer_IdUserAndStatusTrade(user.getIdUser(), ACCEPTED);
        List<Trade> asReceiver =
                tradeRepo.findByReceiver_IdUserAndStatusTrade(user.getIdUser(), ACCEPTED);

        // nº de artículos reutilizados por categoría (los que el usuario adquirió)
        Map<String, Integer> itemsByCategory = new LinkedHashMap<>();
        for (Trade trade : asProposer) {
            countSide(itemsByCategory, trade, SIDE_RECEIVER); // proposer adquiere lado receiver
        }
        for (Trade trade : asReceiver) {
            countSide(itemsByCategory, trade, SIDE_PROPOSER); // receiver adquiere lado proposer
        }

        return build(itemsByCategory, asProposer.size() + asReceiver.size());
    }

    @Override
    public ImpactResponseDTO getCommunityImpact() {
        List<Trade> completed = tradeRepo.findByStatusTrade(ACCEPTED);

        Map<String, Integer> itemsByCategory = new LinkedHashMap<>();
        for (Trade trade : completed) {
            // a nivel comunidad cuentan todos los artículos reutilizados (ambos lados)
            for (Trade_item ti : tradeItemRepo.findByTrade_IdTrade(trade.getIdTrade())) {
                addItem(itemsByCategory, ti.getItem());
            }
        }
        return build(itemsByCategory, completed.size());
    }

    private void countSide(Map<String, Integer> itemsByCategory, Trade trade, int side) {
        for (Trade_item ti : tradeItemRepo.findByTrade_IdTrade(trade.getIdTrade())) {
            if (ti.getSideTradeItem() == side) {
                addItem(itemsByCategory, ti.getItem());
            }
        }
    }

    private void addItem(Map<String, Integer> itemsByCategory, Item item) {
        if (item == null) {
            return;
        }
        String categoryName = item.getCategory() != null ? item.getCategory().getNameCategory() : null;
        String label = (categoryName == null || categoryName.isBlank()) ? "Otros" : categoryName;
        itemsByCategory.merge(label, 1, Integer::sum);
    }

    /**
     * Calcula el CO2e total a partir del nº de artículos por categoría. Llama a Climatiq UNA vez
     * por categoría (el CO2e por artículo es constante por categoría) y multiplica por la cantidad.
     */
    private ImpactResponseDTO build(Map<String, Integer> itemsByCategory, int tradesCompleted) {
        double totalCo2 = 0;
        int totalItems = 0;
        boolean climatiqUsed = false;
        List<CategoryImpactDTO> breakdown = new ArrayList<>();

        for (Map.Entry<String, Integer> e : itemsByCategory.entrySet()) {
            String category = e.getKey();
            int count = e.getValue();

            Optional<Double> climatiqCo2 = climatiq.estimateCo2(category);
            double co2PerItem = climatiqCo2.orElseGet(() -> fallback.co2For(category));
            if (climatiqCo2.isPresent()) {
                climatiqUsed = true;
            }

            double categoryCo2 = round(co2PerItem * count);
            totalCo2 += categoryCo2;
            totalItems += count;

            CategoryImpactDTO c = new CategoryImpactDTO(category);
            c.setItems(count);
            c.setCo2SavedKg(categoryCo2);
            breakdown.add(c);
        }

        ImpactResponseDTO dto = new ImpactResponseDTO();
        dto.setCo2SavedKg(round(totalCo2));
        dto.setItemsReused(totalItems);
        dto.setTradesCompleted(tradesCompleted);
        dto.setSource(climatiqUsed ? "climatiq" : "estimado");
        dto.setBreakdown(breakdown);
        return dto;
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
