package priv.yue.logging.es7.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.searchafter.SearchAfterBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import priv.yue.logging.es7.domain.LogOpEs;
import priv.yue.logging.es7.service.LogOpEsService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZhangLongYue
 * @since 2021/4/8 14:10
 */
@Service
public class LogOpEsServiceImpl implements LogOpEsService {

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * 构造ES查询
     *
     * @return 返回值是mybatis的Page，不是springDataPage
     */
    public Page<LogOpEs> selectPage(Map<String, Object> map, Integer currPage, Integer size) {
        // 当前页需减1
        currPage = currPage - 1;
        // from..size存在深度分页问题，限制查询10000以上的数据
        if (currPage * size + size > 10000) {
            currPage = 10000 / size - 1;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 构造bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 通用查询
        Object searchStr = map.get("search");
        if (searchStr != null && StrUtil.isNotBlank(searchStr.toString())) {
            Map<String, Float> fieldsMap = new HashMap<>();
            fieldsMap.put("parameter", 1F);
            fieldsMap.put("return_value", 1F);
            boolQueryBuilder.must(
                new BoolQueryBuilder()
                    .should(QueryBuilders.queryStringQuery(searchStr.toString()).fields(fieldsMap))
                    .should(QueryBuilders.wildcardQuery("all", searchStr.toString()))
            );
        }

        // 业务类型查询
        if (map.get("businessType") != null && StrUtil.isNotBlank(map.get("businessType").toString())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("business_type",
                    map.get("businessType").toString()));
        }

        // 状态查询
        if (map.get("status") != null && StrUtil.isNotBlank(map.get("status").toString())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("status", map.get("status").toString()));
        }

        // 日志创建时间范围查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("create_time");
        if (map.get("createTimeBegin") != null && StrUtil.isNotBlank(map.get("createTimeBegin").toString())) {
            rangeQueryBuilder.gte(map.get("createTimeBegin").toString());
        }
        if (map.get("createTimeEnd") != null && StrUtil.isNotBlank(map.get("createTimeEnd").toString())) {
            rangeQueryBuilder.lte(map.get("createTimeEnd").toString());
        }
        boolQueryBuilder.must(rangeQueryBuilder);

        // 排序
        queryBuilder.withSort(SortBuilders.fieldSort("create_time").order(SortOrder.DESC));

        // 分页
        queryBuilder.withPageable(PageRequest.of(currPage, size));

        // 高亮
        String[] highlightFields = {
            "parameter",
            "return_value",
        };
        HighlightBuilder.Field[] fields = new HighlightBuilder.Field[highlightFields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new HighlightBuilder.Field(highlightFields[i])
                .preTags("<em>").postTags("</em>")
                .numOfFragments(0);
        }

        queryBuilder.withHighlightFields(fields);

        // 执行查询
        queryBuilder.withQuery(boolQueryBuilder);
        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        nativeSearchQuery.setTrackTotalHits(true);
        SearchHits<LogOpEs> searchHits = elasticsearchOperations.search(nativeSearchQuery, LogOpEs.class);

        // 将结果封装到mybatis的Page
        Page<LogOpEs> page = new Page<>(currPage, size);
        // 设置总条数
        page.setTotal(searchHits.getTotalHits());
        // 设置返回的实体集合
        page.setRecords(searchHits.get().map(hit -> {
            // 从searchHit中获取到实体
            LogOpEs logOpEs = hit.getContent();
            // 通过反射将返回的高亮内容替换source中对应的字段内容
            // 注意高亮numOfFragments设置为0，将返回全部字段，并且不进行文本分割，所以content的size为1
            hit.getHighlightFields().forEach((hithlight, content) -> {
                ReflectUtil.invoke(logOpEs,
                        StrUtil.upperFirstAndAddPre(hithlight, "set"), content.get(0));
            });
            return logOpEs;
        }).collect(Collectors.toList()));
        return page;
    }

}
