package unknownnote.unknownnoteserver.dto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@RequiredArgsConstructor
public class EssayDTO {
    @JsonProperty("eTitle")
    private String eTitle;
    @JsonProperty("eContent")
    private String eContent;
    @JsonProperty("openable")
    private int openable;
    @JsonProperty("eLikes")
    private int eLikes;
    @JsonProperty("eCategory")
    private String eCategory;
}
