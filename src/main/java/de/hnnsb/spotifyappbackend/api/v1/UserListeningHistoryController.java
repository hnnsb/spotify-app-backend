package de.hnnsb.spotifyappbackend.api.v1;

import de.hnnsb.spotifyappbackend.SpotifyApiService.SpotifyApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserListeningHistoryController {
    private final SpotifyApiService spotifyApiService;

    @GetMapping("/users/top-artists")
    public ResponseEntity<Artist[]> getTopArtists() {
        final Artist[] artists = this.spotifyApiService.getUserTopArtists();

        return new ResponseEntity<>(artists, HttpStatus.OK);
    }
}
