package com.efit.hrms.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


	@Entity
	@Table(name = "polls")
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class PollsVO {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pollsgen")
		@SequenceGenerator(name = "pollsgen", sequenceName = "pollsseq", initialValue = 1000000001, allocationSize = 1)
		@Column(name = "pollsid")
		private Long id;
		@Column(name = "orgid")
		private long orgId;
		@Column(name = "branchcode")
		private String branchCode;
		@Column(name = "branchname")
		private String branchName;
		@Column(name = "department")
		private String department;
		
		@Column(name = "question")
		private String question;
		@Column(name = "mutliselect")
		private String multiSelect;
		@Column(name = "maxselection")
		private Long maxSelection;
		@Column(name = "expiresdate")
		private LocalDate expiresDate;
		@Column(name = "type")
		private String type;
		
		@Column(name = "createdby")
		private String createdBy;
		@Column(name = "modifiedby")
		private String updatedBy;
		@Column(name = "active")
		private boolean active=true;
		@Column(name = "remarks")
		private String remarks;
		@Embedded
		private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
		
		@OneToMany(mappedBy = "pollsVO", cascade = CascadeType.ALL)
		@JsonManagedReference
		List<PollDetailsVO> pollDetailsVO = new ArrayList<>(); 
		
		
		@JsonGetter("active")
		public String getActive() {
			return active ? "Active" : "In-Active";

	}
	}

