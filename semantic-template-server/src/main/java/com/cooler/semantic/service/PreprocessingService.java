package com.cooler.semantic.service;

import com.cooler.semantic.entity.SemanticParserRequest;

public interface PreprocessingService {

    /**
     * 预处理：
     * 1.校验请求，检查请求的格式是否正确，是否有缺失的参数
     * 2.校验accountId账号：检查此accountId的权限，是否有访问权限（密码是否正确）
     * 3.设置自定义参数：查出用户设置的配置参数（非常重要的用户自定义参数），保存起来用以影响后面的业务流程，提升匹配准确度。
     * @param semanticParserRequest 请求参数
     * @return  是否通过
     */
    boolean preprocess(SemanticParserRequest semanticParserRequest);

}
