package com.app.external.oauth.service;

import com.app.external.oauth.model.OauthAttributes;

public interface SocialLoginApiService {

    OauthAttributes getUserInfo(String accessToken);

}
