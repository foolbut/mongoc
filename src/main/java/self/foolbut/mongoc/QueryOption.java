package self.foolbut.mongoc;

import java.util.ArrayList;
import java.util.List;

public class QueryOption {

    private Integer limit;
    private Integer offset;
    private List<String> orderby = new ArrayList<String>();
    public Integer getLimit() {
        return limit;
    }
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    public Integer getOffset() {
        return offset;
    }
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
    public List<String> getOrderby() {
        return orderby;
    }
    public void setOrder(String field,Boolean asc) {
        if(null == asc || true == asc.booleanValue()){
            orderby.add(field);
        }else{
            orderby.add("-"+field);
        }
    }
    
}
