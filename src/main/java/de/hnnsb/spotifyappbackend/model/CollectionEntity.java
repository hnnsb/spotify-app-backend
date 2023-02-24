package de.hnnsb.spotifyappbackend.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    private UserEntity user;

    @OneToMany
    private Set<AlbumEntity> album;
}
