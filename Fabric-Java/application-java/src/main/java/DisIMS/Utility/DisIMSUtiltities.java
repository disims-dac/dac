package DisIMS.Utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.json.JSONArray;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class DisIMSUtiltities {

    public static String getHash(String s) {
        try {

            byte[] bytes = s.getBytes();
            Digest digest = new SHA256Digest();
            digest.reset();
            digest.update(bytes, 0, bytes.length);
            int digestSize = digest.getDigestSize();
            byte[] hash = new byte[digestSize];
            digest.doFinal(hash, 0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHashStringFromFile(String filename) {
        try {
            File file = new File(filename);
            FileInputStream f = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            f.read(bytes);
            Digest digest = new SHA256Digest();
            digest.reset();
            digest.update(bytes, 0, bytes.length);
            int digestSize = digest.getDigestSize();
            byte[] hash = new byte[digestSize];
            digest.doFinal(hash, 0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            f.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeElementToFile(Pairing pairing, Element e, DataOutputStream dOut) throws Exception {
        dOut.writeInt(pairing.getFieldIndex(e.getField()));
        byte[] bytes = e.toBytes();
        dOut.writeInt(bytes.length);
        dOut.write(bytes);
    }

    public static Element readElementFromFile(Pairing pairing, DataInputStream dIn)
            throws Exception {
        int fieldIndex = dIn.readInt();
        int length = dIn.readInt();
        byte[] bytes = new byte[length];
        dIn.read(bytes);
        return pairing.getFieldAt(fieldIndex).newElementFromBytes(bytes).getImmutable();
    }

    public static Element generateZrFromHashOfAttribute(String attribute, Pairing pairing) {
        byte[] bytes = attribute.getBytes();
        Digest digest = new SHA256Digest();
        digest.reset();
        digest.update(bytes, 0, bytes.length);
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);
        return pairing.getZr().newElementFromHash(hash, 0, hash.length).getImmutable();
    }

    public static Element generateZrFromShowAttributes(Pairing pairing, Element c0_prime, Element c1_prime,
            Element c2_prime[], Element c3_prime[], Element S, Element W, Element X, Element Y, Element D, Element E,
            Element pk_U_A) {

        byte[] c_0_prime_bytes = c0_prime.toBytes();
        byte[] c_1_prime_bytes = c1_prime.toBytes();
        byte[][] c2_prime_bytes = new byte[c2_prime.length][];
        byte[][] c3_prime_bytes = new byte[c3_prime.length][];
        int primeLength = 0;
        for (int i = 0; i < c2_prime.length; i++) {
            c2_prime_bytes[i] = c2_prime[i].toBytes();
            c3_prime_bytes[i] = c3_prime[i].toBytes();
            primeLength += c2_prime_bytes[i].length + c3_prime_bytes[i].length;
        }

        byte[] S_bytes = S.toBytes();
        byte[] W_bytes = W.toBytes();
        byte[] X_bytes = X.toBytes();
        byte[] Y_bytes = Y.toBytes();
        byte[] D_bytes = D.toBytes();
        byte[] E_bytes = E.toBytes();
        byte[] pk_U_A_bytes = pk_U_A.toBytes();

        byte[] combined = new byte[c_0_prime_bytes.length + c_1_prime_bytes.length + primeLength + S_bytes.length
                + W_bytes.length + X_bytes.length + Y_bytes.length + D_bytes.length + E_bytes.length
                + pk_U_A_bytes.length];

        int accumulatedLength = 0;
        System.arraycopy(c_0_prime_bytes, 0, combined, 0, c_0_prime_bytes.length);
        System.arraycopy(c_1_prime_bytes, 0, combined, accumulatedLength += c_0_prime_bytes.length,
                c_1_prime_bytes.length);
        byte[] previous = c_1_prime_bytes;
        byte[] current;
        for (int i = 0; i < c2_prime.length; i++) {
            current = c2_prime_bytes[i];
            System.arraycopy(current, 0, combined, accumulatedLength += previous.length, current.length);
            previous = current;
            current = c3_prime_bytes[i];
            System.arraycopy(current, 0, combined, accumulatedLength += previous.length, current.length);
            previous = current;
        }
        System.arraycopy(S_bytes, 0, combined, accumulatedLength += previous.length, S_bytes.length);
        System.arraycopy(W_bytes, 0, combined, accumulatedLength += S_bytes.length, W_bytes.length);
        System.arraycopy(X_bytes, 0, combined, accumulatedLength += W_bytes.length, X_bytes.length);
        System.arraycopy(Y_bytes, 0, combined, accumulatedLength += X_bytes.length, Y_bytes.length);
        System.arraycopy(D_bytes, 0, combined, accumulatedLength += Y_bytes.length, D_bytes.length);
        System.arraycopy(E_bytes, 0, combined, accumulatedLength += D_bytes.length, E_bytes.length);
        System.arraycopy(pk_U_A_bytes, 0, combined, accumulatedLength += E_bytes.length, pk_U_A_bytes.length);
        // System.out.println("Length: " + combined.length);
        Digest digest = new SHA256Digest();
        digest.reset();
        digest.update(combined, 0, combined.length);
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);
        return pairing.getZr().newElementFromHash(hash, 0, hash.length).getImmutable();
    }

    public static Element decodeStringToElement(String eString, Element e) {
        Element ee = e.duplicate();
        byte[] eBytes = Base64.getDecoder().decode(eString);
        ee.setFromBytes(eBytes);
        return ee.getImmutable();
    }

    public static Element[] decodeJSONStringToElements(String eJSONString, Element e) {
        JSONArray jsonArray = new JSONArray(eJSONString);
        // System.out.println(jsonArray);
        int noOfElements = jsonArray.length();
        Element[] es = new Element[noOfElements];
        for (int i = 0; i < noOfElements; i++) {
            es[i] = decodeStringToElement(jsonArray.getString(i), e);
        }
        return es;
    }

    public static String encodeElementToString(Element e) {
        byte[] eBytes = e.toBytes();
        String eString = Base64.getEncoder().encodeToString(eBytes);
        return eString;
    }

    public static String encodeElementsToJSONString(Element es[]) {

        JSONArray jsonArray = new JSONArray();
        for (Element e : es) {
            byte[] eBytes = e.toBytes();
            String eString = Base64.getEncoder().encodeToString(eBytes);
            jsonArray.put(eString);
        }
        String eJSONString = jsonArray.toString();
        return eJSONString;
    }

    public static String encodeCredentialPIsJSONtoSingleJSON(String credentialPIsJSON[]) {

        JSONArray jsonArray = new JSONArray();
        for (String s : credentialPIsJSON) {
            jsonArray.put(s);
        }
        return jsonArray.toString();
    }

    public static String[] decodeSingleJSONtoCredentialPIsJSON(String credentialPIsJSON) {

        JSONArray jsonArray = new JSONArray(credentialPIsJSON);
        int noOfElements = jsonArray.length();
        String[] s = new String[noOfElements];
        for (int i = 0; i < noOfElements; i++) {
            s[i] = new String(jsonArray.getString(i));
        }
        return s;
    }

    public static String encodeJSONsToJSON(String[] cps) {
        JSONArray jsonArray = new JSONArray();
        for (String s : cps) {
            jsonArray.put(s);
        }
        return jsonArray.toString();
    }

    public static String[] decodeJSONToJSONs(String cps) {
        JSONArray jsonArray = new JSONArray(cps);
        int noOfElements = jsonArray.length();
        String[] cpsStrings = new String[noOfElements];
        for (int i = 0; i < noOfElements; i++) {
            cpsStrings[i] = new String(jsonArray.getString(i));
        }
        return cpsStrings;
    }

}
