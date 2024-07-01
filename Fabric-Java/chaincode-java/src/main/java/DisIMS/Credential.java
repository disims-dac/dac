package DisIMS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class Credential {

    Pairing pairing;
    Element attributes[];
    Element c0;
    Element c1;
    Element c2[];
    Element c3[];

    public Credential(Pairing pairing) {
        this.pairing = pairing;
    }

    public void serialize(DataOutputStream dOut) throws Exception {

        // To serialize the attributes, c0, c1, c2 and c3
        // attributes, c2, c3: ZrElement
        // c0, c1: CurveElement
        dOut.writeInt(attributes.length);
        for (Element e : attributes) {
            // System.out.println("attributes");
            DisIMSUtiltities.writeElementToFile(pairing, e, dOut);

        }
        // System.out.println("c0");
        DisIMSUtiltities.writeElementToFile(pairing, c0, dOut);
        // System.out.println("c1");
        DisIMSUtiltities.writeElementToFile(pairing, c1, dOut);
        dOut.writeInt(c2.length);
        for (Element e : c2) {
            // System.out.println("c2");
            DisIMSUtiltities.writeElementToFile(pairing, e, dOut);
        }
        dOut.writeInt(c3.length);
        for (Element e : c3) {
            // System.out.println("c3");
            DisIMSUtiltities.writeElementToFile(pairing, e, dOut);
        }

    }

    public void deserialize(DataInputStream dIn) throws Exception {

        // To deserialize the attributes, c0, c1, c2 and c3
        // attributes, c2, c3: ZrElement
        // c0, c1: CurveElement
        int attributeLength = dIn.readInt();
        attributes = new Element[attributeLength];
        for (int i = 0; i < attributeLength; i++) {
            // System.out.println("attributes");
            attributes[i] = DisIMSUtiltities.readElementFromFile(pairing, dIn);

        }
        // System.out.println("c0");
        c0 = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        // System.out.println("c1");
        c1 = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        int c2Length = dIn.readInt();
        c2 = new Element[c2Length];
        for (int i = 0; i < c2Length; i++) {
            // System.out.println("c2");
            c2[i] = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        }
        int c3Length = dIn.readInt();
        c3 = new Element[c3Length];
        for (int i = 0; i < c3Length; i++) {
            // System.out.println("c3");
            c3[i] = DisIMSUtiltities.readElementFromFile(pairing, dIn);
        }

    }

    public void generate(Element attr[], CAKey caKey, UKey uKey, Element g, Element w, Element v, Element h,
            Element u) {
        attributes = attr;
        int noOfAttributes = attributes.length;
        Element r = pairing.getZr().newRandomElement().getImmutable();
        Element rs[] = new Element[noOfAttributes];

        for (int i = 0; i < noOfAttributes; i++) {
            rs[i] = pairing.getZr().newRandomElement().getImmutable();
        }

        Element pk_U = uKey.getPk_U();
        Element alpha = caKey.getAlpha();

        c0 = pk_U.powZn(alpha).mul(w.powZn(r));
        c1 = g.powZn(r);

        c2 = new Element[noOfAttributes];
        c3 = new Element[noOfAttributes];
        for (int i = 0; i < noOfAttributes; i++) {
            c2[i] = g.powZn(rs[i]);
            c3[i] = (u.powZn(attributes[i]).mul(h)).powZn(rs[i]).mul(v.powZn(r));
        }
    }

    public boolean userVerify(Element Z, Element beta, Element g, Element w, Element v, Element h, Element u) {

        Element lhs1 = pairing.pairing(c0, g);
        // System.out.println("LHS1 = " + lhs1);
        Element rhs1 = Z.powZn(beta).mul(pairing.pairing(w, c1));
        // System.out.println("RHS1 = " + rhs1);
        if (!lhs1.equals(rhs1))
            return false;

        int noOfAttributes = attributes.length;
        Element productOfC3 = c3[0];
        for (int i = 1; i < noOfAttributes; i++) {
            productOfC3 = productOfC3.mul(c3[i]);
        }
        Element lhs2 = pairing.pairing(productOfC3, g);
        // System.out.println("LHS2 = " + lhs2);

        Element productOfC2PowAttr = c2[0].powZn(attributes[0]);
        for (int i = 1; i < noOfAttributes; i++) {
            productOfC2PowAttr = productOfC2PowAttr.mul(c2[i].powZn(attributes[i]));
        }
        Element rhs2_1 = pairing.pairing(productOfC2PowAttr, u);
        // System.out.println("RHS2_1 = " + rhs2_1);
        Element productOfC2 = c2[0];
        for (int i = 1; i < noOfAttributes; i++) {
            productOfC2 = productOfC2.mul(c2[i]);
        }
        Element rhs2_2 = pairing.pairing(productOfC2, h);
        // System.out.println("RHS2_2 = " + rhs2_2);

        Element rhs2_3 = pairing.pairing(c1, v).powZn(pairing.getZr().newElement(new BigInteger(noOfAttributes + "")));
        Element rhs2 = rhs2_1.mul(rhs2_2).mul(rhs2_3);
        // System.out.println("RHS2 = " + rhs2);
        if (!lhs2.equals(rhs2))
            return false;

        return true;
    }

    public Element[] getAttributes() {
        return attributes;
    }

    public Element getC0() {
        return c0;
    }

    public Element getC1() {
        return c1;
    }

    public Element[] getC2() {
        return c2;
    }

    public Element[] getC3() {
        return c3;
    }

    public boolean equals(Credential c) {
        for (int i = 0; i < attributes.length; i++) {
            if (!attributes[i].equals(c.getAttributes()[i])) {
                System.out.println("==attributes");
                return false;
            }
        }
        if (!c0.equals(c.getC0())) {
            System.out.println("==c0");
            return false;
        }
        if (!c1.equals(c.getC1())) {
            System.out.println("==c1");
            return false;
        }
        for (int i = 0; i < c2.length; i++) {
            if (!c2[i].equals(c.getC2()[i])) {

                System.out.println("==c2" + i);
                return false;
            }
        }
        for (int i = 0; i < c2.length; i++) {
            if (!c3[i].equals(c.getC3()[i])) {

                System.out.println("==c3" + i);
                return false;
            }
        }
        return true;
    }

}
