package br.com.ibico.api.entities.queries;

import java.io.Serializable;

public record UserQuery(
        String cpf
) implements Serializable {
}
