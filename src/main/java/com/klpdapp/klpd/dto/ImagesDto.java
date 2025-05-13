package com.klpdapp.klpd.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagesDto {
    private int pid;
    private MultipartFile imgURL;  
    private Boolean isPrimary;
}
