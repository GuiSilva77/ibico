package br.com.ibico.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.HashMap;

@Configuration
public class AWSClientsConfiguration {

    @Bean
    public SnsClient snsClient() {
        SnsClient client = SnsClient.builder().region(Region.SA_EAST_1).credentialsProvider(ProfileCredentialsProvider.create("IBico")).build();

        HashMap<String, String> attributes = new HashMap<>(2);
        attributes.put("DefaultSMSType", "Transactional");
        attributes.put("DefaultSenderID", "IBico");

        return client;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(Region.SA_EAST_1).credentialsProvider(ProfileCredentialsProvider.create("IBico")).build();
    }
}
