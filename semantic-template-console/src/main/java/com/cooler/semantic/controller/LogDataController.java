package com.cooler.semantic.controller;

import com.cooler.semantic.entity.LogDataCalculation;
import com.cooler.semantic.entity.LogDataProcess;
import com.cooler.semantic.service.LogDataCalculationService;
import com.cooler.semantic.service.LogDataProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/log")
public class LogDataController {
    @Autowired
    private LogDataProcessService logDataProcessService;

    @Autowired
    private LogDataCalculationService logDataCalculationService;

    @RequestMapping(value = "/process.do", method = RequestMethod.GET)
    public String getProcessLog(Model model){
        List<LogDataProcess> logDataProcesses = logDataProcessService.selectByAIdUIdDateTime(1, 2, "2018-01-18 20:41:00", "2018-02-26 23:27:00");
        model.addAttribute("logDataProcesses", logDataProcesses);
        model.addAttribute("pageLabel", "process");
        return "/log/framework";
    }

    @RequestMapping(value = "/calculation.do", method = RequestMethod.GET)
    public String getCalculationLog(Model model){
        LogDataCalculation logDataCalculation = logDataCalculationService.selectByPrimaryKey(1);
        model.addAttribute("logDataCalculation", logDataCalculation);
        model.addAttribute("pageLabel", "calculation");
        return "/log/framework";
    }

    @RequestMapping(value = "/test.do", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap getCalculationLog(ModelMap modelMap){
        LogDataCalculation logDataCalculation = logDataCalculationService.selectByPrimaryKey(1);
        modelMap.addAttribute("logDataCalculation", logDataCalculation);

        return modelMap;
    }
}
