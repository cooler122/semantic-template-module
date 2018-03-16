package com.cooler.semantic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cooler.semantic.dto.LogDataProcessDTO;
import com.cooler.semantic.entity.LogDataCalculation;
import com.cooler.semantic.entity.LogDataProcess;
import com.cooler.semantic.model.*;
import com.cooler.semantic.model.console.AmnesiacData;
import com.cooler.semantic.model.console.CoupleAlterationRateData;
import com.cooler.semantic.model.console.SimilarityCalculationData_LPM;
import com.cooler.semantic.service.LogDataCalculationService;
import com.cooler.semantic.service.LogDataProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/log")
public class LogDataController {
    @Autowired
    private LogDataProcessService logDataProcessService;
    @Autowired
    private LogDataCalculationService logDataCalculationService;

    @RequestMapping(value = "/goWelcomePage.do", method = RequestMethod.GET)
    public String goWelcomePage(){
        return "stm-welcome";
    }

    @RequestMapping(value = "/goAccountPage.do", method = RequestMethod.GET)
    public String goAccountPage(){
        return "stm-account";
    }

    @RequestMapping(value = "/goProcessPage.do", method = RequestMethod.GET)
    public String goProcessPage(){
        return "stm-process";
    }

    @ResponseBody
    @RequestMapping(value = "/getProcessData.do", method = RequestMethod.POST)
    public List<LogDataProcessDTO> goProcessPage(@RequestBody JSONObject jsonObject, Model model){
        String accountId = jsonObject.getString("accountId");
        String userId = jsonObject.getString("userId");
        String fromDateTime = jsonObject.getString("fromDateTime");
        String toDateTime = jsonObject.getString("toDateTime");

        Integer accountIdParam = Integer.parseInt(accountId);
        Integer userIdParam = Integer.parseInt(userId);
        List<LogDataProcessDTO> logDataProcessDTOs = logDataProcessService.selectByAIdUIdDateTime(accountIdParam, userIdParam, fromDateTime, toDateTime);
        model.addAttribute("logDataProcessDTOs", logDataProcessDTOs);
        return logDataProcessDTOs;
    }

    @ResponseBody
    @RequestMapping(value = "/getOneLogDataProcess.do", method = RequestMethod.POST )
    public LogDataProcess getOneLogDataProcess(@RequestParam("id") String id){
        LogDataProcess logDataProcess = logDataProcessService.selectByPrimaryKey(Integer.parseInt(id));
        if(logDataProcess != null){
            return logDataProcess;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/checkLogDataCalculation.do", method = RequestMethod.POST )
    public Integer checkLogDataCalculation(@RequestBody JSONObject jsonObject){
        Integer accountId = jsonObject.getInteger("accountId");
        Integer userId = jsonObject.getInteger("userId");
        Integer contextId = jsonObject.getInteger("contextId");
        Long currentTimeMillis = jsonObject.getLong("currentTimeMillis");
        int recordCount = logDataCalculationService.checkBy4Params(accountId, userId, contextId, currentTimeMillis);
        if(recordCount > 0){
            return 1;
        }
        return 0;
    }


    @RequestMapping(value = "/getLogDataCalculation.do", method = RequestMethod.GET )
    public String getLogDataCalculation(@RequestParam("accountId") String accountId,
                                          @RequestParam("userId")String userId,
                                          @RequestParam("contextId")String contextId,
                                          @RequestParam("currentTimeMillis")String currentTimeMillis,
                                          Model model){
        Integer accountIdParam = Integer.parseInt(accountId);
        Integer userIdParam = Integer.parseInt(userId);
        Integer contextIdParam = Integer.parseInt(contextId);
        Long currentTimeMillisParam = Long.parseLong(currentTimeMillis);
        LogDataCalculation logDataCalculation = logDataCalculationService.getLogDataCalculation(accountIdParam, userIdParam, contextIdParam, currentTimeMillisParam);
        int resultParam = 0;
        if(logDataCalculation != null){
            model.addAttribute("accountId", accountId);
            model.addAttribute("userId", userId);
            model.addAttribute("contextId", contextId);
            model.addAttribute("dateTime", logDataCalculation.getDateTime());
            model.addAttribute("processTrace", logDataCalculation.getProcessTrace());
            model.addAttribute("detailContextOwner", logDataCalculation.getDetailContextOwner());
            int resultParamLPM = createData_LPM(logDataCalculation, model);
            int resultParamCPM = createData_CPM(logDataCalculation, model);
            int resultParamFPM = createData_FPM(logDataCalculation, model);
            resultParam = resultParam + resultParamCPM + resultParamLPM + resultParamFPM;
        }
        model.addAttribute("resultParam", resultParam);
        return "stm-calculation";
    }

    private Integer createData_LPM(LogDataCalculation logDataCalculation, Model model) {
        String lpmJsonData = logDataCalculation.getLpmJsonData();
        if(lpmJsonData != null){
            CalculationLogParam_LPM calculationLogParam_lpm = JSON.parseObject(lpmJsonData, CalculationLogParam_LPM.class);

            List<SentenceVector> sentenceVectors_lpm = calculationLogParam_lpm.getSentenceVectors();
            Map<Integer, SVRuleInfo> historySVRuleInfoMap_lpm = calculationLogParam_lpm.getHistorySVRuleInfoMap();
            Set<Integer> canceledLPMContextIdSet = calculationLogParam_lpm.getCanceledLPMContextIdSet();
            List<AmnesiacData> amnesiacDatas = calculationLogParam_lpm.getAmnesiacDatas();
            List<CoupleAlterationRateData> coupleAlterationRateDatas = calculationLogParam_lpm.getCoupleAlterationRateDatas();
            List<SimilarityCalculationData_LPM> similarityCalculationDataLPMS = calculationLogParam_lpm.getSimilarityCalculationDataLPMS();

            model.addAttribute("sentenceVectors_lpm", sentenceVectors_lpm);
            model.addAttribute("historySVRuleInfoMap_lpm", historySVRuleInfoMap_lpm);
            model.addAttribute("canceledLPMContextIdSet", canceledLPMContextIdSet);
            model.addAttribute("amnesiacDatas", amnesiacDatas);
            model.addAttribute("coupleAlterationRateDatas", coupleAlterationRateDatas);
            model.addAttribute("similarityCalculationDataLPMS", similarityCalculationDataLPMS);
            return 1;
        }
        return 0;
    }

    private Integer createData_CPM(LogDataCalculation logDataCalculation, Model model) {
        String cpmJsonData = logDataCalculation.getCpmJsonData();
        if(cpmJsonData != null){
            CalculationLogParam_CPM calculationLogParam_cpm = JSON.parseObject(cpmJsonData, CalculationLogParam_CPM.class);

            List<SentenceVector> sentenceVectors_cpm = calculationLogParam_cpm.getSentenceVectors();
            Map<Integer, SVRuleInfo> historySVRuleInfoMap_cpm = calculationLogParam_cpm.getHistorySVRuleInfoMap();
            Map<String, REntityWordInfo> hitCurrentREntityWordInfoMap = calculationLogParam_cpm.getHitCurrentREntityWordInfoMap();
            Map<String, Double> svIdcontextId_productValueMap = calculationLogParam_cpm.getSvIdcontextId_productValueMap();
            Integer maxValueSentenceVectorId = calculationLogParam_cpm.getMaxValueSentenceVectorId();
            Integer maxValueContextId = calculationLogParam_cpm.getMaxValueContextId();
            Double maxValue = calculationLogParam_cpm.getMaxValue();
            SentenceVector oldBestSentenceVector = sentenceVectors_cpm.get(maxValueSentenceVectorId);
            SVRuleInfo oldBestSvRuleInfo = historySVRuleInfoMap_cpm.get(maxValueContextId);
            SVRuleInfo changeParamOptimalSvRuleInfo = calculationLogParam_cpm.getChangeParamOptimalSvRuleInfo();
            int historyEntitiesCount = calculationLogParam_cpm.getHistoryEntitiesCount();
            int currentEntitiesCount = calculationLogParam_cpm.getCurrentEntitiesCount();
            int currentCoreEntitiesCount = calculationLogParam_cpm.getCurrentCoreEntitiesCount();
            double currentHitEntityWeightRate = calculationLogParam_cpm.getCurrentHitEntityWeightRate();

            model.addAttribute("sentenceVectors_cpm", sentenceVectors_cpm);
            model.addAttribute("historySVRuleInfoMap_cpm", historySVRuleInfoMap_cpm);

            model.addAttribute("hitCurrentREntityWordInfoMap", hitCurrentREntityWordInfoMap);
            model.addAttribute("svIdcontextId_productValueMap", svIdcontextId_productValueMap);
            model.addAttribute("maxValueSentenceVectorId", maxValueSentenceVectorId);
            model.addAttribute("maxValueContextId", maxValueContextId);
            model.addAttribute("maxValue", maxValue);

            model.addAttribute("oldBestSentenceVector", oldBestSentenceVector);
            model.addAttribute("oldBestSvRuleInfo", oldBestSvRuleInfo);
            model.addAttribute("changeParamOptimalSvRuleInfo", changeParamOptimalSvRuleInfo);

            model.addAttribute("historyEntitiesCount", historyEntitiesCount);
            model.addAttribute("currentEntitiesCount", currentEntitiesCount);
            model.addAttribute("currentCoreEntitiesCount", currentCoreEntitiesCount);
            model.addAttribute("currentHitEntityWeightRate", currentHitEntityWeightRate);
            return 10;
        }
        return 0;
    }

    private Integer createData_FPM(LogDataCalculation logDataCalculation, Model model) {
        String fpmJsonData = logDataCalculation.getFpmJsonData();
        if(fpmJsonData != null){
            return 100;
        }
        return 0;
    }

    @RequestMapping(value = "/getOneLogDataCalculation.do", method = RequestMethod.GET )
    public String getOneLogDataCalculation(@RequestParam("accountId") String accountId, @RequestParam("userId")String userId, @RequestParam("contextId")String contextId, @RequestParam("currentTimeMillis")String currentTimeMillis, Model model){
        Integer accountIdParam = Integer.parseInt(accountId);
        Integer userIdParam = Integer.parseInt(userId);
        Integer contextIdParam = Integer.parseInt(contextId);
        Long currentTimeMillisParam = Long.parseLong(currentTimeMillis);
        LogDataCalculation logDataCalculation = logDataCalculationService.selectBy4Params(accountIdParam, userIdParam, contextIdParam, currentTimeMillisParam);
        if(logDataCalculation != null){
            Integer accountId1 = logDataCalculation.getAccountId();
            Integer userId1 = logDataCalculation.getUserId();
            Integer contextId1 = logDataCalculation.getContextId();
            String dateTime1 = logDataCalculation.getDateTime();
            String processTrace = logDataCalculation.getProcessTrace();

            String cpmJsonData = logDataCalculation.getCpmJsonData();
            String lpmJsonData = logDataCalculation.getLpmJsonData();
            String fpmJsonData = logDataCalculation.getFpmJsonData();

            CalculationLogParam_CPM calculationLogParam_cpm = null;
            CalculationLogParam_LPM calculationLogParam_lpm = null;
            CalculationLogParam_FPM calculationLogParam_fpm = null;
            if(cpmJsonData != null) {
                calculationLogParam_cpm = JSON.parseObject(cpmJsonData, CalculationLogParam_CPM.class);
            }
            if(lpmJsonData != null){
                calculationLogParam_lpm = JSON.parseObject(lpmJsonData, CalculationLogParam_LPM.class);
            }
            if(fpmJsonData != null){
                calculationLogParam_fpm = JSON.parseObject(fpmJsonData, CalculationLogParam_FPM.class);
            }
            model.addAttribute("accountId", accountId1);
            model.addAttribute("userId", userId1);
            model.addAttribute("contextId", contextId1);
            model.addAttribute("dateTime", dateTime1);
            model.addAttribute("processTrace", processTrace);
            model.addAttribute("calculationLogParam_cpm", calculationLogParam_cpm);
            model.addAttribute("calculationLogParam_lpm", calculationLogParam_lpm);
            model.addAttribute("calculationLogParam_fpm", calculationLogParam_fpm);

            model.addAttribute("pageLabel", "calculation");
            return "stm-process";
        }
        return null;
    }

//    @RequestMapping(value = "/buildProcessPage.do", method = RequestMethod.GET)
//    public String buildProcessPage(){
//        return "/page/process";
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/getLogDataProcess.do", method = RequestMethod.POST )
//    public LogDataProcess getLogDataProcess(@RequestParam("id") String id){
//        LogDataProcess logDataProcess = logDataProcessService.selectByPrimaryKey(Integer.parseInt(id));
//        if(logDataProcess != null){
//            return logDataProcess;
//        }
//        return null;
//    }


}
