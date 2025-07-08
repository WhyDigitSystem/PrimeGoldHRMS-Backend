package com.efit.hrms.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "announcement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "circulargen")
	@SequenceGenerator(name = "circulargen", sequenceName = "circularseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "announcementid")
	private Long id;
	@Column(name = "orgid")
	private long orgId;
	@Column(name = "branchcode")
	private String branchCode;
	@Column(name = "branchname")
	private String branchName;
	@Column(name = "department")
	private String department;
	
	@Column(name = "topic")
	private String topic;
	
	@Column(name = "announcement")
	private String announcement;
	
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	@Column(name = "screencode", length = 30)
	private String screenCode = "ANC";
	@Column(name = "screenname", length = 30)
	private String screenName = "Announcement";
	@Column(name = "active")
	private boolean active=true ;
	@Column(name = "expiresdate")
	private LocalDate expiresDate;
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
				

}
}