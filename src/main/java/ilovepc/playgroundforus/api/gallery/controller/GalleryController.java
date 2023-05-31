package ilovepc.playgroundforus.api.gallery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/api/gallery")
public class GalleryController {

    @GetMapping("/images")
    public ResponseEntity<?> getImages(@RequestParam(value = "page")int page){
        log.error("page==>{}",page);
        Map<String,Object> dummy = new HashMap<>();
        dummy.put("id",1);
        dummy.put("url","/asdfsfsf/asdfsdf");
        dummy.put("content","hi my name is ~");
        return new ResponseEntity<Object>(dummy,HttpStatus.OK);
    }
}
