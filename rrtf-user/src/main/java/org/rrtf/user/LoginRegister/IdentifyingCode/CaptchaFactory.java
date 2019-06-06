package org.rrtf.user.LoginRegister.IdentifyingCode;

import java.io.Serializable;

import com.github.bingoohuang.patchca.color.RandomColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.word.RandomWordFactory;

/**
 * @author 张自豪:zzh431@taohuichang.com
 * @date 2016/9/20
 */
public class CaptchaFactory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

	static {
		cs.setColorFactory(new RandomColorFactory());
		RandomWordFactory wf = new RandomWordFactory();
		String str = "abcdefhijkmnpqrstuvwxy";
		wf.setCharacters("345678" + str + str.toUpperCase());
		wf.setMaxLength(4);
		wf.setMinLength(4);
		cs.setWordFactory(wf);
	}

	public static ConfigurableCaptchaService getInstance() {
		return cs;
	}
}
