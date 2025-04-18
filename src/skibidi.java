import java.io.IOException;
import java.io.PrintWriter;
public class skibidi {
    public static void main(String[] args) {
        String md = "# Tax Summary\n" +
                    "\n" +
                    "| Description | Amount (THB) |\n" +
                    "|-------------|---------------|\n" +
                    String.format("| Total Income\n") +
                    String.format("| Total Deductions\n") +
                    String.format("| Taxable Income\n") +
                    String.format("| Tax Due\n");
        try (PrintWriter out = new PrintWriter("TaxSummary.md")) {
            out.println(md);
        } catch (IOException e) {
            System.err.println("Failed to write Markdown file: " + e.getMessage());
        }
    }
}
