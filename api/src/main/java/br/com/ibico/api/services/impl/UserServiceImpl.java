package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findUserByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", "")
        );

        return user.toUserDto();
    }

    @Override
    public UserDto saveUser(UserPayload payload) {
        User user = payload.toUser();

        User savedUser = userRepository.save(user);

        return savedUser.toUserDto();
    }

    @Override
    public UserDto updateUser(String cpf, UserPayload userDto) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", "")
        );

        user.setName(userDto.name());
        user.setActive(userDto.active());
        user.setImgURL(userDto.imgURL());
        user.setTelephone(userDto.telephone());
        user.setSkills(userDto.toUser().getSkills());

        User savedUser = userRepository.save(user);

        return savedUser.toUserDto();
    }

    @Override
    public void deactivateUser(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", "")
        );

        user.setActive(false);

        userRepository.save(user);
    }
}
