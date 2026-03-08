package com.batch.demo.batch;

import com.batch.demo.model.UserDto;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class UserReader implements ItemReader<UserDto> {

    private FlatFileItemReader<UserDto> delegate;

    @PostConstruct
    public void init() throws Exception {

        delegate = new FlatFileItemReader<>();
        delegate.setResource(new ClassPathResource("users.csv"));
        delegate.setLinesToSkip(1);

        // CSV tokenizer
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("userId", "userName", "userEmail");

        // Object mapper
        BeanWrapperFieldSetMapper<UserDto> mapper =
                new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(UserDto.class);

        // Line mapper
        DefaultLineMapper<UserDto> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);

        delegate.setLineMapper(lineMapper);

        delegate.open(new ExecutionContext());
    }

    @Override
    public UserDto read() throws Exception {
        UserDto userDto = delegate.read();

        if (userDto != null) {
            System.out.println("Reading User: " + userDto.getUserName());
        }

        return userDto;
    }
}