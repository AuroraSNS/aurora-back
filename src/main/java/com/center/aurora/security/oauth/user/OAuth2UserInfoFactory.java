package com.center.aurora.security.oauth.user;


import com.center.aurora.domain.user.AuthProvider;
import com.center.aurora.exception.OAuth2AuthenticationProcessingException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuthUSerInfo(String registrationId, Map<String, Object> attributes){
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.naver.toString())) {
            String response = attributes.get("response").toString();
            String[] responses = response.substring(1, response.length() - 1).split(", ");

            Map<String, Object> realAttributes = new HashMap<>();
            for (String response1 : responses) {
                String[] tmp = response1.split("=");
                realAttributes.put(tmp[0], tmp[1]);
            }

            return new NaverOAuth2UserInfo(realAttributes);
        }  else{
            throw new OAuth2AuthenticationProcessingException(registrationId + " 로그인은 지원하지 않습니다.");
        }
    }
}
