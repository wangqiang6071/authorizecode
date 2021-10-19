package com.zelu.authorizecode.exceptions;


import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zelu.authorizecode.common.ResponseCode;
import com.zelu.authorizecode.common.ServerResponse;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
public class GlobExceptionHander {
	//请求方式不对1
	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ServerResponse<String> handle1(HttpRequestMethodNotSupportedException e) {
		return ServerResponse.createByError("HTTP请求方式不对");
	}

	//系统sql语法错误2
	@ResponseBody
	@ExceptionHandler(SQLSyntaxErrorException.class)
	public ServerResponse<String> handle2(SQLSyntaxErrorException e) {
		return ServerResponse.createByError(e.getMessage());
	}

	//系统sql语法错误3
	@ResponseBody
	@ExceptionHandler(SQLException.class)
	public ServerResponse<String> handle3(SQLException e) {
		return ServerResponse.createByError(e.getMessage());
	}

	// 字符串不合法
	@ResponseBody
	@ExceptionHandler(StringException.class)
	public ServerResponse<String> handle4(StringException e) {
		return ServerResponse.createByError(e.getMessage());
	}

	//数据库插入数据外键约束
	@ResponseBody
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ServerResponse<String> handle5(SQLIntegrityConstraintViolationException e) {
		return ServerResponse.createByError(e.getMessage());
	}

	//账号信息
	@ResponseBody
	@ExceptionHandler(UnknownAccountException.class)
	public ServerResponse<String> handle6(UnknownAccountException e) {
		return ServerResponse.createByError(e.getMessage());
	}
	//密码信息
	@ResponseBody
	@ExceptionHandler(IncorrectCredentialsException.class)
	public ServerResponse<String> handle7(IncorrectCredentialsException e) {
		return ServerResponse.createByError( e.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(ClassCastException.class)
	public ServerResponse<String> handle8(ClassCastException e) {
		return ServerResponse.createByError(e.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(TokenExpiredException.class)
	public ServerResponse<String> handle9(TokenExpiredException e) {
		return ServerResponse.createByError("授权码已过期");
	}

	@ResponseBody
	@ExceptionHandler(SignatureVerificationException.class)
	public ServerResponse<String> handle10(SignatureVerificationException e) {
		return ServerResponse.createByError("密钥不正确");
	}
//	@ResponseBody
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ServerResponse<String> handle11(IllegalArgumentException e) {
//		return ServerResponse.createByError("授权码不正确");
//	}

}