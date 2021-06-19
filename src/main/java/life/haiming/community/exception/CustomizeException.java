package life.haiming.community.exception;

//如果不继承RuntimeException，在QuestionController中会直接抛出异常，除非加上try catch.
//我希望在调用时异常不对程序有影响，仅仅在ControllerAdvice中try catch
public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
