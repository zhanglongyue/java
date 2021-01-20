package priv.yue.sboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import priv.yue.sboot.common.RestResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author ZhangLongYue
 * @since 2021/1/14 16:29
 */
@RestController
@Slf4j
public class UploadController {

    private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png", "image/gif", "image/bmp");

    @PostMapping("/uploadImage")
    public RestResponse<Object> uploadImage(@RequestParam("file") MultipartFile file){
        String staticPath = getStaticPath("static/images/avatar/");
        try {
            staticPath = URLDecoder.decode(staticPath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID() + "." + originalFilename.split("\\.")[1];

        // 校验文件的类型
        String contentType = file.getContentType();
        if (!IMAGE_TYPES.contains(contentType)){
            // 文件类型不合法，直接返回null
            return RestResponse.fail("文件类型不合法：{}", originalFilename);
        }

        try {
            // 校验文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                RestResponse.fail("文件内容不合法：{}", originalFilename);
            }

            String url_path = "images/avatar" + File.separator + filename;
            // 保存到服务器
            file.transferTo(new File(staticPath + File.separator + url_path));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return RestResponse.success(new HashMap<String, Object>() {{
            put("filePath", "/images/avatar/"+filename);
        }});
    }

    private String getStaticPath(String savePath) {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), savePath);
        if(!upload.exists()) {
            upload.mkdirs();
        }
        return path.getAbsolutePath() + "/static";
    }
}
