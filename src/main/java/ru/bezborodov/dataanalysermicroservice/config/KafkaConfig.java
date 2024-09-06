package ru.bezborodov.dataanalysermicroservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Value("${topics}")
    private List<String> topics;

    @Bean
    public Map<String, Object> receiverProperties() {
        Map<String, Object> props = new HashMap<>();
     props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
     props.put(ConsumerConfig.GROUP_ID_CONFIG, "my_group");
     props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
     props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
     props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
     return props;
    }

    @Bean
    public ReceiverOptions<String, Object> receiverOptions() {
        ReceiverOptions<String, Object> receiverOptions = ReceiverOptions
                .create(receiverProperties());
        return receiverOptions.subscription(topics)
                .addAssignListener(partitions ->
                        System.out.println("onPartitionAssigned: "
                                + partitions))
                .addRevokeListener(partitions ->
                        System.out.println("onPartitionRevoked: "
                                + partitions));
    }

    @Bean
    public KafkaReceiver<String, Object> receiver(
            ReceiverOptions<String, Object> receiverOptions
    ) {
        return KafkaReceiver.create(receiverOptions);
    }
}
