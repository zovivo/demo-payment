package vn.vnpay.process.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {

    protected Long id;
    protected Timestamp createdAt;
    protected Timestamp updatedAt;

}
