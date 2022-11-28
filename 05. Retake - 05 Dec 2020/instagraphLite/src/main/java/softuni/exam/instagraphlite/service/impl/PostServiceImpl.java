package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.Post;
import softuni.exam.instagraphlite.models.User;
import softuni.exam.instagraphlite.models.dto.PostDTO;
import softuni.exam.instagraphlite.models.dto.wrapper.PostsWrapperDTO;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.ValidationUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.instagraphlite.util.constants.Messages.INVALID_POST;
import static softuni.exam.instagraphlite.util.constants.Messages.VALID_POST_FORMAT;
import static softuni.exam.instagraphlite.util.constants.Paths.POSTS_XML_PATH;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final JAXBContext context;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PictureRepository pictureRepository, UserRepository userRepository, JAXBContext context, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.postRepository = postRepository;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }


    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POSTS_XML_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        PostsWrapperDTO postsDto = (PostsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(POSTS_XML_PATH));


        for (PostDTO postDto : postsDto.getPosts()) {

            boolean isValid = this.validator.isValid(postDto);

            if (this.pictureRepository.findFirstByPath(postDto.getPicture().getPath()).isEmpty() ||
            this.userRepository.findFirstByUsername(postDto.getUser().getUsername()).isEmpty()) {
                isValid = false;
            }

            if (isValid) {

                final Post post = this.mapper.map(postDto, Post.class);

                Picture picture = this.pictureRepository.findFirstByPath(postDto.getPicture().getPath()).get();
                User user = this.userRepository.findFirstByUsername(postDto.getUser().getUsername()).get();

                post.setUser(user);
                post.setPicture(picture);

                this.postRepository.save(post);

                result.append(String.format(VALID_POST_FORMAT,
                       post.getUser().getUsername()));

            } else {
                result.append(INVALID_POST).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
