package com.kshrd.soccer_date.mapper;
import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.dto.UserAppDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
@Mapper
public interface  UserMapper {
    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);

//    @Mapping(source = "is_skip",target = "is_skip")
    UserAppDTO toUserAppDTO(UserApp userApp);
    List<UserAppDTO> toUserAppDTOs(List<UserApp> userApps);
}

