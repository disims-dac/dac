package DisIMS.Utility;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class DisIMS extends Contract {
    public static final String BINARY = "60806040525f60085534801562000014575f80fd5b506040516200166238038062001662833981016040819052620000379162000127565b80604051806040016040528060068152602001654449534d697360d01b8152506040518060400160405280600381526020016213919560ea1b815250815f9081620000839190620001f6565b506001620000928282620001f6565b5050506001600160a01b038116620000c357604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b620000ce81620000d6565b5050620002be565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b5f6020828403121562000138575f80fd5b81516001600160a01b03811681146200014f575f80fd5b9392505050565b634e487b7160e01b5f52604160045260245ffd5b600181811c908216806200017f57607f821691505b6020821081036200019e57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f821115620001f1575f81815260208120601f850160051c81016020861015620001cc5750805b601f850160051c820191505b81811015620001ed57828155600101620001d8565b5050505b505050565b81516001600160401b0381111562000212576200021262000156565b6200022a816200022384546200016a565b84620001a4565b602080601f83116001811462000260575f8415620002485750858301515b5f19600386901b1c1916600185901b178555620001ed565b5f85815260208120601f198616915b8281101562000290578886015182559484019460019091019084016200026f565b5085821015620002ae57878501515f19600388901b60f8161c191681555b5050505050600190811b01905550565b61139680620002cc5f395ff3fe608060405234801561000f575f80fd5b5060043610610111575f3560e01c80638ada066e1161009e578063b88d4fde1161006e578063b88d4fde14610228578063c87b56dd1461023b578063e985e9c51461024e578063eacabe1414610261578063f2fde38b14610274575f80fd5b80638ada066e146101f45780638da5cb5b146101fc57806395d89b411461020d578063a22cb46514610215575f80fd5b806323b872dd116100e457806323b872dd1461019257806342842e0e146101a55780636352211e146101b857806370a08231146101cb578063715018a6146101ec575f80fd5b806301ffc9a71461011557806306fdde031461013d578063081812fc14610152578063095ea7b31461017d575b5f80fd5b610128610123366004610e8f565b610287565b60405190151581526020015b60405180910390f35b6101456102b1565b6040516101349190610ef7565b610165610160366004610f09565b610340565b6040516001600160a01b039091168152602001610134565b61019061018b366004610f3b565b610367565b005b6101906101a0366004610f63565b610376565b6101906101b3366004610f63565b610404565b6101656101c6366004610f09565b610423565b6101de6101d9366004610f9c565b61042d565b604051908152602001610134565b610190610472565b6008546101de565b6007546001600160a01b0316610165565b610145610485565b610190610223366004610fb5565b610494565b610190610236366004611075565b61049f565b610145610249366004610f09565b6104b6565b61012861025c3660046110ec565b6105c1565b6101de61026f36600461111d565b6105ee565b610190610282366004610f9c565b610625565b5f6001600160e01b03198216632483248360e11b14806102ab57506102ab82610662565b92915050565b60605f80546102bf9061117b565b80601f01602080910402602001604051908101604052809291908181526020018280546102eb9061117b565b80156103365780601f1061030d57610100808354040283529160200191610336565b820191905f5260205f20905b81548152906001019060200180831161031957829003601f168201915b5050505050905090565b5f61034a826106b1565b505f828152600460205260409020546001600160a01b03166102ab565b6103728282336106e9565b5050565b6001600160a01b0382166103a457604051633250574960e11b81525f60048201526024015b60405180910390fd5b5f6103b08383336106f6565b9050836001600160a01b0316816001600160a01b0316146103fe576040516364283d7b60e01b81526001600160a01b038086166004830152602482018490528216604482015260640161039b565b50505050565b61041e83838360405180602001604052805f81525061049f565b505050565b5f6102ab826106b1565b5f6001600160a01b038216610457576040516322718ad960e21b81525f600482015260240161039b565b506001600160a01b03165f9081526003602052604090205490565b61047a6107e8565b6104835f610815565b565b6060600180546102bf9061117b565b610372338383610866565b6104aa848484610376565b6103fe84848484610904565b60606104c1826106b1565b505f82815260066020526040812080546104da9061117b565b80601f01602080910402602001604051908101604052809291908181526020018280546105069061117b565b80156105515780601f1061052857610100808354040283529160200191610551565b820191905f5260205f20905b81548152906001019060200180831161053457829003601f168201915b505050505090505f61056d60408051602081019091525f815290565b905080515f0361057e575092915050565b8151156105b05780826040516020016105989291906111b3565b60405160208183030381529060405292505050919050565b6105b984610a2a565b949350505050565b6001600160a01b039182165f90815260056020908152604080832093909416825291909152205460ff1690565b5f6105f76107e8565b6008546106059060016111e1565b60088190556106148482610a9a565b61061e8184610afb565b9392505050565b61062d6107e8565b6001600160a01b03811661065657604051631e4fbdf760e01b81525f600482015260240161039b565b61065f81610815565b50565b5f6001600160e01b031982166380ac58cd60e01b148061069257506001600160e01b03198216635b5e139f60e01b145b806102ab57506301ffc9a760e01b6001600160e01b03198316146102ab565b5f818152600260205260408120546001600160a01b0316806102ab57604051637e27328960e01b81526004810184905260240161039b565b61041e8383836001610b4a565b5f828152600260205260408120546001600160a01b039081169083161561072257610722818486610c4e565b6001600160a01b0381161561075c5761073d5f855f80610b4a565b6001600160a01b0381165f90815260036020526040902080545f190190555b6001600160a01b0385161561078a576001600160a01b0385165f908152600360205260409020805460010190555b5f8481526002602052604080822080546001600160a01b0319166001600160a01b0389811691821790925591518793918516917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef91a4949350505050565b6007546001600160a01b031633146104835760405163118cdaa760e01b815233600482015260240161039b565b600780546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0905f90a35050565b6001600160a01b03821661089857604051630b61174360e31b81526001600160a01b038316600482015260240161039b565b6001600160a01b038381165f81815260056020908152604080832094871680845294825291829020805460ff191686151590811790915591519182527f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31910160405180910390a3505050565b6001600160a01b0383163b156103fe57604051630a85bd0160e11b81526001600160a01b0384169063150b7a0290610946903390889087908790600401611200565b6020604051808303815f875af1925050508015610980575060408051601f3d908101601f1916820190925261097d9181019061123c565b60015b6109e7573d8080156109ad576040519150601f19603f3d011682016040523d82523d5f602084013e6109b2565b606091505b5080515f036109df57604051633250574960e11b81526001600160a01b038516600482015260240161039b565b805181602001fd5b6001600160e01b03198116630a85bd0160e11b14610a2357604051633250574960e11b81526001600160a01b038516600482015260240161039b565b5050505050565b6060610a35826106b1565b505f610a4b60408051602081019091525f815290565b90505f815111610a695760405180602001604052805f81525061061e565b80610a7384610cb2565b604051602001610a849291906111b3565b6040516020818303038152906040529392505050565b6001600160a01b038216610ac357604051633250574960e11b81525f600482015260240161039b565b5f610acf83835f6106f6565b90506001600160a01b0381161561041e576040516339e3563760e11b81525f600482015260240161039b565b5f828152600660205260409020610b1282826112a4565b506040518281527ff8e1a15aba9398e019f0b49df1a4fde98ee17ae345cb5f6b5e2c27f5033e8ce79060200160405180910390a15050565b8080610b5e57506001600160a01b03821615155b15610c1f575f610b6d846106b1565b90506001600160a01b03831615801590610b995750826001600160a01b0316816001600160a01b031614155b8015610bac5750610baa81846105c1565b155b15610bd55760405163a9fbf51f60e01b81526001600160a01b038416600482015260240161039b565b8115610c1d5783856001600160a01b0316826001600160a01b03167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92560405160405180910390a45b505b50505f90815260046020526040902080546001600160a01b0319166001600160a01b0392909216919091179055565b610c59838383610d42565b61041e576001600160a01b038316610c8757604051637e27328960e01b81526004810182905260240161039b565b60405163177e802f60e01b81526001600160a01b03831660048201526024810182905260440161039b565b60605f610cbe83610da3565b60010190505f8167ffffffffffffffff811115610cdd57610cdd610fee565b6040519080825280601f01601f191660200182016040528015610d07576020820181803683370190505b5090508181016020015b5f19016f181899199a1a9b1b9c1cb0b131b232b360811b600a86061a8153600a8504945084610d1157509392505050565b5f6001600160a01b038316158015906105b95750826001600160a01b0316846001600160a01b03161480610d7b5750610d7b84846105c1565b806105b95750505f908152600460205260409020546001600160a01b03908116911614919050565b5f8072184f03e93ff9f4daa797ed6e38ed64bf6a1f0160401b8310610de15772184f03e93ff9f4daa797ed6e38ed64bf6a1f0160401b830492506040015b6d04ee2d6d415b85acef81000000008310610e0d576d04ee2d6d415b85acef8100000000830492506020015b662386f26fc100008310610e2b57662386f26fc10000830492506010015b6305f5e1008310610e43576305f5e100830492506008015b6127108310610e5757612710830492506004015b60648310610e69576064830492506002015b600a83106102ab5760010192915050565b6001600160e01b03198116811461065f575f80fd5b5f60208284031215610e9f575f80fd5b813561061e81610e7a565b5f5b83811015610ec4578181015183820152602001610eac565b50505f910152565b5f8151808452610ee3816020860160208601610eaa565b601f01601f19169290920160200192915050565b602081525f61061e6020830184610ecc565b5f60208284031215610f19575f80fd5b5035919050565b80356001600160a01b0381168114610f36575f80fd5b919050565b5f8060408385031215610f4c575f80fd5b610f5583610f20565b946020939093013593505050565b5f805f60608486031215610f75575f80fd5b610f7e84610f20565b9250610f8c60208501610f20565b9150604084013590509250925092565b5f60208284031215610fac575f80fd5b61061e82610f20565b5f8060408385031215610fc6575f80fd5b610fcf83610f20565b915060208301358015158114610fe3575f80fd5b809150509250929050565b634e487b7160e01b5f52604160045260245ffd5b5f67ffffffffffffffff8084111561101c5761101c610fee565b604051601f8501601f19908116603f0116810190828211818310171561104457611044610fee565b8160405280935085815286868601111561105c575f80fd5b858560208301375f602087830101525050509392505050565b5f805f8060808587031215611088575f80fd5b61109185610f20565b935061109f60208601610f20565b925060408501359150606085013567ffffffffffffffff8111156110c1575f80fd5b8501601f810187136110d1575f80fd5b6110e087823560208401611002565b91505092959194509250565b5f80604083850312156110fd575f80fd5b61110683610f20565b915061111460208401610f20565b90509250929050565b5f806040838503121561112e575f80fd5b61113783610f20565b9150602083013567ffffffffffffffff811115611152575f80fd5b8301601f81018513611162575f80fd5b61117185823560208401611002565b9150509250929050565b600181811c9082168061118f57607f821691505b6020821081036111ad57634e487b7160e01b5f52602260045260245ffd5b50919050565b5f83516111c4818460208801610eaa565b8351908301906111d8818360208801610eaa565b01949350505050565b808201808211156102ab57634e487b7160e01b5f52601160045260245ffd5b6001600160a01b03858116825284166020820152604081018390526080606082018190525f9061123290830184610ecc565b9695505050505050565b5f6020828403121561124c575f80fd5b815161061e81610e7a565b601f82111561041e575f81815260208120601f850160051c8101602086101561127d5750805b601f850160051c820191505b8181101561129c57828155600101611289565b505050505050565b815167ffffffffffffffff8111156112be576112be610fee565b6112d2816112cc845461117b565b84611257565b602080601f831160018114611305575f84156112ee5750858301515b5f19600386901b1c1916600185901b17855561129c565b5f85815260208120601f198616915b8281101561133357888601518255948401946001909101908401611314565b508582101561135057878501515f19600388901b60f8161c191681555b5050505050600190811b0190555056fea26469706673582212201805a34bcdcc40a8ee42f6faa91c15fbfee9b35f8cde5922f64b1bba9ea7d9a864736f6c63430008140033";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_GETCOUNTER = "getCounter";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_MINTNFT = "mintNFT";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_safeTransferFrom = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event BATCHMETADATAUPDATE_EVENT = new Event("BatchMetadataUpdate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event METADATAUPDATE_EVENT = new Event("MetadataUpdate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected DisIMS(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DisIMS(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DisIMS(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DisIMS(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.approved = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public static List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalForAllEventResponse getApprovalForAllEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVALFORALL_EVENT, log);
        ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalForAllEventFromLog(log));
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }

    public static List<BatchMetadataUpdateEventResponse> getBatchMetadataUpdateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BATCHMETADATAUPDATE_EVENT, transactionReceipt);
        ArrayList<BatchMetadataUpdateEventResponse> responses = new ArrayList<BatchMetadataUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BatchMetadataUpdateEventResponse typedResponse = new BatchMetadataUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._fromTokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._toTokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BatchMetadataUpdateEventResponse getBatchMetadataUpdateEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BATCHMETADATAUPDATE_EVENT, log);
        BatchMetadataUpdateEventResponse typedResponse = new BatchMetadataUpdateEventResponse();
        typedResponse.log = log;
        typedResponse._fromTokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse._toTokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<BatchMetadataUpdateEventResponse> batchMetadataUpdateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBatchMetadataUpdateEventFromLog(log));
    }

    public Flowable<BatchMetadataUpdateEventResponse> batchMetadataUpdateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BATCHMETADATAUPDATE_EVENT));
        return batchMetadataUpdateEventFlowable(filter);
    }

    public static List<MetadataUpdateEventResponse> getMetadataUpdateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(METADATAUPDATE_EVENT, transactionReceipt);
        ArrayList<MetadataUpdateEventResponse> responses = new ArrayList<MetadataUpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MetadataUpdateEventResponse typedResponse = new MetadataUpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MetadataUpdateEventResponse getMetadataUpdateEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(METADATAUPDATE_EVENT, log);
        MetadataUpdateEventResponse typedResponse = new MetadataUpdateEventResponse();
        typedResponse.log = log;
        typedResponse._tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<MetadataUpdateEventResponse> metadataUpdateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMetadataUpdateEventFromLog(log));
    }

    public Flowable<MetadataUpdateEventResponse> metadataUpdateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(METADATAUPDATE_EVENT));
        return metadataUpdateEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getApproved(BigInteger tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getCounter() {
        final Function function = new Function(FUNC_GETCOUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> isApprovedForAll(String owner, String operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.Address(160, operator)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mintNFT(String recipient, String tokenURI) {
        final Function function = new Function(
                FUNC_MINTNFT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient), 
                new org.web3j.abi.datatypes.Utf8String(tokenURI)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> ownerOf(BigInteger tokenId) {
        final Function function = new Function(FUNC_OWNEROF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_safeTransferFrom, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger tokenId, byte[] data) {
        final Function function = new Function(
                FUNC_safeTransferFrom, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setApprovalForAll(String operator, Boolean approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, operator), 
                new org.web3j.abi.datatypes.Bool(approved)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> tokenURI(BigInteger tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger tokenId) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from), 
                new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(tokenId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static DisIMS load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DisIMS(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DisIMS load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DisIMS(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DisIMS load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DisIMS(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DisIMS load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DisIMS(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DisIMS> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(DisIMS.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<DisIMS> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(DisIMS.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DisIMS> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(DisIMS.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DisIMS> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String initialOwner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, initialOwner)));
        return deployRemoteCall(DisIMS.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String approved;

        public BigInteger tokenId;
    }

    public static class ApprovalForAllEventResponse extends BaseEventResponse {
        public String owner;

        public String operator;

        public Boolean approved;
    }

    public static class BatchMetadataUpdateEventResponse extends BaseEventResponse {
        public BigInteger _fromTokenId;

        public BigInteger _toTokenId;
    }

    public static class MetadataUpdateEventResponse extends BaseEventResponse {
        public BigInteger _tokenId;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger tokenId;
    }
}
