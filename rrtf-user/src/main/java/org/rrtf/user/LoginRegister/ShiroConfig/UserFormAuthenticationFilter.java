package org.rrtf.user.LoginRegister.ShiroConfig;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.rrtf.user.entity.User;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class UserFormAuthenticationFilter extends FormAuthenticationFilter {

	@Resource
	RedisTemplate redisTemplate;

	// 将登录用户保存到session中
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		// 获取已登陆的用户信息
		User user = (User) subject.getPrincipal();
		// 获取session
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpSession session = httpServletRequest.getSession();
		// 把用户信息保存到session
		session.setAttribute("user", user);
		String s = JSON.toJSONString(user);
		JSONObject object = JSON.parseObject(s);
		redisTemplate.opsForValue().set(user.getUsername(), object, 60*60000, TimeUnit.SECONDS);
		return super.onLoginSuccess(token, subject, request, response);
	}

	// 校验验证码
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		// 从session获取正确的验证码
		HttpSession session = ((HttpServletRequest) request).getSession();
		// 获取用户输入的信息
		String randomcode = request.getParameter("userCode");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 从session中取出验证码
		String validateCode = (String) session.getAttribute("code");
		if (randomcode != null && validateCode != null) {
			if (!randomcode.equalsIgnoreCase(validateCode)) {
				// kaptchaValidateFailed表示验证码错误
				request.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");
				/*
				 * 将用户输入的用户名和密码返回到页面显示，以免用户再次输入
				 */
				request.setAttribute("username", username);
				request.setAttribute("password", password);
				// 拒绝访问，不再校验账号和密码
				return true;
			}
		}
		return super.onAccessDenied(request, response, mappedValue);
	}
}
