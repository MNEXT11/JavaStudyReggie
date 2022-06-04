package rdm.example.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rdm.example.reggie.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description 文件上传啊下载处理
 * @Author rdm
 * @data 2022/5/26 - 17:28
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    //读取配置文件中默认路径
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    //参数名file必须和前端提交的表单属性保持一致
    public R<String> upload(MultipartFile file){
        //此时的file是一个临时文件，需要转存到指定位置
        log.info(file.toString());

        //截取图片后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名称相同被覆盖
        String fileName = UUID.randomUUID().toString() + suffix;  //sfjsjf.jpg

        //创建目录对象
        File dir = new File(basePath);
        //判断目录是否存在,不存在则创建
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            //将文件转存
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回转存的文件名
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name , HttpServletResponse response){
        FileInputStream fis = null;
        ServletOutputStream os = null;
        try {
            //输入流读取文件内容
            fis = new FileInputStream(new File(basePath + name));
            //输出流写回到浏览器
            os = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1){
                os.write(bytes,0,len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
