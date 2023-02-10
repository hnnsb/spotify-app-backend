package de.hnnsb.spotifyappbackend.SpotifyApiService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Service
public class SpotifyApiService {

    @Value("${spotifyApi.clientID}")
    String clientId;
    @Value("${spotifyApi.clientSecret}")
    String clientSecret;
    @Value("${spotifyApi.redirectUri}")
    String redirectUri;
    @Value("${frontend.redirectTarget}")
    String redirectTarget;
    @Getter
    private SpotifyApi spotifyApi;

    private String userCode;

    public void initSpotifyApi() {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(this.redirectUri))
                .build();
    }

    public String requestAuthCode() {
        final AuthorizationCodeUriRequest authorizationCodeUriRequest = this.spotifyApi
                .authorizationCodeUri()
                .scope("user-read-private, user-read-email, user-top-read")
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    public String initUserSession(final String userCode, final HttpServletResponse response) throws IOException {
        this.userCode = userCode;

        final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi.authorizationCode(this.userCode).build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            this.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            this.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            log.info("Expires in:" + authorizationCodeCredentials.getExpiresIn());

        } catch (final IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error could not set authentication and refresh token: " + e.getMessage());
        }

        log.info("Initialized new user session");
        response.sendRedirect(this.redirectTarget);
        return this.spotifyApi.getAccessToken();
    }

    public Artist[] getUserTopArtists() {
        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = this.spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();

            return artistPaging.getItems();
        } catch (final Exception e) {
            log.error("Could not retrieve top artists: " + e.getMessage());
        }
        return new Artist[0];
    }
}
