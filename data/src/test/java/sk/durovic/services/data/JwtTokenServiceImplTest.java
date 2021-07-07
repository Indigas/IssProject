package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.JwtToken;
import sk.durovic.repositories.JwtTokenRepository;
import sk.durovic.services.JwtTokenService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceImplTest {
    @Mock
    private JwtTokenRepository jwtTokenRepository;

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenServiceImpl(jwtTokenRepository);
    }

    @Test
    void findByToken() {
        Mockito.when(jwtTokenRepository.findByToken(Mockito.any()))
                .thenReturn(Optional.of(new JwtToken("token", 0L)));

        boolean found = jwtTokenService.findByToken("token").isPresent();

        assertTrue(found);
        Mockito.verify(jwtTokenRepository, Mockito.atMostOnce()).findByToken(Mockito.any());
    }

    @Test
    void isValid() {
        Mockito.when(jwtTokenRepository.findByToken(Mockito.any()))
                .thenReturn(Optional.empty());

        boolean valid = jwtTokenService.isValid("token");

        assertFalse(valid);
        Mockito.verify(jwtTokenRepository, Mockito.atMostOnce()).findByToken(Mockito.any());
    }

    @Test
    void isNotValid(){
        Mockito.when(jwtTokenRepository.findByToken(Mockito.any()))
                .thenReturn(Optional.of(new JwtToken("token",0L)));

        boolean valid = jwtTokenService.isValid("token");

        assertTrue(valid);
        Mockito.verify(jwtTokenRepository, Mockito.atMostOnce()).findByToken(Mockito.any());
    }

    @Test
    void saved(){
        JwtToken jwt = new JwtToken("token",0L);
        Mockito.when(jwtTokenRepository.save(Mockito.any()))
                .thenReturn(jwt);

        JwtToken saved = jwtTokenService.save(jwt);

        assertThat(saved, Matchers.is(jwt));
        Mockito.verify(jwtTokenRepository, Mockito.atMostOnce()).save(jwt);
    }

    @Test
    void createTokenSuccessfull(){
        Mockito.doNothing().when(jwtTokenRepository).deleteAllByUserId(Mockito.any());
        Mockito.when(jwtTokenRepository.save(Mockito.any()))
                .thenReturn(new JwtToken("token",0L));

        JwtToken created = jwtTokenService.createToken("token", 0L);

        Mockito.verify(jwtTokenRepository, Mockito.atMostOnce()).deleteAllByUserId(0L);
        Mockito.verify(jwtTokenRepository, Mockito.atMostOnce()).save(Mockito.any());

        assertThat(created, Matchers.hasProperty("token", Matchers.is("token")));
        assertThat(created, Matchers.hasProperty("userId", Matchers.is(0L)));

    }
}