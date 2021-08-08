import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("HfxDonairExpress Unit Tests")
public class UnitTests {

    @Test
    @DisplayName("order method: Order a small Donair")
    void orderASmallDonair() {
        final HfxDonairExpress hfxDonairExpress = new HfxDonairExpress();
        final int type = 0;
        final int size = 0;
        final ArrayList<String> toppings = new ArrayList<>();
        final String coupon = "";
        final String finalAmount = "5.00";
        assertAll(() -> assertEquals(finalAmount, String.format("%.2f", hfxDonairExpress.order(type, size, toppings, coupon))));
        assertAll(() -> assertEquals(5.00, hfxDonairExpress.order(type, size, toppings, coupon)));
    }

    @Test
    @DisplayName("order method: Order a large Donair with coupon code YOLO5")
    void orderALargeDonairWithCouponCodeYOLO5() {
        final HfxDonairExpress hfxDonairExpress = new HfxDonairExpress();
        final int type = 0;
        final int size = 2;
        final ArrayList<String> toppings = new ArrayList<>();
        final String coupon = "YOLO5";
        final String finalAmount = "6.65";
        assertAll(() -> assertEquals(finalAmount, String.format("%.2f", hfxDonairExpress.order(type, size, toppings, coupon))));
        assertAll(() -> assertEquals(6.6499999999999995, hfxDonairExpress.order(type, size, toppings, coupon)));
    }

    @Test
    @DisplayName("order method: Order a medium Pizza with Pepperoni and Mushrooms")
    void orderAMediumPizzaWithPepperoniAndMushrooms() {
        final HfxDonairExpress hfxDonairExpress = new HfxDonairExpress();
        final int type = 1;
        final int size = 1;
        final ArrayList<String> toppings = new ArrayList<>();
        toppings.add("pepperoni");
        toppings.add("mushroom");
        final String coupon = "";
        final String finalAmount = "10.75";
        assertAll(() -> assertEquals(finalAmount, String.format("%.2f", hfxDonairExpress.order(type, size, toppings, coupon))));
        assertAll(() -> assertEquals(10.75, hfxDonairExpress.order(type, size, toppings, coupon)));
    }

    @Test
    @DisplayName("makeOrder: Make 1 order")
    void makeOneOrder() {
        final HfxDonairExpress hfxDonairExpress = new HfxDonairExpress();
        final int type = 0;
        final int size = 1;
        final ArrayList<String> toppings = new ArrayList<>();
        assertAll(() -> assertEquals(0, hfxDonairExpress.makeOrder(type, size, toppings)));
    }

    @Test
    @DisplayName("makeOrder: Make 2 orders in a row")
    void makeTwoOrdersInARow() {
        final HfxDonairExpress hfxDonairExpress = new HfxDonairExpress();
        final int type1 = 0;
        final int size1 = 1;
        final ArrayList<String> toppings1 = new ArrayList<>();
        assertAll(() -> assertEquals(0, hfxDonairExpress.makeOrder(type1, size1, toppings1)));
        final int type2 = 1;
        final int size2 = 2;
        final ArrayList<String> toppings2 = new ArrayList<>();
        toppings2.add("pepperoni");
        assertAll(() -> assertEquals(1, hfxDonairExpress.makeOrder(type2, size2, toppings2)));
    }
}
