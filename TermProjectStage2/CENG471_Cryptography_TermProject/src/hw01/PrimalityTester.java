package hw01;

import java.math.BigInteger;
import java.util.Random;

public class PrimalityTester {

	FastExponentiation fastExponentiation;
	
	public PrimalityTester(FastExponentiation fastExponentiation) {
		
		this.fastExponentiation = fastExponentiation;
		
	}


	public boolean checkPrimalityWithFermatLittleTheorem(BigInteger candidatePrimeNumber){

		boolean isPrimeFlag = true;

		BigInteger resultingRemainder;
		BigInteger k = candidatePrimeNumber.subtract(BigInteger.ONE);
				
		for(BigInteger i = BigInteger.ONE; i.compareTo(k) == -1; i = i.add(BigInteger.ONE)) {

			//Pick a random big integer between [2,p-1]
			BigInteger randomBigInteger = getRandomBigInteger(candidatePrimeNumber);
			
			resultingRemainder = fastExponentiation.getModularExponentiation(randomBigInteger, 
																			 candidatePrimeNumber.subtract(BigInteger.ONE), 
																			 candidatePrimeNumber);
			
			if(!resultingRemainder.equals(BigInteger.ONE)) {

				isPrimeFlag = false;
				i = k; //To finish the loop.				
			}
			
			k = k.divide(new BigInteger("2"));
		}

		return isPrimeFlag;
	}

	public BigInteger getRandomBigInteger(BigInteger upperLimit) {

		Random rand = new Random();
		BigInteger result;

		do {
			
			result = new BigInteger(upperLimit.bitLength(), rand);
			
		} while(result.compareTo(upperLimit.subtract(new BigInteger("2"))) >= 0);

		return result.add(new BigInteger("2")); //resulting random BigInteger is in range [2, 2^^128)
	}

}
