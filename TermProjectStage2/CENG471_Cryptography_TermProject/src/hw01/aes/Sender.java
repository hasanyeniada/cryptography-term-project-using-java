package hw01.aes;

import java.math.BigInteger;

public class Sender extends Person{
	
	public Sender(BigInteger generator, BigInteger fixedLargePrime) {
		
		super(generator, fixedLargePrime);
				
	}
	
	public String encryptString(AES aes, String strToEncrypt) {
		
		String encryptedString =  aes.encryptString(strToEncrypt);
		
		return encryptedString;
	}
	
	public byte[] encryptDocument(AES aes, byte[] plainBytes) {
		
		byte[] encryptedBytes = aes.encryptDocument(plainBytes);
		
		return encryptedBytes;
	}

	
}
