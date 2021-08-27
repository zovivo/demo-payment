package vn.vnpay.process.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryTemplate {

    private String query;
    private HashMap<String, Object> parameterMap = new HashMap<>();
    private Pageable pageable;
    private boolean isNative = false;
    private Class resultType;

    public QueryTemplate(String query, HashMap<String, Object> parameterMap, Pageable pageable) {
        this.query = query;
        this.parameterMap = parameterMap;
        this.pageable = pageable;
    }

    public QueryTemplate(String query, HashMap<String, Object> parameterMap) {
        this.query = query;
        this.parameterMap = parameterMap;
    }

    public QueryTemplate(String query) {
        this.query = query;
    }

}
