package DisIMS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import org.json.*;

import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class DisIMSBackupEth {

    PairingParameters parameters;
    Pairing pairing;
    Element g, w, v, h, u;

    public DisIMSBackupEth() {
        parameters = PairingFactory.getPairingParameters("params/curves/a.properties");
        pairing = PairingFactory.getPairing(parameters);
        g = pairing.getG1().newRandomElement().getImmutable();
        w = pairing.getG1().newRandomElement().getImmutable();
        v = pairing.getG1().newRandomElement().getImmutable();
        h = pairing.getG1().newRandomElement().getImmutable();
        u = pairing.getG1().newRandomElement().getImmutable();
    }

    public static void main(String[] args) {

        // An implementation of DisMis
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

        // Credential newC = new Credential(pairing);
        try {
            // System.out.println("=== Saving Credential To File ===");
            // DataOutputStream dataOutputStream = new DataOutputStream(new
            // FileOutputStream("credential.data"));
            // c.serialize(dataOutputStream);

            // System.out.println("=== Reading Credential To File ===");
            // DataInputStream dataInputStream = new DataInputStream(new
            // FileInputStream("credential.data"));
            // newC.deserialize(dataInputStream);

            // System.out.println("Is c and recovered c equal? " + c.equals(newC));

            // UserVerify
            // The credential is retrieved from a file that is owned and stored by user
            System.out.println("=== UserVerify === ");
            boolean userVerifyResult = c.userVerify(caKey.getZ(), uKey.getBeta(), g, w, v, h, u);
            System.out.println("=== UserVerify Result: " + userVerifyResult + " === ");

            // Show
            CredentialPI cre_prime = new CredentialPI(pairing);
            Element[] attributesVer = { a1, a3 };
            Element pk_U_A = cre_prime.show(c, attributesVer, caKey, uKey, g, w, v, h, u);

            // Check
            // Convert all input paramaters to String that can be submitted to chaincode
            // Encode to String
            // System.out.println("g: " + g);
            // System.out.println("w: " + w);
            // System.out.println("v: " + v);
            // System.out.println("h: " + h);
            // System.out.println("u: " + u);
            // System.out.println("pk_U_A: " + pk_U_A);
            // System.out.println("caKey.getZ(): " + caKey.getZ());
            // System.out.println("a1: " + attributesVer[0]);
            // System.out.println("a2: " + attributesVer[1]);

            System.out.println("Encoding Objects to Strings");
            String pkUAString = DisIMSUtiltities.encodeElementToString(pk_U_A);
            String caKeyZString = DisIMSUtiltities.encodeElementToString(caKey.getZ());
            String attributesVerJSON = DisIMSUtiltities.encodeElementsToJSONString(attributesVer);
            // System.out.println(attributesVerJSON);
            String gString = DisIMSUtiltities.encodeElementToString(g);
            String wString = DisIMSUtiltities.encodeElementToString(w);
            String vString = DisIMSUtiltities.encodeElementToString(v);
            String hString = DisIMSUtiltities.encodeElementToString(h);
            String uString = DisIMSUtiltities.encodeElementToString(u);

            // Decode to Objects
            System.out.println("Decoding Objects to Strings");
            Element g1Element = pairing.getG1().newElement();
            Element gTElement = pairing.getGT().newElement();
            Element zr = pairing.getZr().newElement();
            pk_U_A = DisIMSUtiltities.decodeStringToElement(pkUAString, g1Element);
            Element Z = DisIMSUtiltities.decodeStringToElement(caKeyZString, gTElement);
            attributesVer = DisIMSUtiltities.decodeJSONStringToElements(attributesVerJSON, zr);
            g = DisIMSUtiltities.decodeStringToElement(gString, g1Element);
            w = DisIMSUtiltities.decodeStringToElement(wString, g1Element);
            v = DisIMSUtiltities.decodeStringToElement(vString, g1Element);
            h = DisIMSUtiltities.decodeStringToElement(hString, g1Element);
            u = DisIMSUtiltities.decodeStringToElement(uString, g1Element);
            // System.out.println("g2: " + g2);
            // System.out.println("w: " + w);
            // System.out.println("v: " + v);
            // System.out.println("h: " + h);
            // System.out.println("u: " + u);
            // System.out.println("pk_U_A: " + pk_U_A);
            // System.out.println("caKey.getZ(): " + Z);
            // System.out.println("a1: " + attributesVer[0]);
            // System.out.println("a2: " + attributesVer[1]);

            System.out.println("=== Check === ");
            boolean checkResult = cre_prime.check(pk_U_A, Z, attributesVer, g, w, v, h, u);
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
            // Credential newC2 = new Credential(pairing);

            // System.out.println("=== Saving Credential 2 To File ===");
            // dataOutputStream = new DataOutputStream(new
            // FileOutputStream("credential2.data"));
            // c2.serialize(dataOutputStream);

            // System.out.println("=== Reading Credential 2 from File ===");
            // dataInputStream = new DataInputStream(new
            // FileInputStream("credential2.data"));
            // newC2.deserialize(dataInputStream);

            Element[] attributesVer2 = { a6, a7 };

            CredentialPI cre_prime2 = new CredentialPI(pairing);
            Element pk_U_A2 = cre_prime2.show(c2, attributesVer2, caKey, uKey, g, w, v,
                    h, u);

            // System.out.println("=== Saving Credential Prime 1 To File ===");
            // dataOutputStream = new DataOutputStream(new
            // FileOutputStream("credentialPrime.data"));
            // cre_prime.serialize(dataOutputStream);
            // System.out.println("=== Reading Credential Prime 1 from File ===");
            // dataInputStream = new DataInputStream(new
            // FileInputStream("credentialPrime.data"));
            // CredentialPI newCre_prime = new CredentialPI(pairing);
            // newCre_prime.deserialize(dataInputStream);

            // System.out.println("=== Saving Credential Prime 2 To File ===");
            // dataOutputStream = new DataOutputStream(new
            // FileOutputStream("credentialPrime2.data"));
            // cre_prime2.serialize(dataOutputStream);
            // System.out.println("=== Reading Credential Prime 2 To File ===");
            // dataInputStream = new DataInputStream(new
            // FileInputStream("credentialPrime2.data"));
            // CredentialPI newCre_prime2 = new CredentialPI(pairing);
            // newCre_prime2.deserialize(dataInputStream);

            CredentialPI cps[] = { cre_prime, cre_prime2 };
            // CredentialPI cpsPrime[] = { cre_prime, cre_prime2 };
            Element pk_U_As[] = { pk_U_A, pk_U_A2 };
            Element attributesVerS[][] = { attributesVer, attributesVer2 };
            System.out.println("Encoding Objects to Strings");

            System.out.println("=== MintVerify === ");
            boolean mintVerifyResult = CredentialPI.mintVerify(cps, pk_U_As,
                    caKey.getZ(), attributesVerS, pairing, g, w,
                    v, h, u);
            System.out.println("=== Check Result: " + mintVerifyResult + " === ");

            // boolean mintVerifyResult2 = CredentialPI.mintVerify(cpsPrime, pk_U_As,
            // caKey.getZ(), attributesVerS, pairing, g, w,
            // v, h, u);
            // System.out.println("=== Check Result From Blockchain: " + mintVerifyResult2 +
            // " === ");

            // System.out.println("=== Hash of credentialPrime.data: "
            // + DisIMSUtiltities.getHashStringFromFile("credentialPrime.data") + " === ");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Element getW() {
        return w;
    }

    public Element getV() {
        return v;
    }

    public Element getH() {
        return h;
    }

    public Element getU() {
        return u;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public Element getG() {
        return g;
    }

}
