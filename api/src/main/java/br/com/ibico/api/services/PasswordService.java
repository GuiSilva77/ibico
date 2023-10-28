package br.com.ibico.api.services;

import br.com.ibico.api.entities.dto.PasswordResetRequestDto;
import br.com.ibico.api.entities.dto.PasswordResetCodeDto;
import br.com.ibico.api.entities.payload.PasswordResetCodePayload;

public interface PasswordService {

    PasswordResetCodeDto generatePasswordResetCode(String cpf);

    PasswordResetRequestDto validatePasswordResetCode(PasswordResetCodePayload payload);

    void resetPassword(String accessToken, String newPassword);
}
