package DisIMS;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class UKey {

    private Element g;
    private Pairing pairing;

    private Element beta; // private key
    private Element pk_U; // public key

    public UKey(Pairing pairing, Element g) {
        this.pairing = pairing;
        this.g = g;
    }

    public void genKeys() {
        beta = pairing.getZr().newRandomElement().getImmutable();
        pk_U = g.powZn(beta).getImmutable();
    }

    public Element getBeta() {
        return beta;
    }

    public Element getPk_U() {
        return pk_U;
    }

}