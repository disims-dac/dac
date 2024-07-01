/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Running TestApp:
// gradle runApp

package application.java;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

import DisIMS.CAKey;
import DisIMS.CredentialPI;
import DisIMS.Credential;
import DisIMS.DisIMS;
import DisIMS.UKey;
import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class App {

	private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
	private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "basic");

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	// helper function for getting connected to the gateway
	public static Gateway connect() throws Exception{
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "javaAppUser").networkConfig(networkConfigPath).discovery(true);
		return builder.connect();
	}

	public static void main(String[] args) throws Exception {
		//enrolls the admin and registers the user
		try {
			// enrolls the admin and registers the user
		    // Once enrolled, the following two lines can be comments
			// EnrollAdmin.main(null);
			// RegisterUser.main(null);

			// connect to the network and invoke the smart contract
			try (Gateway gateway = connect()) {
				// get the network and contract
				Network network = gateway.getNetwork(CHANNEL_NAME);
				Contract contract = network.getContract(CHAINCODE_NAME);
				
				check(gateway, network, contract);
				mintVerify(gateway, network, contract);
			} catch(Exception e) {
				System.err.println(e);
				System.err.println("Ending program");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	private static void check(Gateway gateway, Network network, Contract contract) throws Exception {
	
			byte[] result;

			// Parameter Setup
			DisIMS disIMS = new DisIMS();
			Element g = disIMS.getG();
			Element w = disIMS.getW();
			Element v = disIMS.getV();
			Element h = disIMS.getH();
			Element u = disIMS.getU();
			Pairing pairing = disIMS.getPairing();
			// CKeyGen
			System.out.println("=== CA Key Generation === ");
			CAKey caKey = new CAKey(pairing, g);
			caKey.genKeys();

			System.out.println("=== User Key Generation === ");
			// UKeyGen
			UKey uKey = new UKey(pairing, g);
			uKey.genKeys();

			// Issue
			System.out.println("=== Issue === ");
			// Message Preparation
			Element a1 = DisIMSUtiltities.generateZrFromHashOfAttribute("First Attribute", pairing);
			Element a2 = DisIMSUtiltities.generateZrFromHashOfAttribute("Second Attribute", pairing);
			Element a3 = DisIMSUtiltities.generateZrFromHashOfAttribute("Third Attribute", pairing);

			Element attributes[] = { a1, a2, a3 };
			Credential c = new Credential(pairing);
			c.generate(attributes, caKey, uKey, g, w, v, h, u);

			// Show
			CredentialPI cre_prime = new CredentialPI(pairing);
			Element[] attributesVer = { a1, a3 };
			Element pk_U_A = cre_prime.show(c, attributesVer, caKey, uKey, g, w, v, h, u);

			// System.out.println("Encoding Objects to JSON Strings");
			String cpString = cre_prime.encodeToJSONStrings();
			String pkUAString = DisIMSUtiltities.encodeElementToString(pk_U_A);
			String caKeyZString = DisIMSUtiltities.encodeElementToString(caKey.getZ());
			String attributesVerJSON = DisIMSUtiltities.encodeElementsToJSONString(attributesVer);
			String gString = DisIMSUtiltities.encodeElementToString(g);
			String wString = DisIMSUtiltities.encodeElementToString(w);
			String vString = DisIMSUtiltities.encodeElementToString(v);
			String hString = DisIMSUtiltities.encodeElementToString(h);
			String uString = DisIMSUtiltities.encodeElementToString(u);
		
			System.out.println("Submitting to Transaction \"Check\"");
			/*
			 * Measure the time of executing the following contract function call
			 */
			result = contract.submitTransaction("Check", pkUAString, caKeyZString, attributesVerJSON, gString, wString, vString, hString, uString, cpString);
			System.out.println("Transaction \"Check\" is Executed, return value is: " + new String(result));
	}

	private static void mintVerify(Gateway gateway, Network network, Contract contract) throws Exception{

			byte[] result;

			// Parameter Setup
			DisIMS disIMS = new DisIMS();
			Element g = disIMS.getG();
			Element w = disIMS.getW();
			Element v = disIMS.getV();
			Element h = disIMS.getH();
			Element u = disIMS.getU();
			Pairing pairing = disIMS.getPairing();
			// CKeyGen
			System.out.println("=== CA Key Generation === ");
			CAKey caKey = new CAKey(pairing, g);
			caKey.genKeys();

			System.out.println("=== User Key Generation === ");
			// UKeyGen
			UKey uKey = new UKey(pairing, g);
			uKey.genKeys();

			// Issue
			System.out.println("=== Issue === ");
			// Message Preparation
			Element a1 = DisIMSUtiltities.generateZrFromHashOfAttribute("First Attribute", pairing);
			Element a2 = DisIMSUtiltities.generateZrFromHashOfAttribute("Second Attribute", pairing);
			Element a3 = DisIMSUtiltities.generateZrFromHashOfAttribute("Third Attribute", pairing);

			Element attributes[] = { a1, a2, a3 };
			Credential c = new Credential(pairing);
			c.generate(attributes, caKey, uKey, g, w, v, h, u);

			// Show - Create the 1st Credential PI
			CredentialPI cre_prime = new CredentialPI(pairing);
			Element[] attributesVer = { a1, a3 };
			Element pk_U_A = cre_prime.show(c, attributesVer, caKey, uKey, g, w, v, h, u);

			// MintVerify - Perform on Blockchain
			// Create one more credential with different attributes and its credential prime
			Element a4 = DisIMSUtiltities.generateZrFromHashOfAttribute("Rivest", pairing);
			Element a5 = DisIMSUtiltities.generateZrFromHashOfAttribute("Schnorr", pairing);
			Element a6 = DisIMSUtiltities.generateZrFromHashOfAttribute("ElGamal", pairing);
			Element a7 = DisIMSUtiltities.generateZrFromHashOfAttribute("Fiat", pairing);
			Element[] attributes2 = { a4, a5, a6, a7 };

			Credential c2 = new Credential(pairing);

			c2.generate(attributes2, caKey, uKey, g, w, v, h, u);

			Element[] attributesVer2 = { a6, a7 };

			// Show - Create the 2nd Credential PI
			CredentialPI cre_prime2 = new CredentialPI(pairing);
			Element pk_U_A2 = cre_prime2.show(c2, attributesVer2, caKey, uKey, g, w, v,
					h, u);

			CredentialPI cps[] = { cre_prime, cre_prime2 };
			Element pk_U_As[] = { pk_U_A, pk_U_A2 };
			Element attributesVerS[][] = { attributesVer, attributesVer2 };

			System.out.println("Encoding Objects to JSON Strings");
			// Encode Credential PI to JSON
			String cpsString[] = new String[cps.length];
			for (int i = 0; i < cps.length; i++) {
				cpsString[i] = cps[i].encodeToJSONStrings();
			}
			// Join all CPs JSON to a single JSON
			String cpsStringArrayJSON = DisIMSUtiltities.encodeJSONsToJSON(cpsString);

			// Encode all pk_U_As to JSON
			String pkUAsString[] = new String[pk_U_As.length];
			for (int i = 0; i < pk_U_As.length; i++) {
				pkUAsString[i] = DisIMSUtiltities.encodeElementToString(pk_U_As[i]);
			}
			// Join all pk_U_A JSON to a single JSON
			String pkUAsStringArrayJSON = DisIMSUtiltities.encodeJSONsToJSON(pkUAsString);

			// Encode CA's Z
			String caKeyZString = DisIMSUtiltities.encodeElementToString(caKey.getZ());

			// Encode Attributes
			String attributeVerSString[] = new String[attributesVerS.length];
			for (int i = 0; i < attributesVerS.length; i++) {
				attributeVerSString[i] = DisIMSUtiltities.encodeElementsToJSONString(attributesVerS[i]);
			}
			// Join all Attributes Array JSON to a single JSON
			String attributeVerSStringArrayJSON = DisIMSUtiltities.encodeJSONsToJSON(attributeVerSString);

			// Encode the generators
			String gStringForMint = DisIMSUtiltities.encodeElementToString(g);
			String wStringForMint = DisIMSUtiltities.encodeElementToString(w);
			String vStringForMint = DisIMSUtiltities.encodeElementToString(v);
			String hStringForMint = DisIMSUtiltities.encodeElementToString(h);
			String uStringForMint = DisIMSUtiltities.encodeElementToString(u);

			System.out.println("Submitting to Transaction \"MintVerify\"");
			/*
			 * Measure the time of executing the following contract function call
			 */
			result = contract.submitTransaction("MintVerify", cpsStringArrayJSON, pkUAsStringArrayJSON, caKeyZString, attributeVerSStringArrayJSON, gStringForMint, wStringForMint, vStringForMint, hStringForMint, uStringForMint);
			System.out.println("Transaction \"MiniVerify\" is Executed, return value is: " + new String(result));
	}
}
