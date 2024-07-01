package DisIMS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import DisIMS.Utility.BlockchainUtiltities;
import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class DisIMSExperiment {
        public static void main(String[] args) throws Exception {

                Properties appProps = new Properties();
                appProps.load(new FileInputStream(".properties"));

                BlockchainUtiltities.privateKey = appProps.getProperty("sk");
                BlockchainUtiltities.contractAddress = appProps.getProperty("contract");
                BlockchainUtiltities.provider = appProps.getProperty("provider");
                BlockchainUtiltities.chainId = Long.parseLong(appProps.getProperty("chainId"));
                BlockchainUtiltities.publicKey = appProps.getProperty("pk");

                // Parameter Setup
                DisIMSBackupEth disMIS = new DisIMSBackupEth();
                Element g = disMIS.getG();
                Element w = disMIS.getW();
                Element v = disMIS.getV();
                Element h = disMIS.getH();
                Element u = disMIS.getU();
                Pairing pairing = disMIS.getPairing();

                // CKeyGen
                System.out.println("=== CA Key Generation === ");
                // Measure the time of CA key generation
                // Start
                CAKey caKey = new CAKey(pairing, g);
                caKey.genKeys();
                // End

                System.out.println("=== User Key Generation === ");
                // UKeyGen
                // Measure the time of User key generation
                // Start
                UKey uKey = new UKey(pairing, g);
                uKey.genKeys();
                // End

                // Issue
                System.out.println("=== Issue === ");
                // Message Preparation
                // Measure the time of Issue
                // Also, measure the average time of 1 - 50 attributes
                // Start
                Element a1 = DisIMSUtiltities.generateZrFromHashOfAttribute("First Attribute", pairing);
                Element a2 = DisIMSUtiltities.generateZrFromHashOfAttribute("Second Attribute", pairing);
                Element a3 = DisIMSUtiltities.generateZrFromHashOfAttribute("Third Attribute", pairing);
                Element attributes[] = { a1, a2, a3 };
                Credential c = new Credential(pairing);
                c.generate(attributes, caKey, uKey, g, w, v, h, u);
                // End

                Credential newC = new Credential(pairing);

                System.out.println("=== Saving Credential To File ===");
                DataOutputStream dataOutputStream = new DataOutputStream(
                                new FileOutputStream("credential.data"));
                c.serialize(dataOutputStream);

                System.out.println("=== Hasing the File ===");
                String hashOfFile = DisIMSUtiltities.getHashStringFromFile("credential.data");

                // Measure the time of putting the hash to the blockchain
                // Need to check the gas spent as well which can be found in the console output
                System.out.println("=== Putting the hash to the blockchain ===");
                // Start
                String tokenID = BlockchainUtiltities.putHashToBlockchain(hashOfFile);
                // End

                // Measure the time of getting the hash from the blockchain
                System.out.println("=== Getting the hash from the blockchain ===");
                // Start
                String fileHashFromBlockchain = BlockchainUtiltities.getHashFromBlockchain(tokenID);
                // End

                // The credential is retrieved from a file that is owned and stored by user
                System.out.println("=== Reading Credential from File ===");
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream("credential.data"));
                newC.deserialize(dataInputStream);

                System.out.println(
                                "=== Compare the hash between the one retrieved from blockchain and the one generated from file ===");
                System.out.println("Valid: "
                                + fileHashFromBlockchain.equals(
                                                DisIMSUtiltities.getHashStringFromFile("credential.data")));

                // UserVerify
                System.out.println("=== UserVerify === ");
                // Measure the time of getting the hash from the blockchain
                // Start
                boolean userVerifyResult = newC.userVerify(caKey.getZ(), uKey.getBeta(), g, w, v, h, u);
                // End
                System.out.println("=== UserVerify Result: " + userVerifyResult + " === ");

                // Show
                CredentialPI cre_prime = new CredentialPI(pairing);
                // For the number of attributes, suppose we have 50 attributes in the credential
                // We test the average time of generating the proof for sub-credential
                Element[] attributesVer = { a1, a3 };
                // Measure the time of generating the proof for sub-credential
                // Start
                Element pk_U_A = cre_prime.show(newC, attributesVer, caKey, uKey, g, w, v, h, u);
                // End

                // Check
                System.out.println("=== Check === ");
                // Measure the time of verifying the proof for sub-credential
                // Start
                boolean checkResult = cre_prime.check(pk_U_A, caKey.getZ(),
                                attributesVer, g, w, v, h, u);
                // End
                System.out.println("=== Check Result: " + checkResult + " === ");

                // MintVerify
                // Create one more credential with different attributes and its credential prime
                Element a4 = DisIMSUtiltities.generateZrFromHashOfAttribute("Rivest", pairing);
                Element a5 = DisIMSUtiltities.generateZrFromHashOfAttribute("Schnorr", pairing);
                Element a6 = DisIMSUtiltities.generateZrFromHashOfAttribute("ElGamal", pairing);
                Element a7 = DisIMSUtiltities.generateZrFromHashOfAttribute("Fiat", pairing);
                Element[] attributes2 = { a4, a5, a6, a7 };
                Credential c2 = new Credential(pairing);
                c2.generate(attributes2, caKey, uKey, g, w, v, h, u);
                Credential newC2 = new Credential(pairing);

                System.out.println("=== Saving Credential 2 from File ===");
                dataOutputStream = new DataOutputStream(new FileOutputStream("credential2.data"));
                c2.serialize(dataOutputStream);

                System.out.println("=== Reading Credential 2 from File ===");
                dataInputStream = new DataInputStream(new FileInputStream("credential2.data"));
                newC2.deserialize(dataInputStream);

                Element[] attributesVer2 = { a6, a7 };

                CredentialPI cre_prime2 = new CredentialPI(pairing);
                Element pk_U_A2 = cre_prime2.show(newC2, attributesVer2, caKey, uKey, g, w, v,
                                h, u);

                System.out.println("=== Saving Credential Prime 1 to File ===");
                dataOutputStream = new DataOutputStream(new FileOutputStream("credentialPrime.data"));
                cre_prime.serialize(dataOutputStream);
                System.out.println("=== Saving Credential Prime 2 to File ===");
                dataOutputStream = new DataOutputStream(new FileOutputStream("credentialPrime2.data"));
                cre_prime2.serialize(dataOutputStream);

                // CredentialPrime cps[] = { cre_prime, cre_prime2 };
                Element pk_U_As[] = { pk_U_A, pk_U_A2 };
                Element attributesVerS[][] = { attributesVer, attributesVer2 };

                System.out.println("=== Hasing the Files ===");
                String hashOfPrime = DisIMSUtiltities.getHashStringFromFile("credentialPrime.data");
                String hashOfPrime2 = DisIMSUtiltities.getHashStringFromFile("credentialPrime2.data");

                // Measure the time of putting the hash to the blockchain
                // Need to check the gas spent as well which can be found in the console output
                // Start
                System.out.println("=== Putting the hash to the blockchain ===");
                String tokenIDprime = BlockchainUtiltities.putHashToBlockchain(hashOfPrime);
                String tokenIDprime2 = BlockchainUtiltities.putHashToBlockchain(hashOfPrime2);
                // End

                // Measure the time of getting the hash from the blockchain
                // Start
                System.out.println("=== Getting the hash from the blockchain ===");
                String fileHashFromBlockchainCPrime = BlockchainUtiltities.getHashFromBlockchain(tokenIDprime);
                String fileHashFromBlockchainCPrime2 = BlockchainUtiltities
                                .getHashFromBlockchain(tokenIDprime2);
                // End

                System.out.println("=== Reading Credential Prime 1 from File ===");
                dataInputStream = new DataInputStream(new FileInputStream("credentialPrime.data"));
                CredentialPI newCre_prime = new CredentialPI(pairing);
                newCre_prime.deserialize(dataInputStream);
                System.out.println("=== Reading Credential Prime 2 from File ===");
                dataInputStream = new DataInputStream(new FileInputStream("credentialPrime2.data"));
                CredentialPI newCre_prime2 = new CredentialPI(pairing);
                newCre_prime2.deserialize(dataInputStream);

                System.out.println(
                                "=== Compare the hash between the one retrieved from blockchain and the one generated from file ===");
                System.out.println("Valid: "
                                + (fileHashFromBlockchainCPrime
                                                .equals(DisIMSUtiltities
                                                                .getHashStringFromFile("credentialPrime.data"))
                                                && fileHashFromBlockchainCPrime2
                                                                .equals(DisIMSUtiltities.getHashStringFromFile(
                                                                                "credentialPrime2.data"))));

                CredentialPI cpsPrime[] = { newCre_prime, newCre_prime2 };

                System.out.println("=== MintVerify === ");

                // Measure the time for verifying a group of shown credentials
                // Start
                boolean mintVerifyResult2 = CredentialPI.mintVerify(cpsPrime, pk_U_As,
                                caKey.getZ(), attributesVerS, pairing, g, w,
                                v, h, u);
                // End
                System.out.println("=== Check Result From Blockchain: " + mintVerifyResult2 +
                                " === ");

        }
}
