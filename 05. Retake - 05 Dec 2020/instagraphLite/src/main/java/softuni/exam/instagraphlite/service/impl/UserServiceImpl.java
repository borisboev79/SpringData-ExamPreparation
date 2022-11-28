package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.User;
import softuni.exam.instagraphlite.models.dto.UserDTO;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static softuni.exam.instagraphlite.util.constants.Messages.INVALID_USER;
import static softuni.exam.instagraphlite.util.constants.Messages.VALID_USER_FORMAT;
import static softuni.exam.instagraphlite.util.constants.Paths.USERS_JSON_PATH;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureRepository pictureRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_JSON_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        List<UserDTO> users = Arrays.stream(gson.fromJson(readFromFileContent(), UserDTO[].class)).toList();

        for (UserDTO userDto : users) {
            boolean isValid = this.validator.isValid(userDto);


            if (this.pictureRepository.findFirstByPath(userDto.getProfilePicture()).isEmpty()) {
                isValid = false;
            }

            if (isValid) {

                User user = this.mapper.map(userDto, User.class);

                Picture picture = this.pictureRepository.findFirstByPath(userDto.getProfilePicture()).get();

                user.setProfilePicture(picture);

                this.userRepository.save(user);

                result.append(String.format(VALID_USER_FORMAT,
                        userDto.getUsername()));


            } else {
                result.append(INVALID_USER).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        return this.userRepository
                .exportUsers()
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(User::toString)
                .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));

    }
}
