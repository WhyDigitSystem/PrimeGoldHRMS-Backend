package com.efit.hrms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.efit.hrms.repo.CompanyRepo;

@Component
public class OtScheduler {


    private final CheckInOutService checkInOutService;

    @Autowired
    private CheckInOutService otCalculationService;
     
    @Autowired
    private CompanyRepo companyRepo;

    OtScheduler(CheckInOutService checkInOutService, CompanyRepo companyRepo) {
        this.checkInOutService = checkInOutService;
        this.companyRepo = companyRepo;
    }
   

//    @Scheduled(cron = "0 0 1 * * ?") // runs daily at 1:00 AM
    @Scheduled(cron = "0 */2 * * * ?") // runs every 2 minutes
//    @Scheduled(cron = "0 0 */5 * * ?")
    public void runOtCalculationJob() {
        List<Long> orgIds = companyRepo.findActiveCompanyIds();

        for (Long orgId : orgIds) {
            try {
                System.out.println("Running OT Calculation for OrgID: " + orgId);
                otCalculationService.generateOtAndSave(orgId);
            } catch (Exception e) {
                System.err.println("OT calculation failed for org: " + orgId);
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    @Scheduled(cron = "0 0 12 * * ?")
    public void fetchAttendanceLogs() {
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);

            checkInOutService.createCheckInOutBiometricDeviceSchedular(
                    1000000001L,       // orgId
                    "AUTO SCHEDULER",  // createdBy
                    yesterday,         // fromDate
                    yesterday,         // toDate
                    "HOSUR",           // branch
                    "HOS"              // branchCode
            );

            System.out.println("Scheduler executed for date: " + yesterday);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
