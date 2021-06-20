package life.haiming.community.dto;

import life.haiming.community.exception.CustomizeErrorCode;
import life.haiming.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {
    private Integer code; //像HTTP码一样，告诉前端要如何处理
    private String message;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }
}
