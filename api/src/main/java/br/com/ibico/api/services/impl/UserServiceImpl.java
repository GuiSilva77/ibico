package br.com.ibico.api.services.impl;

import br.com.ibico.api.constants.S3Constants;
import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Review;
import br.com.ibico.api.entities.Role;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.dto.UserGetDto;
import br.com.ibico.api.entities.dto.UserPutDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.exceptions.ResourceNotValidException;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.FileStorageService;
import br.com.ibico.api.services.SkillService;
import br.com.ibico.api.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SkillService skillService;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final FileStorageService fileStorageService;

    public UserServiceImpl(UserRepository userRepository,
                           SkillService skillService, PasswordEncoder passwordEncoder, EntityManager entityManager, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.skillService = skillService;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public Response<UserDto> findUsers(String query, int pageNo, int pageSize, String sortBy, String sortDir) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<User> result = searchSession.search(User.class)
                .where(f -> f.match().fields("name", "username").matching(query).fuzzy(2))
                .fetch(pageNo * pageSize, pageSize);

        List<UserDto> users = result.hits().stream()
                .map(user -> {
                    if (!user.isActive()) return null;

                    double avgRating = user.getReviews().stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0);

                    return user.toUserDtoMinusCPF(Math.floor(avgRating));
                })
                .toList();

        long totalElements = result.total().hitCount();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        boolean last = (pageNo + 1) >= totalPages;

        return new Response<>(users, pageNo, pageSize, (int) totalElements, totalPages, last, false);
    }


    @Override
    public UserDto findUserByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", cpf)
        );

        return user.toUserDto();
    }

    @Override
    public UserDto saveUser(UserPayload payload, MultipartFile profilePic) {
        User user = payload.toUser();

        if (payload.passwd().isEmpty() || payload.passwd().length() < 8 || !payload.passwd().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$"))
            throw new ResourceNotValidException("password is empty or not valid");

        user.setPasswd(passwordEncoder.encode(user.getPasswd()));
        user.setRoles(Set.of(new Role(2L, "ROLE_USER")));

        user.setSkills(skillService.convertToSkills(payload.skills()));

        fileStorageService.uploadFile(String.format("%s/%s.png",S3Constants.PROFILE_PICS_FOLDER, payload.username()), profilePic, S3Constants.PROFILE_PICS_BUCKET);

        user.setImgURL(fileStorageService.getFileUrl(String.format("%s/%s.png", S3Constants.PROFILE_PICS_FOLDER, payload.username()), S3Constants.PROFILE_PICS_BUCKET));

        User savedUser = userRepository.save(user);

        return savedUser.toUserDto();
    }

    @Override
    public UserPutDto updateUser(UserPayload userDto) {
        User user = userRepository.findByCpf(userDto.cpf()).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", userDto.cpf())
        );

        user.setUsername(userDto.username());
        user.setName(userDto.name());
        user.setActive(userDto.active());
        user.setImgURL(userDto.imgURL());
        user.setTelephone(userDto.telephone());

        user.setPasswd(passwordEncoder.encode(userDto.passwd()));

        user.setSkills(skillService.convertToSkills(userDto.skills()));

        User savedUser = userRepository.save(user);

        return savedUser.toUserPutDto();
    }

    @Override
    public void deactivateUser(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() ->
                new ResourceNotFoundException("User", "CPF", cpf)
        );

        user.setActive(false);

        userRepository.save(user);
    }

    @Override
    public UserGetDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User", "Username", username)
        );

        return user.toUserGetDto();
    }

    @Override
    public void updateProfilePic(String username, MultipartFile profilePic) {
        fileStorageService.deleteFile(username, S3Constants.PROFILE_PICS_BUCKET);

        fileStorageService.uploadFile(String.format("%s/%s.png", S3Constants.PROFILE_PICS_FOLDER, username), profilePic, S3Constants.PROFILE_PICS_BUCKET);
    }
}
