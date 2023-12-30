package DisIMS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class CredentialPrime {

    public Element getC0_prime() {
        return c0_prime;
    }

    public Element getC1_prime() {
        return c1_prime;
    }

    public Element[] getC2_prime() {
        return c2_prime;
    }

    public Element[] getC3_prime() {
        return c3_prime;
    }

    public Element getS() {
        return S;
    }

    public Element getW() {
        return W;
    }

    public Element getX() {
        return X;
    }

    public Element getY() {
        return Y;
    }

    public Element getD() {
        return D;
    }

    public Element getE() {
        return E;
    }

    public Element gets() {
        return s;
    }

    public Element gete() {
        return e;
    }

    Pairing pairing;
    // Element attributesVer[];
    Element c0_prime;
    Element c1_prime;
    Element c2_prime[];
    Element c3_prime[];

    Element S, W, X, Y, D, E, s, e;

    public CredentialPrime(Pairing pairing) {
        this.pairing = pairing;
    }

    public void serialize(DataOutputStream dOut) throws Exception {

        // To serialize the elements

        // System.out.println("c0_prime");
        DisIMSUtiltities.writeElementToFile(pairing, c0_prime, dOut);
        // System.out.println("c1_prime");
        DisIMSUtiltities.writeElementToFile(pairing, c1_prime, dOut);
        dOut.writeInt(c2_prime.length);
        for (Element e : c2_prime) {
            // System.out.println("c2_prime");
            DisIMSUtiltities.writeElementToFile(pairing, e, dOut);
        }
        dOut.writeInt(c3_prime.length);
        for (Element e : c3_prime) {
            // System.out.println("c3_prime");
            DisIMSUtiltities.writeElementToFile(pairing, e, dOut);
        }

        DisIMSUtiltities.writeElementToFile(pairing, S, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, W, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, X, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, Y, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, D, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, E, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, s, dOut);
        DisIMSUtiltities.writeElementToFile(pairing, e, dOut);
    }

    public void deserialize(DataInputStream dIn) throws Exception {

        // System.out.println("c0_prime");
        c0_prime = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        // System.out.println("c1_prime");
        c1_prime = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        int c2Length = dIn.readInt();
        c2_prime = new Element[c2Length];
        for (int i = 0; i < c2Length; i++) {
            // System.out.println("c2");
            c2_prime[i] = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        }
        int c3Length = dIn.readInt();
        c3_prime = new Element[c3Length];
        for (int i = 0; i < c3Length; i++) {
            // System.out.println("c3");
            c3_prime[i] = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        }

        S = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        W = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        X = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        Y = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        D = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        E = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        s = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        e = DisIMSUtiltities.readElementFromFile(pairing, dIn);

    }

    // Return the credential user public key pk_U_A
    public Element show(Credential credential, Element attributesVer[], CAKey caKey, UKey uKey, Element g, Element w,
            Element v, Element h, Element u) {
        Element attributes[] = credential.getAttributes();
        Element c0 = credential.getC0();
        Element c1 = credential.getC1();
        Element c2[] = credential.getC2();
        Element c3[] = credential.getC3();
        Element mpk_CA = caKey.getZ();
        int noOfAttributes = attributes.length;
        int noOfAttributesVer = attributesVer.length;

        Element t = pairing.getZr().newRandomElement().getImmutable();
        S = mpk_CA.powZn(t);
        W = g.powZn(t);
        c0_prime = c0.powZn(t);
        c1_prime = c1.powZn(t);

        // Determine which attributes have to be proved
        boolean attributesIndices[] = new boolean[attributes.length];
        for (int i = 0; i < noOfAttributesVer; i++) {
            for (int j = 0; j < noOfAttributes; j++) {
                if (attributesVer[i].equals(attributes[j])) {
                    attributesIndices[j] = true;
                    break;
                }
            }
        }

        c2_prime = new Element[noOfAttributesVer];
        c3_prime = new Element[noOfAttributesVer];
        int attributeIndiceCounter = 0;
        for (int i = 0; i < attributesIndices.length; i++) {
            if (attributesIndices[i]) {
                c2_prime[attributeIndiceCounter] = c2[i].powZn(t);
                c3_prime[attributeIndiceCounter] = c3[i].powZn(t);
                attributeIndiceCounter++;
            }
        }
        Element beta = uKey.getBeta();
        Element sk_U_A = beta.mul(t);
        Element pk_U_A = g.powZn(sk_U_A);

        Element x = pairing.getZr().newRandomElement().getImmutable();
        Element d = pairing.getZr().newRandomElement().getImmutable();
        X = W.powZn(x);
        Y = S.powZn(x);
        D = g.powZn(d);
        E = mpk_CA.powZn(d);

        // Create the hash f
        Element f = DisIMSUtiltities.generateZrFromShowAttributes(pairing, c0_prime, c1_prime, c2_prime, c3_prime, S, W,
                X, Y, D, E, pk_U_A);
        s = x.add(f.negate().mulZn(beta));
        e = d.add(f.negate().mulZn(sk_U_A));

        return pk_U_A;
        // \pi_c = (s, e, D, S, W, X, Y, E)
    }

    public boolean check(Element Z, Element pk_U_A, Element mpk_CA, Element[] attributesVer, Element g, Element w,
            Element v, Element h, Element u) {

        // Check
        // The number of attributes has to be equal to the number of c2_prime
        if (attributesVer.length != c2_prime.length) {
            return false;
        }

        // Reconstruct f
        Element f = DisIMSUtiltities.generateZrFromShowAttributes(pairing, c0_prime, c1_prime, c2_prime, c3_prime, S, W,
                X, Y, D, E, pk_U_A);
        Element LHS3 = S.powZn(s)
                .mul((pairing.pairing(c0_prime, g).mul(pairing.pairing(w, c1_prime).invert())).powZn(f));
        // System.out.println("LHS3 = " + LHS3);
        Element RHS3 = Y;
        // System.out.println("RHS3 = " + RHS3);
        if (!LHS3.equals(RHS3))
            return false;

        // Check Y
        Element LHS3_5 = mpk_CA.powZn(e)
                .mul((pairing.pairing(c0_prime, g).mul(pairing.pairing(w, c1_prime).invert())).powZn(f));
        // System.out.println("LHS3_5 = " + LHS3_5);
        Element RHS3_5 = E;
        // System.out.println("RHS3_5 = " + RHS3_5);
        if (!LHS3_5.equals(RHS3_5))
            return false;

        Element LHS4 = W.powZn(s).mul((pk_U_A).powZn(f));
        // System.out.println("LHS4 = " + LHS4);
        Element RHS4 = X;
        // System.out.println("RHS4 = " + RHS4);
        if (!LHS4.equals(RHS4))
            return false;

        Element LHS5 = g.powZn(e).mul((pk_U_A).powZn(f));
        // System.out.println("LHS5 = " + LHS5);
        Element RHS5 = D;
        // System.out.println("RHS5 = " + RHS5);
        if (!LHS5.equals(RHS5))
            return false;

        Element c3_prime_prod = c3_prime[0];
        for (int i = 1; i < c3_prime.length; i++) {
            c3_prime_prod = c3_prime_prod.mul(c3_prime[i]);
        }
        Element LHS6 = pairing.pairing(c3_prime_prod, g);
        // System.out.println("LHS6 = " + LHS6);

        Element c2_prime_A_prod = c2_prime[0].powZn(attributesVer[0]);
        for (int i = 1; i < c2_prime.length; i++) {
            c2_prime_A_prod = c2_prime_A_prod.mul(c2_prime[i].powZn(attributesVer[i]));
        }
        Element rhs6_1 = pairing.pairing(c2_prime_A_prod, u);

        Element c2_prime_prod = c2_prime[0];
        for (int i = 1; i < c2_prime.length; i++) {
            c2_prime_prod = c2_prime_prod.mul(c2_prime[i]);
        }
        Element rhs6_2 = pairing.pairing(c2_prime_prod, h);

        Element rhs6_3 = pairing.pairing(c1_prime, v)
                .powZn(pairing.getZr().newElement(new BigInteger(attributesVer.length + "")));
        Element RHS6 = rhs6_1.mul(rhs6_2).mul(rhs6_3);
        // System.out.println("RHS6 = " + RHS6);
        if (!LHS6.equals(RHS6))
            return false;

        return true;
    }

    public static boolean mintVerify(CredentialPrime cp[], Element pk_U_A[], Element mpk_CA, Element attributesVerS[][],
            Pairing pairing, Element g, Element w, Element v, Element h, Element u) {

        int noOfCP = cp.length;

        // Regenerate the f(s) from c'(s)
        Element f[] = new Element[noOfCP];
        for (int i = 0; i < noOfCP; i++) {
            f[i] = DisIMSUtiltities.generateZrFromShowAttributes(pairing, cp[i].getC0_prime(), cp[i].getC1_prime(),
                    cp[i].getC2_prime(), cp[i].getC3_prime(), cp[i].getS(), cp[i].getW(), cp[i].getX(), cp[i].getY(),
                    cp[i].getD(), cp[i].getE(), pk_U_A[i]);
        }

        Element SpowS = cp[0].getS().powZn(cp[0].gets());
        for (int i = 1; i < noOfCP; i++) {
            SpowS = SpowS.mul(cp[i].getS().powZn(cp[i].gets()));
        }

        Element c0powf = cp[0].getC0_prime().powZn(f[0]);
        for (int i = 1; i < noOfCP; i++) {
            c0powf = c0powf.mul(cp[i].getC0_prime().powZn(f[i]));
        }

        Element c1powf = cp[0].getC1_prime().powZn(f[0]);
        for (int i = 1; i < noOfCP; i++) {
            c1powf = c1powf.mul(cp[i].getC1_prime().powZn(f[i]));
        }

        Element LHS11_pairing = pairing.pairing(c0powf, g).mul(pairing.pairing(w, c1powf).invert());
        Element LHS11 = SpowS.mul(LHS11_pairing);
        // System.out.println("LHS11 = " + LHS11);
        Element RHS11 = cp[0].getY();
        for (int i = 1; i < noOfCP; i++) {
            RHS11 = RHS11.mul(cp[i].getY());
        }
        // System.out.println("RHS11 = " + RHS11);
        if (!LHS11.equals(RHS11))
            return false;

        // mpkCA
        Element mpkCApowe = mpk_CA.powZn(cp[0].gete());
        for (int i = 1; i < noOfCP; i++) {
            mpkCApowe = mpkCApowe.mul(mpk_CA.powZn(cp[i].gete()));
        }

        Element LHS11_5 = mpkCApowe.mul(LHS11_pairing);
        // System.out.println("LHS11_5 = " + LHS11_5);
        Element RHS11_5 = cp[0].getE();
        for (int i = 1; i < noOfCP; i++) {
            RHS11_5 = RHS11_5.mul(cp[i].getE());
        }
        // System.out.println("RHS11_5= " + RHS11_5);
        if (!LHS11_5.equals(RHS11_5))
            return false;

        Element Wpows = cp[0].getW().powZn(cp[0].gets());
        for (int i = 1; i < noOfCP; i++) {
            Wpows = Wpows.mul(cp[i].getW().powZn(cp[i].gets()));
        }

        Element LHS12_pk_U_A = pk_U_A[0].powZn(f[0]);
        for (int i = 1; i < noOfCP; i++) {
            LHS12_pk_U_A = LHS12_pk_U_A.mul(pk_U_A[i].powZn(f[i]));
        }
        Element LHS12 = Wpows.mul(LHS12_pk_U_A);
        // System.out.println("LHS12 = " + LHS12);
        Element RHS12 = cp[0].getX();
        for (int i = 1; i < noOfCP; i++) {
            RHS12 = RHS12.mul(cp[i].getX());
        }
        // System.out.println("RHS12 = " + RHS12);
        if (!LHS12.equals(RHS12))
            return false;

        Element LHS13_c3_prod = pairing.getG1().newOneElement();
        // Element abc = pairing.getG1().newRandomElement();
        // System.out.println(abc);
        // Element result = abc.mul(LHS13_c3_prod);
        // System.out.println(result);
        for (int i = 0; i < cp.length; i++) {
            for (int j = 0; j < cp[i].c3_prime.length; j++) {
                LHS13_c3_prod = LHS13_c3_prod.mul(cp[i].c3_prime[j]);
            }
        }
        Element LHS13 = pairing.pairing(LHS13_c3_prod, g);
        // System.out.println("LHS13 = " + LHS13);

        Element RHS13_c2PowA = pairing.getG1().newOneElement();
        for (int i = 0; i < cp.length; i++) {
            for (int j = 0; j < cp[i].c2_prime.length; j++) {
                RHS13_c2PowA = RHS13_c2PowA.mul(cp[i].c2_prime[j].powZn(attributesVerS[i][j]));
            }
        }
        Element RHS13_1 = pairing.pairing(RHS13_c2PowA, u);

        Element RHS13_c2_prod = pairing.getG1().newOneElement();
        for (int i = 0; i < cp.length; i++) {
            for (int j = 0; j < cp[i].c2_prime.length; j++) {
                RHS13_c2_prod = RHS13_c2_prod.mul(cp[i].c2_prime[j]);
            }
        }
        Element RHS13_2 = pairing.pairing(RHS13_c2_prod, h);

        Element RHS13_3 = pairing.getGT().newOneElement();
        for (int i = 0; i < cp.length; i++) {
            RHS13_3 = RHS13_3.mul(pairing.pairing(cp[i].getC1_prime(), v)
                    .powZn(pairing.getZr().newElement(new BigInteger("" + attributesVerS[i].length))));
            // System.out.println(attributesVerS[i].length);
        }

        Element RHS13 = RHS13_1.mul(RHS13_2).mul(RHS13_3);
        // System.out.println("RHS13 = " + RHS13);
        if (!LHS13.equals(RHS13))
            return false;

        return true;
    }
}
