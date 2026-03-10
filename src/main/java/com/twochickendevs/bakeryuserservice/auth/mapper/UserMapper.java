package com.twochickendevs.bakeryuserservice.auth.mapper;

import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(UserDTO userDto);

    UserDTO toDTO(UserEntity entity);

    List<UserEntity> toEntities(List<UserDTO> userDTO);

    List<UserDTO> toDTOs(List<UserEntity> entities);
}
