package vn.nhd.flightagency.account.domain;


public enum Provider {
    LOCAL, GOOGLE, FACEBOOK, GITHUB;

    public static Provider getProvider(String provider) {
        if("google".equalsIgnoreCase(provider))
            return Provider.GOOGLE;
        else if("facebook".equalsIgnoreCase(provider))
            return Provider.FACEBOOK;
        else if("github".equalsIgnoreCase(provider))
            return Provider.GITHUB;
        else
            return Provider.LOCAL;
    }
}
