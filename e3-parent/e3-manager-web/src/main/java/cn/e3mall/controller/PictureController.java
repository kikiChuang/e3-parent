package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传Controller
 */

@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String image_server_url;

    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {
        //把图片上传到图片服务器
        //得到一个图片地址和文件名
        //补充为完整的url
        //封装到map中返回
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            String originame = uploadFile.getOriginalFilename();
            String extName = originame.substring(originame.lastIndexOf(".") + 1);
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url = image_server_url + url;
            Map result = new HashMap();
            result.put("error", 0);
            result.put("url", url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map result = new HashMap();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }

    }
}
