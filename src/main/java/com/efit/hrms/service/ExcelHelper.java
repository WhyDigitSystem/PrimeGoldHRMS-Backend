package com.efit.hrms.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.EmployeeDTO;
import com.efit.hrms.dto.EmployeeLeaveDTO;
import com.efit.hrms.exception.ApplicationException;

@Component
public class ExcelHelper {

    public List<EmployeeDTO> parseExcelToEmployeeDTO(MultipartFile file) throws ApplicationException, IOException {
        List<EmployeeDTO> employeeList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    EmployeeDTO dto = new EmployeeDTO();

                    dto.setEmployeeType(getStringValue(row.getCell(1)));
                    dto.setEmployeeName(getStringValue(row.getCell(2)));
                    dto.setEmployeeCode(getStringValue(row.getCell(3)));
                    dto.setBranch(getStringValue(row.getCell(4)));
                    dto.setGender(getStringValue(row.getCell(5)));
                    dto.setEmail(getStringValue(row.getCell(6)));
                    dto.setJoiningDate(parseDate(row.getCell(7)));
                    dto.setGrade(getStringValue(row.getCell(8)));
                    dto.setDepartment(getStringValue(row.getCell(9)));
                    dto.setDesignation(getStringValue(row.getCell(10)));
                    dto.setReportingPerson(getStringValue(row.getCell(11)));
                    dto.setReportingPersonCode(getStringValue(row.getCell(12)));
                    dto.setReportingPersonEmail(getStringValue(row.getCell(13)));
                    dto.setReportingRole(getStringValue(row.getCell(14)));           
                    dto.setEmployeeAddress(getStringValue(row.getCell(15)));
                    dto.setDateOfBirth(parseDate(row.getCell(16)));
                    dto.setBloodGroup(getStringValue(row.getCell(17)));
                    dto.setMobileNo(parseLong(row.getCell(18)));
                    dto.setAlternativeMobileNo(parseLong(row.getCell(19)));
                    dto.setAadharNo(parseLong(row.getCell(20)));
                    dto.setPanNo(getStringValue(row.getCell(21)));
                    dto.setUanNo(parseLong(row.getCell(22)));
                    dto.setAccountNo(getStringValue(row.getCell(23)));
                    dto.setBankName(getStringValue(row.getCell(24)));
                    dto.setBranchCode(getStringValue(row.getCell(25)));
                    dto.setIfscCode(getStringValue(row.getCell(26)));
                    dto.setActive(parseBoolean(row.getCell(27)));
                    dto.setCreatedBy(getStringValue(row.getCell(28)));
                    dto.setOrgId(parseLong(row.getCell(29)));
                    dto.setResignDate(parseDate(row.getCell(30)));
                    dto.setEsiFlag(parseBoolean(row.getCell(31)));
                    dto.setEsiPercentage(parseBigDecimal(row.getCell(32)));
                    dto.setPfFlag(parseBoolean(row.getCell(33)));
                    dto.setPfPercentage(parseBigDecimal(row.getCell(34)));
                    dto.setFlag(parseBoolean(row.getCell(35)));
                    dto.setFlagValue(getStringValue(row.getCell(36)));

                    // Leave section
                    List<EmployeeLeaveDTO> leaveList = new ArrayList<>();
                    String[] codes = getSafeSplit(row, 37);
                    String[] types = getSafeSplit(row, 38);
                    String[] totals = getSafeSplit(row, 39);

                    for (int j = 0; j < codes.length; j++) {
                        if (codes[j] == null || codes[j].trim().isEmpty()) continue;
                        EmployeeLeaveDTO leave = new EmployeeLeaveDTO();
                        leave.setLeaveCode(codes[j]);
                        leave.setLeaveType(types.length > j ? types[j] : "");
                        leave.setTotalLeave(new BigDecimal(totals.length > j ? totals[j] : "0"));
                        leave.setEffectiveFrom(LocalDate.now());
                        leaveList.add(leave);
                    }

                    dto.setEmployeeLeaveDTO(leaveList);
                    employeeList.add(dto);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ApplicationException("Error at row " + (i + 1) + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new IOException("Error reading Excel file: " + e.getMessage());
        }

        return employeeList;
    }

    private Long parseLong(Cell cell) {
        try {
            String val = getStringValue(cell);
            return (val == null || val.isEmpty()) ? 0L : new BigDecimal(val).longValue();
        } catch (Exception e) {
            return 0L;
        }
    }

    private BigDecimal parseBigDecimal(Cell cell) {
        try {
            String val = getStringValue(cell);
            return (val == null || val.isEmpty()) ? BigDecimal.ZERO : new BigDecimal(val);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private Boolean parseBoolean(Cell cell) {
        try {
            if (cell == null) return false;
            CellType type = cell.getCellType();
            if (type == CellType.BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (type == CellType.STRING) {
                String val = cell.getStringCellValue().trim().toLowerCase();
                return val.equals("true") || val.equals("1");
            } else if (type == CellType.NUMERIC) {
                return cell.getNumericCellValue() == 1;
            }
        } catch (Exception e) {
            System.err.println("parseBoolean error: " + e.getMessage());
        }
        return false;
    }

    // ✅ Use this to read only yyyy-MM-dd format from string or numeric cells
    private LocalDate parseDate(Cell cell) {
        try {
            if (cell == null) return null;
            String val;

            if (cell.getCellType() == CellType.STRING) {
                val = cell.getStringCellValue().trim();
            } else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                val = getStringValue(cell).trim();
            }

            if (val.isEmpty()) return null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(val, formatter);

        } catch (Exception e) {
            System.err.println("❌ parseDate error: " + e.getMessage());
            return null;
        }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        try {
            CellType type = cell.getCellType();
            if (type == CellType.STRING) {
                return cell.getStringCellValue().trim();
            } else if (type == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            } else if (type == CellType.BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private String[] getSafeSplit(Row row, int cellIndex) {
        try {
            String value = getStringValue(row.getCell(cellIndex));
            return value == null || value.equalsIgnoreCase("NULL") ? new String[0] : value.split(",");
        } catch (Exception e) {
            return new String[0];
        }
    }
}
