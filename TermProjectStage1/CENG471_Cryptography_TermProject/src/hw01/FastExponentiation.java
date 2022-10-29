package hw01;

import java.math.BigInteger;

public class FastExponentiation {

	//a to the power b (mod q)
	public BigInteger getModularExponentiation(BigInteger a, BigInteger b, BigInteger q) {

		BigInteger zero = java.math.BigInteger.ZERO;
		BigInteger one = java.math.BigInteger.ONE;
		BigInteger two = new BigInteger("2");

		if(b.equals(zero)) {
			return one;
		}
		if(b.mod(two).equals(zero)){
			return (getModularExponentiation(a, b.divide(two), q).pow(2))
											.mod(q);
		} else {
			return (a.multiply( getModularExponentiation(a, b.subtract(one), q)))
					.mod(q);
		}
	}

}
