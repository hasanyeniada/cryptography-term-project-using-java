package hw01.digitalsignature;

import java.math.BigInteger;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;

public class Person {

	private BigInteger privateKeyX;
	private BigInteger publicKeyY;
	
	private FastExponentiation fE;	
	private DiffieHelmanKeyExchange dH;
	private ExtendedEuclidean eE;
	private SHA sha;

	public Person(FastExponentiation fE, DiffieHelmanKeyExchange dH, ExtendedEuclidean eE, SHA sha) {
		this.fE = fE;
		this.dH = dH;
		this.eE = eE;
		this.sha = sha;
	}

	public BigInteger generateRandomPrivateKeyX(BigInteger q) {

		this.privateKeyX = DSA.getRandomBigInteger(BigInteger.ONE, q);

		return this.privateKeyX;
	}

	public BigInteger computePublicKeyY(BigInteger g, BigInteger p) {

		BigInteger x = getPrivateKey();
		
		this.publicKeyY = fE.getModularExponentiation(g, x, p); //y = g**x(mod p)

		return this.publicKeyY;
	}

	public BigInteger getPrivateKey() {
		return privateKeyX;
	}

	public void setPrivateKey(BigInteger privateKey) {
		this.privateKeyX = privateKey;
	}

	public BigInteger getPublicKey() {
		return publicKeyY;
	}

	public void setPublicKey(BigInteger publicKey) {
		this.publicKeyY = publicKey;
	}

	public FastExponentiation getfE() {
		return fE;
	}

	public void setfE(FastExponentiation fE) {
		this.fE = fE;
	}

	public DiffieHelmanKeyExchange getdH() {
		return dH;
	}

	public void setdH(DiffieHelmanKeyExchange dH) {
		this.dH = dH;
	}

	public ExtendedEuclidean geteE() {
		return eE;
	}

	public void seteE(ExtendedEuclidean eE) {
		this.eE = eE;
	}

	public SHA getSha() {
		return sha;
	}

	public void setSha(SHA sha) {
		this.sha = sha;
	}



}
