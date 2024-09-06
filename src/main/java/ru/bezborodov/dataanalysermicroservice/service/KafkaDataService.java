package ru.bezborodov.dataanalysermicroservice.service;

import ru.bezborodov.dataanalysermicroservice.model.Data;

public interface KafkaDataService {

    void handle(Data data);
}
