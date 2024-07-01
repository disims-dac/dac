/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

import DisIMS.CredentialPI;
import DisIMS.Utility.DisIMSUtiltities;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

@Contract(name = "basic", info = @Info(title = "DisIMS", description = "To test the DisIMS Algorithms", version = "0.0.1-SNAPSHOT", license = @License(name = "Apache 2.0 License", url = "http://www.apache.org/licenses/LICENSE-2.0.html"), contact = @Contact(email = "a.transfer@example.com", name = "Adrian Transfer", url = "https://hyperledger.example.com")))
@Default
public final class AssetTransfer implements ContractInterface {

    /**
     * Creates some initial assets on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public boolean Check(final Context ctx, String pkUAString, String caKeyZString, String attributesVerJSON, String gString, String wString, String vString, String hString, String uString, String cpString) {
 
        PairingParameters parameters;
        Pairing pairing;
        parameters = PairingFactory.getPairingParameters("a.properties");
        pairing = PairingFactory.getPairing(parameters);
        CredentialPI cre_prime = new CredentialPI(pairing);

        // Decode to Objects
        // System.out.println("Decoding JSON Strings to Objects");
        Element g1Element = pairing.getG1().newElement();
        Element gTElement = pairing.getGT().newElement();
        Element zr = pairing.getZr().newElement();
        cre_prime.decodeFromJsonString(cpString);
        Element pk_U_A = DisIMSUtiltities.decodeStringToElement(pkUAString, g1Element);
        Element Z = DisIMSUtiltities.decodeStringToElement(caKeyZString, gTElement);
        Element[] attributesVer = DisIMSUtiltities.decodeJSONStringToElements(attributesVerJSON, zr);
        Element g = DisIMSUtiltities.decodeStringToElement(gString, g1Element);
        Element w = DisIMSUtiltities.decodeStringToElement(wString, g1Element);
        Element v = DisIMSUtiltities.decodeStringToElement(vString, g1Element);
        Element h = DisIMSUtiltities.decodeStringToElement(hString, g1Element);
        Element u = DisIMSUtiltities.decodeStringToElement(uString, g1Element);
        
        // System.out.println("=== Check === ");
        boolean checkResult = cre_prime.check(pk_U_A, Z, attributesVer, g, w, v, h, u);
        // System.out.println("=== Check Result in Fabric: " + checkResult + " === ");
        return checkResult;
        
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public boolean MintVerify(final Context ctx, String cpsStringArrayJSON, String pkUAsStringArrayJSON, String caKeyZString, String attributeVerSStringArrayJSON, String gStringForMint, String wStringForMint, String vStringForMint, String hStringForMint, String uStringForMint) {
 
        PairingParameters parameters;
        Pairing pairing;
        parameters = PairingFactory.getPairingParameters("a.properties");
        pairing = PairingFactory.getPairing(parameters);
        Element g1Element = pairing.getG1().newElement();
        Element gTElement = pairing.getGT().newElement();
        Element zr = pairing.getZr().newElement();

        // System.out.println("Decoding JSON Strings to Objects");

        // Decode the Credentials PIs
        String cpsStringNew[] = DisIMSUtiltities.decodeJSONToJSONs(cpsStringArrayJSON);
        CredentialPI cpsNew[] = new CredentialPI[cpsStringNew.length];
        for (int i = 0; i < cpsStringNew.length; i++) {
            cpsNew[i] = new CredentialPI(pairing);
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
        return mintVerifyResult;
        
    }
}
