package com.fox.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.fox.es.dto.BlogSimpleInfoDTO;
import com.fox.es.entity.Result;
import com.fox.es.service.ShareResourceSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 狐狸半面添
 * @create 2023-03-22 20:18
 */
@Slf4j
@Service
public class ShareResourceSearchServiceImpl implements ShareResourceSearchService {
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Value("${elasticsearch.blog.index}")
    private String blogIndexStore;
    @Value("${elasticsearch.blog.source_fields}")
    private String blogFields;


    public Result list(String keyWords, int pageNo, int pageSize) {
        // 1.设置索引 - blog
        SearchRequest searchRequest = new SearchRequest(blogIndexStore);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 2.source源字段过虑
        String[] sourceFieldsArray = blogFields.split(",");
        searchSourceBuilder.fetchSource(sourceFieldsArray, new String[]{});

        // 3.关键字
        if (StringUtils.hasText(keyWords)) {
            // 哪些字段匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyWords, "title", "tags", "introduce", "content");
            // 设置匹配占比（表示最少匹配的子句个数,例如有五个可选子句,最少的匹配个数为5*70%=3.5.向下取整为3,这就表示五个子句最少要匹配其中三个才能查到）
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            // 提升字段的Boost值
            multiMatchQueryBuilder.field("title", 10);
            multiMatchQueryBuilder.field("tags", 7);
            multiMatchQueryBuilder.field("introduce", 5);

            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        // 4.分页
        int start = (pageNo - 1) * pageSize;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(pageSize);

        // 布尔查询
        searchSourceBuilder.query(boolQueryBuilder);

        // 6.高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='red'>");
        highlightBuilder.postTags("</font>");
        // 设置高亮字段
        ArrayList<HighlightBuilder.Field> fields = new ArrayList<>();
        fields.add(new HighlightBuilder.Field("title"));
        fields.add(new HighlightBuilder.Field("introduce"));
        highlightBuilder.fields().addAll(fields);
        searchSourceBuilder.highlighter(highlightBuilder);

        // 请求搜索
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("博客搜索异常：{}", e.getMessage());
            return Result.error(e.getMessage());
        }

        // 结果集处理
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        // 记录总数
        long totalHitsCount = hits.getTotalHits().value;
        // 数据列表
        List<BlogSimpleInfoDTO> list = new ArrayList<>();

        for (SearchHit hit : searchHits) {

            String sourceAsString = hit.getSourceAsString();
            // json 转 对象
            BlogSimpleInfoDTO blog = JSON.parseObject(sourceAsString, BlogSimpleInfoDTO.class);

            // 取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                blog.setTitle(parseHighlightStr(blog.getTitle(),highlightFields.get("title")));
                blog.setIntroduce(parseHighlightStr(blog.getIntroduce(),highlightFields.get("introduce")));
                }

            list.add(blog);
        }

        // 封装信息返回前端
        HashMap<String, Object> resultMap = new HashMap<>(4);
        // 页码
        resultMap.put("pageNo",pageNo);
        // 每页记录数量
        resultMap.put("pageSize",pageSize);
        // 总记录数
        resultMap.put("total",totalHitsCount);
        // 该页信息
        resultMap.put("items",list);

        return Result.ok(resultMap);

    }

    public String parseHighlightStr(String text, HighlightField field){
        if (field != null) {
            Text[] fragments = field.getFragments();
            StringBuilder stringBuilder = new StringBuilder();
            for (Text str : fragments) {
                stringBuilder.append(str.string());
            }
            return stringBuilder.toString();
        }else{
            return text;
        }
    }
}
