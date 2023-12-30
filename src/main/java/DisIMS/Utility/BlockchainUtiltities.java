package DisIMS.Utility;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Properties;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

public class BlockchainUtiltities {

    public static String privateKey;
    public static String contractAddress;
    public static String provider;
    public static long chainId;
    public static String publicKey;

    public static void main(String[] args) {

        try {
            Properties appProps = new Properties();
            appProps.load(new FileInputStream(".properties2"));

            privateKey = appProps.getProperty("sk");
            contractAddress = appProps.getProperty("contract");
            provider = appProps.getProperty("provider");
            chainId = Long.parseLong(appProps.getProperty("chainId"));
            publicKey = appProps.getProperty("pk");

            // System.out.println(privateKey + " " + contractAddress + " " + provider + " "
            // + chainId);

            // testDeploy();
            // testTransferToContract();
            // testWithdraw();

            // getBalance("0x" + contractAddress);
            // testDeployERC721();
            // testMint();
            // testGetURLToken();
            // getcurrentgasprice();

            // createNoNFTContract();
            System.out.println(testNoNFT(DisIMSUtiltities.getHash("ok")));
            // getNoNFTHash();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getNoNFTHash() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);

            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.divide(new BigInteger("10")));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + gasPrice);
            System.out.println("Gas Price I am willing to pay: " + gasPrice);

            TestNoNFT tnn = TestNoNFT.load(contractAddress, web3j,
                    txMananger, gasProvider);

            System.out.println(tnn.getHashOfC(new BigInteger("0")).send().toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static String testNoNFT(String hash) {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);

            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.divide(new BigInteger("10")));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + gasPrice);
            System.out.println("Gas Price I am willing to pay: " + gasPrice);

            TestNoNFT tnn = TestNoNFT.load(contractAddress, web3j,
                    txMananger, gasProvider);
            TransactionReceipt tr = tnn.createCredential(publicKey, hash).sendAsync().get();
            String transactionHash = tr.getTransactionHash();
            System.out.println(transactionHash);
            EthGetTransactionReceipt ethGetTransactionReceiptResp;
            Optional<TransactionReceipt> transactionReceipt = null;
            do {
                System.out.println("Checking if transaction " + transactionHash + " is mined....");
                ethGetTransactionReceiptResp = web3j
                        .ethGetTransactionReceipt(transactionHash)
                        .send();
                transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();

                Thread.sleep(1000); // Wait for 0.1 sec

            } while (!transactionReceipt.isPresent());
            System.out.println("Actual Gas Used: "
                    + new BigInteger(ethGetTransactionReceiptResp.getResult().getGasUsedRaw().substring(2), 16)
                            .toString(10));
            // return the TokenID
            BigInteger bi = new BigInteger(tnn.getCounter().send().toString());
            System.out.println(bi);

            return tnn.getHashOfC(bi.subtract(BigInteger.ONE)).send().toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }

        return null;
    }

    public static void createNoNFTContract() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.subtract(gasPrice.divide(new BigInteger("2"))));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + ethGasPrice.getGasPrice());
            System.out.println("Current Gas Price: " + newGasPrice);

            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j, Credentials.create(privateKey),
                    chainId);
            TestNoNFT.deploy(web3j, txMananger, gasProvider).send();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static String getHashFromBlockchain(String tokenID) {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);

            DisIMS disIMS = DisIMS.load(contractAddress, web3j,
                    txMananger, new DefaultGasProvider());
            String tokenURL = disIMS.tokenURI(new BigInteger(tokenID)).send();
            return tokenURL;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
        return null;
    }

    public static String putHashToBlockchain(String hash) {
        // Put the hash to the tokenURL field of NFT
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);

            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.divide(new BigInteger("10")));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + gasPrice);
            System.out.println("Gas Price I am willing to pay: " + gasPrice);

            DisIMS disIMS = DisIMS.load(contractAddress, web3j,
                    txMananger, gasProvider);
            TransactionReceipt tr = disIMS.mintNFT(publicKey,
                    hash).send();
            String transactionHash = tr.getTransactionHash();
            EthGetTransactionReceipt ethGetTransactionReceiptResp;
            Optional<TransactionReceipt> transactionReceipt = null;
            do {
                System.out.println("Checking if transaction " + transactionHash + " is mined....");
                ethGetTransactionReceiptResp = web3j
                        .ethGetTransactionReceipt(transactionHash)
                        .send();
                transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();

                Thread.sleep(100); // Wait for 0.01 sec

            } while (!transactionReceipt.isPresent());
            System.out.println("Actual Gas Used: "
                    + new BigInteger(ethGetTransactionReceiptResp.getResult().getGasUsedRaw().substring(2), 16)
                            .toString(10));
            // return the TokenID
            return disIMS.getCounter().send().toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }

        return null;
    }

    public static void testGetURLToken() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);

            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.subtract(gasPrice.divide(new BigInteger("2"))));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + ethGasPrice.getGasPrice());
            System.out.println("Current Gas Price: " + newGasPrice);

            DisIMS disIMS = DisIMS.load(contractAddress, web3j,
                    txMananger, gasProvider);
            String tokenURL = disIMS.tokenURI(new BigInteger("1")).send();
            System.out.println(tokenURL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static void getcurrentgasprice() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            System.out.println("Current Gas Price: " + ethGasPrice.getGasPrice());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static void testMint() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);

            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.subtract(gasPrice.divide(new BigInteger("2"))));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + ethGasPrice.getGasPrice());
            System.out.println("Current Gas Price: " + newGasPrice);

            DisIMS disIMS = DisIMS.load(contractAddress, web3j,
                    txMananger, gasProvider);
            TransactionReceipt tr = disIMS.mintNFT(publicKey,
                    "http://www.google.com").send();
            String transactionHash = tr.getTransactionHash();

            Optional<TransactionReceipt> transactionReceipt = null;
            do {
                System.out.println("checking if transaction " + transactionHash + " is mined....");
                EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j
                        .ethGetTransactionReceipt(transactionHash)
                        .send();
                transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
                Thread.sleep(3000); // Wait for 3 sec
            } while (!transactionReceipt.isPresent());

            System.out.println(disIMS.getCounter().send().toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static void getBalance(String fromAddress) {
        BigInteger balance = null;
        try {
            Web3j web3j = Web3j.build(new HttpService(provider));
            EthGetBalance ethGetBalance = web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST).send();
            balance = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER).toBigInteger();
            System.out.println("Address: " + fromAddress + " Balance: " + balance + " ether");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testDeployERC721() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            BigInteger newGasPrice = gasPrice.add(gasPrice.subtract(gasPrice.divide(new BigInteger("2"))));
            StaticGasProvider gasProvider = new StaticGasProvider(newGasPrice, BigInteger.valueOf(9_000_000));
            System.out.println("Current Gas Price: " + ethGasPrice.getGasPrice());
            System.out.println("Current Gas Price: " + newGasPrice);

            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j, Credentials.create(privateKey),
                    chainId);
            DisIMS.deploy(web3j, txMananger, gasProvider, publicKey).send();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static void testWithdraw() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {

            long chainId = 11155111;
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);
            HelloWorld helloWorld = HelloWorld.load(contractAddress, web3j,
                    txMananger, new DefaultGasProvider());
            if (helloWorld.isValid()) {
                BigInteger value = Convert.toWei("0.02", Convert.Unit.ETHER).toBigInteger();
                TransactionReceipt tr = helloWorld.withdraw(value).send();
                String transactionHash = tr.getTransactionHash();
                Optional<TransactionReceipt> transactionReceipt = null;
                do {
                    System.out.println("checking if transaction " + transactionHash + " is mined....");
                    EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j
                            .ethGetTransactionReceipt(transactionHash)
                            .send();
                    transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
                    Thread.sleep(3000); // Wait for 3 sec
                } while (!transactionReceipt.isPresent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static void testTransferToContract() {
        Web3j web3j = Web3j.build(new HttpService(provider));
        try {
            Credentials credentials = Credentials
                    .create(privateKey);
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(credentials.getAddress(),
                            DefaultBlockParameterName.LATEST)
                    .send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            System.out.println(credentials.getAddress());
            System.out.println("nonce:" + nonce);

            // Recipient address
            String recipientAddress = contractAddress;
            // Value to transfer (in wei)
            BigInteger value = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();
            // Gas Parameter
            BigInteger gasLimit = BigInteger.valueOf(2100000);
            BigInteger gasPrice = Convert.toWei("200", Convert.Unit.GWEI).toBigInteger();

            // Prepare the rawTransaction
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce,
                    gasPrice, gasLimit,
                    recipientAddress, value);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            // Send transaction
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            // get TransactionHash
            if (ethSendTransaction.getError() != null)
                System.out.println(ethSendTransaction.getError().getMessage());
            else {
                String transactionHash = ethSendTransaction.getTransactionHash();
                String result = ethSendTransaction.getResult();
                System.out.println("transactionHash: " + transactionHash);
                System.out.println("result: " + result);

                Optional<TransactionReceipt> transactionReceipt = null;
                do {
                    System.out.println("checking if transaction " + transactionHash + " is mined....");
                    EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j
                            .ethGetTransactionReceipt(transactionHash)
                            .send();
                    transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
                    Thread.sleep(3000); // Wait for 3 sec
                } while (!transactionReceipt.isPresent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    public static void testDeploy() {

        try {
            Web3j web3j = Web3j.build(new HttpService(provider));
            FastRawTransactionManager txMananger = new FastRawTransactionManager(web3j,
                    Credentials.create(privateKey), chainId);
            HelloWorld.deploy(web3j, txMananger, new DefaultGasProvider()).send();
            web3j.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
