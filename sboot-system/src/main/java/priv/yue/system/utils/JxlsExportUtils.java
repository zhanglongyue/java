package priv.yue.system.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author ZhangLongYue
 * @since 2021/3/5 20:30
 */
@Slf4j
public class JxlsExportUtils {
    /**
     * 导出执行方法
     * @param response
     * @param model
     * @param templatePath 模板
     * @param exportFileName 导出文件名
     */
    public static void doExcelExport(HttpServletResponse response,
                                     Map<String, Object> model, String templatePath, String exportFileName) throws UnsupportedEncodingException {
        //导出文件名重编码
        String fileName = URLEncoder.encode(exportFileName, "UTF-8").replaceAll("\\+", "%20");

        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        response.setHeader("filename", fileName + ".xlsx");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        OutputStream out = null;
        InputStream in = null;
        try {
            //输出流为请求响应
            out = response.getOutputStream();
            //模板文件流
            in = getTemplateInputStream(templatePath); //getTemplateStream(templatePath);
            //模板数据构建
            exportExcel(in, out, model);
        } catch (IOException e) {
            log.info("模板填充异常：{}", e);
        } finally {
            //释放资源
            if (out != null) {
                IoUtil.flush(out);
                IoUtil.close(out);
            }
            if (in != null) {
                IoUtil.close(in);
            }
        }
    }

    /**
     * 模板处理方法
     * @param is
     * @param os
     * @param model
     * @throws IOException
     */
    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        if (is == null) {
            log.info("获取模板异常，模板文件流为空！");
        }

        Context context = new Context();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();

        Map<String, Object> funcs = new HashMap<>();
        funcs.put("utils", new JxlsExportUtils()); // 添加自定义功能，可在excel模板中，可直接使用该自定义对象的方法

        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        jxlsHelper.processTemplate(context, transformer);
    }

    public static InputStream getTemplateInputStream(String path) {
        log.info("模板路径为：{}", path);
        return FileUtil.getInputStream(path);
    }

    /* map中value求和 */
    public BigDecimal doSum(Map<String, Object> items) {
        BigDecimal sum = BigDecimal.ZERO;
        if (items != null) {
            for (Entry<String, Object> entry  : items.entrySet()) {
                String value = ObjectUtil.toString(entry.getValue());
                sum = sum.add(new BigDecimal(value));
            }
        }
        return sum;
    }

    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // if判断
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

    // if判断
    public Object ifelse(Integer b, Object o1, Object o2) {
        return ifelse(b == 1, o1, o2);
    }

    public Object collection2Str(Collection<?> collection, String className, String property) throws ClassNotFoundException {
        Class c = Class.forName(className);
        return Arrays.toString(collection.stream().map((x) -> {
            try {
                Method method = c.getMethod("get" + property);
                Object o = method.invoke(x);
                return o.toString();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).toArray());
    }
}
