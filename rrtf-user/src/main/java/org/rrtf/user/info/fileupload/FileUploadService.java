package org.rrtf.user.info.fileupload;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.annotation.Resource;

import org.rrtf.user.dao.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class FileUploadService {
	@Value("${upload.location}")
	String UPLOAD_LOCATION;

	@Resource
	UserRepository userRepository;

	// 上传文件
	public void uploadFile(String username, MultipartFile file, RedirectAttributes attributes) throws Exception {

		// 获取跟目录
		File path1 = new File(ResourceUtils.getURL("classpath:").getPath());
		if (!path1.exists()) {
			path1 = new File("");
		}
		// 如果上传目录为/static/tuoFu/img/，则可以如下获取：
		File upload = new File(path1.getAbsolutePath(), "static/tuoFu/img/");
		if (!upload.exists()) {
			upload.mkdirs();
		}
		// 上传文件为空时
		if (file.isEmpty()) {
			attributes.addFlashAttribute("file-messafe", "你需要先选择一个文件再上传");
		}
		// 不为空时
		byte[] bytes = file.getBytes();
		// 获取文件名
		String filename = file.getOriginalFilename();
		// 获取文件名扩展名
		String fileForm = filename.substring(filename.lastIndexOf("."));
		// 文件重命名
		String newFilename = UUID.randomUUID().toString() + fileForm;
		// 保存文件
		Path path = Paths.get(upload.getAbsolutePath(), newFilename);
		Files.write(path, bytes);
		// 将用户头像名保存到数据库
		userRepository.savePicture(newFilename, username);
		attributes.addFlashAttribute("file-messafe", "你已成功上传文件" + filename);
	}
}
