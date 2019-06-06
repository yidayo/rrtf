package org.rrtf.user.info.fileupload;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice // 定义一个针对MVC控制器的统一的异常处理
public class GlobalExceptionHandler {
	@ExceptionHandler
	public String handler(MultipartException exception, RedirectAttributes attr) {
		attr.addFlashAttribute("message", "上传错误:" + exception.getMessage());
		return "redirect:/uploadStatus";
	}

}
