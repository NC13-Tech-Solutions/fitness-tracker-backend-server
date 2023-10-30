package com.nc13techsolutions.fitnesstrackerbackendserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@PropertySource("application.properties")
public class RedisConfig {
    @Value("${redis.host}")
    private String HOST;
    @Value("${redis.port}")
    private int PORT;
    @Value("${redis.username}")
    private String USERNAME;
    @Value("${redis.password}")
    private String PASSWORD;

    @Bean
    public JedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(HOST);
        configuration.setPort(PORT);
        configuration.setUsername(USERNAME);
        configuration.setPassword(PASSWORD);

        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        // template.setHashKeySerializer(new StringRedisSerializer());
        // template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}
