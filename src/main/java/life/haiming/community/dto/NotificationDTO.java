package life.haiming.community.dto;

import life.haiming.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Long outerId;
    private Integer status;
    private Integer notifier; //发起通知的人
    private String notifierName; //发起通知的人
    private String outerTitle;
    private String typeName;
    private Integer type;

}
