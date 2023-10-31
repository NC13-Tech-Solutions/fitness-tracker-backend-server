package com.nc13techsolutions.fitnesstrackerbackendserver.models;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@RedisHash("Exercises")
public class Exercise implements Serializable {
    @Id
    @NonNull
    private Integer exId;
    @NonNull
    private String name;
    @NonNull
    private String description;

    private MiscDataType miscDataType=MiscDataType.NONE;

    private String miscData = "";

    private boolean disabled = false;
}
