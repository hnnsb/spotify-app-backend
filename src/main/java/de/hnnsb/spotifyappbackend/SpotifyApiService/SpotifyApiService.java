package de.hnnsb.spotifyappbackend.SpotifyApiService;

import jakarta.annotation.PostConstruct;
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
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;

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

    @PostConstruct
    private void initSpotifyApi() {
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
        final AuthorizationCodeRequest authorizationCodeRequest = this.spotifyApi.authorizationCode(userCode).build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            this.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            this.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            response.addHeader("accessToken", authorizationCodeCredentials.getAccessToken());
            response.addHeader("refreshToken", authorizationCodeCredentials.getRefreshToken());

            log.info("Expires in:" + authorizationCodeCredentials.getExpiresIn());

        } catch (final IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error could not set authentication and refresh token: " + e.getMessage());
        }
        log.info("Initialized new user session");
        response.sendRedirect(this.redirectTarget);
        return this.spotifyApi.getAccessToken();
    }

    public Artist[] getUserTopArtists(final String timeRange, final int amount) {
        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = this.spotifyApi.getUsersTopArtists()
                .time_range(timeRange)
                .limit(amount)
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();

            return artistPaging.getItems();
        } catch (final Exception e) {
            log.error("Could not retrieve top artists: " + e.getMessage());
        }
        return new Artist[0];
    }

    public Track[] getUserTopTracks(final String timeRange, final int amount) {
        final GetUsersTopTracksRequest getUsersTopTracksRequest = this.spotifyApi.getUsersTopTracks()
                .time_range(timeRange)
                .limit(amount)
                .build();

        try {
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

            return trackPaging.getItems();
        } catch (final Exception e) {
            log.error("Could not retrieve top tracks: " + e.getMessage());
        }
        return new Track[0];
    }
}
