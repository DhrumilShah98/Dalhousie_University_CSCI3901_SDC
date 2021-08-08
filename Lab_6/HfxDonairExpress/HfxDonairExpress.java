import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * This program is used by HfxDonairExpress to take their online orders. The current menu is as follows.
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

    /**
     * The base prices of each type of food and size
     */
    static Double[][] basePrices = {{5.00, 6.00, 7.00}, {8.00, 9.00, 10.00}};

    /**
     * Holds the prices of various toppings
     */
    static Map<String, Double> toppingPrices;
    static {
        toppingPrices = new HashMap<>();
        toppingPrices.put("pepperoni", 1.00);
        toppingPrices.put("jalapeno", 0.99);
        toppingPrices.put("mushroom", 0.75);
    }

    /**
     * Holds the current coupon discount values
     */
    static Map<String, Double> coupons;
    static {
        coupons = new HashMap<>();
        coupons.put("NIFTYFIFTY", 0.50);
        coupons.put("YOLO5", 0.05);
        coupons.put("ASDHN345FDFGN", 0.75);
    }

    /**
     * The order queue for a local store
     */
    ArrayList<Food> orders;

    /**
     * Constructor for a new location
     */
    HfxDonairExpress() {
        orders = new ArrayList<Food>();
    }

    /**
     * The HfxDonairExpress ordering system. Orders come in through this method in an automated way.
     * Upon receiving an order, our kitchen is notified and gets to work making the best donair or pizza
     * you could ask for immediately. Since our staff needs to be paid, we charge a small fee for the service.
     * Really, it's just to keep the staff alive. Everyone here just wants the customers to have the best
     * culinary experience possible, regardless of our own well-being.
     *
     * @param type The type of order. Either 0 (donair) or 1 (pizza)
     * @param size The size desired. Either 0 (small), 1 (medium), or 2 (large)
     * @param toppings A list of the toppings desired
     * @param coupon Any coupon code. Empty or null if no coupon code is given.
     * @return The price of the order as a double precision float.
     * @throws Exception if any error occurred.
     */
    public double order(int type, int size, ArrayList<String> toppings, String coupon) throws Exception {
        if (type < 0 || type > 1 || size < 0 || size > 2) {
            throw new Exception();
        }

        // Calculate the base price
        Double price = basePrices[type][size];

        // Add the price of toppings
        if (type == 1) {
            for (String topping : toppings) {
                if (toppingPrices.get(topping) == null) {
                    throw new Exception();
                } else {
                    price += toppingPrices.get(topping);
                }
            }
        }

        // Calculate the final price
        if (coupon != null && coupons.get(coupon) != null) {
            price *= (1 - coupons.get(coupon));
        }

        return price;
    }

    /**
     * The kitchen will make a tasty morsel of local cuisine
     * @param type The type of order
     * @param size The size
     * @param toppings The toppings
     * @return An integer order number
     */
    public int makeOrder(int type, int size, ArrayList<String> toppings) {
        int currentOrderNo = orders.size();

        orders.add(new Garbage());

        return currentOrderNo;
    }

    /**
     * Private food class. Nothing to see here.
     */
    private static class Garbage implements Food {
        public String describe() {
            return "Mystery food. You don't want to eat this.";
        }
    }
}
