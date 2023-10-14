package br.com.ibico.api.exceptions;

public class ResourceNotValidException extends RuntimeException {
    public ResourceNotValidException(String message) {
        super(message);
    }
}
