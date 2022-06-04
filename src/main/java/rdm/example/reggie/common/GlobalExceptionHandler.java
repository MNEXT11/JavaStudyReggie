package rdm.example.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Description 全局异常处理
 * @Author rdm
 * @data 2022/5/24 - 20:04
 */
//指定拦截那些controller
@ControllerAdvice(annotations = {RestController.class, Controller.class})
//返回json数据
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法(新增员工用户名重复)，抛出此异常统一拦截处理
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 异常处理方法(删除有关联无法删除)，抛出此异常统一拦截处理
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}
