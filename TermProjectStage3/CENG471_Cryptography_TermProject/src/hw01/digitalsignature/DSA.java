package hw01.digitalsignature;

import java.security.interfaces.DSAParams;
import java.math.BigInteger;
import java.util.Random;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;

public class DSA {

	private BigInteger globalPublicKeyP;
	private BigInteger globalPublicKeyQ;
	private BigInteger globalPublicKeyG;
	
	private DiffieHelmanKeyExchange DH;
	private FastExponentiation fE;
	private ExtendedEuclidean eE;
	private SHA sha;

	private DSAParams dsaParams;
	
	public DSA(DSAParams dsaParams, DiffieHelmanKeyExchange dH, FastExponentiation fE, ExtendedEuclidean eE, SHA sha) {
		this.DH = dH;
		this.fE = fE;
		this.eE = eE;
		this.sha = sha;
		this.dsaParams = dsaParams;
	}

	// I generated (p, q, g) using DSAParams class, 
	// because execution time become very high If I use my implemented modules.
	
	public BigInteger generateQ() {

		this.globalPublicKeyQ = dsaParams.getQ();
		
		return this.globalPublicKeyQ;
	}

	public BigInteger generateP() {

		this.globalPublicKeyP = dsaParams.getP();

		return this.globalPublicKeyP;
	}

	public BigInteger generateG() {

		this.globalPublicKeyG = dsaParams.getG();

		return this.globalPublicKeyG;
	}
	
	//Generates a random BigInteger in a given range.[lower, upper)
	public static BigInteger getRandomBigInteger(BigInteger lowerBound, BigInteger upperLimit) {

		Random rand = new Random();
		BigInteger result;

		do {
			result = new BigInteger(upperLimit.bitLength(), rand);
			result = result.add(lowerBound);

		} while(result.compareTo(upperLimit) >= 0);

		return result; //resulting random BigInteger is in range [2, 2^^1024)
	}

	public BigInteger getGlobalPublicKeyP() {
		return globalPublicKeyP;
	}

	public void setGlobalPublicKeyP(BigInteger globalPublicKeyP) {
		this.globalPublicKeyP = globalPublicKeyP;
	}

	public BigInteger getGlobalPublicKeyQ() {
		return globalPublicKeyQ;
	}

	public void setGlobalPublicKeyQ(BigInteger globalPublicKeyQ) {
		this.globalPublicKeyQ = globalPublicKeyQ;
	}

	public BigInteger getGlobalPublicKeyG() {
		return globalPublicKeyG;
	}

	public void setGlobalPublicKeyG(BigInteger globalPublicKeyG) {
		this.globalPublicKeyG = globalPublicKeyG;
	}
	
	

}
