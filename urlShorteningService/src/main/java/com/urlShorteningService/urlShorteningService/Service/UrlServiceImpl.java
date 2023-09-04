package com.urlShorteningService.urlShorteningService.Service;

import com.google.common.hash.Hashing;
import com.urlShorteningService.urlShorteningService.model.Url;
import com.urlShorteningService.urlShorteningService.model.UrlDto;
import com.urlShorteningService.urlShorteningService.repository.UrlRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlServiceImpl implements UrlService{

    @Autowired
    private UrlRepository urlRepository;
    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(StringUtils.isNotEmpty(urlDto.getUrl())){
            String encodedUrl=encodeUrl(urlDto.getUrl());
            Url urlToPersist=new Url();
            urlToPersist.setCreateDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
            Url urlToRet=persistShortLink(urlToPersist);

            if(urlToRet!=null){
                return urlToRet;
            }
            else{
                return null;
            }
        }
        return null;
    }
    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate){
        if(StringUtils.isBlank(expirationDate)){
            return creationDate.plusSeconds(60);
        }
        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }
    private String encodeUrl(String url) {
        String encodeUrl="";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl= Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
        return encodeUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncodedUrl(String url) {
        return urlRepository.findByShortLink(url);
    }

    @Override
    public void deleteShortLink(Url url) {

    }
}
