package pro.squadup.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    @GetMapping(
            value = "/background-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType() throws IOException {
        InputStream in = getClass().getResourceAsStream("/images/gitsquares6.png");
        return IOUtils.toByteArray(in);
    }
}
