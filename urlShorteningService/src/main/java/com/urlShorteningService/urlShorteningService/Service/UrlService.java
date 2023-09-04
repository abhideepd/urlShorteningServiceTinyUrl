package com.urlShorteningService.urlShorteningService.Service;

import com.urlShorteningService.urlShorteningService.model.Url;
import com.urlShorteningService.urlShorteningService.model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {
    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
}
