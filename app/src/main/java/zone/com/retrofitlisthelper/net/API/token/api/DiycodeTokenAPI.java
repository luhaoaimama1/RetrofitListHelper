package zone.com.retrofitlisthelper.net.API.token.api;

import zone.com.retrofit.callwrapper.DialogCall;
import zone.com.retrofitlisthelper.net.API.token.bean.Token;

/**
 * [2017] by Zone
 */

public interface DiycodeTokenAPI {

    DialogCall<Token> getToken(String grant_type, String username, String password);

    DialogCall<Token> refreshToken(String refresh_token);
}