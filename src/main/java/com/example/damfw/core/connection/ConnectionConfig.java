package com.example.damfw.core.connection;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConnectionConfig {
    String type;
    String hostname;
    String username;
    String password;
    String database;
    Map<Object, Object> params;
}