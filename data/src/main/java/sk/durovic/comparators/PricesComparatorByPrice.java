package sk.durovic.comparators;

import sk.durovic.model.Prices;

import java.util.Comparator;

public class PricesComparatorByPrice implements Comparator<Prices> {
    @Override
    public int compare(Prices prices, Prices t1) {
        return prices.getPrice().compareTo(t1.getPrice());
    }
}
