package hw01;

import java.math.BigInteger;

public class ExtendedEuclidean {
	
	public ExtendedEuclidean() {
		
	}
	
	public BigInteger findGCD(BigInteger num1, BigInteger num2) {
		
		BigInteger gcd = new BigInteger("-1");
		BigInteger temp;

		if (num1.compareTo(num2) > 1) { // swap if num1 is greater than num2
			temp = num2;
			num2 = num1;
			num1 = temp;
		}
		
		BigInteger remainder = new BigInteger("-1");
		
		while(!remainder.equals(BigInteger.ZERO)) {

			remainder = num2.remainder(num1);
			
			if(remainder.equals(BigInteger.ZERO)) {
				
				gcd = num1;
				
			}
			
			num2 = num1;
			num1 = remainder;
						
		}
		
		return gcd;
	}
	
	public BigInteger findMultiplicativeInverse(BigInteger num, BigInteger mod)// Finds inverse of num in mod.
	{

		BigInteger pTwoStepBefore = BigInteger.ZERO;
		BigInteger pOneStepBefore = BigInteger.ONE;
		BigInteger qOneStepBefore = new BigInteger("-1");
		BigInteger qTwoStepBefore = new BigInteger("-1");
		
		BigInteger p; // inverse will be p.

		BigInteger y = mod;
		BigInteger x = num; // x in y mod unda inversini bulcaz

		BigInteger remainder = y.mod(x);
		qTwoStepBefore = y.divide(x);

		if (remainder.equals(BigInteger.ZERO)) {
			
			p = new BigInteger("-1");

		} else { 
			
			qOneStepBefore = x.divide(remainder);
			
			BigInteger counter = BigInteger.ZERO;
			BigInteger temp;
			
			while (!remainder.equals(BigInteger.ZERO)) {

				remainder = y.mod(x);
				
				if (counter.compareTo(new BigInteger("2")) >= 0) {
					
					p = pTwoStepBefore.subtract(pOneStepBefore.multiply(qTwoStepBefore));
					p = p.remainder(mod);
					
					pTwoStepBefore = pOneStepBefore;
					pOneStepBefore = p;
					
					qTwoStepBefore = qOneStepBefore;
					qOneStepBefore = y.divide(x);

				}

				temp = y;
				y = x;
				x = temp.remainder(x);
				
				counter = counter.add(BigInteger.ONE);
			}

			p = pTwoStepBefore.subtract(pOneStepBefore.multiply(qTwoStepBefore));
			p = p.mod(mod);
		}	

		return p;
	}

	public boolean isRelativelyPrime(BigInteger num1, BigInteger num2) {

		if (findGCD(num1, num2).equals(BigInteger.ONE)) {
			return true;
			
		} else {
			return false;
		}

	}
	
}