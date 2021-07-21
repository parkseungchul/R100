package com.psc.sample.r102.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
public class TestDeptRepository {

    @Autowired
    DeptRepository deptRepository;

    @Autowired
    DeptRepository2 deptRepository2;

    @Test
    @Commit
    public void dept(){
        for(int i=1; i<101; i++){
            deptRepository.save(new Dept(i, String.valueOf(i)+"_name", String.valueOf(i) +"_loc"));
        }
    }

    @Test
    @Commit
    public void dept2(){
        for(int i=1; i<2; i++){
            deptRepository2.save(new Dept2(i, String.valueOf(i)+"_name", String.valueOf(i) +"_loc"));
        }
    }


}
