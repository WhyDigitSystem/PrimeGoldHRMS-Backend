package com.efit.hrms.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.CommentsDTO;
import com.efit.hrms.dto.TicketDTO;
import com.efit.hrms.entity.CommentsVO;
import com.efit.hrms.entity.TicketVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.CommentsRepo;
import com.efit.hrms.repo.NotificationRepo;
import com.efit.hrms.repo.TicketRepo;

@Service
public class TicketServiceImpl implements TicketService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

	@Autowired
	TicketRepo ticketRepo;

	@Autowired
	CommentsRepo commentsRepo;

	@Autowired
	NotificationRepo notificationRepo;

	@Override
	public Map<String, Object> createUpdateTicket(@Valid TicketDTO ticketDTO) throws ApplicationException {

		TicketVO ticketVO;

		String message = null;

		if (ObjectUtils.isEmpty(ticketDTO.getId())) {

			ticketVO = new TicketVO();

			ticketVO.setCreatedBy(ticketDTO.getCreatedBy());
			ticketVO.setUpdatedBy(ticketDTO.getCreatedBy());

			message = "Ticket Creation Successfully";
		}

		else {
			ticketVO = ticketRepo.findById(ticketDTO.getId())
					.orElseThrow(() -> new ApplicationException("TicketVO Not Found with id: " + ticketDTO.getId()));
			ticketVO.setUpdatedBy(ticketDTO.getCreatedBy());

			message = "Ticket Updation Successfully";
		}

		ticketVO = getTicketVOFroTticketDTO(ticketVO, ticketDTO);
		ticketRepo.save(ticketVO);

//		NotificationVO notificationVO = new NotificationVO();
//
//		notificationVO.setTicketId(ticketVO.getId());
//		notificationVO.setDescription(ticketVO.getDescription());
//		notificationVO.setMessage("New ticket created by :" + ticketVO.getCreatedBy());
//		notificationVO.setNotifiedTo("EBSPL/ITADMIN");
//		notificationVO.setSubject(ticketVO.getSubject());
//		notificationVO.setCreatedBy(ticketVO.getCreatedBy());
//		notificationVO.setOrgId(ticketVO.getOrgId());
//		notificationVO.setUpdatedBy(ticketVO.getCreatedBy());
//
//		notificationRepo.save(notificationVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("ticketVO", ticketVO);
		return response;
	}

	private TicketVO getTicketVOFroTticketDTO(TicketVO ticketVO, @Valid TicketDTO ticketDTO) {

		ticketVO.setSubject(ticketDTO.getSubject());
		ticketVO.setDescription(ticketDTO.getDescription());
		ticketVO.setUserName(ticketDTO.getUserName());
		ticketVO.setOrgId(ticketDTO.getOrgId());
		ticketVO.setStatus(ticketDTO.getStatus());

		return ticketVO;

	}

	@Override
	public TicketVO uploadTicketScreenShotInBloob(MultipartFile file, Long id) throws IOException {
		TicketVO ticketVO = ticketRepo.findById(id).get();
		ticketVO.setScreenShot(file.getBytes());
		return ticketRepo.save(ticketVO);
	}

	@Override
	public Optional<TicketVO> getTicketById(Long id) {
		return ticketRepo.findById(id);
	}

	@Override
	public List<TicketVO> getTicketByUserName(String userName, Long orgId) {

		return ticketRepo.findByUserName(userName, orgId);
	}

	@Override
	public List<TicketVO> getTicketByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return ticketRepo.getByOrgId(orgId);
	}

	@Override
	public Map<String, Object> updateCreateComments(@Valid CommentsDTO commentsDTO) throws ApplicationException {

		CommentsVO commentsVO;

		String message = null;

		if (ObjectUtils.isEmpty(commentsDTO.getId())) {

			commentsVO = new CommentsVO();

			commentsVO.setCreatedBy(commentsDTO.getCreatedBy());
			commentsVO.setUpdatedBy(commentsDTO.getCreatedBy());

			message = "Comments Creation Successfully";
		}

		else {
			commentsVO = commentsRepo.findById(commentsDTO.getId()).orElseThrow(
					() -> new ApplicationException("CommentsVO  Not Found with id: " + commentsDTO.getId()));
			commentsVO.setUpdatedBy(commentsDTO.getCreatedBy());

			message = "Comments Updation Successfully";
		}

		commentsVO = getCommentsVOFromCommentsDTO(commentsVO, commentsDTO);
		commentsRepo.save(commentsVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("commentsVO", commentsVO);
		return response;
	}

	private CommentsVO getCommentsVOFromCommentsDTO(CommentsVO commentsVO, @Valid CommentsDTO commentsDTO) {

		commentsVO.setComments(commentsDTO.getComments());
		commentsVO.setOrgId(commentsDTO.getOrgId());
		commentsVO.setUserName(commentsDTO.getUserName());
		commentsVO.setTicketId(commentsDTO.getTicketId());
		return commentsVO;
	}

	@Override
	public List<CommentsVO> getCommentsByTicketId(Long ticketId, Long orgId) {
		// TODO Auto-generated method stub
		return commentsRepo.getComments(ticketId, orgId);
	}

	@Override
	public TicketVO updateTicketStatus(Long orgId, Long ticketId, String status, String comments) {

		TicketVO ticketVO = ticketRepo.findByTicketIdAndorgId(ticketId, orgId);

		if (ticketVO == null) {
			throw new RuntimeException("Ticket Records not found This Id");
		}

		ticketVO.setStatus(status);
		ticketVO.setUpdatedBy("EBSPL/ITADMIN");
		ticketVO.setNotificationFlag(true);

		ticketRepo.save(ticketVO);

		List<CommentsVO> commentsVO1 = commentsRepo.findByTicketId(ticketId);

		if (commentsVO1 == null) {

			throw new RuntimeException("Comments Records Not Found This Id");
		}

		CommentsVO commentsVO = new CommentsVO();

		commentsVO.setTicketId(ticketVO.getId());
		commentsVO.setCreatedBy(ticketVO.getCreatedBy());
		commentsVO.setComments(comments);
		commentsVO.setUpdatedBy("EBSPL/ITADMIN");
		commentsVO.setUserName(ticketVO.getUserName());
		commentsVO.setOrgId(ticketVO.getOrgId());

		commentsRepo.save(commentsVO);

		return ticketVO;

	}

	@Override
	public void deleteCommentsById(Long id) {

		commentsRepo.deleteById(id);
	}

	// Notification

	@Override
	public List<Map<String, Object>> getTicketNotification(Long orgId) {

		Set<Object[]> subGroupDetails = ticketRepo.getTicketNotification(orgId);
		return getTicket(subGroupDetails);
	}

	private List<Map<String, Object>> getTicket(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("id", sub[0] != null ? sub[0].toString() : 0);
			mp.put("ticketId", sub[1] != null ? sub[1].toString() : 0);
			mp.put("createdBy", sub[2] != null ? sub[2].toString() : "");
			mp.put("description", sub[3] != null ? sub[3].toString() : " ");
			mp.put("orgId", sub[4] != null ? sub[4].toString() : 0);
			mp.put("status", sub[5] != null ? sub[5].toString() : "");
			mp.put("subject", sub[6] != null ? sub[6].toString() : " ");
			mp.put("userName", sub[7] != null ? sub[7].toString() : " ");

			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public TicketVO updateNotification(Long orgId, Long ticketId, String status) {

		if ("Clear".equalsIgnoreCase(status)) {
			TicketVO ticketVO1 = ticketRepo.findByTicketIdAndorgId(ticketId, orgId);
			if (ticketVO1 != null) {
				ticketVO1.setStatusFlag(false);
				ticketRepo.save(ticketVO1);
				return ticketVO1;
			}
		}

		if ("ClearAll".equalsIgnoreCase(status)) {
			List<TicketVO> ticketList = ticketRepo.findByOrgId(orgId);
			for (TicketVO ticket : ticketList) {
				ticket.setStatusFlag(false);
			}
			ticketRepo.saveAll(ticketList);
			return ticketList.isEmpty() ? null : ticketList.get(0);
		}

		return null;
	}

	@Override
	public List<Map<String, Object>> getNotificationFromUser(String userName) {

		Set<Object[]> subGroupDetails = ticketRepo.getNotificationFromUser(userName);
		return getNotificationUser(subGroupDetails);
	}

	private List<Map<String, Object>> getNotificationUser(Set<Object[]> subGroupDetails) {
		List<Map<String, Object>> subgroup = new ArrayList<>();
		for (Object[] sub : subGroupDetails) {
			Map<String, Object> mp = new HashMap<>();
			mp.put("id", sub[0] != null ? sub[0].toString() : 0);
			mp.put("ticketId", sub[1] != null ? sub[1].toString() : 0);
			mp.put("updatedBy", sub[2] != null ? sub[2].toString() : "");
			mp.put("status", sub[3] != null ? sub[3].toString() : " ");
			mp.put("orgId", sub[4] != null ? sub[4].toString() : 0);
			mp.put("comments", sub[5] != null ? sub[5].toString() : "");

			subgroup.add(mp);
		}
		return subgroup;
	}

	@Override
	public TicketVO clearUserNotification(Long orgId, String userName, Long ticketId, String status) {

		if ("Clear".equalsIgnoreCase(status)) {
			// Clear notification flag for a specific TicketVO
			TicketVO ticketVO1 = ticketRepo.findByTicketIdAndorgId(ticketId, orgId);
			if (ticketVO1 != null) {
				ticketVO1.setNotificationFlag(false);
				ticketRepo.save(ticketVO1);

				// Clear notification flag for all CommentsVOs associated with this ticket
				List<CommentsVO> commentsList = commentsRepo.findByTicketIdAndorgId(ticketId, orgId);
				for (CommentsVO commentsVO1 : commentsList) {
					commentsVO1.setNotificationFlag(false);
				}
				commentsRepo.saveAll(commentsList); // Save all updated comments

				return ticketVO1;
			}
		}

		if ("ClearAll".equalsIgnoreCase(status)) {
			// Clear notification flags for all TicketVOs
			List<TicketVO> ticketList = ticketRepo.findByUserName(userName);
			for (TicketVO ticket : ticketList) {
				ticket.setNotificationFlag(false);
			}
			ticketRepo.saveAll(ticketList);

			// Clear notification flags for all CommentsVOs
			List<CommentsVO> commentList = commentsRepo.findByUserName(userName);
			for (CommentsVO commentsVO : commentList) {
				commentsVO.setNotificationFlag(false);
			}
			commentsRepo.saveAll(commentList);

			// Return the first ticket or null
			return ticketList.isEmpty() ? null : ticketList.get(0);
		}

		return null;
	}

}

