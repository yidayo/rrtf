package org.rrtf.lesson.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.rrtf.lesson.entity.LessonData;
import org.rrtf.lesson.entity.Member;
import org.rrtf.lesson.entity.Teacher;
import org.rrtf.lesson.entity.User;
import org.rrtf.lesson.mapper.LessonDataRepository;
import org.rrtf.lesson.mapper.MemberRepository;
import org.rrtf.lesson.mapper.PubLessonRepository;
import org.rrtf.lesson.mapper.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/lessonData")
public class LessonDataController {// 上传与下载

	@Autowired
	LessonDataRepository lessonDataRepository;

	@Autowired
	PubLessonRepository lessonRepository;
	@Autowired//查找teacher表用的,被调用了,暂时先放这里
	TeacherRepository teacherRepository;
	@Autowired//查找teacher表用的,被调用了,暂时先放这里
	MemberRepository memberRepository;
	
	@Autowired//redis
    private RedisTemplate redisTemplate;

	@Value("${upload.location}")
	String UPLOAD_LOCATION;// 从属性文件中读取上传文件最终保存的位置
	@Value("${download.location}")
	String DOWNLOAD_LOCATION;// 从属性文件中读取上传文件最终保存的位置

	@RequestMapping("/findByLessonId") // 显示所有资料:http://localhost:8080/lessonData/findByLessonId
	public List<LessonData> findByLessonId(int lessonId) {
		return lessonDataRepository.findByLessonId(lessonId);
	}

	// 文件上传
	@PostMapping("/uploadFile") // 上传资料:http://localhost:8080/lessonData/uploadFile
	public String uploadFile(HttpServletRequest request) throws IOException {
		// ↓权限检测
		int lessonId = Integer.parseInt(request.getParameter("lessonId"));
		int lessonTeacherId = lessonRepository.findByLessonIdAndLessonStatus(lessonId, 1).getTeacherId();
		int thisTeacherId = getTeacherId(request);
		if (thisTeacherId != lessonTeacherId)
			return "你不能胡乱给别人的课程上传资料";
		// ↑权限检测
		// ↓获取路径
		String urlPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
		// ↑获取路径
		// ↓处理上传的文件,dataName是原文件名,fileName是保存时的文件名
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("lessonData");
		LessonData data = new LessonData();
		if (file.isEmpty()) {
			return "文件为空,原因未知";
		}
		byte[] bytes = file.getBytes();// 获取上传文件的字节流
		String dataName = file.getOriginalFilename();// 获取上传文件名称
		int beginIndex = dataName.length() - 10 < 0 ? 0 : dataName.length() - 10;
		String fileName = "LessonData" + String.valueOf(new Date().getTime()) + dataName.substring(beginIndex);
		Path path = Paths.get(urlPath, UPLOAD_LOCATION, fileName);// 定义保存路径nio包
		Files.write(path, bytes);// 写入最终保存的位置
		data.setDataName(dataName);
		data.setFileName(fileName);
		data.setLessonId(Integer.parseInt(request.getParameter("lessonId")));
		lessonDataRepository.save(data);
		// ↑处理上传的文件,dataName是原文件名,fileName是保存时的文件名
		return "文件上传成功,请不要重复上传";
	}

	// 文件列表查看:localhost:8080/lessonData/showFiles?lessonId=20
	@RequestMapping("/showFiles")
	private List<LessonData> showFiles(int lessonId) {
		return lessonDataRepository.findByLessonId(lessonId);
	}

	// 文件下载:localhost:8080/lessonData/downloadFile?dataName=1.txt
	@RequestMapping("/downloadFile")
	private String downloadFile(HttpServletResponse response, String dataName) {
		// ↓获取路径
		String urlPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
		// ↑获取路径
		String downloadFilePath = DOWNLOAD_LOCATION;// 被下载的文件在服务器中的路径
		String fileName = lessonDataRepository.findDataName(dataName);
		File file = new File(urlPath + downloadFilePath + fileName);
		System.out.println(urlPath + downloadFilePath + fileName);// 测试
		if (file.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream outputStream = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					outputStream.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				return "下载成功";
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "下载失败";
	}

	// cookie,redis和session相关
	// 基础,从cookie中获取String
	private String readCookie(String name, HttpServletRequest request) {
		// System.out.println(response+"-----"+request);
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					System.out.println("cookie的值:" + cookie.getValue());
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	// 基础,从redis中获取user
	private User getUser(String username) {
		ValueOperations opsForValue = redisTemplate.opsForValue();
		JSONObject object = (JSONObject) opsForValue.get(username);
		if (object != null) {
			User user = object.toJavaObject(User.class);
			System.out.println("user--" + user);
			return user;
			// return ((JSONObject)
			// redisTemplate.opsForValue().get(username)).toJavaObject(User.class);
		}
		return null;
	}

	// 基础,获取userId
	private int getUserId(HttpServletRequest request) {
		// addCookie();//创建cookie,测试用
		String username = readCookie("name", request);// 从cookie中获取-username为"user"
		if (username != null) {
			User user = getUser(username);
			if (user != null) {
				System.out.println("userId" + user.getUserId());
				return user.getUserId();// 从redis中获取-userId为2
			}
		}
		return -1;
	}

	// 获取session中的教师信息,没有则尝试从数据库中查询,并添加到session中
	private Teacher getSessionTeacher(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if (teacher == null) {
			int userId = getUserId(request);
			teacher = teacherRepository.findByUserId(userId);
			if (teacher != null) {
				session.setAttribute("teacher", teacher);
			}
		}
		System.out.println("teacher--" + teacher);
		return teacher;
	}

	// 获取session中的会员信息,没有则尝试从数据库中查询,并添加到session中
	private Member getSessionMember(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			int userId = getUserId(request);
			member = memberRepository.findByUserId(userId);
			if (member != null)
				session.setAttribute("member", member);
		}
		System.out.println("member--" + member);
		return member;
	}

	// 偷懒,获取teacherId
	public int getTeacherId(HttpServletRequest request) {
		if (isLanding(request)) {
			Teacher teacher = getSessionTeacher(request);
			if (teacher != null) {
				System.out.println("teacherId" + teacher.getTeacherId());
				return teacher.getTeacherId();
			}
		}
		return -1;
	}

	// 偷懒,获取memberId
	public int getMemberId(HttpServletRequest request) {
		Member member = getSessionMember(request);
		if (isLanding(request)) {
			if (member != null) {
				System.out.println("memberId" + member.getMemberId());
				return member.getMemberId();
			}
		}
		return -1;
	}

	// 偷懒,看是否登陆(这里的name后期要改)
	public boolean isLanding(HttpServletRequest request) {
		// addCookie();//创建cookie,临时方法
		String username = readCookie("user", request);// 从cookie中获取-username为"user"
		System.out.println(username != null && getUser(username) != null);
		return username != null && getUser(username) != null;// 两个都非空则处于登录状态
	}

	// 偷懒,看是否管理员(这里的name2后期要改)
	public boolean isAdmin(HttpServletRequest request) {
		// addCookie2();//创建cookie,临时方法
		String adminname = readCookie("user", request);
		System.out.println("adminname" + adminname);
		System.out.println("getUser" + getUser(adminname));
		boolean isAdmin = adminname != null && getUser(adminname) != null;
		System.out.println("是管理员吗?" + isAdmin);
		return isAdmin;// 两个都非空是管理员
	}
}
