package com.efit.hrms.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
@Table(name = "companyweekoff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWeekOffVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companyweekoffgen")
	@SequenceGenerator(name = "companyweekoffgen", sequenceName = "companyweekoffseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "companyweekoffid")
	private Long id;

	@Column(name = "weekoffdays")
	private String weekOffDays;
//	@Column(name = "orgid")
//	private String orgId;

	@ElementCollection
	@CollectionTable(name = "weekoffoccurrences", joinColumns = @JoinColumn(name = "companyweekoffid"))
	@Column(name = "weeknumber") // 1, 2, 3, 4 or -1 for ALL
	private List<Integer> weekNumbers; // e.g., [1, 3], or [-1] for ALL

	@ManyToOne
	@JoinColumn(name = "companyid")
	@JsonBackReference
	private CompanyVO companyVO;

}
