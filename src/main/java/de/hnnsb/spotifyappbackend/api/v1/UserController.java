package de.hnnsb.spotifyappbackend.api.v1;

import de.hnnsb.spotifyappbackend.api.v1.dto.UserDto;
import de.hnnsb.spotifyappbackend.model.UserEntity;
import de.hnnsb.spotifyappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/register")
    public void createUser(@RequestBody final UserDto userDto) {
        final UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        this.userRepository.save(user);
    }

    @PostMapping("/login")
    public UUID loginUser(@RequestBody final UserDto userDto) {
        final UserEntity user = this.userRepository.findByNameAndPassword(userDto.getName(), userDto.getPassword());
        return user.getId();
    }

    @GetMapping
    public Collection<UserEntity> getAllUsers() {
        return (Collection<UserEntity>) this.userRepository.findAll();
    }

}
