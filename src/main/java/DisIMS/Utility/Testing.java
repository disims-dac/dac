package DisIMS.Utility;

import java.math.BigInteger;

public class Testing {

    public static void main(String[] args) {
        BigInteger r = new BigInteger(
                "730750818665451621361119245571504901405976559617");
        System.out.println(r.bitCount());
        BigInteger h = new BigInteger(
                "12016012264891146079388821366740534204802954401251311822919615131047207289359704531102844802183906537786776");
        BigInteger q = new BigInteger(
                "8780710799663312522437781984754049815806883199414208211028653399266475630880222957078625179422662221423155858769582317459277713367317481324925129998224791");
        System.out.println(q.bitCount());

        System.out.println(r.multiply(h).subtract(new BigInteger("1")));

        int counter = 0;
        while (!r.equals(BigInteger.ZERO)) {
            r = r.divide(BigInteger.TWO);
            counter++;
        }
        System.out.println(counter);
    }
}
