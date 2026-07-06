package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.ConteoDTO;

import java.util.List;

public interface IEstadisticaService {
    List<ConteoDTO> itemsPorCategoria();
    List<ConteoDTO> truequesPorEstado();
    List<ConteoDTO> rankingUsuarios();
}
