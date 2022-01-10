package ir.soodeh.mancala.controller.mappers;

import ir.soodeh.mancala.controller.dto.GameDto;
import ir.soodeh.mancala.model.Game;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class GameMapper {

    @AfterMapping
    protected void enrichDTOWithStatus(Game game, @MappingTarget GameDto gameDto) {
          gameDto.setStatus (game.getBoard ().getStatus ());
    }

    public abstract GameDto toDto(Game entity);
}
