package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Prices;
import sk.durovic.repositories.PricesRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class PricesServiceImplTest {
    @Mock
    private PricesRepository pricesRepository;

    @InjectMocks
    private PricesServiceImpl pricesService;

    private Prices prices;

    @BeforeEach
    void setUp() {
        prices = Prices.builder(null).addPrice(10).addDay(5).build();
    }

    @Test
    void findAll() {
        List<Prices> list = List.of(prices, Prices.builder(null).build());
        Mockito.when(pricesRepository.findAll()).thenReturn(list);

        Set<Prices> found = pricesService.findAll();

        Mockito.verify(pricesRepository, Mockito.atMostOnce()).findAll();

        assertThat(found, Matchers.hasSize(2));
        assertThat(found, Matchers.hasItem(prices));
    }

    @Test
    void findById() {
        Mockito.when(pricesRepository.findById(1L)).thenReturn(Optional.of(prices));

        Prices found = pricesService.findById(1L);

        Mockito.verify(pricesRepository, Mockito.atMostOnce()).findById(1L);
        assertThat(found, Matchers.notNullValue());
        assertThat(found, Matchers.is(prices));
    }

    @Test
    void save() {
        Mockito.when(pricesRepository.save(prices)).thenReturn(prices);

        Prices saved = pricesService.save(prices);

        Mockito.verify(pricesRepository, Mockito.atMostOnce()).save(prices);
        assertThat(saved, Matchers.is(prices));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(pricesRepository).delete(prices);

        pricesService.delete(prices);

        Mockito.verify(pricesRepository, Mockito.atMostOnce()).delete(prices);
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(pricesRepository).deleteById(1L);

        pricesService.deleteById(1L);

        Mockito.verify(pricesRepository, Mockito.atMostOnce()).deleteById(1L);
    }

    @Test
    void findByCarId() {
        Mockito.when(pricesRepository.findByCarIdOrderByDaysAsc(1L))
                .thenReturn(Optional.of(List.of(prices)));

        List<Prices> list = pricesService.findByCarId(1L).orElse(null);

        Mockito.verify(pricesRepository, Mockito.atMostOnce()).findByCarIdOrderByDaysAsc(1L);
        assertThat(list, Matchers.notNullValue());
        assertThat(list, Matchers.hasSize(1));
        assertThat(list, Matchers.contains(prices));
    }
}