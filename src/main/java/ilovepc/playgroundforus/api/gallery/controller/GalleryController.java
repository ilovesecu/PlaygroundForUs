package ilovepc.playgroundforus.api.gallery.controller;

import ilovepc.playgroundforus.api.gallery.vo.GalleryImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/api/gallery")
public class GalleryController {

    @PostMapping(value = "/post")
    public void onCreate(@RequestBody GalleryImage galleryImage){
        log.error("{}",galleryImage);
    }

    @GetMapping(value = "/images")
    public ResponseEntity<?> getImages(@RequestParam(value = "page")int page){
        log.error("page==>{}",page);
        Map<String,Object> dummy = new HashMap<>();
        dummy.put("id",1);
        dummy.put("url","/asdfsfsf/asdfsdf");
        dummy.put("content","hi my name is ~");
        return new ResponseEntity<Object>(dummy,HttpStatus.OK);
    }

    @PostMapping(value = "/upload_image")
    public void onUploadGalleryImage(@RequestParam(value = "images") MultipartFile[] images){
        log.error("{}",images[0].getOriginalFilename());


    }
}
