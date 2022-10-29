package hw01.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import hw01.DiffieHelmanKeyExchange;
import hw01.FastExponentiation;
import hw01.PrimalityTester;

public class AESTest {

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
		//_______INITIALIZATIONS FOR MAIN SYMMETRICAL ENCRYPTION OPERAIONS_________

		System.out.println("_______Stage 1 is Starting______\n");

		FastExponentiation fastExponentiation = new FastExponentiation();
		PrimalityTester primalityTester = new PrimalityTester(fastExponentiation);
		DiffieHelmanKeyExchange DH = new DiffieHelmanKeyExchange(primalityTester);

		BigInteger fixedGenerator = new BigInteger("2");
		System.out.println("Fixed generator using in key generation operations:" + fixedGenerator);
		
		BigInteger fixedLargePrime = DH.generateBigPrimeNumber(128);
		System.out.println("Generated Fixed Large Prime Number:");
		System.out.println(fixedLargePrime);

		Person senderAlice = new Sender( fixedGenerator, fixedLargePrime );
		Person receiverBob = new Receiver(  fixedGenerator, fixedLargePrime );

		//_________________________________________________________
		//----------MAIN SYMMETRICAL ENCRYPTION OPERAIONS----------	

		//--------------------------------------------------------------------------
		// 1) Sender and receiver generate their own public-private keys.

		//Alice's private key is specified.
		System.out.println("\nAlice generates her private key as:");
		senderAlice.generatePrivateKeyForAES(DH, 128); //Alice generates her 128 bit private key.
		System.out.println(senderAlice.getPrivateKey());

		//Bob's private key is specified.
		System.out.println("\nBob generates her private key as:");
		receiverBob.generatePrivateKeyForAES(DH, 128); //Bob generates her 128 bit private key.
		System.out.println( receiverBob.getPrivateKey() );		

		//Alice computes her public key.
		System.out.println("\nAlice's public key is being computing...");
		senderAlice.generatePublicKeyforAES(fastExponentiation);
		System.out.println("Public key of Alice:");
		System.out.println(senderAlice.getPublicKey());

		//Bob computes her public key.
		System.out.println("\nBob's public key is being computing...");
		receiverBob.generatePublicKeyforAES(fastExponentiation);
		System.out.println("Public key of Bob:");
		System.out.println(receiverBob.getPublicKey());


		//--------------------------------------------------------------------------
		// 2) Both parties generate a common secret key by DH Key Exchange scheme.


		//Alice computes shared secret key from Bob's public key.
		//Bob computes shared secret key from Bob's public key.
		DH.performDHKeyExchange(fastExponentiation, senderAlice, receiverBob);


		//--------------------------------------------------------------------------
		// 3) With any AES/DES usage, a basic String is encrypted by sender and decrypted by receiver.(I chosen AES because it is more secure than DES).
		
		System.out.println("\n________________________________________________________");
		System.out.println("\nAES En/Decryption with a String...");
		
		//SharedSecretKey are the same for both Alice and Bob, I used Alice's SharedSecretKey.
		AES aes = new AES(senderAlice.getSharedSecretKey()); 

		String originalString = "CENG 471 Term Project Stage1...";
		String encryptedString = ((Sender) senderAlice).encryptString(aes, originalString) ;
		String decryptedString = ((Receiver) receiverBob).decryptString(aes, encryptedString) ;

		System.out.println("Original Message= "+ originalString);
		System.out.println("Encrypted Message= " + encryptedString);
		System.out.println("Decrypted Message= " + decryptedString);
		
		//--------------------------------------------------------------------------
		//Different lengths of data files are encrypted by sender and decrypted by receiver.


		//______1 PAGE DOCUMENT ECRRPTION AND DECRYPRION_____

		System.out.println("\n________________________________________________________");
		System.out.println("\n1 Page Document Encryption/Decryption is starting...");

		long startTime1 = System.nanoTime();

		//Alice encrypts the originalFile1Page.pdf and save it as encryptedFile1Page(document1PageLengthEncrypted.pdf).
		byte[] encryptedBytes1PageDocument = ((Sender) senderAlice).encryptDocument(aes, inputBytesOf1PageDocument);

		//Then, Alice sends encrypted file to Bob, Bob decrypt it and save as document1PageLengthDecrypted.pdf.
		byte[] decryptedBytes1PageDocument = ((Receiver) receiverBob).decryptDocument(aes, encryptedBytes1PageDocument);

		long stopTime1 = System.nanoTime();
		long elapsedTime1 = stopTime1 - startTime1;
		System.out.println("\n1 Page Document AES Execution Time: " + elapsedTime1 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime1 = Runtime.getRuntime();

		// Run the garbage collector
		runtime1.gc();

		// Calculate the used memory
		long memory1 = runtime1.totalMemory() - runtime1.freeMemory();

		System.out.println("\n1 Page Document Used memory is bytes: " + memory1);
		System.out.println("1 Page Document Used memory is megabytes: "
				+ bytesToMegabytes(memory1));

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

		//Alice encrypts the originalFile10Page and save it as document10PageLengthEncrypted.pdf.
		byte[] encryptedBytes10PageDocument = ((Sender) senderAlice).encryptDocument(aes, inputBytesOf10PageDocument);

		//Then, Alice sends encrypted file to Bob, Bob decrypt it and save as document10PageLengthDecrypted.pdf.
		byte[] decryptedBytes10PageDocument = ((Receiver) receiverBob).decryptDocument(aes, encryptedBytes10PageDocument);

		long stopTime2 = System.nanoTime();
		long elapsedTime2 = stopTime2 - startTime2;

		System.out.println("\n10 Page Document AES Execution Time: " + elapsedTime2 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime2 = Runtime.getRuntime();

		// Run the garbage collector
		runtime2.gc();

		// Calculate the used memory
		long memory2 = runtime2.totalMemory() - runtime2.freeMemory();

		System.out.println("\n10 Page Document Used memory is bytes: " + memory2);
		System.out.println("10 Page Document Used memory is megabytes: "
				+ bytesToMegabytes(memory2));

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

		//Alice encrypts the originalFile100Page and save it as document100PageLengthEncrypted.pdf.
		byte[] encryptedBytes100PageDocument = ((Sender) senderAlice).encryptDocument(aes, inputBytesOf100PageDocument);

		//Then, Alice sends encrypted file to Bob, Bob decrypt it and save as document100PageLengthDecrypted.pdf.
		byte[] decryptedBytes100PageDocument = ((Receiver) receiverBob).decryptDocument(aes, encryptedBytes100PageDocument);

		long stopTime3 = System.nanoTime();
		long elapsedTime3 = stopTime3 - startTime3;

		System.out.println("\n100 Page Document AES Execution Time: " + elapsedTime3 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime3 = Runtime.getRuntime();

		// Run the garbage collector
		runtime3.gc();

		// Calculate the used memory
		long memory3 = runtime3.totalMemory() - runtime3.freeMemory();

		System.out.println("\n100 Page Document Used memory is bytes: " + memory3);
		System.out.println("100 Page Document Used memory is megabytes: "
				+ bytesToMegabytes(memory3));

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

		//Alice encrypts the originalFile1000Page and save it as document1000PageLengthEncrypted.pdf.
		byte[] encryptedBytes1000PageDocument = ((Sender) senderAlice).encryptDocument(aes, inputBytesOf1000PageDocument);

		//Then, Alice sends encrypted file to Bob, Bob decrypt it and save as document1000PageLengthDecrypted.pdf.
		byte[] decryptedBytes1000PageDocument = ((Receiver) receiverBob).decryptDocument(aes, encryptedBytes1000PageDocument);

		long stopTime4 = System.nanoTime();
		long elapsedTime4 = stopTime4 - startTime4;

		System.out.println("\n1000 Page Document AES Execution Time: " + elapsedTime4 / 1000000 + "ms.");

		// Get the Java runtime
		Runtime runtime4 = Runtime.getRuntime();

		// Run the garbage collector
		runtime4.gc();

		// Calculate the used memory
		long memory4 = runtime4.totalMemory() - runtime4.freeMemory();

		System.out.println("\n1000 Page Document Used memory is bytes: " + memory4);
		System.out.println("1000 Page Document Used memory is megabytes: "
				+ bytesToMegabytes(memory4));

		System.out.println("________________________________________________________");

		try {
			outputStream4.write(decryptedBytes1000PageDocument);

			outputStream4.close();

		} catch (IOException e) {
			e.printStackTrace();
		}


		//___________________END OF THE APPLICATION_______________
		System.out.println("\nEn/Decryption with different size Documents is performed successfully...");

		System.out.println("\n___End Of The Application___");

	}

}
