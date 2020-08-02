/**
 * 
 */
package whatsapp;

/**
 * @author ICT
 *
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

public class Eth {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    System.out.println("Connecting to Ethereum ...");
		Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:8545"));
	    System.out.println("Successfuly connected to Ethereum");
		
		try {
			  // web3_clientVersion returns the current client version.
			  Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
			  //eth_blockNumber returns the number of most recent block.
			  EthBlockNumber blockNumber = web3.ethBlockNumber().send();
			  //eth_gasPrice, returns the current price per gas in wei.
			  EthGasPrice gasPrice =  web3.ethGasPrice().send();
			  // Print result
		      System.out.println("Client version: " + clientVersion.getWeb3ClientVersion());
		      System.out.println("Block number: " + blockNumber.getBlockNumber());
		      System.out.println("Gas price: " + gasPrice.getGasPrice());
		      
		      EthGetBalance balanceWei = web3.ethGetBalance("0x6cacb165f4ca32a537c772c9c68c5eeda1f830613c2c1d7fd810f70e3c6f554e", DefaultBlockParameterName.LATEST).send();
		      System.out.println("balance in wei: " + balanceWei);

		      BigDecimal balanceInEther = Convert.fromWei(balanceWei.getBalance().toString(), Unit.ETHER);
		      System.out.println("balance in ether: " + balanceInEther);
		      
		      EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount("0xF0f15Cedc719B5A55470877B0710d5c7816916b1", DefaultBlockParameterName.LATEST).send();

		      BigInteger nonce =  ethGetTransactionCount.getTransactionCount();
		      System.out.println("Eth Transaction Count: " + nonce);

			} catch(IOException ex) {
			  throw new RuntimeException("Error whilst sending json-rpc requests", ex);
			}

	}

}
