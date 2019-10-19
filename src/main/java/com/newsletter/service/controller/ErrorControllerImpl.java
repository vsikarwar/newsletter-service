package com.newsletter.service.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorControllerImpl implements ErrorController{
	
	@RequestMapping(value="/error")
	@ResponseBody
	public ResponseEntity<Map<String, String>> error(HttpServletRequest request){
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Object errorMsg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
		Integer statusCode = Integer.valueOf(status.toString());
		Map<String, String> error = new HashMap<>();
		if(StringUtils.isEmpty(errorMsg.toString())) {
			error.put("ERROR", "Something went wrong");
			statusCode = 500;
		}else {
			error.put("ERROR", errorMsg.toString());
		}
		return ResponseEntity.status(statusCode).body(error);
		
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
