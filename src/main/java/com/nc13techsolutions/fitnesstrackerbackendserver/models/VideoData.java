package com.nc13techsolutions.fitnesstrackerbackendserver.models;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoData implements Serializable{
    @NonNull
    private String data;
    @NonNull
    private VideoDataType type;
}
