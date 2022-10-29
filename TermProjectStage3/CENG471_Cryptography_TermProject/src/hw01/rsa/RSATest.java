package hw01.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;
import hw01.PrimalityTester;
import hw01.aes.AES;
import hw01.rsa.Person;
import hw01.rsa.Receiver;
import hw01.rsa.Sender;

public class RSATest {

	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	public static void main(String[] args) {

		//--------------------------------------------------------------------------	
		//____________________   IO Operation  ____________________________________	
		//Note: I seperated IO operations from main En/Decryption operations, because of the performance measurements should not include IO operations...

		FileInputStream inputStream1 = null;
		FileInputStream inputStream2 = null;
		FileInputStream inputStream3 = null;
		FileInputStream inputStream4 = null;

		byte[] inputBytesOf1PageDocument = null;
		byte[] inputBytesOf10PageDocument = null;
		byte[] inputBytesOf100PageDocument = null;
		byte[] inputBytesOf1000PageDocument = null;

		FileOutputStream outputStream1 = null;
		FileOutputStream outputStream2 = null;
		FileOutputStream outputStream3 = null;
		FileOutputStream outputStream4 = null;

		try {

			File originalFile1Page = new File("document1PageLength.pdf");
			File decryptedFile1Page = new File("document1PageLengthDecrypted.pdf");

			inputStream1 = new FileInputStream(originalFile1Page);
			inputBytesOf1PageDocument = new byte[(int) originalFile1Page.length()];
			inputStream1.read(inputBytesOf1PageDocument);


			File originalFile10Page = new File("document10PageLength.pdf");
			File decryptedFile10Page = new File("document10PageLengthDecrypted.pdf");

			inputStream2 = new FileInputStream(originalFile10Page);
			inputBytesOf10PageDocument = new byte[(int) originalFile10Page.length()];
			inputStream2.read(inputBytesOf10PageDocument);

			File originalFile100Page = new File("document100PageLength.pdf");
			File decryptedFile100Page = new File("document100PageLengthDecrypted.pdf");

			inputStream3 = new FileInputStream(originalFile100Page);
			inputBytesOf100PageDocument = new byte[(int) originalFile100Page.length()];
			inputStream3.read(inputBytesOf100PageDocument);

			File originalFile1000Page = new File("document1000PageLength.pdf");
			File decryptedFile1000Page = new File("document1000PageLengthDecrypted.pdf");

			inputStream4 = new FileInputStream(originalFile1000Page);
			inputBytesOf1000PageDocument = new byte[(int) originalFile1000Page.length()];
			inputStream4.read(inputBytesOf1000PageDocument);

			outputStream1 = new FileOutputStream(decryptedFile1Page);
			outputStream2 = new FileOutputStream(decryptedFile10Page);
			outputStream3 = new FileOutputStream(decryptedFile100Page);
			outputStream4 = new FileOutputStream(decryptedFile1000Page);

			inputStream1.close();
			inputStream2.close();
			inputStream3.close();
			inputStream4.close();

		} catch (IOException ex) {

			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		//------------------------------------------------------------------------	
		//_______INITIALIZATIONS FOR MAIN ASYMMETRICAL ENCRYPTION OPERAIONS_________

		ExtendedEuclidean euclidean = new ExtendedEuclidean();
		FastExponentiation fE = new FastExponentiation();

		PrimalityTester tester = new PrimalityTester(fE);
		DiffieHelmanKeyExchange DH = new DiffieHelmanKeyExchange(tester);

		RSA rsa = new RSA(DH, euclidean, fE);

		Person senderAlice = new Sender();
		Person receiverBob = new Receiver();

		//_________________________________________________________
		//----------MAIN ASYMMETRICAL ENCRYPTION OPERAIONS----------	

		//1) Sender and Receiver generates their own public/private key pairs.

		System.out.println("\nThe sender Alice generates her own public/private key pair...");
		senderAlice.generatePublicPrivateKeyPair(rsa, 1024);
		System.out.println("Public/Private Key Pair of Sender Alice:");
		System.out.println("Public Key (N,e) = (" + senderAlice.getPublicKey().get(0) + ", " + senderAlice.getPublicKey().get(1) + ")");
		System.out.println("Private Key (N,d) = (" + senderAlice.getPrivateKey().get(0) + ", " + senderAlice.getPrivateKey().get(1) + ")");

		System.out.println("\nThe receiver Bob generates her own public/private key pair...");
		receiverBob.generatePublicPrivateKeyPair(rsa, 1024);
		System.out.println("Public/Private Key Pair of Receiver Bob:");
		System.out.println("Public Key (N,e) = (" + receiverBob.getPublicKey().get(0) + ", " + receiverBob.getPublicKey().get(1) + ")");
		System.out.println("Private Key (N,d) = (" + receiverBob.getPrivateKey().get(0) + ", " + receiverBob.getPrivateKey().get(1) + ")");


		//--------------------------------------------------------------------------
		//2) Sender and Receiver share their public keys.

		((Sender) senderAlice).sendPublicKeyToReceiver(receiverBob);
		((Receiver) receiverBob).sendPublicKeyToSender(senderAlice);

		//--------------------------------------------------------------------------
		//3) RSA encryption scheme is used to encrypt and decrypt a String.

		System.out.println("\n________________________________________________________");
		System.out.println("\nRSA En/Decryption with a String...\n");

		//A random Symmetric key is generated by sender and 
		//then encrypted using RSA encryption scheme with receiver's public key.
		BigInteger randomSymmetricKey = ((Sender) senderAlice).generateRandomSymmetricKey(DH, 128);	
		BigInteger encryptedSymmetricKey = ((Sender) senderAlice).encrypSymmetricKey(rsa, randomSymmetricKey, receiverBob);

		String plainText = "CENG 471 Term Project Stage1...";
		String encryptedString = ((Sender) senderAlice).encryptString(randomSymmetricKey, plainText);
		String decryptedString = ((Receiver) receiverBob).decryptString(rsa, encryptedSymmetricKey, encryptedString);

		System.out.println("Original Message= "+ plainText);
		System.out.println("Encrypted Message= " + encryptedString);
		System.out.println("Decrypted Message= " + decryptedString);

		//--------------------------------------------------------------------------
		// 3)RSA encryption scheme is used to encrypt and decrypt different length files.

		//______1 PAGE DOCUMENT ECRRPTION AND DECRYPRION_____

		System.out.println("\n________________________________________________________");
		System.out.println("\n1 Page Document Encryption/Decryption is starting...");

		long startTime1 = System.nanoTime();

		//A random Symmetric key is generated by sender and 
		//then encrypted using RSA encryption scheme with receiver's public key.
		BigInteger randomSymmetricKey1 = ((Sender) senderAlice).generateRandomSymmetricKey(DH, 128);	
		BigInteger encryptedSymmetricKey1 = ((Sender) senderAlice).encrypSymmetricKey(rsa, randomSymmetricKey1, receiverBob);

		//Alice encrypts original document with generated randomSymmetricKey using AES.
		byte[] encryptedBytes1PageDocument = ((Sender) senderAlice).encryptDocument(randomSymmetricKey1, inputBytesOf1PageDocument);

		/*
		Then, Alice sends encryptedSymmetricKey and encryptedDocument to Bob, 
		Bob decrypts encryptedSymmetricKey using RSA and decrypts encryptedDocument 
		with that decryptedSymmetricKey using AES. */
		byte[] decryptedBytes1PageDocument = ((Receiver) receiverBob).decryptDocument(rsa, encryptedSymmetricKey1, encryptedBytes1PageDocument);


		long stopTime1 = System.nanoTime();
		long elapsedTime1 = stopTime1 - startTime1;
		System.out.println("\n1 Page Document RSA Execution Time: " + elapsedTime1 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime1 = Runtime.getRuntime();

		// Run the garbage collector
		runtime1.gc();

		// Calculate the used memory
		long memory1 = runtime1.totalMemory() - runtime1.freeMemory();

		System.out.println("\n1 Page Document Used memory is bytes: " + memory1);
		System.out.println("1 Page Document Used memory is megabytes: " + bytesToMegabytes(memory1));

		System.out.println("________________________________________________________");

		try {
			outputStream1.write(decryptedBytes1PageDocument);

			outputStream1.close();

		} catch (IOException e) {
			e.printStackTrace();

		}


		//______10 PAGE DOCUMENT ECRRPTION AND DECRYPRION_____

		System.out.println("\n________________________________________________________");
		System.out.println("\n10 Page Document Encryption/Decryption is starting...");

		long startTime2 = System.nanoTime();

		//A random Symmetric key is generated by sender and 
		//then encrypted using RSA encryption scheme with receiver's public key.
		BigInteger randomSymmetricKey2 = ((Sender) senderAlice).generateRandomSymmetricKey(DH, 128);	
		BigInteger encryptedSymmetricKey2 = ((Sender) senderAlice).encrypSymmetricKey(rsa, randomSymmetricKey2, receiverBob);

		//Alice encrypts original document with generated randomSymmetricKey using AES.
		byte[] encryptedBytes10PageDocument = ((Sender) senderAlice).encryptDocument(randomSymmetricKey2, inputBytesOf10PageDocument);

		/*
				Then, Alice sends encryptedSymmetricKey and encryptedDocument to Bob, 
				Bob decrypts encryptedSymmetricKey using RSA and decrypts encryptedDocument 
				with that decryptedSymmetricKey using AES. */
		byte[] decryptedBytes10PageDocument = ((Receiver) receiverBob).decryptDocument(rsa, encryptedSymmetricKey2, encryptedBytes10PageDocument);


		long stopTime2 = System.nanoTime();
		long elapsedTime2 = stopTime2 - startTime2;
		System.out.println("\n10 Page Document RSA Execution Time: " + elapsedTime2 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime2 = Runtime.getRuntime();

		// Run the garbage collector
		runtime2.gc();

		// Calculate the used memory
		long memory2 = runtime2.totalMemory() - runtime2.freeMemory();

		System.out.println("\n10 Page Document Used memory is bytes: " + memory2);
		System.out.println("10 Page Document Used memory is megabytes: " + bytesToMegabytes(memory2));

		System.out.println("________________________________________________________");

		try {
			outputStream2.write(decryptedBytes10PageDocument);

			outputStream2.close();

		} catch (IOException e) {
			e.printStackTrace();

		}

		//______100 PAGE DOCUMENT ECRRPTION AND DECRYPRION_____

		System.out.println("\n________________________________________________________");
		System.out.println("\n100 Page Document Encryption/Decryption is starting...");

		long startTime3 = System.nanoTime();

		//A random Symmetric key is generated by sender and 
		//then encrypted using RSA encryption scheme with receiver's public key.
		BigInteger randomSymmetricKey3 = ((Sender) senderAlice).generateRandomSymmetricKey(DH, 128);	
		BigInteger encryptedSymmetricKey3 = ((Sender) senderAlice).encrypSymmetricKey(rsa, randomSymmetricKey3, receiverBob);

		//Alice encrypts original document with generated randomSymmetricKey using AES.
		byte[] encryptedBytes100PageDocument = ((Sender) senderAlice).encryptDocument(randomSymmetricKey3, inputBytesOf100PageDocument);

		/*
				Then, Alice sends encryptedSymmetricKey and encryptedDocument to Bob, 
				Bob decrypts encryptedSymmetricKey using RSA and decrypts encryptedDocument 
				with that decryptedSymmetricKey using AES. */
		byte[] decryptedBytes100PageDocument = ((Receiver) receiverBob).decryptDocument(rsa, encryptedSymmetricKey3, encryptedBytes100PageDocument);


		long stopTime3 = System.nanoTime();
		long elapsedTime3 = stopTime3- startTime3;
		System.out.println("\n100 Page Document RSA Execution Time: " + elapsedTime3 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime3 = Runtime.getRuntime();

		// Run the garbage collector
		runtime3.gc();

		// Calculate the used memory
		long memory3 = runtime3.totalMemory() - runtime3.freeMemory();

		System.out.println("\n100 Page Document Used memory is bytes: " + memory3);
		System.out.println("100 Page Document Used memory is megabytes: " + bytesToMegabytes(memory3));

		System.out.println("________________________________________________________");

		try {
			outputStream3.write(decryptedBytes100PageDocument);

			outputStream3.close();

		} catch (IOException e) {
			e.printStackTrace();

		}

		//______1000 PAGE DOCUMENT ECRRPTION AND DECRYPRION_____

		System.out.println("\n________________________________________________________");
		System.out.println("\n1000 Page Document Encryption/Decryption is starting...");

		long startTime4 = System.nanoTime();

		//A random Symmetric key is generated by sender and 
		//then encrypted using RSA encryption scheme with receiver's public key.
		BigInteger randomSymmetricKey4 = ((Sender) senderAlice).generateRandomSymmetricKey(DH, 128);	
		BigInteger encryptedSymmetricKey4 = ((Sender) senderAlice).encrypSymmetricKey(rsa, randomSymmetricKey4, receiverBob);

		//Alice encrypts original document with generated randomSymmetricKey using AES.
		byte[] encryptedBytes1000PageDocument = ((Sender) senderAlice).encryptDocument(randomSymmetricKey4, inputBytesOf1000PageDocument);

		/*
			Then, Alice sends encryptedSymmetricKey and encryptedDocument to Bob, 
			Bob decrypts encryptedSymmetricKey using RSA and decrypts encryptedDocument 
			with that decryptedSymmetricKey using AES. */
		byte[] decryptedBytes1000PageDocument = ((Receiver) receiverBob).decryptDocument(rsa, encryptedSymmetricKey4, encryptedBytes1000PageDocument);


		long stopTime4 = System.nanoTime();
		long elapsedTime4 = stopTime4 - startTime4;
		System.out.println("\n1000 Page Document RSA Execution Time: " + elapsedTime4 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime4 = Runtime.getRuntime();

		// Run the garbage collector
		runtime4.gc();

		// Calculate the used memory
		long memory4 = runtime4.totalMemory() - runtime4.freeMemory();

		System.out.println("\n1000 Page Document Used memory is bytes: " + memory4);
		System.out.println("1000 Page Document Used memory is megabytes: " + bytesToMegabytes(memory4));

		System.out.println("________________________________________________________");

		try {
			outputStream4.write(decryptedBytes1000PageDocument);

			outputStream4.close();

		} catch (IOException e) {
			e.printStackTrace();

		}


	}


}
