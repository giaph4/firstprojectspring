package com.devgiapho.identity_springboot;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class IdentitySpringbootApplicationTests {

    @Test
    void hash() throws NoSuchAlgorithmException {
        String password = "12345";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte[] disgest = md.digest();

        //827CCB0EEA8A706C4C34A16891F84E7B
        String md5Hash = DatatypeConverter.printHexBinary(disgest);

        System.out.println("md5Hash round 1: " + md5Hash);

        md.update(password.getBytes());
        disgest = md.digest();

        //827CCB0EEA8A706C4C34A16891F84E7B
        System.out.println("md5Hash round 2: " + md5Hash);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        System.out.println("Bcrypt round 1: " + passwordEncoder.encode(password));
        System.out.println("Bcrypt round 2: " + passwordEncoder.encode(password));
    }
}
