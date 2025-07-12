package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.BranchDTO;
import com.efit.hrms.dto.DesignationLeaveDTO;
import com.efit.hrms.dto.EmployeeDTO;
import com.efit.hrms.dto.ProjectMasterDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.entity.BranchVO;
import com.efit.hrms.entity.DesignationLeaveVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.ProjectMasterVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.service.MasterService;

@CrossOrigin
@RestController
@RequestMapping("/api/master")
public class MasterController extends BaseController {

	@Autowired
	MasterService masterService;

	public static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

	// Branch
		@GetMapping("/branch")
		public ResponseEntity<ResponseDTO> getAllBranch(@RequestParam Long orgid) {
			String methodName = "getAllBranch()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<BranchVO> branchVO = new ArrayList<>();
			try {
				branchVO = masterService.getAllBranch(orgid);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Branch information get successfully");
				responseObjectsMap.put("branchVO", branchVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Branch information receive failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/branch/{branchid}")
		public ResponseEntity<ResponseDTO> getBranchById(@PathVariable Long branchid) {
			String methodName = "getBranchById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			BranchVO branchVO = null;
			try {
				branchVO = masterService.getBranchById(branchid).orElse(null);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Branch found by ID");
				responseObjectsMap.put("Branch", branchVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Branch not found for ID: " + branchid;
				responseDTO = createServiceResponseError(responseObjectsMap, "Branch not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
	 
		@PutMapping("/createUpdateBranch")
		public ResponseEntity<ResponseDTO> createUpdateBranch(@RequestBody BranchDTO branchDTO) {
			String methodName = "createBranch()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> createdBranchVO = masterService.createUpdateBranch(branchDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,createdBranchVO.get("message"));
				responseObjectsMap.put("branchVO", createdBranchVO.get("branchVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
		        errorMsg = e.getMessage();
		        LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		        responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		    }
		    LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		    return ResponseEntity.ok().body(responseDTO);
		}
		
		// Employee

		@GetMapping("/getAllEmployeeByOrgId")
		public ResponseEntity<ResponseDTO> getAllEmployeeByOrgId(@RequestParam Long orgId) {
			String methodName = "getAllEmployeeByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> employeeVO = new ArrayList<>();
			try {
				employeeVO = masterService.getEmployeesWithCompanyInfoByOrgId(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information get successfully");
				responseObjectsMap.put("employeeVO", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("/getAllEmployeeByOrgIdAndEmployeeCode")
		public ResponseEntity<ResponseDTO> getAllEmployeeByOrgIdAndEmployeeCode(@RequestParam Long orgId,@RequestParam String employeeCode) {
			String methodName = "getAllEmployeeByOrgIdAndEmployeeCode()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<EmployeeVO> employeeVO = new ArrayList<>();
			try {
				employeeVO = masterService.getAllEmployeeByOrgIdAndEmployeeCode(orgId,employeeCode);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information get successfully");
				responseObjectsMap.put("employeeVO", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/getAllEmployee")
		public ResponseEntity<ResponseDTO> getAllEmployee() {
			String methodName = "getAllEmployeeByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<EmployeeVO> employeeVO = new ArrayList<>();
			try {
				employeeVO = masterService.getAllEmployee();
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee information get successfully");
				responseObjectsMap.put("employeeVO", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/employee/{employeeid}")
		public ResponseEntity<ResponseDTO> getEmployeeById(@PathVariable Long employeeid) {
			String methodName = "getEmployeeById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			EmployeeVO employeeVO = null;
			try {
				employeeVO = masterService.getEmployeeById(employeeid).orElse(null);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee found by ID");
				responseObjectsMap.put("Employee", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Employee not found for ID: " + employeeid;
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@PutMapping("/createUpdateEmployee")
		public ResponseEntity<ResponseDTO> createUpdateEmployee(@RequestBody EmployeeDTO employeeDTO) {
			String methodName = "createEmployee()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> createdEmployeeVO = masterService.createEmployee(employeeDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,createdEmployeeVO.get("message") );
				responseObjectsMap.put("employeeVO", createdEmployeeVO.get("createdEmployeeVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg,
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("/getReportingNameForEmployee")
		public ResponseEntity<ResponseDTO> getReportingNameForEmployee(@RequestParam Long orgId,@RequestParam String branchCode,@RequestParam String employeeCode) {
			String methodName = "getReportingNameForEmployee()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> mapp = new ArrayList<>();

			try {
				mapp = masterService.getReportingNameForEmployee(orgId,branchCode,employeeCode);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}

			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Reportnig Name Details retrieved successfully");
				responseObjectsMap.put("employeeVO", mapp);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Reportnig Name Details", errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@PostMapping("/uploadEmployeeImageInBloob")
		public ResponseEntity<ResponseDTO> uploadEmployeeImageInBloob(@RequestParam("file") MultipartFile file,
				@RequestParam Long id) {
			String methodName = "uploadEmployeeImageInBloob()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			EmployeeVO employeeVO = null;
			try {
				employeeVO = masterService.uploadEmployeeImageInBloob(file, id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error("Unable To Upload PartImage", methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Employee Image Successfully Upload");
				responseObjectsMap.put("employeeVO", employeeVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Employee Image Upload Failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@PutMapping("/createUpdateDesignationLeave")
		public ResponseEntity<ResponseDTO> createUpdateDesignationLeave(@RequestBody DesignationLeaveDTO designationLeaveDTO) {
			String methodName = "createUpdateDesignationLeave()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> designationLeaveVO = masterService.createUpdateDesignationLeave(designationLeaveDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,designationLeaveVO.get("message") );
				responseObjectsMap.put("designationLeaveVO", designationLeaveVO.get("designationLeaveVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg,
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("getDesignationLeaveById")
		public ResponseEntity<ResponseDTO> getDesignationLeaveById(@RequestParam Long id) {
			String methodName = "getDesignationLeaveById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			DesignationLeaveVO designationLeaveVO = null;
			try {
				designationLeaveVO = masterService.getDesignationLeaveById(id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DesignationLeave found by ID");
				responseObjectsMap.put("designationLeaveVO", designationLeaveVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "DesignationLeave not found for ID: " + id;
				responseDTO = createServiceResponseError(responseObjectsMap, "DesignationLeave not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("getDesignationLeaveByOrgId")
		public ResponseEntity<ResponseDTO> getDesignationLeaveByOrgId(@RequestParam Long orgId) {
			String methodName = "getDesignationLeaveByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<DesignationLeaveVO> designationLeaveVO = null;
			try {
				designationLeaveVO = masterService.getDesignationLeaveByOrgId(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "DesignationLeave found by ORGID");
				responseObjectsMap.put("designationLeaveVO", designationLeaveVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "DesignationLeave not found for orgID: " + orgId;
				responseDTO = createServiceResponseError(responseObjectsMap, "DesignationLeave not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		
		@GetMapping("/getLeaveDetailsFromDesignationLeave")
		public ResponseEntity<ResponseDTO> getLeaveDetailsFromDesignationLeave(@RequestParam Long orgId,@RequestParam String designationCode,@RequestParam String leaveApplicable) {
			String methodName = "getLeaveDetailsFromDesignationLeave()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> mapp = new ArrayList<>();

			try {
				mapp = masterService.getLeaveDetailsFromDesignationLeave(orgId,designationCode,leaveApplicable);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}

			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "LeaveDetails retrieved successfully");
				responseObjectsMap.put("employeeVO", mapp);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve LeaveDetails", errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		
		//ProjectMaster
		 
		 @PutMapping("/createUpdateProjectMaster")
			public ResponseEntity<ResponseDTO> createUpdateProjectMaster(@Valid @RequestBody ProjectMasterDTO projectMasterDTO) {
				String methodName = "createUpdateProjectMaster()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				try {
					Map<String, Object> projectMasterVO = masterService.createUpdateProjectMaster(projectMasterDTO);
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, projectMasterVO.get("message"));
					responseObjectsMap.put("projectMasterVO", projectMasterVO.get("projectMasterVO"));
					responseDTO = createServiceResponse(responseObjectsMap);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
					responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
		 
		 
		 @GetMapping("getProjectMasterByOrgId")
			public ResponseEntity<ResponseDTO> getProjectMasterByOrgId(@RequestParam Long orgId) {
				String methodName = "getProjectMasterByOrgId()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				List<ProjectMasterVO> projectMasterVO = null;
				try {
					projectMasterVO = masterService.getProjectMasterByOrgId(orgId);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ProjectMaster found by ORGID");
					responseObjectsMap.put("projectMasterVO", projectMasterVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "ProjectMaster not found for orgID: " + orgId;
					responseDTO = createServiceResponseError(responseObjectsMap, "ProjectMaster not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}
		
		 
		 @GetMapping("getProjectMasterById")
			public ResponseEntity<ResponseDTO> getProjectMasterById(@RequestParam Long id) {
				String methodName = "getProjectMasterById()";
				LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
				String errorMsg = null;
				Map<String, Object> responseObjectsMap = new HashMap<>();
				ResponseDTO responseDTO = null;
				ProjectMasterVO projectMasterVO = null;
				try {
					projectMasterVO = masterService.getProjectMasterById(id);
				} catch (Exception e) {
					errorMsg = e.getMessage();
					LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				}
				if (StringUtils.isEmpty(errorMsg)) {
					responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "ProjectMaster found by ID");
					responseObjectsMap.put("projectMasterVO", projectMasterVO);
					responseDTO = createServiceResponse(responseObjectsMap);
				} else {
					errorMsg = "ProjectMaster not found for ID: " + id;
					responseDTO = createServiceResponseError(responseObjectsMap, "ProjectMaster not found", errorMsg);
				}
				LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
				return ResponseEntity.ok().body(responseDTO);
			}

		 //uploadEmployee
		 
		

		     @PostMapping("/upload")
		     public ResponseEntity<Map<String, Object>> uploadEmployeeExcel(@RequestParam("file") MultipartFile file) {
		         try {
		             Map<String, Object> response = masterService.uploadEmployeeExcel(file);
		             return ResponseEntity.ok(response);
		         } catch (ApplicationException e) {
		             return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
		         }
		     }
		 }


