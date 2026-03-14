package com.batch.demo.batch;

import com.batch.demo.model.User;
import com.batch.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoWriter implements ItemWriter<User> {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDtoWriter.class);

    private final UserRepository userRepository;

    public UserDtoWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void write(Chunk<? extends User> chunk) {
        logger.info("Writing {} users to database", chunk.size());
        userRepository.saveAll(chunk.getItems());
        logger.info("Users successfully saved");
    }

}