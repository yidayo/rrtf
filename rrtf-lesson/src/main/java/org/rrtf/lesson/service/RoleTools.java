package org.rrtf.lesson.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.rrtf.lesson.entity.Member;
import org.rrtf.lesson.entity.Teacher;
import org.rrtf.lesson.entity.User;
import org.rrtf.lesson.mapper.MemberRepository;
import org.rrtf.lesson.mapper.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class RoleTools {
	
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpServletRequest request;

	@Autowired//尝试
    private RedisTemplate redisTemplate;
	
	@Autowired//查找teacher表用的,被调用了,暂时先放这里
	TeacherRepository teacherRepository;
	@Autowired//查找teacher表用的,被调用了,暂时先放这里
	MemberRepository memberRepository;
	
	//基础,从cookie中获取String
	private String readCookie(String name) {
		//System.out.println(response+"-----"+request);
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					System.out.println("cookie的值:"+cookie.getValue());
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	//基础,从redis中获取user
	private User getUser(String username) {
		ValueOperations opsForValue = redisTemplate.opsForValue();
		JSONObject object = (JSONObject) opsForValue.get(username);
		if(object!=null) {
			User user = object.toJavaObject(User.class);
			System.out.println("user--"+user);
			return user;
			//return ((JSONObject) redisTemplate.opsForValue().get(username)).toJavaObject(User.class);
		}
		return null;
	}
	//基础,获取userId
	private int getUserId() {
		//addCookie();//创建cookie,测试用
		String username = readCookie("name");//从cookie中获取-username为"user"
		if(username!=null) {
			User user = getUser(username);
			if(user!=null) {
				System.out.println("userId"+user.getUserId());
				return user.getUserId();//从redis中获取-userId为2
			}
		}
		return -1;
	}
	//获取session中的教师信息,没有则尝试从数据库中查询,并添加到session中
	private Teacher getSessionTeacher() {
		HttpSession session = request.getSession();
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if(teacher==null) {
			int userId = getUserId();
			teacher = teacherRepository.findByUserId(userId);
			if(teacher!=null) {
				session.setAttribute("teacher", teacher);
			}
		}
		System.out.println("teacher--"+teacher);
		return teacher;
	}
	//获取session中的会员信息,没有则尝试从数据库中查询,并添加到session中
	private Member getSessionMember() {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		if(member==null) {
			int userId = getUserId();
			member = memberRepository.findByUserId(userId);
			if(member!=null) session.setAttribute("member", member);
		}
		System.out.println("member--"+member);
		return member;
	}
	//偷懒,获取teacherId
	public int getTeacherId() {
		if(isLanding()) {
			Teacher teacher = getSessionTeacher();
			if(teacher!=null) {
				System.out.println("teacherId"+teacher.getTeacherId());
				return teacher.getTeacherId();
			}
		}
		return -1;
	}
	//偷懒,获取memberId
	public int getMemberId() {
		Member member = getSessionMember();
		if(isLanding()) {
			if(member!=null) {
				System.out.println("memberId"+member.getMemberId());
				return member.getMemberId();
			}
		}
		return -1;
	}
	//偷懒,看是否登陆(这里的name后期要改)
	public boolean isLanding() {
		//addCookie();//创建cookie,临时方法
		String username = readCookie("user");//从cookie中获取-username为"user"
		System.out.println(username!=null&&getUser(username)!=null);
		return username!=null&&getUser(username)!=null;//两个都非空则处于登录状态
	}
	//偷懒,看是否管理员(这里的name2后期要改)
	public boolean isAdmin() {
		//addCookie2();//创建cookie,临时方法
		String adminname = readCookie("user");
		System.out.println("adminname"+adminname);
		System.out.println("getUser"+getUser(adminname));
		boolean isAdmin = adminname!=null&&getUser(adminname)!=null;
		System.out.println("是管理员吗?"+isAdmin);
		return isAdmin;//两个都非空是管理员
	}
	/*//临时方法添加cookie
	public void addCookie() {
		Cookie cookie = new Cookie("name", "user");//(key,value)
	    cookie.setPath("/");// 这个要设置
	    cookie.setMaxAge(365 * 24 * 60 * 60);// 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
	    //cookie.setMaxAge(0);
	    response.addCookie(cookie);//添加Cookie
	}
	//临时方法添加cookie
	public void addCookie2() {
		Cookie cookie = new Cookie("name2", "user1");//(key,value)
	    cookie.setPath("/");// 这个要设置
	    cookie.setMaxAge(365 * 24 * 60 * 60);// 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
	    //cookie.setMaxAge(0);
	    response.addCookie(cookie);//添加Cookie
	}*/
}
