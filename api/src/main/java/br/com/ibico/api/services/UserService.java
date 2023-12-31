package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.dto.UserGetDto;
import br.com.ibico.api.entities.dto.UserPutDto;
import br.com.ibico.api.entities.payload.UserPayload;

public interface UserService {

    Response<UserDto> findUsers(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    UserDto findUserByCpf(String cpf);
    UserDto saveUser(UserPayload payload);
    UserPutDto updateUser(UserPayload userDto);
    void deactivateUser(String cpf);
    UserGetDto findUserByUsername(String username);
}
