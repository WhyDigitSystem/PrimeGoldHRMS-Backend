package com.efit.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.efit.hrms.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rolepermission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermissionVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolepermissiongen")
	@SequenceGenerator(name = "rolepermissiongen", sequenceName = "rolepermissionseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "rolepermissionid")
	private Long id;
	
	@Column(name ="screenid")
	private String screenId;
	
	@Column(name ="screenname")
    private String screenName;
    
	@Column(name = "canread")
	private boolean canRead;
	
	@Column(name = "canwrite")
	private boolean canWrite;
	
	@Column(name = "candelete")
	private boolean canDelete;

	
	
	

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

	
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "rolepermissionheaderid")
	private RolesPermissionHeaderVO rolesPermissionHeaderVO;
	

	

}

