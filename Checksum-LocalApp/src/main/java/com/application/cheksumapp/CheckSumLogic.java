package com.application.cheksumapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class CheckSumLogic 
{
	public static String calculateChecksum(String filePath, String algorithm) 
			throws IOException, NoSuchAlgorithmException 
    {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(filePath);

        byte[] byteArray = new byte[1024];
        int bytesCount;

        while ((bytesCount = fis.read(byteArray)) != -1) 
        {
            digest.update(byteArray, 0, bytesCount);
        }

        fis.close();

        byte[] bytes = digest.digest();

        StringBuilder checksum = new StringBuilder();
        for (byte b : bytes) 
            checksum.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

        return checksum.toString();
    }
}
