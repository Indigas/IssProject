package sk.durovic.services.data;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.durovic.model.JwtToken;
import sk.durovic.repositories.JwtTokenRepository;
import sk.durovic.services.JwtTokenService;

import java.util.Optional;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private JwtTokenRepository jwtTokenRepository;

    public JwtTokenServiceImpl(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    public Optional<JwtToken> findByToken(String token) {
        return jwtTokenRepository.findByToken(token);
    }

    @Override
    public boolean isValid(String token){
        return findByToken(token).isPresent();
    }

    @Override
    public JwtToken save(JwtToken token) {
        return jwtTokenRepository.save(token);
    }

    @Override
    @Transactional
    public JwtToken createToken(String token, Long userId){
        jwtTokenRepository.deleteAllByUserId(userId);

        return this.save(new JwtToken(token, userId));
    }


}
