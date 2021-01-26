package com.boyue.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie 工具类
 * @author Sun
 */
@Slf4j
public final class CookieUtils {

    /**
     * 得到Cookie的值, 不编码
     *
     * @param request 请求
     * @param cookieName cookie名
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, null);
    }

    /**
     * 得到Cookie的值,
     *
     * @param request 请求
     * @param cookieName cookie名
     * @return string
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String charset) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (charset != null && charset.length() > 0) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), charset);
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Cookie Decode Error.", e);
        }
        return retValue;
    }

    public static CookieBuilder newCookieBuilder() {
        return new CookieBuilder();
    }

    public static void deleteCookie(String cookieName, String domain,HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    public static class CookieBuilder {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private Integer maxAge;
        private String charset;
        private boolean httpOnly = false;
        private String domain;
        private String path = "/";
        private String name;
        private String value;

        public CookieBuilder() {

        }

        public CookieBuilder request(HttpServletRequest request) {
            this.request = request;
            return this;
        }
        public CookieBuilder response(HttpServletResponse response) {
            this.response = response;
            return this;
        }

        public CookieBuilder maxAge(int maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        public CookieBuilder charset(String charset) {
            this.charset = charset;
            return this;
        }
        public CookieBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }
        public CookieBuilder path(String path) {
            this.path = path;
            return this;
        }
        public CookieBuilder value(String value) {
            this.value = value;
            return this;
        }
        public CookieBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CookieBuilder httpOnly(boolean httpOnly) {
            this.httpOnly = httpOnly;
            return this;
        }

        public void build() {
            try {
                if (StringUtils.isBlank(charset)) {
                    charset = "utf-8";
                }
                if(StringUtils.isBlank(name)||StringUtils.isBlank(value)){
                    throw new RuntimeException("cookie名称和值不能为空！");
                }
                if (StringUtils.isNotBlank(charset)) {
                    value = URLEncoder.encode(value, charset);
                }
                Cookie cookie = new Cookie(name, value);
                if (maxAge != null && maxAge >= 0)
                    cookie.setMaxAge(maxAge);

                if(StringUtils.isNotBlank(domain)){
                    cookie.setDomain(domain);
                }else if (null != request) {
                    // 设置域名的cookie
                    cookie.setDomain(getDomainName(request));
                }
                // 设置path
                cookie.setPath("/");
                if(StringUtils.isNotBlank(path)){
                    cookie.setPath(path);
                }
                cookie.setHttpOnly(httpOnly);
                response.addCookie(cookie);
            } catch (Exception e) {
                log.error("Cookie Encode Error.", e);
            }
        }

        /**
         * 得到cookie的域名
         */
        private String getDomainName(HttpServletRequest request) {
            String domainName = null;

            String serverName = request.getRequestURL().toString();
            if (serverName == null || serverName.equals("")) {
                domainName = "";
            } else {
                serverName = serverName.toLowerCase();
                serverName = serverName.substring(7);
                final int end = serverName.indexOf("/");
                serverName = serverName.substring(0, end);
                final String[] domains = serverName.split("\\.");
                int len = domains.length;
                if (len > 3) {
                    // www.xxx.com.cn
                    domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
                } else if (len <= 3 && len > 1) {
                    // xxx.com or xxx.cn
                    domainName = domains[len - 2] + "." + domains[len - 1];
                } else {
                    domainName = serverName;
                }
            }

            if (domainName != null && domainName.indexOf(":") > 0) {
                String[] ary = domainName.split("\\:");
                domainName = ary[0];
            }
            return domainName;
        }
    }
}
