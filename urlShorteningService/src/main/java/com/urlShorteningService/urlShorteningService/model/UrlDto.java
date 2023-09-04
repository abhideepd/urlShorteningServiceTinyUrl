package com.urlShorteningService.urlShorteningService.model;

public class UrlDto {
    // expected fields which we are expecting from users in the url dto
    private String url;
    private String expirationDate; // optional

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UrlDto(String url, String expirationDate) {
        this.url = url;
        this.expirationDate = expirationDate;
    }

    public UrlDto() {
    }
}
