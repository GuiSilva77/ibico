package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.dto.UserGetDto;
import br.com.ibico.api.entities.dto.UserPutDto;
import br.com.ibico.api.entities.payload.UserPayload;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    Response<UserDto> findUsers(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    UserDto findUserByCpf(String cpf);
    UserDto saveUser(UserPayload payload, MultipartFile profilePic);
    UserPutDto updateUser(UserPayload userDto);
    void deactivateUser(String cpf);
    UserGetDto findUserByUsername(String username);
    void updateProfilePic(String username, MultipartFile profilePic);
}
