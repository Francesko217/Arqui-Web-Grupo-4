package pe.edu.upc.tutrade.ServicesInterfaces;

import pe.edu.upc.tutrade.DTOs.ImpactResponseDTO;

public interface IImpactService {
    /** Impacto ambiental acumulado por el usuario autenticado (trueques ACCEPTED). */
    ImpactResponseDTO getUserImpact(String email);

    /** Impacto ambiental agregado de toda la comunidad. */
    ImpactResponseDTO getCommunityImpact();
}
