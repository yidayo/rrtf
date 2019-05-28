package org.rrtf.lesson.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rrtf.lesson.entity.PubLesson;
import org.rrtf.lesson.mapper.MyLessonsRepository;
import org.rrtf.lesson.mapper.PubLessonRepository;
import org.rrtf.lesson.service.RoleTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/pubLesson")
public class PubLessonController {//留着
	
	@Autowired
	RoleTools tools;
	
	@Value("${upload.location}")
	String UPLOAD_LOCATION;// 从属性文件中读取上传文件最终保存的位置
	
	@Autowired
	PubLessonRepository lessonRepository;
	
	@Autowired//查找mylessons中间表用的,被调用了,暂时先放这里
	MyLessonsRepository myLessonsRepository;
	
	//发布:localhost:8080/pubLesson/release(将数据填入后发送到这里
	@RequestMapping("/release")
	public String release(HttpServletRequest request) throws IOException {
		int teacherId = tools.getTeacherId();
		if(teacherId==-1) {
			return "不是老师不允许创建课程";
		}
		//↓获取路径
		String urlPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
		//↑获取路径
		//↓上课日期格式化
		PubLesson lesson = new PubLesson();
		lesson.setLessonName(request.getParameter("lessonName"));
		String stringDate = request.getParameter("date");
		Timestamp ts = Timestamp.valueOf(stringDate);
		//↑上课日期格式化
		lesson.setDate(ts);
		lesson.setBeginTime(request.getParameter("beginTime"));
		lesson.setEndTime(request.getParameter("endTime"));
		lesson.setSuitStu(request.getParameter("suitStu"));
		lesson.setLessonType(request.getParameter("lessonType"));
		lesson.setTeacher(request.getParameter("teacher"));
		lesson.setLessonGroup(request.getParameter("lessonGroup"));
		//↓处理上传的图片
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("lessonPicture");
		if (file.isEmpty()) {
			return "图片为空,原因未知";
		}
		byte[] bytes = file.getBytes();//获取上传文件的字节流
		String fileName = file.getOriginalFilename();//获取上传文件名称
		int beginIndex = fileName.length()-10<0?0:fileName.length()-10;
		fileName = "PubLesson"+String.valueOf(new Date().getTime())+fileName.substring(beginIndex);
		Path path = Paths.get(urlPath,UPLOAD_LOCATION, fileName);//定义保存路径nio包
		Files.write(path, bytes);//写入最终保存的位置
		//↑处理上传的图片
		lesson.setLessonPicture(fileName);
		lesson.setLessonIntro(request.getParameter("lessonIntro"));
		lesson.setLessonWay(request.getParameter("lessonWay"));
		lesson.setLessonGuide(request.getParameter("lessonGuide"));
		lesson.setTeacherId(teacherId);
		lesson.setGroupId(1);
		lesson.setLesStuNum(0);
		lesson.setLessonMoney(20.0f);
		lesson.setUploadTime(new java.sql.Date(new Date().getTime()));
		lesson.setLessonStatus(1);
		lessonRepository.save(lesson);
		return ""+lesson.getLessonId();
	}
	
	//删除:localhost:8080/pubLesson/delete?lessonId=1
	@RequestMapping("/delete")
	public String delete(int lessonId) {
		int lessonTeacherId = lessonRepository.findByLessonIdAndLessonStatus(lessonId,1).getTeacherId();
		int thisTeacherId = tools.getTeacherId();
		if(lessonTeacherId!=thisTeacherId) return "你在尝试删除谁的课程?";
		lessonRepository.delete(lessonId);
		return "删除成功";
	}
	//修改:localhost:8080/pubLesson/modify?lessonId=1
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request,int lessonId) throws IOException {
		int thisTeacherId = tools.getTeacherId();
		int lessonTeacherId = lessonRepository.findByLessonIdAndLessonStatus(lessonId,1).getTeacherId();
		if(lessonTeacherId!=thisTeacherId) {
			return "不是你的课程不允许改";
		}
		//↓获取路径
		String urlPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
		//↑获取路径
		PubLesson lesson = lessonRepository.findByLessonIdAndLessonStatus(lessonId,1);
		//↓上课日期格式化
		lesson.setLessonName(request.getParameter("lessonName"));
		String stringDate = request.getParameter("date");
		Timestamp ts = Timestamp.valueOf(stringDate);
		//↑上课日期格式化
		lesson.setDate(ts);
		lesson.setBeginTime(request.getParameter("beginTime"));
		lesson.setEndTime(request.getParameter("endTime"));
		lesson.setSuitStu(request.getParameter("suitStu"));
		lesson.setLessonType(request.getParameter("lessonType"));
		lesson.setTeacher(request.getParameter("teacher"));
		lesson.setLessonGroup(request.getParameter("lessonGroup"));
		//↓处理上传的图片
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("lessonPicture");
		if (!file.isEmpty()) {//如果非空,也就是说用户修改了图片
			byte[] bytes = file.getBytes();//获取上传文件的字节流
			String fileName = file.getOriginalFilename();//获取上传文件名称
			int beginIndex = fileName.length()-10<0?0:fileName.length()-10;
			fileName = "PubLesson"+String.valueOf(new Date().getTime())+fileName.substring(beginIndex);
			Path path = Paths.get(urlPath,UPLOAD_LOCATION, fileName);//定义保存路径nio包
			Files.write(path, bytes);//写入最终保存的位置
			lesson.setLessonPicture(fileName);
		}
		//↑处理上传的图片
		lesson.setLessonIntro(request.getParameter("lessonIntro"));
		lesson.setLessonWay(request.getParameter("lessonWay"));
		lesson.setLessonGuide(request.getParameter("lessonGuide"));
		//lesson.setTeacherId(1);
		//lesson.setGroupId(1);
		//lesson.setLesStuNum(0);
		//lesson.setLessonMoney(20.0f);
		//lesson.setLesStuNum(0);
		//lesson.setUploadTime(new java.sql.Date(new Date().getTime()));
		//lesson.setLessonStatus(1);
		lesson.setLessonId(lessonId);//比添加多了这一行
		lessonRepository.save(lesson);
		return ""+lesson.getLessonId();
	}
	
	//显示某一天后所有课程信息(最新公开课):localhost:8080/pubLesson/showLessons
	@RequestMapping("/showLessons")
	public List<PubLesson> showLesson() {
	    Date date = new Date();
	    Timestamp ts = new Timestamp(date.getTime());
	    //System.out.println(ts);
		return lessonRepository.findByDateAfterAndLessonStatus(ts,1);
	}
	
	//显示某一门课程信息:localhost:8080/pubLesson/showLesson?lessonId=63
	@RequestMapping("/showLesson")
	public PubLesson showLesson(int lessonId) {
		return lessonRepository.findByLessonIdAndLessonStatus(lessonId,1);
	}
	
	//分页显示我报名的公开课:http://localhost:8011/pubLesson/myPubLesson?no=1
	@RequestMapping("/myPubLesson")
	public Page<PubLesson> myLesson(int no) {
		int memberId = tools.getMemberId();//获取userId
		Pageable pageable = new PageRequest(no-1, 2);//稍微处理了一下
		List<Integer> lessonIdList = myLessonsRepository.findMyLessons(memberId);
		return lessonRepository.findByLessonIdInAndLessonStatus(lessonIdList,pageable,1);
	}
	
	//分页显示所有课程信息:http://localhost:8080/pubLesson/pageShowLessons
	@RequestMapping("/pageShowLessons")
	public Page<PubLesson> pageShowLessons(int no) {
		Pageable pageable = new PageRequest(no-1, 2);//稍微处理了一下
		return lessonRepository.findAll(pageable);
	}
	
	//分页显示我教的公开课http://localhost:8011/pubLesson/myPubLesson?no=1
	@RequestMapping("/teachLesson")
	public Page<PubLesson> teachLesson(HttpServletResponse response,HttpServletRequest request,int no) {
		int teacherId = tools.getTeacherId();//获取teacherId
		if(teacherId!=-1) {
			//System.out.println("teacherId="+teacherId);
			Pageable pageable = new PageRequest(no-1, 2);//稍微处理了一下
			return lessonRepository.findByTeacherIdAndLessonStatus(teacherId,pageable,1);
		}
		return null;
	}
	
	//分页显示最新的三节公开课:localhost:8080/pubLesson/show3Lessons
	@RequestMapping("/show3Lessons")
	public Page<PubLesson> show3Lessons() {
		Pageable pageable = new PageRequest(0, 3);//稍微处理了一下
		Date date = new Date();
	    Timestamp ts = new Timestamp(date.getTime());
		return lessonRepository.findByDateAfterAndLessonStatus(ts,pageable,1);
	}
	
	//名称,讲师,分类多条件模糊查询,分页:localhost:8080/pubLesson/fuzzySearch?no=1&lessonName=java课1&teacher=教师1&lessonType=
	@RequestMapping("/fuzzySearch")
	public Page<PubLesson> fuzzySearch(int no,String lessonName,String teacher,String lessonType) {
		if(!tools.isAdmin()) return null;
		Pageable pageable = new PageRequest(no-1, 10);//稍微处理了一下
		System.out.println(lessonName+"|"+teacher+"|"+lessonType);
		return lessonRepository.findByLessonNameContainingAndTeacherContainingAndLessonTypeContaining(lessonName,teacher,lessonType,pageable);
	}
	
	//管理员关闭功能与恢复功能
	@RequestMapping("/adminStatus")
	public String adminStatus(int lessonStatus,@RequestParam("lessonId") List<Integer> lessonId) {
		System.out.println(lessonId);
		if(!tools.isAdmin()) return null;
		int n = lessonRepository.adminStatus(lessonStatus, lessonId);
		return "处理了"+n+"条";
	}
	//管理员删除功能
	@RequestMapping("/adminDel")
	public String adminDel(@RequestParam("lessonId") List<Integer> lessonId) {
		if(!tools.isAdmin()) return null;
		int n = lessonRepository.adminDel(lessonId);
		return "删除了"+n+"条";
	}
	
	//购买课程人数加一localhost:8080/pubLesson/lesStuNumAdd?lessonId=70
	@RequestMapping("/lesStuNumAdd")
	public String lesStuNumAdd(int lessonId) {
		if(lessonRepository.lesStuNumAdd(lessonId)==1)
		return "购买成功";
		return "购买失败";
	}
}
