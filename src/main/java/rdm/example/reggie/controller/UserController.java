package rdm.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rdm.example.reggie.common.R;
import rdm.example.reggie.entity.User;
import rdm.example.reggie.service.UserService;
import rdm.example.reggie.utils.SMSUtils;
import rdm.example.reggie.utils.ValidateCodeUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description
 * @Author rdm
 * @data 2022/5/30 - 10:36
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 模拟手机验证码发送，实际上前端自动生成
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user , HttpSession session){
        //获取需要登录的手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云提供的短信服务api完成发送短信
            SMSUtils.sendMessage("", "", phone, code);
            //验证码存入session
            session.setAttribute(phone, code);
        }
        return null;
    }

    /**
     * 模拟用户短信登录(实际上服务端无需校验验证码)
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map , HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        // 取出map中的验证码。(省略)
        // 判断验证码是否与sesstion中的验证码相等，（省略）
        // 如果相等,则登录成功。（省略）
        // 进一步判断手机号是否为新用户，
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);
        //新用户自动注册
        if(user == null){
            User newUser = new User();
            newUser.setPhone(phone);
            userService.save(newUser);
            //id存入session中,方便过滤器筛选
            session.setAttribute("user", newUser.getId());
            return R.success(newUser);
        }
        session.setAttribute("user", user.getId());
        return R.success(user);
        //登录不成功返回失败（省略）
    }
}
