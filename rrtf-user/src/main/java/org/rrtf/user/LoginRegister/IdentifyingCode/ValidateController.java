package org.rrtf.user.LoginRegister.IdentifyingCode;

import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.FilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("user")
public class ValidateController  {

	// 日志对象
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static ConfigurableCaptchaService cs = CaptchaFactory.getInstance();

	private static List<FilterFactory> factories;

	static {
		factories = new ArrayList<FilterFactory>();
		factories.add(new CurvesRippleFilterFactory(cs.getColorFactory()));
		factories.add(new MarbleRippleFilterFactory());
		factories.add(new DoubleRippleFilterFactory());
		factories.add(new WobbleRippleFilterFactory());
		factories.add(new DiffuseRippleFilterFactory());
	}

	@RequestMapping("/getImage")
	public void getImage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			cs.setFilterFactory(factories.get(new Random().nextInt(5)));
			setResponseHeaders(response);
			
			String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
			session.setAttribute("code", token);
			System.out.println(token);
			logger.info("当前的SessionID = " + session.getId() + "，  验证码 = " + token);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}

}
