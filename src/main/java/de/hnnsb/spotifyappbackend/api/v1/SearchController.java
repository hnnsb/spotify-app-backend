package de.hnnsb.spotifyappbackend.api.v1;

import de.hnnsb.spotifyappbackend.SpotifyApiService.SpotifyApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search")
@Tag(name = "Search", description = "For searching the spotify db for artists, albums and tracks.")
public class SearchController {

    private final SpotifyApiService spotifyApi;

    @GetMapping("/artist")
    @Operation(summary = "Get top artists")
    @ApiResponse(responseCode = "200", description = "Returned artists")
    @ApiResponse(responseCode = "400", description = "Wrong search parameters")
    @ApiResponse(responseCode = "401", description = "Invalid access token")
    public ResponseEntity<Artist[]> getArtistSearchResult(@RequestParam(name = "query") final String query) throws IOException, ParseException, SpotifyWebApiException {
        final Artist[] artists = this.spotifyApi.searchForArtist(query);

        return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @GetMapping("/album")
    @Operation(summary = "Get top artists")
    @ApiResponse(responseCode = "200", description = "Returned albums")
    @ApiResponse(responseCode = "400", description = "Wrong search parameters")
    @ApiResponse(responseCode = "401", description = "Invalid access token")
    public ResponseEntity<AlbumSimplified[]> getAlbumSearchResult(@RequestParam(name = "query") final String query) throws IOException, ParseException, SpotifyWebApiException {
        final AlbumSimplified[] albums = this.spotifyApi.searchForAlbum(query);

        return new ResponseEntity<>(albums, HttpStatus.OK);
    }
}
