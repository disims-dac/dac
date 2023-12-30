package DisIMS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class DisIMS {

    PairingParameters parameters;
    Pairing pairing;
    Element g, w, v, h, u;

    public DisIMS() {
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
        DisIMS disMIS = new DisIMS();
        Element g = disMIS.getG();
        Element w = disMIS.getW();
        Element v = disMIS.getV();
        Element h = disMIS.getH();
        Element u = disMIS.getU();
        Pairing pairing = disMIS.getPairing();
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

        Credential newC = new Credential(pairing);
        try {
            System.out.println("=== Saving Credential To File ===");
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("credential.data"));
            c.serialize(dataOutputStream);

            System.out.println("=== Reading Credential To File ===");
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream("credential.data"));
            newC.deserialize(dataInputStream);

            // System.out.println("Is c and recovered c equal? " + c.equals(newC));

            // UserVerify
            // The credential is retrieved from a file that is owned and stored by user
            System.out.println("=== UserVerify === ");
            boolean userVerifyResult = newC.userVerify(caKey.getZ(), uKey.getBeta(), g, w, v, h, u);
            System.out.println("=== UserVerify Result: " + userVerifyResult + " === ");

            // Show
            CredentialPrime cre_prime = new CredentialPrime(pairing);
            Element[] attributesVer = { a1, a3 };
            Element pk_U_A = cre_prime.show(newC, attributesVer, caKey, uKey, g, w, v, h, u);

            // Check
            System.out.println("=== Check === ");
            boolean checkResult = cre_prime.check(caKey.getZ(), pk_U_A, caKey.getZ(),
                    attributesVer, g, w, v, h, u);
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

            System.out.println("=== Saving Credential 2 To File ===");
            dataOutputStream = new DataOutputStream(new FileOutputStream("credential2.data"));
            c2.serialize(dataOutputStream);

            System.out.println("=== Reading Credential 2 from File ===");
            dataInputStream = new DataInputStream(new FileInputStream("credential2.data"));
            newC2.deserialize(dataInputStream);

            Element[] attributesVer2 = { a6, a7 };

            CredentialPrime cre_prime2 = new CredentialPrime(pairing);
            Element pk_U_A2 = cre_prime2.show(newC2, attributesVer2, caKey, uKey, g, w, v,
                    h, u);

            System.out.println("=== Saving Credential Prime 1 To File ===");
            dataOutputStream = new DataOutputStream(new FileOutputStream("credentialPrime.data"));
            cre_prime.serialize(dataOutputStream);
            System.out.println("=== Reading Credential Prime 1 from File ===");
            dataInputStream = new DataInputStream(new FileInputStream("credentialPrime.data"));
            CredentialPrime newCre_prime = new CredentialPrime(pairing);
            newCre_prime.deserialize(dataInputStream);

            System.out.println("=== Saving Credential Prime 2 To File ===");
            dataOutputStream = new DataOutputStream(new FileOutputStream("credentialPrime2.data"));
            cre_prime2.serialize(dataOutputStream);
            System.out.println("=== Reading Credential Prime 2 To File ===");
            dataInputStream = new DataInputStream(new FileInputStream("credentialPrime2.data"));
            CredentialPrime newCre_prime2 = new CredentialPrime(pairing);
            newCre_prime2.deserialize(dataInputStream);

            CredentialPrime cps[] = { cre_prime, cre_prime2 };
            CredentialPrime cpsPrime[] = { newCre_prime, newCre_prime2 };
            Element pk_U_As[] = { pk_U_A, pk_U_A2 };
            Element attributesVerS[][] = { attributesVer, attributesVer2 };
            System.out.println("=== MintVerify === ");
            boolean mintVerifyResult = CredentialPrime.mintVerify(cps, pk_U_As,
                    caKey.getZ(), attributesVerS, pairing, g, w,
                    v, h, u);
            System.out.println("=== Check Result: " + mintVerifyResult + " === ");

            boolean mintVerifyResult2 = CredentialPrime.mintVerify(cpsPrime, pk_U_As,
                    caKey.getZ(), attributesVerS, pairing, g, w,
                    v, h, u);
            System.out.println("=== Check Result From Blockchain: " + mintVerifyResult2 + " === ");

            System.out.println("=== Hash of credentialPrime.data: "
                    + DisIMSUtiltities.getHashStringFromFile("credentialPrime.data") + " === ");

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
