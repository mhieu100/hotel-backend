package com.application.vaccine_system.model.response;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDTO {
    private String fileName;
    private Instant uploadedAt;
}
