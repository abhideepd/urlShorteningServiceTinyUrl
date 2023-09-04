package com.urlShorteningService.urlShorteningService.controller;

import com.urlShorteningService.urlShorteningService.Service.UrlService;
import com.urlShorteningService.urlShorteningService.model.Url;
import com.urlShorteningService.urlShorteningService.model.UrlDto;
import com.urlShorteningService.urlShorteningService.model.UrlErrorResponseDto;
import com.urlShorteningService.urlShorteningService.model.UrlResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto){
        Url urlToRet=urlService.generateShortLink(urlDto);
        if(urlToRet!=null){
            UrlResponseDto urlResponseDto=new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }
        UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
        urlErrorResponseDto.setError("404");
        urlErrorResponseDto.setStatus("There was an error, please try again");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOrignialUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(shortLink)){
            UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid Url");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }
        Url urlToRet=urlService.getEncodedUrl(shortLink);
        if(urlToRet==null){
            UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url doesn't exist");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }
        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())){
            UrlErrorResponseDto urlErrorResponseDto=new UrlErrorResponseDto();
            urlErrorResponseDto.setError("expired");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }
        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }
}
