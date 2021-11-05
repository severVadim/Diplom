package com.diplom.repository.cassandra;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class CassandraConfiguration{
    @Value("${cassandra.keyspace}")
    private String keyspaceName;
    @Value("${cassandra.host}")
    private String host;
    @Value("${cassandra.port}")
    private int port;
    @Value("${cassandra.username}")
    private String user;
    @Value("${cassandra.password}")
    private String pass;


}
