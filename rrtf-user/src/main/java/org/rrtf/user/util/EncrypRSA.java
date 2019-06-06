package org.rrtf.user.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class EncrypRSA {

	private KeyPair keyPair;

	/**
	 * 加密方法
	 */
	protected byte[] encrypt(RSAPublicKey publicKey, byte[] srcBytes) throws Exception {
		if (publicKey != null) {
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] resultBytes = cipher.doFinal(srcBytes);
			return resultBytes;
		}
		return null;
	}

	/*
	 * 解密方法
	 */
	protected byte[] decrypt(RSAPrivateKey privateKey, byte[] srcBytes) throws Exception {
		if (privateKey != null) {
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] resultBytes = cipher.doFinal(srcBytes);
			return resultBytes;
		}
		return null;
	}

	/*
	 * 生成密钥对
	 */
	public KeyPair createKeyPair() throws Exception {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为1024位
		keyPairGen.initialize(1024);
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}

	/*
	 * 获取公钥
	 */
	public RSAPublicKey getRSAPublicKey() throws Exception {
		keyPair = createKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		return publicKey;
	}

	/*
	 * 获取私钥
	 */
	public RSAPrivateKey getRSAPrivateKey() throws Exception {
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return privateKey;
	}

	/*
	 * 用公钥加密
	 */
	public byte[] msgEncrypt(String msg) throws Exception {
		RSAPublicKey publicKey = getRSAPublicKey();
		byte[] srcBytes = msg.getBytes();
		byte[] resultBytes = encrypt(publicKey, srcBytes);
		return resultBytes;
	}

	/*
	 * 用公钥解密
	 */
	public String msgDecrypt(byte[] msg) throws Exception {
		RSAPrivateKey privateKey = getRSAPrivateKey();
		byte[] resultBytes = decrypt(privateKey, msg);
		return new String(resultBytes);
	}

	public static void main(String[] args) throws Exception {
		EncrypRSA rsa = new EncrypRSA();
		String msg = "yubinggdfgfdgd";
		byte[] bs = rsa.msgEncrypt(msg);
		String decrypt = rsa.msgDecrypt(bs);

		System.out.println("明文是:" + msg);
		System.out.println("加密后是:" + new String(bs));
		System.out.println("解密后是:" + decrypt);
	}

}
