package de.hnnsb.spotifyappbackend.api.v1.spotify;

import de.hnnsb.spotifyappbackend.SpotifyApiService.SpotifyApiService;
import de.hnnsb.spotifyappbackend.api.v1.dto.SpotifyLoginDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/spotify")
public class SpotifyAuthController {

    private final SpotifyApiService spotifyApiService;

    @GetMapping("/login")
    public ResponseEntity<SpotifyLoginDto> spotifyLogin() {
        final SpotifyLoginDto spotifyLoginDto = new SpotifyLoginDto(this.spotifyApiService.requestAuthCode());
        return new ResponseEntity<>(spotifyLoginDto, HttpStatus.OK);
    }

    @GetMapping(value = "/get-user-code")
    public String getSpotifyUserCode(@RequestParam("code") final String userCode, final HttpServletResponse response) throws IOException {
        return this.spotifyApiService.initUserSession(userCode, response);
    }

}
