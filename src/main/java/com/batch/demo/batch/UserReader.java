package com.batch.demo.batch;

import com.batch.demo.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class UserReader implements ItemReader<UserDto>, ItemStream {

    private static final Logger logger = LoggerFactory.getLogger(UserReader.class);

    @Value("${batch.input.file:users.csv}")
    private String inputFile;

    private FlatFileItemReader<UserDto> delegate;

    private FlatFileItemReader<UserDto> buildDelegate() {
        FlatFileItemReader<UserDto> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(inputFile));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("userId", "userName", "userEmail");

        BeanWrapperFieldSetMapper<UserDto> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(UserDto.class);

        DefaultLineMapper<UserDto> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate = buildDelegate();
        delegate.open(executionContext);
        logger.info("UserReader opened with file: {}", inputFile);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        if (delegate != null) {
            delegate.close();
        }
    }

    @Override
    public UserDto read() throws Exception {
        UserDto userDto = delegate.read();
        if (userDto != null) {
            logger.debug("Reading user: {}", userDto.getUserName());
        }
        return userDto;
    }
}
