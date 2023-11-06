package br.com.ibico.api.services;

public interface SMSService {

    public String sendSMS(String message, String number);
}
