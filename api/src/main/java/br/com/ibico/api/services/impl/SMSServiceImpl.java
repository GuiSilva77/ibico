package br.com.ibico.api.services.impl;

import br.com.ibico.api.services.SMSService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SMSServiceImpl implements SMSService {

    private final SnsClient client;

    public SMSServiceImpl(SnsClient client) {
        this.client = client;
    }

    @Override
    public String sendSMS(String message, String number) {
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(number)
                .build();

        PublishResponse response = client.publish(request);

        return response.messageId();
    }
}
