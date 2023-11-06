package br.com.ibico.api.services.impl;

import br.com.ibico.api.constraints.SmsConstraints;
import br.com.ibico.api.entities.PasswordResetCode;
import br.com.ibico.api.entities.PasswordResetRequest;
import br.com.ibico.api.entities.User;
import br.com.ibico.api.entities.dto.PasswordResetRequestDto;
import br.com.ibico.api.entities.dto.PasswordResetCodeDto;
import br.com.ibico.api.entities.payload.PasswordResetCodePayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.exceptions.ResourceNotValidException;
import br.com.ibico.api.repositories.PasswordResetCodeRepository;
import br.com.ibico.api.repositories.PasswordResetRequestRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.PasswordService;
import br.com.ibico.api.services.SMSService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final PasswordResetRequestRepository passwordResetRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);
    private final SMSService smsService;

    public PasswordServiceImpl(UserRepository userRepository, PasswordResetCodeRepository passwordResetCodeRepository, PasswordResetRequestRepository passwordResetRequestRepository, PasswordEncoder passwordEncoder, SMSService smsService) {
        this.userRepository = userRepository;
        this.passwordResetCodeRepository = passwordResetCodeRepository;
        this.passwordResetRequestRepository = passwordResetRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
    }

    @Override
    public PasswordResetCodeDto generatePasswordResetCode(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new ResourceNotFoundException("User", "cpf", ""));
        Random random = new Random();
        PasswordResetCode passwordResetCode = new PasswordResetCode(
                user,
                String.valueOf(random.nextInt(999999)),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(PasswordResetCode.EXPIRATION_TIME));

        try {
            smsService.sendSMS(String.format(SmsConstraints.SMS_PASSWORD_RESET_MESSAGE, user.getName(), passwordResetCode.getCode()), user.getTelephone());
            logger.info("SMS sent to {}", user.getTelephone());
        } catch (Exception e) {
            logger.error("Error while sending SMS", e);
        }

        return passwordResetCodeRepository.save(passwordResetCode).toDto();
    }

    @Override
    public PasswordResetRequestDto validatePasswordResetCode(PasswordResetCodePayload payload) {
        PasswordResetCode passwordResetCode = passwordResetCodeRepository
                .findById(UUID.fromString(payload.requestId()))
                .orElseThrow(() -> new ResourceNotFoundException("Password Reset Code", "id", payload.requestId()));

        if(!passwordResetCode.getCode().equals(payload.code()))
            throw new ResourceNotValidException("Code is not valid");

        if(passwordResetCode.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new ResourceNotValidException("Code is expired");

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(
            passwordResetCode,
            Base64.getEncoder().encodeToString(passwordResetCode.getCode().getBytes()),
            LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(PasswordResetRequest.EXPIRATION_TIME), false);

        return passwordResetRequestRepository.save(passwordResetRequest).toDto();
    }

    @Override
    @Transactional
    public void resetPassword(String accessToken, String newPassword) {
        PasswordResetRequest passwordResetRequest = passwordResetRequestRepository
                .findByAccessToken(accessToken)
                .orElseThrow(() -> new ResourceNotFoundException("Password Reset Request", "accessToken", accessToken));

        if(passwordResetRequest.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new ResourceNotValidException("Request is expired");

        if(passwordResetRequest.isUsed())
            throw new ResourceNotValidException("token was already used");

        User user = passwordResetRequest.getPasswordResetCode().getUser();
        user.setPasswd(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        passwordResetRequest.setUsed(true);
        passwordResetRequestRepository.save(passwordResetRequest);

    }
}
