package hw01.digitalsignature;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;

public class Sender extends Person {

	private List<BigInteger> signaturePair;
	
	public Sender(DiffieHelmanKeyExchange dH, FastExponentiation fE, ExtendedEuclidean eE, SHA sha) {
		super(fE, dH, eE, sha);
		signaturePair = new ArrayList<BigInteger>();
	}

	// _____________Digital Signature Creation_________

	public BigInteger generateK(BigInteger q) { //Generates a random signature key k, k < q.
		
		BigInteger k = DSA.getRandomBigInteger(BigInteger.ONE, q);

		return k;
	}

	public BigInteger computeR(BigInteger p, BigInteger q, BigInteger g, BigInteger k) {

		BigInteger resultOfModularExpo = getfE().getModularExponentiation(g, k, p); // g**k (mod p).

		BigInteger r = resultOfModularExpo.mod(q); // r = [g**k (mod p)](mod q).

		signaturePair.add(r);
		
		return r;
	}

	public BigInteger computeS(String M, BigInteger p, BigInteger q, 
							   BigInteger k, BigInteger r, BigInteger x) {

		BigInteger inverseK = geteE().findMultiplicativeInverse(k, q); // Gives inverse of k in (mod q).

		BigInteger hashOfMessage = getSha().performSHA(M); // Gives H(M).

		BigInteger xr = x.multiply(r); // Gives (x*r), where x is the user's private key.

		BigInteger xr_plus_hashOfMessage = xr.add(hashOfMessage);  // Gives (xr + H(M)).

		BigInteger s = (inverseK.multiply(xr_plus_hashOfMessage)); // Gives resulting s.

		signaturePair.add(s); //Signature pair is created like (r,s)
		
		return s; // Finally I computed s, and completed to creating signature pair (r,s).
	}

	public List<BigInteger> createSignaturePair(String message, BigInteger p, 
										        BigInteger q, BigInteger g) {
		
		System.out.println("\nStep 1) Generate random signature key k, k < q:");
		BigInteger k = generateK(q);
		System.out.println("\nGenerated k:");
		System.out.println(k);

		System.out.println("\nStep 2) Compute Signature Pair (r, s)...");
		BigInteger r = computeR(p, q, g, k);
		System.out.println("\nGenerated r:");
		System.out.println(r);

		BigInteger s = computeS(message, p, q, k, r, getPrivateKey());
		System.out.println("\nGenerated s:");
		System.out.println(s);
		
		signaturePair.add(r);
		signaturePair.add(s);
		
		return signaturePair; //Signature pair: (r, s)
	}
	
	public BigInteger generateRandomSymmetricKey(int bitLength) {
		
		BigInteger randomSymmetricKey = getdH().generateBigPrimeNumber(bitLength);
		
		return randomSymmetricKey;
	}
	
}
