package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Role;
import br.com.ibico.api.entities.Skill;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.RolesRepsitory;
import br.com.ibico.api.repositories.SkillRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           SkillRepository skillRepository, RolesRepsitory rolesRepsitory, SkillRepository skillRepository1, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository1;
        this.passwordEncoder = passwordEncoder;
    }


    //TODO: Implement Natural Language Search
    @Override
    public Response<UserDto> findUsers(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<User> page = userRepository.findByName(query, PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.valueOf(sortDir), sortBy)));

        if (page.isEmpty()) {
            return new Response<UserDto>(
                    null,
                    page.getNumber(),
                    page.getSize(),
                    page.getNumberOfElements(),
                    page.getTotalPages(),
                    page.isLast()
            );
        }

        return new Response<UserDto>(
                page.getContent().stream()
                        .map(User::toUserDto)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.isLast()
        );
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

        user.setPasswd(passwordEncoder.encode(user.getPasswd()));
        user.setRoles(Set.of(new Role(2L, "ROLE_USER")));

        user.getSkills().forEach(skill -> {
            if (!skillRepository.existsByName(skill.getName()))
                skillRepository.save(skill);
        });

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
