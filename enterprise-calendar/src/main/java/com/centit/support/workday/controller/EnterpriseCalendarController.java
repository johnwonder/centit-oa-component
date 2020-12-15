package com.centit.support.workday.controller;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.workday.po.WorkDay;
import com.centit.support.workday.service.WorkDayManager;
import io.swagger.annotations.ApiOperation;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author : guo_jh
 * Date: 2018/6/27 19:31
 * Description:企业工作日历
 */

@Controller
@RequestMapping("/calendar")
public class EnterpriseCalendarController extends BaseController {

    @Resource
    private WorkDayManager workDayMag;


    /**
     * 获取当前日期标记
     *
     * @param sCurDate  当前选中时间,默认取系统当前时间
     */
    @ApiOperation("获取当前月份所有标记日期，包括：加班和调休。")
    @RequestMapping(value = "/{sCurDate}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public WorkDay getMarkDay(@PathVariable String sCurDate) {
          return this.workDayMag.getWorkDay(sCurDate);
    }

    /**
     * 更新日期标记
     *
     * @param workDay 工作日信息
     */
    @ApiOperation("保存日期标记，如果日期标记为‘0’表示还原默认值，系统会删除对应的标记记录。")
    @WrapUpResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void saveData(WorkDay workDay) {
        if (WorkDay.WORK_DAY_TYPE_IGNORE.equals(workDay.getDayType())) {//还原日期默认标记
            this.workDayMag.deleteWorkDay(workDay.getWorkDay());
        } else {//新增或更新日期标记
            WorkDay dbWorkDay = this.workDayMag.getWorkDay(workDay.getWorkDay());
            if (dbWorkDay != null) {
                dbWorkDay.copyNotNullProperty(workDay);
                this.workDayMag.updateWorkDay(workDay);
            } else {
                this.workDayMag.saveWorkDay(workDay);
            }
        }
    }

    /**
     * 删除日期标记
     *
     * @param sCurDate 工作日信息
     */
    @ApiOperation("保存日期标记，如果日期标记为‘0’表示还原默认值，系统会删除对应的标记记录。")
    @WrapUpResponseBody
    @RequestMapping(value = "/{sCurDate}", method = RequestMethod.DELETE)
    public void deleteWorkDay(@PathVariable String sCurDate) {
        this.workDayMag.deleteWorkDay(sCurDate);
    }

    /**
     * 获取指定范围内特殊日期集合
     *
     * @param sCurDate  当前选中时间,默认取系统当前时间
     */
    @ApiOperation("获取当前月份所有标记日期，包括：加班和调休。")
    @RequestMapping(value = "/month/{sCurDate}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> findMarkDayByCurrMonth(@PathVariable String sCurDate) {
        Date curDate =  DatetimeOpt.smartPraseDate(sCurDate);
        if(curDate == null){
            curDate = DatetimeOpt.currentUtilDate();
        }
        Date startDate = DatetimeOpt.truncateToMonth(curDate);
        Date endDate = DatetimeOpt.seekEndOfMonth(curDate);
        return this.workDayMag.listWorkDays(
            DatetimeOpt.convertDateToString(startDate), DatetimeOpt.convertDateToString(endDate)
        );
    }


    @ApiOperation("查询一定范围内所有标记日期，包括：加班和调休。")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> listMarkDay(HttpServletRequest request) {
        String startDate = WebOptUtils.getRequestFirstOneParameter(request,
            "startDate","start","beginDate","begin");
        String endDate = WebOptUtils.getRequestFirstOneParameter(request,
            "endDate","end");
        return this.workDayMag.listWorkDays(startDate, endDate);
    }

    @ApiOperation("查询一定范围内所有工作日。")
    @RequestMapping(value = "/rangeWorkDays", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> rangeWorkDays(HttpServletRequest request) {
        String startDate = WebOptUtils.getRequestFirstOneParameter(request,
            "startDate","start","beginDate","begin");
        String endDate = WebOptUtils.getRequestFirstOneParameter(request,
            "endDate","end");
        return this.workDayMag.rangeWorkDays(startDate, endDate);
    }

    @ApiOperation("查询一定范围内所有非工作日。")
    @RequestMapping(value = "/rangeWorkDays", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public List<WorkDay> rangeHolidays(HttpServletRequest request) {
        String startDate = WebOptUtils.getRequestFirstOneParameter(request,
            "startDate","start","beginDate","begin");
        String endDate = WebOptUtils.getRequestFirstOneParameter(request,
            "endDate","end");
        return this.workDayMag.rangeHolidays(startDate, endDate);
    }

}
