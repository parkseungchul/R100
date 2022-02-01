package com.psc.sample.r103.db1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dept1")
public class Dept1 {
	@Id
	private Integer deptno;
	private String dname;
	private String loc;

}
