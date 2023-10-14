package br.com.ibico.api.services;

import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.payload.UserPayload;

public interface UserService {
    UserDto findUserByCpf(String cpf);
    UserDto saveUser(UserPayload payload);
    UserDto updateUser(String cpf, UserPayload userDto);
    void deactivateUser(String cpf);
}
