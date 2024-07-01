package DisIMS;

import java.math.BigInteger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class DisMISPrototype {

        public static void main(String[] args) throws Exception {

                // An implementation of DisMis
                // Setup
                PairingParameters parameters;
                parameters = PairingFactory.getPairingParameters("params/curves/a.properties");
                Pairing pairing = PairingFactory.getPairing(parameters);
                Element g, w, v, h, u;
                g = pairing.getG1().newRandomElemhent().getImmutable();
                w = pairing.getG1().newRandomElement().getImmutable();
                v = pairing.getG1().newRandomElement().getImmutable();
                h = pairing.getG1().newRandomElement().getImmutable();
                u = pairing.getG1().newRandomElement().getImmutable();

                // CKeyGen
                Element alpha = pairing.getZr().newRandomElement().getImmutable();
                Element Z = pairing.pairing(g, g).powZn(alpha).getImmutable();

                // UKeyGen
                Element beta = pairing.getZr().newRandomElement().getImmutable();
                Element pk_U = g.powZn(beta).getImmutable();

                // Issue
                String attribute1 = "First Message";
                byte[] bytes = attribute1.getBytes();
                Digest digest = new SHA256Digest();
                digest.reset();
                digest.update(bytes, 0, bytes.length);
                int digestSize = digest.getDigestSize();
                byte[] hash = new byte[digestSize];
                digest.doFinal(hash, 0);
                Element A1 = pairing.getZr().newElementFromHash(hash, 0, hash.length).getImmutable();

                String attribute2 = "Second Message";
                byte[] bytes2 = attribute2.getBytes();
                Digest digest2 = new SHA256Digest();
                digest2.reset();
                digest2.update(bytes2, 0, bytes2.length);
                int digestSize2 = digest2.getDigestSize();
                byte[] hash2 = new byte[digestSize2];
                digest2.doFinal(hash2, 0);
                Element A2 = pairing.getZr().newElementFromHash(hash2, 0, hash2.length).getImmutable();

                Element r = pairing.getZr().newRandomElement().getImmutable();
                Element r1 = pairing.getZr().newRandomElement().getImmutable();
                Element r2 = pairing.getZr().newRandomElement().getImmutable();

                Element c0 = pk_U.powZn(alpha).mul(w.powZn(r));
                Element c1 = g.powZn(r);

                Element c2_1 = g.powZn(r1);
                Element c3_1 = (u.powZn(A1).mul(h)).powZn(r1).mul(v.powZn(r));
                Element c2_2 = g.powZn(r2);
                Element c3_2 = (u.powZn(A2).mul(h)).powZn(r2).mul(v.powZn(r));

                // UserVerify
                System.out.println("UserVerify");
                System.out.println("===The first equality===");
                Element lhs1 = pairing.pairing(c0, g);
                System.out.println("LHS1 = " + lhs1);
                Element rhs1 = Z.powZn(beta).mul(pairing.pairing(w, c1));
                System.out.println("RHS1 = " + rhs1);

                System.out.println("===The second equality===");
                Element lhs2 = pairing.pairing(c3_1.mul(c3_2), g);
                System.out.println("LHS2 = " + lhs2);
                Element rhs2_1 = pairing.pairing(c2_1.powZn(A1).mul(c2_2.powZn(A2)), u);
                Element rhs2_2 = pairing.pairing(c2_1.mul(c2_2), h);
                // Element rhs2_3 = pairing.pairing(c1, v.invert()).mul(pairing.pairing(c1,
                // v.invert()));
                // System.out.println(rhs2_3);
                Element rhs2_3 = pairing.pairing(c1, v).powZn(pairing.getZr().newElement(new BigInteger("2")));
                // System.out.println(rhs2_3);
                Element rhs2 = rhs2_1.mul(rhs2_2).mul(rhs2_3);
                System.out.println("RHS2 = " + rhs2);

                // Show
                Element t = pairing.getZr().newRandomElement().getImmutable();
                Element S = Z.powZn(t);
                Element W = g.powZn(t);
                Element c0_prime = c0.powZn(t);
                Element c1_prime = c1.powZn(t);
                // Assume only the first element is to be proved
                Element c2_1_prime = c2_1.powZn(t);
                Element c3_1_prime = c3_1.powZn(t);
                Element sk_U_A = beta.mul(t);
                Element pk_U_A = g.powZn(sk_U_A);

                Element x = pairing.getZr().newRandomElement().getImmutable();
                Element d = pairing.getZr().newRandomElement().getImmutable();
                Element X = W.powZn(x);
                Element Y = S.powZn(x);
                Element D = g.powZn(d);

                // Create the hash f
                byte[] c_0_prime_bytes = c0_prime.toBytes();
                byte[] c_1_prime_bytes = c1_prime.toBytes();
                byte[] c2_1_prime_bytes = c2_1_prime.toBytes();
                byte[] c3_1_prime_bytes = c3_1_prime.toBytes();
                byte[] S_bytes = S.toBytes();
                byte[] W_bytes = W.toBytes();
                byte[] X_bytes = X.toBytes();
                byte[] Y_bytes = Y.toBytes();
                byte[] D_bytes = D.toBytes();
                byte[] pk_U_A_bytes = pk_U_A.toBytes();

                byte[] combined = new byte[c_0_prime_bytes.length + c_1_prime_bytes.length + c2_1_prime_bytes.length
                                + c3_1_prime_bytes.length + S_bytes.length + W_bytes.length + X_bytes.length
                                + Y_bytes.length
                                + D_bytes.length + pk_U_A_bytes.length];

                System.arraycopy(c_0_prime_bytes, 0, combined, 0, c_0_prime_bytes.length);
                System.arraycopy(c_1_prime_bytes, 0, combined, c_0_prime_bytes.length, c_1_prime_bytes.length);
                System.arraycopy(c2_1_prime_bytes, 0, combined, c_1_prime_bytes.length, c2_1_prime_bytes.length);
                System.arraycopy(c3_1_prime_bytes, 0, combined, c2_1_prime_bytes.length, c3_1_prime_bytes.length);
                System.arraycopy(S_bytes, 0, combined, c3_1_prime_bytes.length, S_bytes.length);
                System.arraycopy(W_bytes, 0, combined, S_bytes.length, W_bytes.length);
                System.arraycopy(X_bytes, 0, combined, W_bytes.length, X_bytes.length);
                System.arraycopy(Y_bytes, 0, combined, X_bytes.length, Y_bytes.length);
                System.arraycopy(D_bytes, 0, combined, Y_bytes.length, D_bytes.length);
                System.arraycopy(pk_U_A_bytes, 0, combined, D_bytes.length, pk_U_A_bytes.length);
                System.out.println("Length: " + combined.length);
                Digest digest3 = new SHA256Digest();
                digest3.reset();
                digest3.update(combined, 0, combined.length);
                int digestSize3 = digest3.getDigestSize();
                byte[] hash3 = new byte[digestSize3];
                digest3.doFinal(hash3, 0);
                Element f = pairing.getZr().newElementFromHash(hash3, 0, hash3.length).getImmutable();

                Element s = x.add(f.negate().mulZn(beta));
                Element e = d.add(f.negate().mulZn(sk_U_A));

                // \pi_c = (s, e, D, S, W, X)
                // Check
                Element LHS3 = S.powZn(s)
                                .mul((pairing.pairing(c0_prime, g).mul(pairing.pairing(w, c1_prime).invert()))
                                                .powZn(f));
                System.out.println("LHS3 = " + LHS3);
                Element RHS3 = Y;
                System.out.println("RHS3 = " + RHS3);

                Element LHS4 = W.powZn(s).mul((pk_U_A).powZn(f));
                System.out.println("LHS4 = " + LHS4);
                Element RHS4 = X;
                System.out.println("RHS4 = " + RHS4);

                Element LHS5 = g.powZn(e).mul((pk_U_A).powZn(f));
                System.out.println("LHS5 = " + LHS5);
                Element RHS5 = D;
                System.out.println("RHS5 = " + RHS5);

                Element LHS6 = pairing.pairing(c3_1_prime, g);
                System.out.println("LHS6 = " + LHS6);
                Element rhs6_1 = pairing.pairing(c2_1_prime.powZn(A1), u);
                Element rhs6_2 = pairing.pairing(c2_1_prime, h);
                // Element rhs2_3 = pairing.pairing(c1, v.invert()).mul(pairing.pairing(c1,
                // v.invert()));
                // System.out.println(rhs2_3);
                Element rhs6_3 = pairing.pairing(c1_prime, v).powZn(pairing.getZr().newElement(new BigInteger("1")));
                Element RHS6 = rhs6_1.mul(rhs6_2).mul(rhs6_3);
                ;
                System.out.println("RHS6 = " + RHS6);

                // MintVerify
                // Create one more proof for message2
                // So that one proof for message 1 and another one for message 2
                // Then, test the batch verification
                Element t2 = pairing.getZr().newRandomElement().getImmutable();
                Element S2 = Z.powZn(t2);
                Element W2 = g.powZn(t2);
                Element c0_prime2 = c0.powZn(t2);
                Element c1_prime2 = c1.powZn(t2);
                // Assume only the first element is to be proved
                Element c2_2_prime = c2_2.powZn(t2);
                Element c3_2_prime = c3_2.powZn(t2);
                Element sk_U_A2 = beta.mul(t2);
                Element pk_U_A2 = g.powZn(sk_U_A2);

                Element x2 = pairing.getZr().newRandomElement().getImmutable();
                Element d2 = pairing.getZr().newRandomElement().getImmutable();
                Element X2 = W2.powZn(x2);
                Element Y2 = S2.powZn(x2);
                Element D2 = g.powZn(d2);

                // Create the hash f
                byte[] c_0_prime_bytes2 = c0_prime.toBytes();
                byte[] c_1_prime_bytes2 = c1_prime.toBytes();
                byte[] c2_2_prime_bytes = c2_2_prime.toBytes();
                byte[] c3_2_prime_bytes = c3_2_prime.toBytes();
                byte[] S_bytes2 = S2.toBytes();
                byte[] W_bytes2 = W2.toBytes();
                byte[] X_bytes2 = X2.toBytes();
                byte[] Y_bytes2 = Y2.toBytes();
                byte[] D_bytes2 = D2.toBytes();
                byte[] pk_U_A_bytes2 = pk_U_A2.toBytes();

                byte[] combined2 = new byte[c_0_prime_bytes2.length + c_1_prime_bytes2.length + c2_2_prime_bytes.length
                                + c3_2_prime_bytes.length + S_bytes2.length + W_bytes2.length + X_bytes2.length
                                + Y_bytes2.length
                                + D_bytes2.length + pk_U_A_bytes2.length];

                System.arraycopy(c_0_prime_bytes2, 0, combined, 0, c_0_prime_bytes2.length);
                System.arraycopy(c_1_prime_bytes2, 0, combined, c_0_prime_bytes2.length, c_1_prime_bytes2.length);
                System.arraycopy(c2_2_prime_bytes, 0, combined, c_1_prime_bytes2.length, c2_2_prime_bytes.length);
                System.arraycopy(c3_2_prime_bytes, 0, combined, c2_2_prime_bytes.length, c3_2_prime_bytes.length);
                System.arraycopy(S_bytes2, 0, combined, c3_2_prime_bytes.length, S_bytes2.length);
                System.arraycopy(W_bytes2, 0, combined, S_bytes2.length, W_bytes2.length);
                System.arraycopy(X_bytes2, 0, combined, W_bytes2.length, X_bytes2.length);
                System.arraycopy(Y_bytes2, 0, combined, X_bytes2.length, Y_bytes2.length);
                System.arraycopy(D_bytes2, 0, combined, Y_bytes2.length, D_bytes2.length);
                System.arraycopy(pk_U_A_bytes2, 0, combined, D_bytes2.length, pk_U_A_bytes2.length);
                System.out.println("Length: " + combined2.length);
                Digest digest4 = new SHA256Digest();
                digest4.reset();
                digest4.update(combined2, 0, combined2.length);
                int digestSize4 = digest4.getDigestSize();
                byte[] hash4 = new byte[digestSize4];
                digest4.doFinal(hash4, 0);
                Element f2 = pairing.getZr().newElementFromHash(hash4, 0, hash4.length).getImmutable();

                Element s2 = x2.add(f2.negate().mulZn(beta));
                Element e2 = d2.add(f2.negate().mulZn(sk_U_A2));

                // Test the proof of message 2 first
                Element LHS7 = S2.powZn(s2)
                                .mul((pairing.pairing(c0_prime2, g).mul(pairing.pairing(w, c1_prime2).invert()))
                                                .powZn(f2));
                System.out.println("LHS7 = " + LHS7);
                Element RHS7 = Y2;
                System.out.println("RHS7 = " + RHS7);

                Element LHS8 = W2.powZn(s2).mul((pk_U_A2).powZn(f2));
                System.out.println("LHS8 = " + LHS8);
                Element RHS8 = X2;
                System.out.println("RHS8 = " + RHS8);

                Element LHS9 = g.powZn(e2).mul((pk_U_A2).powZn(f2));
                System.out.println("LHS9 = " + LHS9);
                Element RHS9 = D2;
                System.out.println("RHS9 = " + RHS9);

                Element LHS10 = pairing.pairing(c3_2_prime, g);
                System.out.println("LHS10 = " + LHS10);
                Element RHS10_1 = pairing.pairing(c2_2_prime.powZn(A2), u);
                Element RHS10_2 = pairing.pairing(c2_2_prime, h);
                // Element rhs2_3 = pairing.pairing(c1, v.invert()).mul(pairing.pairing(c1,
                // v.invert()));
                // System.out.println(rhs2_3);
                Element RHS10_3 = pairing.pairing(c1_prime2, v).powZn(pairing.getZr().newElement(new BigInteger("1")));
                Element RHS10 = RHS10_1.mul(RHS10_2).mul(RHS10_3);
                ;
                System.out.println("RHS10 = " + RHS10);

                // Final batch verfication test
                Element LHS11_S = S.powZn(s).mul(S2.powZn(s2));
                Element LHS11_c0 = c0_prime.powZn(f).mul((c0_prime2).powZn(f2));
                Element LHS11_c1 = c1_prime.powZn(f).mul((c1_prime2).powZn(f2));
                Element LHS11_pairing = pairing.pairing(LHS11_c0, g).mul(pairing.pairing(w, LHS11_c1).invert());
                Element LHS11 = LHS11_S.mul(LHS11_pairing);
                System.out.println("LHS11 = " + LHS11);
                Element RHS11 = S.powZn(x).mul(S2.powZn(x2));
                System.out.println("RHS11 = " + RHS11);

                Element LHS12_W = W.powZn(s).mul(W2.powZn(s2));
                Element LHS12_pk_U_A = pk_U_A.powZn(f).mul(pk_U_A2.powZn(f2));
                Element LHS12 = LHS12_W.mul(LHS12_pk_U_A);
                System.out.println("LHS12 = " + LHS12);
                Element RHS12 = X.mul(X2);
                System.out.println("RHS12 = " + RHS12);

                Element LHS13 = pairing.pairing(c3_1_prime.mul(c3_2_prime), g);
                System.out.println("LHS13 = " + LHS13);
                Element RHS13_1 = pairing.pairing(c2_1_prime.powZn(A1).mul(c2_2_prime.powZn(A2)), u);
                Element RHS13_2 = pairing.pairing(c2_1_prime.mul(c2_2_prime), h);
                Element RHS13_3 = pairing.pairing(c1_prime, v).powZn(pairing.getZr().newElement(new BigInteger("1")))
                                .mul(pairing.pairing(c1_prime2, v)
                                                .powZn(pairing.getZr().newElement(new BigInteger("1"))));
                Element RHS13 = RHS13_1.mul(RHS13_2).mul(RHS13_3);
                ;
                System.out.println("RHS13 = " + RHS13);

        }
}
