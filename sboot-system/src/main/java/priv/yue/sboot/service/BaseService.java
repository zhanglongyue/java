package priv.yue.sboot.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 扩展mybatis Service
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/23 9:22
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 根据某字段检查实体是否存在
     * @param column 表字段名
     * @param value 字段值
     * @return 存在返回true
     */
    public boolean checkExistByColumn(String column, Object value);

    /**
     * 根据某字段检查实体是否存在
     * @param column 表字段名
     * @param value 字段值
     * @return 不存在返回true
     */
    public boolean checkNotExistByColumn(String column, Object value);
}
