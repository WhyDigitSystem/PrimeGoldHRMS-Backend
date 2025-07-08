package com.efit.hrms.entity;

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
@Table(name = "polldetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "polldetailsgen")
	@SequenceGenerator(name = "polldetailsgen", sequenceName = "polldetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "polldetailsid")
	private Long id;
	@Column(name = "options")
	private String options;
	
	@ManyToOne
	@JsonBackReference
    @JoinColumn(name = "pollsid")  // Ensure correct column name
    private PollsVO pollsVO;  

}
