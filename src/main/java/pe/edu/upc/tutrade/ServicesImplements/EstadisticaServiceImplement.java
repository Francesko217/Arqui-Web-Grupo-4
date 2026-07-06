package pe.edu.upc.tutrade.ServicesImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.tutrade.DTOs.ConteoDTO;
import pe.edu.upc.tutrade.Repositories.IItemRepository;
import pe.edu.upc.tutrade.Repositories.ITradeRepository;
import pe.edu.upc.tutrade.ServicesInterfaces.IEstadisticaService;

import java.util.List;

@Service
public class EstadisticaServiceImplement implements IEstadisticaService {

    @Autowired
    private IItemRepository itemRepo;

    @Autowired
    private ITradeRepository tradeRepo;

    @Override
    public List<ConteoDTO> itemsPorCategoria() {
        return itemRepo.contarPorCategoria();
    }

    @Override
    public List<ConteoDTO> truequesPorEstado() {
        return tradeRepo.contarPorEstado();
    }

    @Override
    public List<ConteoDTO> rankingUsuarios() {
        return tradeRepo.rankingUsuarios();
    }
}
