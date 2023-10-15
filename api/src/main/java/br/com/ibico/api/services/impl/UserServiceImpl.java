package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Skill;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.SkillRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserServiceImpl(UserRepository userRepository,
                           SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public UserDto findUserByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", cpf)
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
    public UserDto updateUser(UserPayload userDto) {
        User user = userRepository.findByCpf(userDto.cpf()).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", userDto.cpf())
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
                new ResourceNotFoundException("User", "CPF", cpf)
        );

        user.setActive(false);

        userRepository.save(user);
    }
}
