package no.vigoiks.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Image {

    private String base64Image;
    private String mimeType;
}
