package backend.LameQueue;

import backend.MediaFactory.Lame;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class LameQueueItem {

    private Lame encoder;
    private String title;
    private String status;

    LameQueueItem(Lame encoder, String title, String status) {
        this.encoder = encoder;
        this.title = title;
        this.status = status;
    }
}
