package src.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TaxCalculator {

    public interface TaxCalculationStrategy {
        String calculate(String prompt) throws IOException, InterruptedException;
    }

    public static class GeminiTaxStrategy implements TaxCalculationStrategy {

        private static final String API_KEY = "YOUR_API_KEY_HERE";
        private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

        @Override
        public String calculate(String prompt) throws IOException, InterruptedException {
            if (API_KEY == null || API_KEY.isEmpty()) {
                return "Error: GEMINI_API_KEY not set.";
            }

            String fullPrompt = """
            {
              "contents": [
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "You are a helpful Thai financial advisor. Give clean, step-by-step tax calculation. Don't write ? and at the end give some financial advice"
                    }
                  ]
                },
                {
                  "role": "user",
                  "parts": [
                    {
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """.formatted(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(fullPrompt))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            String marker = "\"text\": \"";
            int start = body.indexOf(marker);
            if (start == -1) return "Error: No text field found in Gemini response.";

            start += marker.length();
            StringBuilder result = new StringBuilder();
            boolean escape = false;

            for (int i = start; i < body.length(); i++) {
                char c = body.charAt(i);

                if (escape) {
                    if (c == 'n') result.append('\n');
                    else if (c == 't') result.append('\t');
                    else result.append(c);
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    break;
                } else {
                    result.append(c);
                }
            }

            return result.toString().trim();
        }
    }


    private static TaxCalculationStrategy strategy = new GeminiTaxStrategy();

    public static void setStrategy(TaxCalculationStrategy newStrategy) {
        strategy = newStrategy;
    }

    public static String calculate(String prompt) throws IOException, InterruptedException {
        return strategy.calculate(prompt);
    }
}
