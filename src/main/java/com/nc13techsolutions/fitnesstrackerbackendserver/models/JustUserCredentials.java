package com.nc13techsolutions.fitnesstrackerbackendserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JustUserCredentials {
    private String USERNAME;
    private String PASSWORD;
}
