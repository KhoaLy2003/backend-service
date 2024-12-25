package com.khoa_ly.backend_service.configuration;

import com.khoa_ly.backend_service.dto.response.AccountDetailResponse;
import com.khoa_ly.backend_service.dto.response.TaskDetailResponse;
import com.khoa_ly.backend_service.model.AbstractEntity;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Account.class, AccountDetailResponse.class).addMappings(mapper -> {
            mapper.map(AbstractEntity::getCreatedAt, AccountDetailResponse::setCreatedAt);
            mapper.map(AbstractEntity::getUpdatedAt, AccountDetailResponse::setUpdatedAt);
        });

        modelMapper.typeMap(Task.class, TaskDetailResponse.class).addMappings(mapper -> {
            mapper.map(AbstractEntity::getCreatedAt, TaskDetailResponse::setCreatedAt);
            mapper.map(AbstractEntity::getUpdatedAt, TaskDetailResponse::setUpdatedAt);
        });

        modelMapper
                .getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true)
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
