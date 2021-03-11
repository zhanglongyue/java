package priv.yue.common.base;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 扩展Mybatis ServiceImpl
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/23 9:09
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 将一个集合转换成树结构，支持多根节点
     * <p>
     * [{id:1,pid:0,sub:[]},{id:2,pid:0,sun[]},{id:3:pid:1,sub[]}]
     * <p>↓↓↓</p>
     * [{id:1,pid:0,sub:[{id:3:pid:1,sub[]}]},{id:2,pid:0,sun[]}]
     * @param root 根节点的父节点，例如根节点的pid可能是-1，null，0等等
     * @param collection 包含每一个树节点的集合
     * @param getId 获取节点id的方法
     * @param getPid 获取节点父id的方法
     * @param setSub 对节点设置子集合的方法，该方法为嵌套方法，先提供要设置子集合的对象
     *               即父对象，再提供如何设置子集合的方法
     * @return 树集合，如果只有一个顶级节点，则该集合size为1
     */
    public List<T> buildTree(Object root, Collection<T> collection, Function<T, Long> getId,
                             Function<T, Long> getPid, Function<T, Function<List<T>, ?>> setSub){
        if (ObjectUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }
        Map<Long, T> idMap = collection.stream().collect(Collectors.toMap(getId, m -> m));
        Map<Long, List<T>> pidGroupMap = collection.stream().collect(Collectors.groupingBy(getPid));
        pidGroupMap.forEach((k, v) -> {
            T parent = idMap.get(k);
            if (parent != null) {
                setSub.apply(parent).apply(v);
            }
        });
        return pidGroupMap.get(root);
    }

    /**
     * 将嵌套集合展开，使其扁平化
     * @param collection 要展开的集合
     * @param getChildren 获取子集合的方法
     * @return
     */
    public List<T> flat(Collection<T> collection, Function<T, Collection<T>> getChildren) {
        return collection.stream()
                .flatMap(new Function<T, Stream<T>>() {
                    public Stream<T> apply(T a) {
                        return Stream.concat(
                                Stream.of(a),
                                getChildren.apply(a).stream().flatMap(this::apply));
                    }
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 将mybatis递归查出来的多节点tree进行重构，去除重复节点
     * 场景：
     *  一个用户存在多个角色，角色表是树形结构，查询用户可查看的角色，如果用户拥有的2个角色存在父子关系，
     *  按mybatis递归查询，会查询2个树，但是子角色树被包含在父角色树中，需要去除这类重复树
     * 其他方法：
     *  在保存时对树形结构做处理，如果勾选了父子关系的角色，子角色权限由于被父角色完全涵盖，可以不保存子角色
     *  前端在勾选角色树时可以增加逻辑判断，在勾选父角色后，不可以勾选子角色
     * @param collection 多个树的集合
     * @param getId 获取节点id的方法
     * @param getSub 获取子节点的方法
     * @return 树集合
     */
    public List<T> rebuildIncompleteTree(Collection<T> collection, Function<T, Long> getId, Function<T,
            Collection<T>> getSub) {
        // 将每一个树展开，并生成以根节点ID为key,以包含根节点ID和所有子节点ID的List为Value的Map
        Map<Long, List<Long>> treeMap = collection.stream().collect(
            Collectors.toMap(getId,
                m -> flat(Collections.singletonList(m), getSub)
                        .stream().map(getId)
                        .collect(Collectors.toList())
            )
        );
        // 判断在树中重复的节点并将其从最初的集合中删除
        treeMap.forEach((key, ids) -> {
            AtomicInteger count = new AtomicInteger();
            treeMap.forEach((_key, _ids) -> {
                if (_ids.contains(key)) {
                    count.getAndIncrement();
                }
            });
            if (count.get() > 1) { // 大于1表明该节点在其他树中已经存在，删除
                collection.removeIf(node -> getId.apply(node).equals(key));
            }
        });
        return new ArrayList<>(collection);
    }

    /**
     * 根据一个属性使用column = value的方式查询数据库中是否存在记录
     * @param column 表字段
     * @param value 值
     * @return 存在返回 true 否则false
     */
    public boolean checkExistByColumn(String column, Object value){
        return baseMapper.selectCount(new QueryWrapper<T>().eq(column, value)) > 0;
    }

    /**
     * 与 checkExistByColumn 返回值相反
     */
    public boolean checkNotExistByColumn(String column, Object value){
        return !checkExistByColumn(column, value);
    }

}
