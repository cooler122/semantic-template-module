package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("optimalResultSelectComponent")
public class OptimalResultSelectComponentImpl extends FunctionComponentBase<SVRuleInfo, SVRuleInfo> {
    private static Logger logger = LoggerFactory.getLogger(OptimalResultSelectComponentImpl.class.getName());

    public OptimalResultSelectComponentImpl() {
        super("ORSC", null, "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<SVRuleInfo> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("ORSC.最佳结果选择");

        String resultCode = null;
        DataComponent<SVRuleInfo> dataComponent = null;
        DataComponent<SVRuleInfo> dataComponent_LPM = componentConstant.getDataComponent("optimalSvRuleInfo_LPM", contextOwner);
        if(dataComponent_LPM != null && dataComponent_LPM.getData() != null){                                                                       //优先缺参结果LPM
            dataComponent = dataComponent_LPM;
            resultCode = "ORSC_S_L";
        }else{
            DataComponent<SVRuleInfo> dataComponent_CPM = componentConstant.getDataComponent("optimalSvRuleInfo_CPM", contextOwner);
            DataComponent<SVRuleInfo> dataComponent_FPM = componentConstant.getDataComponent("optimalSvRuleInfo_FPM", contextOwner);
            if((dataComponent_CPM == null || dataComponent_CPM.getData() == null) && (dataComponent_FPM != null && dataComponent_FPM.getData() != null)){ //如果换参结果为空，而全参结果不为空，则选全参结果
                dataComponent = dataComponent_FPM;
                resultCode = "ORSC_S_F";
            }else if((dataComponent_CPM != null && dataComponent_CPM.getData() != null) && (dataComponent_FPM == null || dataComponent_FPM.getData() == null)){   //如果换参结果不为空，而全参结果为空，则选换参结果
                dataComponent = dataComponent_CPM;
                resultCode = "ORSC_S_C";
            }else if((dataComponent_CPM != null && dataComponent_CPM.getData() != null) && (dataComponent_FPM != null && dataComponent_FPM.getData() != null)){   //如果两者都不为空，选相似度大的结果
                SVRuleInfo svRuleInfo_CPM = dataComponent_CPM.getData();
                SVRuleInfo svRuleInfo_FPM = dataComponent_FPM.getData();
                Double similarity_CPM = svRuleInfo_CPM.getSimilarity();
                Double similarity_FPM = svRuleInfo_FPM.getSimilarity();
                if(similarity_CPM > similarity_FPM){
                    dataComponent = dataComponent_CPM;
                    resultCode = "ORSC_S_C";
                }else{
                    dataComponent = dataComponent_FPM;
                    resultCode = "ORSC_S_F";
                }
            }else{
                resultCode = "ORSC_S";
            }                                                                                                                                       //如果两者都为空，谁都不选
        }
        if(dataComponent != null && dataComponent.getData() != null){
            return new ComponentBizResult(resultCode, Constant.STORE_LOCAL_REMOTE, dataComponent.getData());
        }else{
            return new ComponentBizResult(resultCode);
        }
    }
}
