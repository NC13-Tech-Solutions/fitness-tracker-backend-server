package com.nc13techsolutions.fitnesstrackerbackendserver.services;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.FileData;

@Service
@PropertySource("application.properties")
public class FilesService {
    @Value("${fitness-app-files}")
    private String FILE_LOC;

    /**
     * Save a file in the local server according to the 'fileType' value
     * if fileType = 'images', then the file will be stored in
     * `FILE_LOC`\images\fileName
     * 
     * @param fileData
     * @return new filename
     */
    public Path saveFile(FileData fileData) {
        String folderLoc = "images\\";
        if (fileData.getFileType().equalsIgnoreCase("videos"))
            folderLoc = "videos\\";
        // Checking to see if the file is legit
        try {
            MultipartFile file = fileData.getFile();
            if (file.isEmpty())
                return null;
            String fileName = randomFileNameGenerator(fileData.getFileName());
            if (fileName != null) {
                Path destinationFile = Paths.get(FILE_LOC + folderLoc)
                        .resolve(fileName)
                        .normalize()
                        .toAbsolutePath();
                InputStream inputStream = file.getInputStream();
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                return this.getFile(new FileData( fileName, fileData.getFileType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Path getFile(FileData fileData) {
        if (fileData.getFileName() == "")
            return null;

        String folderLoc = "images\\";
        if (fileData.getFileType() == "videos")
            folderLoc = "videos\\";

        try {
            Path path = Paths.get(FILE_LOC + folderLoc).resolve(fileData.getFileName());
            return path;
        } catch (InvalidPathException | java.io.IOError | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int deleteFile(FileData fileData) {
        if (fileData.getFileName() == "")
            return -1;

        String folderLoc = "images\\";
        if (fileData.getFileType() == "videos")
            folderLoc = "videos\\";

        try {
            Path path = Paths.get(FILE_LOC + folderLoc).resolve(fileData.getFileName());
            File file = path.toFile();
            if (file.exists() && file.canWrite()) {
                if (file.delete())
                    return 1;
            }
        } catch (InvalidPathException | UnsupportedOperationException | SecurityException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public String randomFileNameGenerator(String fileName) {
        if (fileName == null || fileName == "")
            return null;
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String name = fileName.substring(0, fileName.length() - (extension.length() + 1));
        String finalFileName = name + Instant.now().toEpochMilli() + "." + extension;
        return finalFileName;
    }

    public Resource loadAsResource(Path file) {
		try {
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
                
                return null;

			}
		}
		catch (MalformedURLException e) {
            e.printStackTrace();
		}

        return null;
	}
}
