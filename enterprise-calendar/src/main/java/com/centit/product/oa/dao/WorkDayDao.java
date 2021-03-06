package com.centit.product.oa.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.oa.po.WorkDay;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/*
 * @author : guo_jh
 * Date: 2018/6/29 10:59
 * Description:
 */
@Repository
public class WorkDayDao extends BaseDaoImpl<WorkDay, String> {
   // public static final Log log = LogFactory.getLog(WorkDayDao.class);

    public WorkDayDao() {
    }

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("workDay", "EQUAL");
        filterField.put("dayType", "EQUAL");
        filterField.put("workTimeType", "EQUAL");
        filterField.put("startDate", "WORK_DAY >= :startDate");
        filterField.put("endDate", "workDay <= :endDate");
        return filterField;
    }
}
