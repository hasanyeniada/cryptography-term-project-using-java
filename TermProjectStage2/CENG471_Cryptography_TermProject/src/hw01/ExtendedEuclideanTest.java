package hw01;

import java.math.BigInteger;

public class ExtendedEuclideanTest {


	public static void main(String[] args) {

		//************   GCD   ************

		System.out.println("\n___________________________________________");
		System.out.println("__________________TEST GCD___________________");

		ExtendedEuclidean eE = new ExtendedEuclidean();

		BigInteger number1 = new BigInteger("60");
		BigInteger number2 = new BigInteger("22");;

		BigInteger gcdResult = eE.findGCD(number1, number2);

		System.out.println("\nGCD of " + number1 + " and " + number2 + " => " + gcdResult);
		
		number1 = new BigInteger("6");
		number2 = new BigInteger("12");
		
	    gcdResult = eE.findGCD(number1, number2);
		
		System.out.println("\nGCD of " + number1 + " and " + number2 + " => " + gcdResult);

		//*****************************************************************


		//************   MULTIPLICATIVE INVERSE  ************
		
		System.out.println("\n___________________________________________");
		System.out.println("_________TEST MULTIPLICATIVE INVERSE_________");
		
		BigInteger num1 = new BigInteger("15"); 
		BigInteger num2 = new BigInteger("26");
		BigInteger inverse = eE.findMultiplicativeInverse(num1, num2);
		
		if(!inverse.equals(new BigInteger("-1"))) {
			System.out.println("\nInverse of " +  num1 + " in mod " + num2 + " => " + inverse);
			
		} else {
			
			System.out.println("\n" + num1 + " has no inverse on mod " + num2 + "...");
		}

		num1 = new BigInteger("71"); 
		num2 = new BigInteger("256");
		inverse = eE.findMultiplicativeInverse(num1, num2);
		
		if(!inverse.equals(new BigInteger("-1"))) {
			System.out.println("\nInverse of " +  num1 + " in mod " + num2 + " => " + inverse);
			
		} else {
			
			System.out.println("\n" + num1 + " has no inverse on mod " + num2);
		}
		
		//*****************************************************************


		//************  RELATIVELY PRIME  ************		

		System.out.println("\n_______________________________________________________");
		System.out.println("__________________TEST RELATIVELY PRIME___________________");
		
		boolean isRelativelyPrime = eE.isRelativelyPrime(num1, num2);
		if(isRelativelyPrime) {
			System.out.println("\n" + num1 + " and " + num2 + " are relativey prime...");

		} else {
			System.out.println("\n" + num1 + " and " + num2 + " are not relativey prime...");

		}

		num1 = new BigInteger("24"); 
		num2 = new BigInteger("16"); 
		
		isRelativelyPrime = eE.isRelativelyPrime(num1, num2);
		if(isRelativelyPrime) {
			System.out.println("\n" + num1 + " and " + num2 + " are relativey prime...");

		} else {
			System.out.println("\n" + num1 + " and " + num2 + " are not relativey prime...");

		}
		
		//*****************************************************************

	}

}
