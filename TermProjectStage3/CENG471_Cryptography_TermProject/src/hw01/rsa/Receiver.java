package hw01.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import hw01.aes.AES;

public class Receiver extends Person{
	
	private List<BigInteger> publicKeyOfSender;
	
	public Receiver() {
		
		publicKeyOfSender = new ArrayList<BigInteger>();
	}
	
	public String decryptString(RSA rsa, BigInteger encryptedSymmetricKey, String encryptedString) {
		
		BigInteger decryptedSymmetricKey = rsa.decryptSymmetricKey(encryptedSymmetricKey, getPrivateKey());
		
		AES aes = new AES(decryptedSymmetricKey);
		
		String decryptedString =  aes.decryptString(encryptedString);
		
		return decryptedString;
	}
	
	public byte[] decryptDocument(RSA rsa, BigInteger encryptedSymmetricKey, byte[] encryptedBytes) {
		
		BigInteger decryptedSymmetricKey = rsa.decryptSymmetricKey(encryptedSymmetricKey, getPrivateKey());

		AES aes = new AES(decryptedSymmetricKey);
		
		byte[] decryptedBytes = aes.decryptDocument(encryptedBytes);
		
		return decryptedBytes;
	}

	public void sendPublicKeyToSender(Person senderAlice) {
		
		((Sender) senderAlice).setPublicKeyOfReceiver(this.getPublicKey());
	}
	
	public void setPublicKeyOfSender(List<BigInteger> publicKeyOfSender) {
		
		this.publicKeyOfSender = publicKeyOfSender;
	}

}
