package rdm.example.reggie.common;

/**
 * @Description 自定义业务异常
 * @Author rdm
 * @data 2022/5/25 - 21:57
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
