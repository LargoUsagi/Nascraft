package me.bounser.nascraft.market.unit;


import me.bounser.nascraft.NascraftTestBase;
import me.bounser.nascraft.tools.Config;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class PriceTest extends NascraftTestBase {



    @Test
    void value_increases_with_purchase() {
        Price testPrice = new Price(10.0f, 5, 0.5f, 1, 1, 2);
        float initialPrice = testPrice.getValue();

        testPrice.changeStock(100);

        float newPrice = testPrice.getValue();

        assertTrue(initialPrice > newPrice);
    }

    @Test
    void value_decreases_with_sale() {
        Price testPrice = new Price(10.0f, 5, 0.5f, 1, 1, 2);
        float initialPrice = testPrice.getValue();

        testPrice.changeStock(-100);

        float newPrice = testPrice.getValue();

        assertTrue(initialPrice < newPrice);
    }

    @Test
    void price_clamps_work() {


        float min = Config.getInstance().getLimits()[0];
        float max = Config.getInstance().getLimits()[1];
        Price testPrice = new Price(1.0f, 0, 0.5f, 0, 1, 100);

        testPrice.changeStock(1000000);


        assertEquals(min, testPrice.getValue());

        testPrice.changeStock(-430496728);
        testPrice.changeStock(-430496728);

        assertEquals(max, testPrice.getValue());


    }



}