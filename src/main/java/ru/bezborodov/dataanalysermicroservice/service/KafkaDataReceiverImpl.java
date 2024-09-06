package ru.bezborodov.dataanalysermicroservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import ru.bezborodov.dataanalysermicroservice.config.LocalDateTimeDeserializer;
import ru.bezborodov.dataanalysermicroservice.model.Data;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaDataReceiverImpl implements KafkaDataReceiver {

    private final KafkaReceiver<String, Object> receiver;
    private final LocalDateTimeDeserializer deserializer;
    private final KafkaDataService kafkaDataService;

    /**
     * После создания бина KafkaDataReceiverImpl сразу запустится метод fetch()
     *     для считывания данных из топика
     */

    @PostConstruct
    public void init() {
        fetch();
    }

    @Override
    public void fetch() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, deserializer)
                        .create();
        receiver.receive()
                .subscribe(r -> {
                    Data data = gson.fromJson(r.value().toString(), Data.class);
                    kafkaDataService.handle(data);
                    r.receiverOffset().acknowledge();
                });
    }
}
