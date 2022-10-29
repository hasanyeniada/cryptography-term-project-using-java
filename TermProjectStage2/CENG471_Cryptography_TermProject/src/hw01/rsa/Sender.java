package hw01.rsa;

import java.math.BigInteger;
import java.util.List;

import hw01.DiffieHelmanKeyExchange;
import hw01.aes.AES;

public class Sender extends Person{

	private List<BigInteger> publicKeyOfReceiver;
	
	public BigInteger encrypSymmetricKey(RSA rsa, BigInteger randomSymmetricKey, Person receiverBob) {
				
		BigInteger encryptedSymmetricKey = rsa.encryptSymmetricKey(randomSymmetricKey, receiverBob.getPublicKey());
		
		return encryptedSymmetricKey;
	}
	
	public BigInteger generateRandomSymmetricKey(DiffieHelmanKeyExchange DH, int bitLength) {
		
		BigInteger randomSymmetricKey = DH.generateBigPrimeNumber(bitLength);
		
		return randomSymmetricKey;
	}
	
	public String encryptString(BigInteger randomSymmetricKey, String strToEncrypt) {
					
		AES aes = new AES(randomSymmetricKey);
		
		String encryptedString =  aes.encryptString(strToEncrypt);
				
		return encryptedString;
	}
	
	public byte[] encryptDocument(BigInteger randomSymmetricKey, byte[] plainBytes) {
		
		AES aes = new AES(randomSymmetricKey);
		
		byte[] encryptedBytes = aes.encryptDocument(plainBytes);
		
		return encryptedBytes;
	}

	public void sendPublicKeyToReceiver(Person receiverBob) {
		
		((Receiver) receiverBob).setPublicKeyOfSender(this.getPublicKey());	
	}
	
	public void setPublicKeyOfReceiver(List<BigInteger> publicKeyOfReceiver) {
		this.publicKeyOfReceiver = publicKeyOfReceiver;
	}
	
}