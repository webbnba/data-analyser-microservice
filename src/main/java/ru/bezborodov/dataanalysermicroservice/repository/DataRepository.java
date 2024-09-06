package ru.bezborodov.dataanalysermicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bezborodov.dataanalysermicroservice.model.Data;

public interface DataRepository extends JpaRepository<Data, Long> {
}
