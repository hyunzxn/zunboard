package com.hyunzxn.zunboard.crypto;

public interface PasswordEncoder {

	String encrypt(String rawPassword);
	boolean matches(String rawPassword, String encryptedPassword);
}
