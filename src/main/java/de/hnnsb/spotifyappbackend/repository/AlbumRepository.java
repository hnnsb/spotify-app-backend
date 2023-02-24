package de.hnnsb.spotifyappbackend.repository;

import de.hnnsb.spotifyappbackend.model.AlbumEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<AlbumEntity, String> {

}
