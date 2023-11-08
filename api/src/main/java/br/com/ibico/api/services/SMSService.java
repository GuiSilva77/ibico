package br.com.ibico.api.services;

public interface SMSService {

    String sendSMS(String message, String number);
}
