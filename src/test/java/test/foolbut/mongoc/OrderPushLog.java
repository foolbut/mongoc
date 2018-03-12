package test.foolbut.mongoc;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonId;

import self.foolbut.mongoc.annotation.Collection;

@Collection("order_pay_push_log")
public class OrderPushLog {

    @BsonId
    private String id;

    private Long jobId;
    private String orderNo;
    private Date exeTime;
    private String errorMsg;
    private Boolean success;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public Long getJobId() {
        return jobId;
    }
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public Date getExeTime() {
        return exeTime;
    }
    public void setExeTime(Date exeTime) {
        this.exeTime = exeTime;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
