package nico.user;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class UserManager {

    private static Log log = LogFactory.getLog(UserManager.class);

    private CookieStore login(String mail, String password) {

        HttpClientContext context = new HttpClientContext();
        CookieStore cookieStore = null;
        CloseableHttpClient client = HttpClients.createDefault();

        String url = "https://secure.nicovideo.jp/secure/login?site=niconico";
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity("next_url=&mail=" + mail + "&password=" + password, "UTF-8");
        stringEntity.setChunked(false);
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse;

        try {

            httpResponse = client.execute(httpPost, context);
        } catch (IOException e) {
            log.error("login failed.", e);
            throw new IllegalStateException(e);
        } finally {

            try {
                client.close();
            } catch (IOException e) {
                log.error("connection close failed. ", e);
                throw new IllegalStateException(e);
            }
        }

        if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
            log.warn(httpResponse.getStatusLine().getStatusCode());
        }

        cookieStore = context.getCookieStore();

        if (cookieStore == null) {
            log.error("could not get any cookies.");
            throw new IllegalStateException("could not get any cookies.");
        }

        String responseBody;
        try {
            responseBody = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (ParseException | IOException e) {
            log.error("response parse failed.", e);
            throw new IllegalStateException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug(responseBody);
            for (Cookie cookie : cookieStore.getCookies()) {
                log.debug(cookie.getName() + ":" + cookie.getValue());
            }
        }

        log.info("login success. mail: " + mail);
        return cookieStore;
    }
}
