package com.psc.sample.r103;

import com.psc.sample.r103.db1.Dept1;
import com.psc.sample.r103.db1.Dept1Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@Commit
@SpringBootTest
class R103ApplicationTests {

    @Autowired
    Dept1Repository dept1Repository;

    @Test
    public void first(){

        for(int i=1; i<100; i++){
            Dept1 dept1 = new Dept1(i, String.valueOf(i), String.valueOf(i));
            dept1Repository.save(dept1);
        }

    }

}
