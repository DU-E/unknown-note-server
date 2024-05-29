package unknownnote.unknownnoteserver.dto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EssayDTO {
    private String eTitle; // 에세이 제목
    private String eContent; // 에세이 내용
    private int openable; // 에세이 공개 여부
    private int eLikes;// 에세이 좋아요
    private String eCategory; // 에세이 카테고리
}