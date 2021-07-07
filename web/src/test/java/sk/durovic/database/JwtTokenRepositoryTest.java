package sk.durovic.database;

import config.JwtTokenTestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import sk.durovic.model.JwtToken;
import sk.durovic.repositories.JwtTokenRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JwtTokenRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    private JwtToken jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = new JwtToken("token", 0L);
    }

    @Test
    void saveToken(){
        JwtToken saved = jwtTokenRepository.save(jwtToken);

        JwtToken found = testEntityManager.find(JwtToken.class, saved.getId());

        assertThat(found, Matchers.samePropertyValuesAs(saved, "id"));
    }

    @Test
    void foundByTokenString(){
        testEntityManager.persist(jwtToken);

        JwtToken found = jwtTokenRepository.findByToken(jwtToken.getToken()).orElse(null);

        assertThat(found, Matchers.notNullValue());
        assertThat(found, Matchers.samePropertyValuesAs(jwtToken, "id"));
    }

    @Test
    void findAllByUserId(){
        testEntityManager.persist(new JwtToken("token",1L));
        testEntityManager.persist(new JwtToken("test",1L));
        testEntityManager.persist(new JwtToken("company",2L));

        jwtTokenRepository.deleteAllByUserId(1L);

        JwtToken found = testEntityManager.find(JwtToken.class, 1L);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void findAllByUserIdWithNotExistUserId(){
        testEntityManager.persist(new JwtToken("token",1L));
        testEntityManager.persist(new JwtToken("token",1L));

        jwtTokenRepository.deleteAllByUserId(3L);

        JwtToken found = testEntityManager.find(JwtToken.class, 1L);

        assertThat(found, Matchers.nullValue());
     }
}
