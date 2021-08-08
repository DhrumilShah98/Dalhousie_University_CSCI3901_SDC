import java.util.Scanner;
import java.util.ArrayList;

/**
 * User interface for ordering from HfxDonairExpress.
 */
public class Menu {

    /**
     * The main ordering method
     * @param args Should be empty
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int type = -1;
        int size = -1;
        ArrayList<String> toppings = new ArrayList<>();
        String coupon = "";

        HfxDonairExpress menuProvider = new HfxDonairExpress();

        System.out.println("Welcome to HfxDonairExpress! What would you like (0 for Donair, 1 for Pizza)?");

        // Get the type of food
        type = in.nextInt();

        System.out.println("You have selected " + (type == 0 ? "Donair" : "Pizza") + ".");
        System.out.println("Please enter a size (0 for small, 1 for med, 2 for large)");

        // Get the size
        size = in.nextInt();
        in.nextLine();

        // Ask for pizza topings
        if (type == 1) {
            // Get toppings
            System.out.println("Please enter your toppings (one per line, ENTER to confirm)");
            String topping = in.nextLine();

            while (!topping.equals("")) {
                toppings.add(topping);
                topping = in.nextLine();
            }
        }

        // Ask for a coupon
        System.out.println("Enter your coupon code:");
        coupon = in.nextLine();

        try {
            double price = menuProvider.order(type, size, toppings, coupon);

            // Show the final price
            System.out.println("Your order comes to $" + String.format("%.2f", price));

            // Get the payment
            System.out.println("Please pay money now");
            double payment = in.nextDouble();
            while (payment < price) {
                System.out.println("You still owe " + String.format("%.2f", price - payment));
                payment += in.nextDouble();
            }

            // Return the order number
            int orderNo = menuProvider.makeOrder(type, size, toppings);
            System.out.println("Your order number " + orderNo + " is ready. Enjoy!");

        } catch (Exception e) {
            System.out.println("Could not place your order");
        }

    }
}
