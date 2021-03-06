package com.example.timeattack.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AmazonS3Component {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
}