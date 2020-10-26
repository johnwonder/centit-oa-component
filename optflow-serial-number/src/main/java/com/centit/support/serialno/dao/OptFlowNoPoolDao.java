package com.centit.support.serialno.dao;

import com.centit.support.database.utils.PageDesc;
import com.centit.support.serialno.po.OptFlowNoPool;
import com.centit.support.serialno.po.OptFlowNoPoolId;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OptFlowNoPoolDao {

    /**
     * 根据Id查询
     * @return 流水号池中的流水号
     * @param cid 复合主键
     */
    OptFlowNoPool getObjectById(OptFlowNoPoolId cid);

    /**
     * 删除
     * @param optFlowNoPool  流水号池中的流水号
     */
    void deleteObject(OptFlowNoPool optFlowNoPool);

    /**
     * 根据Id删除
     * @param cid 复合主键
     */
    void deleteObjectById(OptFlowNoPoolId cid);

    /**
     * 新增
     * @param optFlowNoPool  流水号池中的流水号
     */
    void saveNewOptFlowNoPool(OptFlowNoPool optFlowNoPool);

    /**
     *  "select min(CurNo) as MinNo from F_OptFlowNoPool" +
                " where OwnerCode = " + QueryUtils.buildStringForQuery(ownerCode) +
                " and CodeCode = " + QueryUtils.buildStringForQuery(ownerCode) +
                " and CodeDate = to_date(" + QueryUtils.buildStringForQuery(
                DatetimeOpt.convertDatetimeToString(codeBaseDate))
                + ",'YYYY-MM-DD HH:MI:SS')");
     * @param codeBaseDate 编码基准日期
     * @param codeCode 编码类别
     * @param ownerCode 归属人员
     * @return long
     */
    long fetchFirstLsh(String ownerCode, String codeCode, Date codeBaseDate);

    List<OptFlowNoPool> listLshInPool(Map<String, Object> filterMap, PageDesc pageDesc);

}
