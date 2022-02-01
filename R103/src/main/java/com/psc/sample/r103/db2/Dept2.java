package com.psc.sample.r103.db2;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dept2")
public class Dept2 {

	@Id
	private Integer deptno;
	private String dname;
	private String loc;

}
