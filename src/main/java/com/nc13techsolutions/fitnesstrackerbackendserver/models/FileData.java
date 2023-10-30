package com.nc13techsolutions.fitnesstrackerbackendserver.models;


import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileData {
    MultipartFile file;
    @NonNull
    String fileName;
    @NonNull
    String fileType;
}
