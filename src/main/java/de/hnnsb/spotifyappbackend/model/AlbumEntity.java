package de.hnnsb.spotifyappbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AlbumEntity {

    @Id
    private String id;

    private String name;
}
