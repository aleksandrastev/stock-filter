
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MakeMeRich {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ",
			"IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE", "AMZN", "CRAY", "CSCO",
			"SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN",
			"YHOO");

	public static void main(String[] args) {

		// 1. Print these symbols using a Java 8 for-each and lambdas
		symbols.stream().forEach(s -> System.out.println(s));

		// 2. Use the StockUtil class to print the price of Bitcoin
		symbols.stream().forEach(s -> System.out.println(StockUtil.getPrice(s)));

		// 3. Create a new List of StockInfo that includes the stock price
		List<StockInfo> listOfStockInfo = new ArrayList<StockInfo>();
		for (String s : symbols) {
			listOfStockInfo.add(StockUtil.getPrice(s));
		}
		System.out.println(listOfStockInfo);
		
	
		// 4. Find the highest-priced stock under $500
		StockInfo max = listOfStockInfo.stream().filter(StockUtil.isPriceLessThan(500)).max(Comparator.comparing(s -> s.price)).get();
		System.out.println("The highest-priced stock under $500 is " + max.ticker + " - " + max.price + "$");
		
	
	}

}
