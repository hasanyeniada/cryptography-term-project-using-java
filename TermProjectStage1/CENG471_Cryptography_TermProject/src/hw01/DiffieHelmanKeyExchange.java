package hw01;

import java.math.BigInteger;
import java.util.Random;

import hw01.aes.Person;

public class DiffieHelmanKeyExchange {

	PrimalityTester tester;

	public DiffieHelmanKeyExchange(PrimalityTester tester) {
		this.tester = tester;
	}


	public void performDHKeyExchange(FastExponentiation fastExponentiation, Person sender, Person receiver) {

		System.out.println("\nAlice is computing her shared secret key from Bob's public key...");
		System.out.println("Alice's resulting Shared Secret Key:");

		//Alice's shared secret key = Bob's Public Key ^^ Alice's private key (mod fixedLargePrime)
		BigInteger sharedSecretKeyOfSender = fastExponentiation.getModularExponentiation(receiver.getPublicKey(), 
																						 sender.getPrivateKey(), 
																						 sender.getFixedLargePrime());
		sender.setSharedSecretKey(sharedSecretKeyOfSender);

		System.out.println( sender.getSharedSecretKey() );	


		System.out.println("\nBob is computing her shared secret key from Alice's public key...");
		System.out.println("Bob's resulting Shared Secret Key:");

		//Bob's shared secret key = Alice's Public Key ^^ Bob's private key (mod fixedLargePrime)
		BigInteger sharedSecretKeyOfReceiver = fastExponentiation.getModularExponentiation(sender.getPublicKey(), 
																						   receiver.getPrivateKey(), 
																						   receiver.getFixedLargePrime());
		receiver.setSharedSecretKey(sharedSecretKeyOfReceiver);

		System.out.println( receiver.getSharedSecretKey() );
	}

	//Generates a prime which has given bit length.
	public BigInteger generateBigPrimeNumber(int bitLength) {

		BigInteger prime = new BigInteger("4"); //That is used to initialize the while loop.
		Random rnd = new Random();   //rnd object to use in probablePrime() method invocation.

		while (!tester.checkPrimalityWithFermatLittleTheorem(prime) ) {

			prime = BigInteger.probablePrime(bitLength, rnd);

		}

		//System.out.println("Integer Value of Generated Number:" + prime.intValue());

		return prime;	
	}

}	
