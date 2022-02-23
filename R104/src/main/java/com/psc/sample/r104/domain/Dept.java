package com.psc.sample.r104.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Dept {

    @Id
    Integer deptNo;
    String dName;
    String loc;

}
