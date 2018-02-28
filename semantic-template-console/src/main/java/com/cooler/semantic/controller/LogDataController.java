package com.cooler.semantic.controller;

import com.cooler.semantic.entity.LogDataCalculation;
import com.cooler.semantic.entity.LogDataProcess;
import com.cooler.semantic.service.LogDataCalculationService;
import com.cooler.semantic.service.LogDataProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/logData")
public class LogDataController {
    @Autowired
    private LogDataProcessService logDataProcessService;

    @Autowired
    private LogDataCalculationService logDataCalculationService;

    @RequestMapping(value = "/getDataLog.do", method = RequestMethod.GET)
    @ResponseBody
    public String getDataLog(){
        LogDataProcess logDataProcess = logDataProcessService.selectByPrimaryKey(1);
        System.out.println(logDataProcess.getAccountId());

        LogDataCalculation logDataCalculation = logDataCalculationService.selectByPrimaryKey(1);
        System.out.println(logDataCalculation.getAccountId());

        return null;
    }
}
