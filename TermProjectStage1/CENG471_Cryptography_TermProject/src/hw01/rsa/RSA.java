package hw01.rsa;

import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;
import hw01.aes.AES;

public class RSA {

	private BigInteger p; 
	private BigInteger q;
	
	private DiffieHelmanKeyExchange DH;
	private ExtendedEuclidean euclidean;
	private FastExponentiation fE;
	
	public RSA(DiffieHelmanKeyExchange DH, ExtendedEuclidean euclidean, FastExponentiation fE) {
		
		this.DH = DH;
		this.euclidean = euclidean;
		this.fE = fE;
	}
	
	public BigInteger generateN(int bitLength){
		
		p = DH.generateBigPrimeNumber(bitLength);
		
		q = DH.generateBigPrimeNumber(bitLength);
			
		while(p.equals(q)) {
			
			q = DH.generateBigPrimeNumber(bitLength);
			
		}
		
		BigInteger N = p.multiply(q);
				
		return N;
	}
	
	public BigInteger generateT(){
		
		BigInteger T = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
				
		return T;
	}	
	
	public BigInteger generateE(BigInteger T) {
				
		boolean flag = true;
		
		BigInteger e = getRandomBigInteger(T);
		
		while(flag) {
			
			if(euclidean.isRelativelyPrime(e, T)) {
				flag = false;
				
			} else {
				
				e = getRandomBigInteger(T);
			}	
		}
				
		return e;

	}
	
	public BigInteger generateD(BigInteger e, BigInteger T) {
						
		BigInteger d = euclidean.findMultiplicativeInverse(e, T);
		
		return d;
	}
	
	public BigInteger encryptSymmetricKey(BigInteger symmetricKey, List<BigInteger> publicKey) {
		
		BigInteger encryptedSymmetricKey = fE.getModularExponentiation(symmetricKey, publicKey.get(1), publicKey.get(0));
		
		return encryptedSymmetricKey;
	}
	
	public BigInteger decryptSymmetricKey(BigInteger encryptedSymmetricKey, List<BigInteger> privateKey) {
		
		BigInteger decryptedSymmetricKey = fE.getModularExponentiation(encryptedSymmetricKey, privateKey.get(1), privateKey.get(0));
				
		return decryptedSymmetricKey;
	}
	
	public byte[] encryptDocument(byte[] plainBytes, BigInteger e, BigInteger N) {
				
		BigInteger plainData = new BigInteger(plainBytes);
		
		BigInteger encryptedData = fE.getModularExponentiation(plainData, e, N);
		
		return encryptedData.toByteArray();
	}
	
	public byte[] decryptDocument(byte[] encryptedBytes, BigInteger d, BigInteger N) {
		
		BigInteger cipherData = new BigInteger(encryptedBytes);
		
		BigInteger decryptedData = fE.getModularExponentiation(cipherData, d, N);
				
		return decryptedData.toByteArray();
	}
	
	public BigInteger getRandomBigInteger(BigInteger upperLimit) {

		Random rand = new Random();
		BigInteger result;

		do {
			
			result = new BigInteger(upperLimit.bitLength(), rand);
			result = result.add(new BigInteger("2"));
			
		} while(result.compareTo(upperLimit) >= 0);

		return result; //resulting random BigInteger is in range [2, 2^^1024)
	}

	
}
