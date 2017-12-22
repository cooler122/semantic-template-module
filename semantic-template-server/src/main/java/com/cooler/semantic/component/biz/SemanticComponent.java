package com.cooler.semantic.component.biz;

import com.cooler.semantic.model.ContextOwner;

public interface SemanticComponent {

    int getType();
    /**
     * 功能组件执行，执行contextOwner的数据
     * @param contextOwner  用于数据定位的上下文对象
     */
    void functionRun(ContextOwner contextOwner);

    /**
     * 判断组件执行，执行contextOwner的数据
     * @param contextOwner  用于数据定位的上下文对象
     */
    void verdictRun(ContextOwner contextOwner, String nextComponentId);

}
