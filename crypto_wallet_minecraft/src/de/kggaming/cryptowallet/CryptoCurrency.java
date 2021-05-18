package de.kggaming.cryptowallet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class CryptoCurrency {
	public float getCurrency() throws IOException, InterruptedException {
		try {
			// Creates an HTTPClient (Java 11+ required) and builds an HTTP Request to the Coingecko API
			HttpClient client = HttpClient.newHttpClient();
			JsonParser parser = new JsonParser();
			HttpRequest request = HttpRequest.newBuilder()
					
					.uri(URI.create("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=eur"))
					.build();

			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			//The response is always a JSON String --> Converts response String into JSON Object with org.json Class
			JsonObject obj = new JsonObject();
			obj = parser.parse(response.body()).getAsJsonObject();
			//Fetch the Value out of the JSON String
			
			float value = obj.get("bitcoin").getAsJsonObject().get("eur").getAsFloat();

			return value;
			//Catch JSON Error for Example: wrong currency given or not supported currency
		} catch (Exception e) {
			System.out.println("Upsala. Da stimmt was mit deiner Eingabe nicht! Überprüfe diese!");
			e.printStackTrace();
			return 0f;
		}
	}
}
