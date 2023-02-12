package de.hnnsb.spotifyappbackend.api.v1;

import de.hnnsb.spotifyappbackend.SpotifyApiService.SpotifyApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
@Tag(name = "Users", description = "For fetching data about the users favorite music and artists.")
public class UserDataController {
    private final SpotifyApiService spotifyApiService;

    private final List<String> allowedTimeRanges = List.of("short_term", "medium_term", "long_term");

    @GetMapping("/users/top-artists")
    @Operation(summary = "Get top artists")
    @ApiResponse(responseCode = "200", description = "Returned artists")
    @ApiResponse(responseCode = "400", description = "Wrong search parameters")
    @ApiResponse(responseCode = "401", description = "Invalid access token")
    public ResponseEntity<Artist[]> getTopArtists(@RequestParam(name = "time-range", defaultValue = "medium_term") final String timeRange,
                                                  @RequestParam(name = "amount", defaultValue = "10") @Min(1) @Max(50) final int amount) {
        if (!this.allowedTimeRanges.contains(timeRange)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Artist[] artists = this.spotifyApiService.getUserTopArtists(timeRange, amount);
        if (artists.length == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @GetMapping("users/top-tracks")
    @Operation(summary = "Get top tracks")
    @ApiResponse(responseCode = "200", description = "Returned tracks")
    @ApiResponse(responseCode = "400", description = "Wrong search parameters")
    @ApiResponse(responseCode = "401", description = "Invalid access token")
    public ResponseEntity<Track[]> getTopTracks(@RequestParam(name = "time-range", defaultValue = "medium_term") final String timeRange,
                                                @RequestParam(name = "amount", defaultValue = "10") @Min(1) @Max(50) final int amount) {
        if (!this.allowedTimeRanges.contains(timeRange)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Track[] tracks = this.spotifyApiService.getUserTopTracks(timeRange, amount);
        if (tracks.length == 0) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }
}
