package priv.yue.system.controller;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author ZhangLongYue
 * @since 2021/3/16 14:41
 */
@RestController
@Slf4j
@RequestMapping("/video")
public class VideoController {

    @RequestMapping("/get")
    public void playVideo(HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        OutputStream os = null;
        try {
            //获取响应的输出流
            os = response.getOutputStream();
            File file = FileUtil.file(".","static/videos/demo.mp4");
            if(file.exists()){
                RandomAccessFile targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if(rangeString != null){
                    long range = Long.parseLong(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    // response.setHeader("Content-Type", "video/mp4");
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                    //返回码需要为206，而不是200
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                }else {//下载
                    //设置响应头，把文件名字设置好
                    response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
                    //设置文件长度
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    //解决编码问题
                    response.setHeader("Content-Type","application/octet-stream");
                }

                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache))!=-1){
                    os.write(cache, 0, flag);
                }
            } else {
                String message = "file: " + file.getName() + " not exists";
                //解决编码问题
                response.setHeader("Content-Type","application/json");
                os.write(message.getBytes(StandardCharsets.UTF_8));
            }

        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}
