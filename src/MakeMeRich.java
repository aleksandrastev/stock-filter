import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class MakeMeRich {
//	public static final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
//			"AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "YHOO", "VRSN");

	final static String API_KEY1 = "S5E21ZCD829EX69D";
	final static String API_KEY2 = "OF49Djz6kGm9zNz8g9IiLTooiDrjQmmodoDibjDC10G0GaK92FQVts4aXshG";

	public static void main(String[] args) throws IOException {
		CopyrightSearcher.searchForCopyright("src");

		// 1. Print these symbols using a Java 8 for-each and lambdas
//		symbols.stream().forEach(s -> System.out.println(s));

		List<String> symbols = getSymbols();
//		symbols.stream().forEach(s -> System.out.println(s));

		// 2. Use the StockUtil class to print the price of Bitcoin
//		symbols.stream().forEach(s -> System.out.println(StockUtil.getPrice(s)));

		// 3. Create a new List of StockInfo that includes the stock price

		// using 20 random elements from symbols
		Collections.shuffle(symbols);

		// creating a new List of StockInfo that includes the stock price
		// list is limited to 10 elements
		List<StockInfo> listOfStockInfo = new ArrayList<StockInfo>();
		symbols.stream().takeWhile(s -> listOfStockInfo.size() < 10).forEach(s -> {
			try {
				double price = getPrice(s);
				if (price > 0) {
					System.out.println(s + " " + price);
					listOfStockInfo.add(new StockInfo(s, price));
				}
				Thread.sleep(13000); // because of Alpha Vantage limit of 5 API calls per minute
			} catch (Exception e) {
				System.err.println("An exception was thrown");
			}
		});

		// 4. Find the highest-priced stock under $500
		StockInfo max = listOfStockInfo.stream().filter(StockUtil.isPriceLessThan(500))
				.max(Comparator.comparing(s -> s.price)).get();
		System.out.println("The highest-priced stock under $500 is " + max.ticker + " - " + max.price + "$");

	}

	// only 500 requests per day
	private static List<String> getSymbols() throws IOException {
		String rootURL = "https://api.worldtradingdata.com/api/v1/forex?base=USD";
		rootURL += "&api_token=" + API_KEY2;
		URL request = new URL(rootURL);
		InputStream openStream = request.openStream();
		String response = IOUtils.toString(openStream);
		JSONObject root = new JSONObject(response);
		JSONObject data = (JSONObject) root.get("data");
		return new ArrayList<String>(data.keySet());
	}

	// only 5 requests per minut, 500 requests per day
	private static double getPrice(String symbol) throws MalformedURLException, IOException {
		String rootURL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE";
		rootURL += "&symbol=" + symbol;
		rootURL += "&apikey=" + API_KEY1;

		URL request = new URL(rootURL);
		InputStream openStream = request.openStream();
		String response = IOUtils.toString(openStream);

		JSONObject root = new JSONObject(response);
		JSONObject glob_quote = (JSONObject) root.get("Global Quote");
		double price = glob_quote.getDouble("05. price");

		return price;

	}

}

// Copyright Aleksandra Stevanovic, 2019