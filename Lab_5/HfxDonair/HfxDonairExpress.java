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
 *
 * ========== New Menu Option ==========
 * Burger:
 * 		price - $2.50
 *
 *
 */
public class HfxDonairExpress {

    static Double[][] basePrices = {{5.00, 6.00, 7.00}, {8.00, 9.00, 10.00}, {2.50}};

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int type;
        int size;
        ArrayList<String> toppings;
        Double coupon;
        Double price = 0.00;

        System.out.println("Welcome to HfxDonairExpress! What would you like (0 for Donair, 1 for Pizza, 2 for Burger)?");

        // Get the type of food
        type = in.nextInt();

		String msg = "";
		if(type == 0) msg = "You have selected Donair.";
		else if(type == 1) msg = "You have selected Pizza.";
		else if(type == 2) msg = "You have selected Burger.";
		
        System.out.println(msg);
		
		if(type == 2){ // Burger
			price = basePrices[2][0];
		} else {
			System.out.println("Please enter a size (0 for small, 1 for med, 2 for large)");

			// Get the size
			size = in.nextInt();
			in.nextLine();

			// Process depending on the type
			switch (type) {
				case 0: // Donair
					price = basePrices[0][size];

					break;

				case 1: // Pizza
					price = basePrices[1][size];

					// Get toppings
					String topping;
					System.out.println("Please enter your toppings (one per line, ENTER to confirm)");
					do {
						topping = in.nextLine();

						if (topping.equals("pepperoni")) {
							price += 1.00;
						} else if (topping.equals("jalapeno")) {
							price += 0.99;
						} else if (topping.equals("mushroom")) {
							price += 0.75;
						}

					} while (!topping.equals(""));

					break;

				default:
					System.out.println("Invalid food choice");
			}
		}
	
        // Ask for a coupon
        System.out.println("Enter your % discount (0-100)");
        coupon = in.nextDouble();

        // Calculate the final price
        price = price - price*coupon/100;


        // Show the final price
        System.out.println("Your order comes to $" + String.format("%.2f", price));

    }
}
