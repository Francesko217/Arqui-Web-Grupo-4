package pe.edu.upc.tutrade.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.upc.tutrade.DTOs.ItemResponseDTO;
import pe.edu.upc.tutrade.DTOs.UserResponseDTO;
import pe.edu.upc.tutrade.Entities.Item;
import pe.edu.upc.tutrade.Entities.User;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();
        mm.createTypeMap(User.class, UserResponseDTO.class);
        mm.createTypeMap(Item.class, ItemResponseDTO.class);
        return mm;
    }
}
