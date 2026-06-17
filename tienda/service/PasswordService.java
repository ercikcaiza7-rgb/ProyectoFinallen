package com.example.tienda.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String encriptar(String password) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    md.digest(password.getBytes());

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hash) {

                sb.append(
                        String.format("%02x", b)
                );
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }

    public boolean verificar(
            String passwordIngresada,
            String passwordGuardada
    ) {

        return encriptar(passwordIngresada)
                .equals(passwordGuardada);
    }
}
