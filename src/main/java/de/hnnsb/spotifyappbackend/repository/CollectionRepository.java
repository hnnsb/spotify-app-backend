package de.hnnsb.spotifyappbackend.repository;

import de.hnnsb.spotifyappbackend.model.CollectionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectionRepository extends CrudRepository<CollectionEntity, UUID> {

}
