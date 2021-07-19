package com.psc.sample.r101.service;

import com.psc.sample.r101.domain.User;
import com.psc.sample.r101.domain.UserRepository;
import com.psc.sample.r101.dto.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 서비스 구분을 위한 작업 (구글: google, 네이버: naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // Oauth2 마지막에 필요한 키 (구글: sub, 네이버: response)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String email;
        Map<String, Object> response = oAuth2User.getAttributes();

        // 인증 서비스마다의 필드 값을 가져오는 방법이 다름
        // 모듈화가 필요함
        if(registrationId.equals("naver")){
            HashMap<String,Object> hashMap = (HashMap<String, Object>) response.get("response");
            email = (String) hashMap.get("email");
        }else if(registrationId.equals("google")){
            email = (String) response.get("email");
        }else{
            throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
        }

        User user;
        // 이미 가입 된 사람인지 구분
        Optional<User> userOptional = userRepository.findByEmail(email);

        // 이미 가입 된 사람이다.
        if(userOptional.isPresent()){
            user = userOptional.get();

        // 신규 가입이며 기본 정보만 저장한 후 로그인 후에 별도 처리하도록 함
        }else{
            user = new User();
            user.setEmail(email);
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }
        httpSession.setAttribute("user", user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()))
                ,oAuth2User.getAttributes()
                ,userNameAttributeName );
    }
}
