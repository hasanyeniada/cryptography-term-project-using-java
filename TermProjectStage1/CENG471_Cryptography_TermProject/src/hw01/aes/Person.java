package hw01.aes;

import java.math.BigInteger;

import hw01.DiffieHelmanKeyExchange;
import hw01.FastExponentiation;

public class Person {

	private BigInteger privateKey;
	private BigInteger publicKey;
	private BigInteger sharedSecretKey;
	
	private BigInteger fixedLargePrime;
	private BigInteger fixedGenerator;
	
	public Person() {		
	}
	
	public Person(BigInteger fixedGenerator, BigInteger fixedLargePrime) {
		this.fixedGenerator = fixedGenerator;
		this.fixedLargePrime = fixedLargePrime;
		
	}
		
	public void generatePrivateKeyForAES(DiffieHelmanKeyExchange DH, int bitLengthOfPrivateKey) {
		privateKey = DH.generateBigPrimeNumber(bitLengthOfPrivateKey);
		
	}
	
	public void generatePublicKeyforAES(FastExponentiation fastExponentiation) {
		publicKey = fastExponentiation.getModularExponentiation(fixedGenerator, privateKey, fixedLargePrime);
		
	}
	
	public void setSharedSecretKey(BigInteger sharedSecretKey) {
		this.sharedSecretKey = sharedSecretKey;
	}

	public void setPublicKey(BigInteger publicKey) {
		this.publicKey = publicKey;
		
	}

	public BigInteger getPrivateKey() {
		return privateKey;
	}

	public BigInteger getPublicKey() {
		return publicKey;
	}

	public BigInteger getSharedSecretKey() {
		return sharedSecretKey;
	}

	public BigInteger getFixedLargePrime() {
		return fixedLargePrime;
	}

	public void setFixedLargePrime(BigInteger fixedLargePrime) {
		this.fixedLargePrime = fixedLargePrime;
	}

	public BigInteger getFixedGenerator() {
		return fixedGenerator;
	}

	public void setFixedGenerator(BigInteger fixedGenerator) {
		this.fixedGenerator = fixedGenerator;
	}
	
}
