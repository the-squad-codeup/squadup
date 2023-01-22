package pro.squadup.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

// this controller returns image view when url path goes to image
// used for images in database we need access to in css
@Controller
public class ImageController {

    @GetMapping(
            value = "/background-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getBackgroundImageWithMediaType() throws IOException {
        InputStream in = getClass().getResourceAsStream("/images/gitsquares6.png");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/edit-icon",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getEditIconImageWithMediaType() throws IOException {
        InputStream in = getClass().getResourceAsStream("/public/Icons/edit.png");
        return IOUtils.toByteArray(in);
    }
}
