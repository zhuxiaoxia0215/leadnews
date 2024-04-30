package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;

public interface ApArticleService extends IService<ApArticle> {

    public ResponseResult load(ArticleHomeDto dto, Short type);

    public ResponseResult load(ArticleHomeDto dto, Short type, boolean firstPage);

    public ResponseResult saveArticle(ArticleDto dto);
}
