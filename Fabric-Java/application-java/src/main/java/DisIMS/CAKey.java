package DisIMS;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class CAKey {

    private Element g;
    private Pairing pairing;

    private Element alpha; // private key
    private Element Z; // public key

    public CAKey(Pairing pairing, Element g) {
        this.pairing = pairing;
        this.g = g;
    }

    public Element getAlpha() {
        return alpha;
    }

    public Element getZ() {
        return Z;
    }

    public void genKeys() {
        alpha = pairing.getZr().newRandomElement().getImmutable();
        Z = pairing.pairing(g, g).powZn(alpha).getImmutable();
    }

}