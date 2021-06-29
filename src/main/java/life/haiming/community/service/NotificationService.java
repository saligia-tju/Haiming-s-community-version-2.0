package life.haiming.community.service;

import life.haiming.community.dto.NotificationDTO;
import life.haiming.community.dto.PaginationDTO;
import life.haiming.community.enums.NotificationStatusEnum;
import life.haiming.community.enums.NotificationTypeEnum;
import life.haiming.community.exception.CustomizeErrorCode;
import life.haiming.community.exception.CustomizeException;
import life.haiming.community.mapper.NotificationMapper;
import life.haiming.community.mapper.UserMapper;
import life.haiming.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired(required = false)
    private NotificationMapper notificationMapper;

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().
                andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(new NotificationExample());

        //计算页数，并根据当前页数选择显示什么标签
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }


        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        Integer offset = size * (page - 1);

        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.size() == 0) {
            return paginationDTO;
        }
/*        //得到所有去重后的user id
        List<Integer> userIds = notifications.stream().map(notify -> notify.getNotifier()).distinct().collect(Collectors.toList());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds); //按id查找user
        List<User> users = userMapper.selectByExample(userExample);
        //为了快速查找user，将users使用stream流转换为map
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));*/

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    //未读消息数
    public Long unreadCount(Integer userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(0);
        return notificationMapper.countByExample(notificationExample);

    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);

        // 若通知为空
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }

        // 若接收者不是当前用户，返回校验
        if(!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        // 上述两项校验成功后，需要将status改为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKeySelective(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

        return notificationDTO;
    }
}
