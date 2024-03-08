package unknownnote.unknownnoteserver.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unknownnote.unknownnoteserver.repository.EssayRepository;

@Component
@Setter
@Getter
public class Essay_cnt {
    private int essay_cnt;

    @Autowired
    private EssayRepository essayRepository;

}
