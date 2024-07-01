package DisIMS;

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
        DisIMSBackupEth disIMS = new DisIMSBackupEth();
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

        // UserVerify
        // The credential is retrieved from a file that is owned and stored by user
        System.out.println("=== UserVerify === ");
        boolean userVerifyResult = c.userVerify(caKey.getZ(), uKey.getBeta(), g, w, v, h, u);
        System.out.println("=== UserVerify Result: " + userVerifyResult + " === ");

        // Show
        CredentialPI cre_prime = new CredentialPI(pairing);
        Element[] attributesVer = { a1, a3 };
        Element pk_U_A = cre_prime.show(c, attributesVer, caKey, uKey, g, w, v, h, u);

        // Check - Performed in Blockchain
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

        System.out.println("Encoding Objects to JSON Strings");
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
        System.out.println("Decoding JSON Strings to Objects");
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

        CredentialPI cre_prime2 = new CredentialPI(pairing);
        Element pk_U_A2 = cre_prime2.show(c2, attributesVer2, caKey, uKey, g, w, v,
                h, u);

        CredentialPI cps[] = { cre_prime, cre_prime2 };
        // CredentialPI cpsPrime[] = { cre_prime, cre_prime2 };

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
        caKeyZString = DisIMSUtiltities.encodeElementToString(caKey.getZ());

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

        System.out.println("Decoding JSON Strings to Objects");
        CredentialPI cre_prime_new = new CredentialPI(pairing);
        CredentialPI cre_prime2_new = new CredentialPI(pairing);

        // Decode the Credentials PIs
        String cpsStringNew[] = DisIMSUtiltities.decodeJSONToJSONs(cpsStringArrayJSON);
        CredentialPI cpsNew[] = { cre_prime_new, cre_prime2_new };
        for (int i = 0; i < cpsStringNew.length; i++) {
            cpsNew[i].decodeFromJsonString(cpsStringNew[i]);
        }

        // Decode the pk_U_As
        String pkUAsStringNew[] = DisIMSUtiltities.decodeJSONToJSONs(pkUAsStringArrayJSON);
        Element pk_U_As_New[] = new Element[pkUAsStringNew.length];
        for (int i = 0; i < cpsStringNew.length; i++) {
            pk_U_As_New[i] = DisIMSUtiltities.decodeStringToElement(pkUAsStringNew[i], g1Element);
        }

        // Decode CA's Z
        Element ZForMint = DisIMSUtiltities.decodeStringToElement(caKeyZString, gTElement);

        // Decode Attributes Arrays
        String attributesVerSStringNew[] = DisIMSUtiltities.decodeJSONToJSONs(attributeVerSStringArrayJSON);
        Element attributesVerSNew[][] = new Element[attributesVerSStringNew.length][];
        for (int i = 0; i < cpsStringNew.length; i++) {
            attributesVerSNew[i] = DisIMSUtiltities.decodeJSONStringToElements(attributesVerSStringNew[i],
                    zr);
        }

        // Decode the generators
        Element gForMint = DisIMSUtiltities.decodeStringToElement(gStringForMint, g1Element);
        Element wForMint = DisIMSUtiltities.decodeStringToElement(wStringForMint, g1Element);
        Element vForMint = DisIMSUtiltities.decodeStringToElement(vStringForMint, g1Element);
        Element hForMint = DisIMSUtiltities.decodeStringToElement(hStringForMint, g1Element);
        Element uForMint = DisIMSUtiltities.decodeStringToElement(uStringForMint, g1Element);

        System.out.println("=== MintVerify === ");
        boolean mintVerifyResult = CredentialPI.mintVerify(cpsNew, pk_U_As_New,
                ZForMint, attributesVerSNew, pairing, gForMint, wForMint,
                vForMint, hForMint, uForMint);
        System.out.println("=== MintVerify Result: " + mintVerifyResult + " === ");

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
