package hw01.aes;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {

	private static final String ALGO = "AES";
	private static SecretKeySpec secretKeyForAES;

	public AES(BigInteger sharedSecretKeyFromDH) {
		
		byte[] encodedSharedSecret = encodeSharedSecret(sharedSecretKeyFromDH, sharedSecretKeyFromDH.bitLength());
		
		secretKeyForAES = generateSecretKeyForAES(encodedSharedSecret);
	}

	public static byte[] encodeSharedSecret(final BigInteger sharedSecret, final int primeSizeBits) {

		final int sharedSecretSize = (primeSizeBits + Byte.SIZE - 1) / Byte.SIZE;
		//System.out.println("\nSharedSecretSize: " + sharedSecretSize + " bytes...");

		final byte[] signedSharedSecretEncoding = sharedSecret.toByteArray();
		final int signedSharedSecretEncodingLength = signedSharedSecretEncoding.length;

		if (signedSharedSecretEncodingLength == sharedSecretSize) {
			return signedSharedSecretEncoding;
		}

		if (signedSharedSecretEncodingLength == sharedSecretSize + 1) {
			final byte[] sharedSecretEncoding = new byte[sharedSecretSize];
			System.arraycopy(signedSharedSecretEncoding, 1, sharedSecretEncoding, 0, sharedSecretSize);
			return sharedSecretEncoding;
		}

		if (signedSharedSecretEncodingLength < sharedSecretSize) {
			final byte[] sharedSecretEncoding = new byte[sharedSecretSize];
			System.arraycopy(signedSharedSecretEncoding, 0,
					sharedSecretEncoding, sharedSecretSize - signedSharedSecretEncodingLength, signedSharedSecretEncodingLength);
			return sharedSecretEncoding;
		}

		throw new IllegalArgumentException("Shared secret is too big");
	}	

	public SecretKeySpec generateSecretKeyForAES(byte[] secretKeyAsAnByteArray) {
		
		SecretKeySpec sKey = new SecretKeySpec(secretKeyAsAnByteArray, ALGO);

		return sKey;
	}

	public String encryptString(String strToEncrypt)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeyForAES);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		
		return null;
	}

	public String decryptString(String strToDecrypt)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKeyForAES);

			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		
		}
		return null;
	}

	public byte[] encryptDocument(byte[] plainBytes)
	{		
		
		byte[] encryptedBytes = null;
		
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeyForAES);

			encryptedBytes = cipher.doFinal(plainBytes);

			
		}	catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException ex) {
			
            System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		
		return encryptedBytes;
	}

	public byte[] decryptDocument(byte[] encryptedBytes)
	{
		
		byte[] decryptedBytes = null;
		
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKeyForAES);

			decryptedBytes = cipher.doFinal(encryptedBytes);

		}	catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException ex) {
			
            System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		
		return decryptedBytes;
	}

}
