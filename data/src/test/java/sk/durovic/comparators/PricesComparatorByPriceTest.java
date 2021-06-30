package sk.durovic.comparators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.durovic.model.Prices;

import static org.junit.jupiter.api.Assertions.*;

class PricesComparatorByPriceTest {

    private PricesComparatorByPrice priceComp;
    private Prices bigger;
    private Prices lower;

    @BeforeEach
    void setUp() {
        priceComp = new PricesComparatorByPrice();

        bigger = Prices.builder(null).addPrice(10).build();
        lower = Prices.builder(null).addPrice(5).build();
    }

    @Test
    void compareBigger() {
        int i = priceComp.compare(bigger, lower);

        assertEquals(1, i);
    }

    @Test
    void compareLower() {
        int i = priceComp.compare(lower, bigger);

        assertEquals(-1, i);
    }

    @Test
    void compareSame() {
        int i = priceComp.compare(lower, Prices.builder(null).addPrice(5).build());

        assertEquals(0, i);
    }
}