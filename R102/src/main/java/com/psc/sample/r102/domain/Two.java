package com.psc.sample.r102.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Two {

    @Id
    String one;
    String two;

}
