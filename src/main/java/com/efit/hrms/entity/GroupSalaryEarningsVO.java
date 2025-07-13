package com.efit.hrms.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groupsalaryearnings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSalaryEarningsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupsalaryearningsgen")
	@SequenceGenerator(name = "groupsalaryearningsgen", sequenceName = "groupsalaryearningsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "groupsalaryearningsid")
	private Long id;
	@Column(name = "heading", length = 100)
	private String heading;
	@Column(name = "amount", length = 100)
	private BigDecimal amount;
	
	
	@ManyToOne
	@JoinColumn(name = "groupsalarystructureid")
	@JsonBackReference
	private GroupSalaryStructureVO groupSalaryStructureVO;
	
}
