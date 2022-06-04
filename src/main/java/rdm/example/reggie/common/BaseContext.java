package rdm.example.reggie.common;

/**
 * @Description 给予thredlocal封装的工具类，用于保存和获取当前登录用户的id
 *              作用范围是每一次请求的那个线程
 * @Author rdm
 * @data 2022/5/25 - 15:29
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
