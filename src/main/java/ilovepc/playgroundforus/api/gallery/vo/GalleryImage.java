package ilovepc.playgroundforus.api.gallery.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class GalleryImage {
    private int id;
    private String fileName;
    private String content;
    private String title;
    @JsonIgnore
    private String fileType;
    @JsonIgnore
    private String originFileName;
}
