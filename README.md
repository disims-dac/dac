## Getting Started

## IDE and Tools
1. [Visual Code](https://code.visualstudio.com/). Install the Extension Pack for Java which contains the Maven plugin [here](https://code.visualstudio.com/docs/java/java-build).
2. [Maven 3.9.6](https://maven.apache.org/download.cgi) (Remember to set PATH)
3. [Hyperledger Fabric v2.5](https://hyperledger-fabric.readthedocs.io/en/release-2.5/)

## Setup 

### Running in a Standalone Java Program
1. Open the downloaded project directory in Visual Studio Code.
2. It should fetch most of the dependencies, except the JPBC libary. Import them manually:
    * In `RefLib`, there are totally four .jar
    * For each of the .jar in RefLib, install it into the local respository by issuing the following command
    ```bash
    mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> 
    ```
    For example, refer to pom.xml, one dependency is
    ```xml
    <dependency>
      <groupId>gas.dia.unisa.it</groupId>
      <artifactId>jna</artifactId>
      <version>3.2.5</version>
    </dependency>
    ```
    The command is:
    ```bash
    mvn install:install-file -Dfile=<path-to-file> -DgroupId="gas.dia.unisa.it" -DartifactId="jna" -Dversion="3.2.5"
    ```
    * If the import is successful, there will be no compilation error for the Java sources.

#### The main program

`src\main\java\DisIMS\DisIMS.java` contains the main() method to test the entire flow of the DisIMS scheme.

### Running in Hyperleger Fabric
1. Set up a test network. [Guidelines](https://hyperledger-fabric.readthedocs.io/en/release-2.5/install.html). The network should contain two endorsing peers, one orderer and one CA, by issuing `./network.sh up createChannel -ca`
2. Open the downloaded project directory in Visual Studio Code.
3. Overwrite the folder "fabric-samples/asset-transfer-basic/chaincode-java" by "Fabric-Java/chaincode-java".
4. Overwrite the folder "fabric-samples/asset-transfer-basic/application-java" by "Fabric-Java/application-java".
5. Deploy the chaincode, "fabric-samples/asset-transfer-basic/chaincode-java".
6. After chaincode is deployed successfully, copy the curve property file to the chaincode container.
7. Run the Java program "fabric-samples/asset-transfer-basic/application-java/src/main/java/application/java/App.java". (Uncommment Lines 57 and 58 for admin enrolment and user registration for the first run)

### Running in Ethereum
1. If the smart contract file is changed, use the following command in the root folder to generate the new Java Wrapper.
    ```bash
    mvn web3j:generate-sources
    ```

`src\main\java\DisIMS\DisIMSExperiment.java` contains the main() method to test the entire flow of the DAC scheme. Place the .properties file in the root directory, which is not uploaded to github for security purpose. It contains the private key of the testing Ethereum testnet (Sepolia) account.

## Pairing Library

Java Pairing Based Cryptography Library (JPBC) is adopted [here](http://gas.dia.unisa.it/projects/jpbc/download.html). 


