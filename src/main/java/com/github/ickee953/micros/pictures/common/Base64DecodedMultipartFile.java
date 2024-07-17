package com.github.ickee953.micros.pictures.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class Base64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;

    private final String filename;

    public Base64DecodedMultipartFile(String filename, byte[] imgContent) {
        this.imgContent = imgContent;
        this.filename = filename;
    }

    @Override
    public String getName() {
        return this.filename;
    }

    @Override
    public String getOriginalFilename() {
        return this.filename;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}
