package com.jm.service;
import com.jm.mock.MockModel;
import com.jm.mock.MockUtil;
import com.jm.model.User;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbsService{

    @Override
    protected List<MockModel> loadObjects() {
            return MockUtil.create(10, User.class,getPrimaryKey());
     }

    @Override
    protected String getFileName() { return "User.txt"; }

    @Override
    protected void setPrimaryKey() {   this.primaryKey="username"; }
}
