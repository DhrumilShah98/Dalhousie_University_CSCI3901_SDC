import java.util.Scanner;
import java.util.ArrayList;

/**
 * This program is used by HfxDonairExpress to take their online orders. Their menu is as follows
 *
 * Donair:
 *      small - $5
 *      med   - $6
 *      large - $7
 *
 * Pizza:
 *      small - $8
 *      med   - $9
 *      large - $10
 *
 *      Toppings:
 *          pepperoni - $1.00
 *          jalapeno  - $0.99
 *          mushroom  - $0.75
 */
public class HfxDonairExpress {

    static Double[][] basePrices = {{5.00, 6.00, 7.00}, {8.00, 9.00, 10.00}};

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int type;
        int size;
        ArrayList<String> toppings = new ArrayList<>(); // Solution: ArrayList initialized to avoid NullPointerException
        Double coupon;
        Double price = 0.00;

        System.out.println("Welcome to HfxDonairExpress! What would you like (0 for Donair, 1 for Pizza)?");

        // Get the type of food
        type = in.nextInt();

        System.out.println("You have selected " + (type == 0 ? "Donair" : "Pizza") + ".");
        System.out.println("Please enter a size (0 for small, 1 for med, 2 for large)");

        // Get the size
        size = in.nextInt();
        in.nextLine();

        // Process depending on the type
        switch (type) {
            case 0: // Donair
                price = basePrices[0][size];
                break; // Solution: break statement added to stop execution of subsequent cases.
            case 1: // Pizza
                price = basePrices[1][size];

                // Get toppings
                String topping;
                System.out.println("Please enter your toppings (one per line, ENTER to confirm)");
                do {
                    topping = in.nextLine();
                    if(!toppings.contains(topping.toLowerCase())) { // Solution: This condition makes sure same topping is not added multiple times
                        // Solution: Used equalsIgnoreCase() to compare two strings instead of '==' at line number 61, 64 and 67. equals() can also be used to enforce letter case.
                        if (topping.equalsIgnoreCase("pepperoni")) {
                            price += 1.00; // Solution: Updated the value of price by adding topping price instead of assigning.
                            toppings.add("pepperoni"); // Solution: Add topping in the list of added toppings
                        } else if (topping.equalsIgnoreCase("jalapeno")) {
                            price += 0.99; // Solution: Updated the value of price by adding topping price instead of assigning.
                            toppings.add("jalapeno"); // Solution: Add topping in the list of added toppings
                        } else if (topping.equalsIgnoreCase("mushroom")) {
                            price += 0.75; // Solution: Updated the value of price by adding topping price instead of assigning.
                            toppings.add("mushroom"); // Solution: Add topping in the list of added toppings
                        }
                    }

                } while (!topping.equals(""));

                break;

            default:
                System.out.println("Invalid food choice");
        }

        // Ask for a coupon
        System.out.println("Enter your % discount (0-100)");
        coupon = in.nextDouble();

        // Calculate the final price
        price = price - (price * coupon/100.0); // Solution: Updated the discount calculation to calculate the final price properly

        // Show the final price
        System.out.println("Your order comes to $" + String.format("%.2f", price));

    }
}