package hw01.digitalsignature;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.List;

import hw01.DiffieHelmanKeyExchange;
import hw01.ExtendedEuclidean;
import hw01.FastExponentiation;
import hw01.PrimalityTester;

public class Simulation {
	
	public static void main(String[] args) {

		//______________________Initialization___________________________
		
		FastExponentiation fastExponentiation = new FastExponentiation();
		PrimalityTester primalityTester = new PrimalityTester(fastExponentiation);
		DiffieHelmanKeyExchange DH = new DiffieHelmanKeyExchange(primalityTester);
		ExtendedEuclidean eE = new ExtendedEuclidean();
		SHA sha = new SHA();
		
		//AES aes = new AES();
		DSA dsa = null; 
		
		Person senderAlice = new Sender(DH, fastExponentiation, eE, sha);
		Person receiverBob = new Receiver(DH, fastExponentiation, eE, sha);

		
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("DSA");
			keyGen.initialize(1024);
			KeyPair keypair = keyGen.genKeyPair();
			DSAPrivateKey privateKey = (DSAPrivateKey) keypair.getPrivate();
			DSAPublicKey publicKey = (DSAPublicKey) keypair.getPublic();
			
			DSAParams dsaParams = privateKey.getParams();
			
			dsa = new DSA(dsaParams, DH, fastExponentiation, eE, sha);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//______________________________________________________________
		
		
		String document = "CENG 471_Stage 3_DigitalSignature";
		
		System.out.println("Digital Signature Simulation is starting...");
		
		System.out.println("\nThe following document will be sent from Sender Alice:");
		System.out.println(document);
		System.out.println("\nAlice will sign the document and then send to Bob...");
		System.out.println("Bob will receive the message then verify the Alice's signature...");
		
		System.out.println("\nLet's start...");
		
		//______________1) Global Keys => (p, q, g) Creation___________
		
		System.out.println("\nThe Shared Global Public Key Values Are Being Creating...");
		
		BigInteger q = dsa.generateQ();
		System.out.println("\nGenerated q:");
		System.out.println(q);
		
		BigInteger p = dsa.generateP();
		System.out.println("\nGenerated p:");
		System.out.println(p);
		
		BigInteger g = dsa.generateG();
		System.out.println("\nGenerated g:");
		System.out.println(g);
		
		System.out.println("Global Public Key Values (p, q, g) are created successfully...");
		
		//______________________________________________________________
		
			
		//______________2) Users Choose their own Private Key and Compute their Public key___________

		System.out.println("\nAlice is Generating her Own Private/Public Keys");
		
		senderAlice.generateRandomPrivateKeyX(q);
		senderAlice.computePublicKeyY(g, p);
		
		System.out.println("Public Key(y) of Alice:");
		System.out.println(senderAlice.getPublicKey());
		System.out.println(senderAlice.getPublicKey().bitLength());
		
		System.out.println("\nBob is Generating his Own Private/Public Keys");
		
		receiverBob.generateRandomPrivateKeyX(q);
		receiverBob.computePublicKeyY(g, p);
		
		System.out.println("Public Key(y) of Bob:");
		System.out.println(receiverBob.getPublicKey());
		
		//______________________________________________________________
		//_________________3) DSA Signature Creation__________________

		System.out.println("\nSender Alice is creating DSA Signature Pair (r, s)...");
		
		List<BigInteger> signaturePair = ((Sender) senderAlice).createSignaturePair(document, p, q, g);
		
		System.out.println("\nSignature Pair (r, s) has been created successfully...");
		
		//______________________________________________________________
		//_________________3) DSA Signature Verification__________________

		System.out.println("\nReceiver Bob is verifying the Signature...");

		BigInteger r = signaturePair.get(0);
		BigInteger s = signaturePair.get(1);
		
		System.out.println("Bob is computing v...");
		
		BigInteger v = ((Receiver) receiverBob).computeV(document, r, s, p, q, g, senderAlice.getPublicKey());
		System.out.println("\nBom computed v as:");
		System.out.println(v);
		
		System.out.println("\nThe r was computed from Sender Alice as:");
		System.out.println(r);
		
		System.out.println("\nFinally Bob verifies the Signature...");
		
		
		//_______________________________________________________
		//________________________Final Result__________________
		
		boolean verificationResult = ((Receiver) receiverBob).verifySignature(signaturePair);
		
		if(verificationResult) {
			System.out.println("(v = r) => Signature is Valid...");
			
		} else {
			System.out.println("(v != r) => Signature is Invalid...");
		}
		
	}

}
