
package com.jm.service;
import com.jm.mock.MockModel;
import com.jm.mock.MockUtil;
import com.jm.model.Home;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HomeService extends AbsService{

    @Override
    protected List<MockModel> loadObjects() {
            return MockUtil.create(100, Home.class,getPrimaryKey());
     }

    @Override
    protected String getFileName() { return "Home.txt"; }

    @Override
    protected void setPrimaryKey() {   this.primaryKey="firstName"; }
}
