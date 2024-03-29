package DisIMS.Utility;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.0.
 */
@SuppressWarnings("rawtypes")
public class TestNoNFT extends Contract {
    public static final String BINARY = "60806040525f600155348015610013575f80fd5b505f80546001600160a01b03191633179055610679806100325f395ff3fe608060405234801561000f575f80fd5b506004361061004a575f3560e01c80631e39460f1461004e5780638ada066e14610074578063ca5415d71461007c578063e0574e3f1461009c575b5f80fd5b61006161005c366004610367565b6100bd565b6040519081526020015b60405180910390f35b600154610061565b61008f61008a366004610430565b6101e3565b60405161006b919061048a565b6100af6100aa366004610430565b610295565b60405161006b9291906104a3565b5f80546001600160a01b031633146101255760405162461bcd60e51b815260206004820152602160248201527f4f6e6c79206f6e7765722063616e2063616c6c207468697320636f6e747261636044820152601d60fa1b606482015260840160405180910390fd5b60018054610132916104ce565b6001908155604080518082019091526001600160a01b038581168252602082018581526002805494850181555f81905283517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ace9590910294850180546001600160a01b0319169190931617825551919283927f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5acf909101906101d39082610573565b5050506001549150505b92915050565b6060600282815481106101f8576101f861062f565b905f5260205f2090600202016001018054610212906104ed565b80601f016020809104026020016040519081016040528092919081815260200182805461023e906104ed565b80156102895780601f1061026057610100808354040283529160200191610289565b820191905f5260205f20905b81548152906001019060200180831161026c57829003601f168201915b50505050509050919050565b600281815481106102a4575f80fd5b5f918252602090912060029091020180546001820180546001600160a01b039092169350906102d2906104ed565b80601f01602080910402602001604051908101604052809291908181526020018280546102fe906104ed565b80156103495780601f1061032057610100808354040283529160200191610349565b820191905f5260205f20905b81548152906001019060200180831161032c57829003601f168201915b5050505050905082565b634e487b7160e01b5f52604160045260245ffd5b5f8060408385031215610378575f80fd5b82356001600160a01b038116811461038e575f80fd5b9150602083013567ffffffffffffffff808211156103aa575f80fd5b818501915085601f8301126103bd575f80fd5b8135818111156103cf576103cf610353565b604051601f8201601f19908116603f011681019083821181831017156103f7576103f7610353565b8160405282815288602084870101111561040f575f80fd5b826020860160208301375f6020848301015280955050505050509250929050565b5f60208284031215610440575f80fd5b5035919050565b5f81518084525f5b8181101561046b5760208185018101518683018201520161044f565b505f602082860101526020601f19601f83011685010191505092915050565b602081525f61049c6020830184610447565b9392505050565b6001600160a01b03831681526040602082018190525f906104c690830184610447565b949350505050565b808201808211156101dd57634e487b7160e01b5f52601160045260245ffd5b600181811c9082168061050157607f821691505b60208210810361051f57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111561056e575f81815260208120601f850160051c8101602086101561054b5750805b601f850160051c820191505b8181101561056a57828155600101610557565b5050505b505050565b815167ffffffffffffffff81111561058d5761058d610353565b6105a18161059b84546104ed565b84610525565b602080601f8311600181146105d4575f84156105bd5750858301515b5f19600386901b1c1916600185901b17855561056a565b5f85815260208120601f198616915b82811015610602578886015182559484019460019091019084016105e3565b508582101561061f57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b5f52603260045260245ffdfea2646970667358221220765b9fbd78941d98f9ca32d29c5979aad536217ae0ffb61a3a6fddb50dc7a14664736f6c63430008140033";

    public static final String FUNC_CREATECREDENTIAL = "createCredential";

    public static final String FUNC_CREDENTIALS = "credentials";

    public static final String FUNC_GETCOUNTER = "getCounter";

    public static final String FUNC_GETHASHOFC = "getHashOfC";

    @Deprecated
    protected TestNoNFT(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TestNoNFT(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TestNoNFT(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TestNoNFT(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> createCredential(String cOwner, String hashOfC) {
        final Function function = new Function(
                FUNC_CREATECREDENTIAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, cOwner), 
                new org.web3j.abi.datatypes.Utf8String(hashOfC)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<String, String>> credentials(BigInteger param0) {
        final Function function = new Function(FUNC_CREDENTIALS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple2<String, String>>(function,
                new Callable<Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getCounter() {
        final Function function = new Function(FUNC_GETCOUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getHashOfC(BigInteger index) {
        final Function function = new Function(FUNC_GETHASHOFC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static TestNoNFT load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TestNoNFT(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TestNoNFT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TestNoNFT(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TestNoNFT load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TestNoNFT(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TestNoNFT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TestNoNFT(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TestNoNFT> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TestNoNFT.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<TestNoNFT> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TestNoNFT.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TestNoNFT> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TestNoNFT.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TestNoNFT> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TestNoNFT.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
