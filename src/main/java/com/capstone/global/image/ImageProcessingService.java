package com.capstone.global.image;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

@Service
public class ImageProcessingService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_WIDTH = 1024;  // 최대 너비
    private static final int MAX_HEIGHT = 768;  // 최대 높이

    public InputStream processImageToStream(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage).size(500, 500).outputFormat("jpg").toOutputStream(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}