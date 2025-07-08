package com.efit.hrms.entity;


import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employeedocuments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentsVO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeedocumentsgen")
    @SequenceGenerator(name = "employeedocumentsgen", sequenceName = "employeedocumentsseq",initialValue = 1000000001, allocationSize = 1)
    @Column(name = "documentid")
    private Long id;

    @Column(name = "employeecode")
    private String employeeCode;

    @Column(name = "employeename")
    private String employeeName;

    @Column(name = "documentname")
    private String documentName;

    @Lob
    @Column(name = "filedata")
    private byte[] fileData;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "contenttype")
    private String contentType;

    @Column(name = "orgid")
    private Long orgId;

    @Column(name = "uploadeddate")
    private LocalDate uploadedDate = LocalDate.now();
}
