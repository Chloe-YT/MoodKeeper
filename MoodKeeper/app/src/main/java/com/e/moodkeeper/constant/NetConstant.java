package com.e.moodkeeper.constant;

public class NetConstant {
    public static final String baseService = "http://39u507s509.wicp.vip:10579";

    private static final String getOtpCodeURL     = "/user/getOtp";
    private static final String loginURL          = "/user/login";
    private static final String registerURL       = "/user/register";

    private static final String updateMessageURL = "/user/update";

    private static final String createItemURL     = "/item/create";
    private static final String getItemListURL    = "/item/list";
    private static final String submitOrderURL    = "/order/createorder";

    private static final String getNewsListURL = "/news/list";

    private static final String getNewsByIdURL = "/news/detail/id?id=";

    private static final String getNewsByTitleURL = "/news/detail/title?title=";

    public static String getGetOtpCodeURL() {
        return getOtpCodeURL;
    }

    public static String getLoginURL() {
        return loginURL;
    }

    public static String getRegisterURL() {
        return registerURL;
    }

    public static String getUpdateMessageURL() {
        return updateMessageURL;
    }

    public static String getCreateItemURL() {
        return createItemURL;
    }

    public static String getGetItemListURL() {
        return getItemListURL;
    }

    public static String getSubmitOrderURL() {
        return submitOrderURL;
    }

    public static String getNewsListURL() {
        return getNewsListURL;
    }

    public static String getNewsByIdURL() {
        return getNewsByIdURL;
    }

    public static String getNewsByTitleURL() {
        return getNewsByTitleURL;
    }
}
