package hw01.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Person {

	private List<BigInteger> privateKey; //(N, d)
	private List<BigInteger> publicKey;  //(N, e)
	
	public Person() {	
		
		privateKey = new ArrayList<BigInteger>();
		publicKey = new ArrayList<BigInteger>();
	}
	
	public void generatePublicPrivateKeyPair(RSA rsa, int keyBitLength) {

		BigInteger N = rsa.generateN(keyBitLength); //N = p * q
		BigInteger T = rsa.generateT();             //T = (p-1) * (q-1)
		BigInteger e = rsa.generateE(T);            //gcd(e, T) = 1

		publicKey.add(N);
		publicKey.add(e);  //public key is generated (N, e)
				
		BigInteger d = rsa.generateD(e, T);         //

		privateKey.add(N);
		privateKey.add(d); //private key is generated (N, d)	
	}

	public List<BigInteger> getPrivateKey() {
		return privateKey;
	}

	public List<BigInteger> getPublicKey() {
		return publicKey;
	}

}
