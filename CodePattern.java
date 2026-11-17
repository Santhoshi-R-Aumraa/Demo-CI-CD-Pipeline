import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InvoiceNumberGenerator {

    public enum PlanType {
        MONTHLY,
        YEARLY
    }

    public static String generateInvoiceNumber(
            String productShortcut,
            char planOption,
            PlanType planType,
            int customerNumber,
            LocalDate invoiceDate) {

        if (productShortcut == null || productShortcut.isBlank()) {
            throw new IllegalArgumentException("Product shortcut cannot be empty.");
        }

        char normalizedPlan = Character.toUpperCase(planOption);
        if (normalizedPlan != 'B' && normalizedPlan != 'G') {
            throw new IllegalArgumentException("Plan option must be 'B' or 'G'.");
        }

        if (customerNumber < 0) {
            throw new IllegalArgumentException("Customer number cannot be negative.");
        }

        LocalDate date = (invoiceDate != null) ? invoiceDate : LocalDate.now();

        // Last 2 digits of the current year (e.g., 2026 -> "26")
        String currentYearTwoDigits = String.format("%02d", date.getYear() % 100);

        // 3-letter uppercase month format (e.g., "MAR", "APR")
        String monthShort = date.format(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)).toUpperCase(Locale.ROOT);

        // Next year last 2 digits if YEARLY, otherwise empty
        String yearlySuffix = "";
        if (planType == PlanType.YEARLY) {
            int nextYearTwoDigits = (date.getYear() + 1) % 100;
            yearlySuffix = String.format("%02d", nextYearTwoDigits);
        }

        // 5-digit padded customer number (e.g., 1 -> "00001")
        String formattedCustomerNumber = String.format("%05d", customerNumber);

        return productShortcut.toUpperCase(Locale.ROOT)
                + normalizedPlan
                + currentYearTwoDigits
                + monthShort
                + yearlySuffix
                + formattedCustomerNumber;
    }

    public static void main(String[] args) {
        // Example 1: KY, Plan B, Yearly in March 2026, Customer 1 -> KYB26MAR2700001
        String inv1 = generateInvoiceNumber("KY", 'B', PlanType.YEARLY, 1, LocalDate.of(2026, 3, 15));
        System.out.println(inv1);

        // Example 2: KY, Plan B, Monthly in April 2026, Customer 1 -> KYB26APR00001
        String inv2 = generateInvoiceNumber("KY", 'B', PlanType.MONTHLY, 1, LocalDate.of(2026, 4, 15));
        System.out.println(inv2);

        // Example 3: KY, Plan G, Yearly in Nov 2026, Customer 42 -> KYG26NOV2700042
        String inv3 = generateInvoiceNumber("KY", 'G', PlanType.YEARLY, 42, LocalDate.of(2026, 11, 17));
        System.out.println(inv3);
    }
}
