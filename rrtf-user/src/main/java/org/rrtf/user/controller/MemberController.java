package org.rrtf.user.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("member")
public class MemberController {

	@RequestMapping("reference")
	@RequiresPermissions("reference")
	public String userinfo() {
		return "userinfo";
	}
}
