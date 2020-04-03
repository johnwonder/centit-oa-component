package com.centit.product.oa.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.oa.po.InnerMsg;
import com.centit.support.algorithm.StringBaseOpt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("innerMsgDao")
public class InnerMsgDao extends BaseDaoImpl<InnerMsg, String>{

    public InnerMsg getObjectById(String msgCode) {
        return super.getObjectById(msgCode);
    }

    @Override
    public List<InnerMsg> listObjects(Map<String, Object> filterMap) {
        return super.listObjectsByProperties(filterMap);
    }


    @Override
    public Map<String, String> getFilterField() {
        HashMap<String, String> filterField = new HashMap<>();
        filterField.put("msgType", CodeBook.EQUAL_HQL_ID);
        filterField.put("msgTitle", CodeBook.LIKE_HQL_ID);
        filterField.put("msgContent", CodeBook.LIKE_HQL_ID);
        filterField.put("msgStateNot", "msgState != :msgStateNot");
        filterField.put("sender", CodeBook.EQUAL_HQL_ID);
        filterField.put("receive", "msgCode in (select re.MSG_CODE from f_inner_msg_recipient re Where re.RECEIVE = :receive )");
        return filterField;
    }

    /*    return "[:msgType | and MSG_TYPE = :msgType ]" +
                "[:(like)msgTitle | and MSG_TITLE like :msgTitle ]" +
                "[:(like)msgContent | and MSG_CONTENT like :msgContent ]" +
                "[:msgStateNot | and MSG_STATE != msgStateNot ]" +
                "[:sender | and SENDER = :sender ] " +
                "[:receive | and MSG_CODE in ( select re.MSG_CODE from M_INNERMSG_RECIPIENT re Where re.RECEIVE = :receive )] ";
    */

    @Transactional
    public void updateInnerMsg(InnerMsg innerMsg){
        super.updateObject(innerMsg);
    }
    public List<InnerMsg> listUnreadMessage(String userCode){
        String queryString ="where MSG_CODE in (Select im.MSG_CODE from f_inner_msg_recipient im where  im.MAIL_TYPE='T'" +
            " and im.msg_state='U' and RECEIVE= ?) order by msg_Code desc";
        return listObjectsByFilter(queryString,
            new Object[]{userCode});
    }
    @Transactional
    public String getNextKey() {
        return StringBaseOpt.objectToString(
            DatabaseOptUtils.getSequenceNextValue(this, "S_MSGCODE"));
    }

}
