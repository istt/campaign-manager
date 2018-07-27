package com.ft.repository;

import com.ft.domain.DataFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DataFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFileRepository extends MongoRepository<DataFile, String> {

}
