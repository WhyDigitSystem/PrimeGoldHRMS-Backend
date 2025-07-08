package com.efit.hrms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.efit.hrms.dto.ChangePasswordFormDTO;
import com.efit.hrms.dto.LoginFormDTO;
import com.efit.hrms.dto.RefreshTokenDTO;
import com.efit.hrms.dto.ResetPasswordFormDTO;
import com.efit.hrms.dto.ResponsibilityDTO;
import com.efit.hrms.dto.RolesDTO;
import com.efit.hrms.dto.SignUpFormDTO;
import com.efit.hrms.dto.UserResponseDTO;
import com.efit.hrms.entity.ResponsibilityVO;
import com.efit.hrms.entity.RolesVO;
import com.efit.hrms.entity.UserVO;
import com.efit.hrms.exception.ApplicationException;

@Service
public interface AuthService {

	public void signup(SignUpFormDTO signUpRequest);

	public UserResponseDTO login(LoginFormDTO loginRequest, HttpServletRequest request) throws ApplicationException;

	public void logout(String userName);

	public void changePassword(ChangePasswordFormDTO changePasswordRequest);

	public void resetPassword(ResetPasswordFormDTO resetPasswordRequest);

	public RefreshTokenDTO getRefreshToken(String userName, String tokenId) throws ApplicationException;
	
	List<Map<String, Object>> getResponsibilityForRolesByOrgId(Long orgId);
	
	Map<String, Object> createUpdateRoles(RolesDTO rolesDTO) throws ApplicationException;
	
	public List<RolesVO> getAllRoles(Long orgId);

	public List<RolesVO> getAllActiveRoles(Long orgId);
	
	RolesVO getRolesById(Long id) throws ApplicationException;
	
	Map<String, Object> createUpdateResponsibilities(ResponsibilityDTO responsibilityDTO) throws ApplicationException;
	
	public List<ResponsibilityVO> getAllResponsibility(Long orgId);

	public List<ResponsibilityVO> getAllActiveResponsibility(Long orgId);
	
	ResponsibilityVO getResponsibilityById(Long id) throws ApplicationException;
	
	List<UserVO>getAllUsersByOrgId(Long orgId);
	
	public UserVO getUserById(Long userId);

	public UserVO getUserByUserName(String userName);

	

}
