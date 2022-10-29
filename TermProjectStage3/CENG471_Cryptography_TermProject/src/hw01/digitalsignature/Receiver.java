package hw01.digitalsignature;

import java.math.BigInteger;
import java.util.List;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;
import hw01.aes.AES;

public class Receiver extends Person{
	
	private BigInteger v;
	
	public Receiver(DiffieHelmanKeyExchange dH, FastExponentiation fE, ExtendedEuclidean eE, SHA sha) {
		super(fE, dH, eE, sha);
	}

	//_____________Digital Signature Verification________

	public BigInteger computeV(String M, BigInteger r, BigInteger s, BigInteger p, BigInteger q, BigInteger g, BigInteger y) {

		BigInteger w = geteE().findMultiplicativeInverse(s, q);      //Gives inverse of s on mod q.
		System.out.println("\nGenerated w:");
		System.out.println(w);
		
		BigInteger u1 = (getSha().performSHA(M).multiply(w)).mod(q); //Gives [H(M) * w] mod q.
		System.out.println("\nGenerated u1:");
		System.out.println(u1);
		
		BigInteger u2 = (r.multiply(w)).mod(q);                      //Gives (r * w) mod q.
		System.out.println("\nGenerated u2:");
		System.out.println(u2);
		
		BigInteger num1 = getfE().getModularExponentiation(g, u1, p); //Gives g**u1 (mod p).
		BigInteger num2 = getfE().getModularExponentiation(y, u2, p); //Gives y**u2 (mod p).
		BigInteger num3 = (num1.multiply(num2)).mod(p);               //Gives [ (g**u1) * (y**u2) mod p ].

		v = num3.mod(q);  //Gives resulting v.
		
		return v;         //Finally I computed v, and completed to compute signature verification.
	}

	public String decryptString(AES aes, String strToDecrypt) {
		
		String decryptedString =  aes.decryptString(strToDecrypt);
		
		return decryptedString;
	}
	
	public boolean verifySignature(List<BigInteger> signaturePair) {
		
		BigInteger r = signaturePair.get(0);
		
		boolean verificationResult = v.compareTo(r) == 0; //If v = r, signature is valid, othervise invalid.
		
		return verificationResult;
	}
		
}
