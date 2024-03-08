package unknownnote.unknownnoteserver.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Monthly_emo {
    private int happy = 0;
    private int love = 0;
    private int anticipation = 0;
    private int thank = 0;
    private int sad = 0;
    private int anger = 0;
    private int fear = 0;
    private int regret = 0;

    public void incrementEmotion(String emotion) {
        switch (emotion.toLowerCase()) {
            case "happy":
                this.happy++;
                break;
            case "love":
                this.love++;
                break;
            case "anticipation":
                this.anticipation++;
                break;
            case "thank":
                this.thank++;
                break;
            case "sad":
                this.sad++;
                break;
            case "anger":
                this.anger++;
                break;
            case "fear":
                this.fear++;
                break;
            case "regret":
                this.regret++;
                break;
            default:
                break;
        }
    }
}
