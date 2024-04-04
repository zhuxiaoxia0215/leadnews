package com.heima.article.fegin;

import com.heima.apis.article.IArticleClient;
import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApArticleService articleService;

    @PostMapping("/api/v1/article/save")
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return articleService.saveArticle(dto);
    }
}
