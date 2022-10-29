package hw01.aes;

import java.math.BigInteger;

public class Receiver extends Person{
	
	public Receiver(BigInteger generator, BigInteger fixedLargePrime) {
		
		super(generator, fixedLargePrime);
				
	}
	
	public String decryptString(AES aes, String strToDecrypt) {
		
		String decryptedString =  aes.decryptString(strToDecrypt);
		
		return decryptedString;
	}
	
	public byte[] decryptDocument(AES aes, byte[] encryptedBytes) {
		
		byte[] decryptedBytes = aes.decryptDocument(encryptedBytes);
		
		return decryptedBytes;
	}

}
