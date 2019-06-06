$(function() {
	$("#basic").click(function(e) {
		$(".flcb_cardMenu>ul>li").removeClass("on");
		$(e.currentTarget).parent().addClass("on");
		$("#basicContent").css("display", "block");
		$("#imgUploadContent").css("display", "none");
		$("#passwordChangeContent").css("display", "none");
		$("#teacherBasicContent").css("display", "none");
	})
	$("#teacherBasic").click(function(e) {
		$(".flcb_cardMenu>ul>li").removeClass("on");
		$(e.currentTarget).parent().addClass("on");
		$("#basicContent").css("display", "none");
		$("#imgUploadContent").css("display", "none");
		$("#passwordChangeContent").css("display", "none");
		$("#teacherBasicContent").css("display", "block");
	})
	$("#imgUpload").click(function(e) {
		$(".flcb_cardMenu>ul>li").removeClass("on");
		$(e.currentTarget).parent().addClass("on");
		$("#basicContent").css("display", "none");
		$("#imgUploadContent").css("display", "block");
		$("#passwordChangeContent").css("display", "none");
		$("#teacherBasicContent").css("display", "none");
	})

	$("#passwordChange").click(function(e) {
		$(".flcb_cardMenu>ul>li").removeClass("on");
		$(e.currentTarget).parent().addClass("on");
		$("#basicContent").css("display", "none");
		$("#imgUploadContent").css("display", "none");
		$("#passwordChangeContent").css("display", "block");
		$("#teacherBasicContent").css("display", "none");
	})

	$("#myFile").change(function(e) {
		var imgFile = e.currentTarget.files[0];
		var fr = new FileReader();
		fr.readAsDataURL(imgFile);
		fr.onload = function(e) {
			$(".fileUploadLabel").css({
				background : "url(" + this.result + ")",
				backgroundSize : "100% 100%"
			})
			$(".fileUploadLabel").html("")
		}
	})

	$(".userCenter_save1").click(function() {
		var form = $(this).parents("form");
		// $("#modifyMemeber").submit();
		$.mbox({
			area : [ "250px", "auto" ], // 弹框大小
			border : [ 0, .5, "#666" ],
			dialog : {
				msg : "您确定要保存修改吗",
				btns : 2, // 1: 只有一个按钮 2：两个按钮 3：没有按钮 提示框
				type : 2, // 1:对钩 2：问号 3：叹号
				btn : [ "确定", "取消" ], // 自定义按钮
				yes : function() { // 点击左侧按钮：成功
					form.submit();
				},
				no : function() { // 点击右侧按钮：失败
				}
			}
		});
	})

})

function checkForm() {
	var passtip = checkPassword();
	var conpasstip = ConfirmPassword();
	return passtip && conpasstip;
}

// 密码规范提示
function tipPassword() {
	var tipPass = document.getElementById('tipPassword');
	tipPass.innerHTML = "提示：密码中英文数字以及下划线等字符，字符长度8-20字符。"
	tipPass.className = "success";
}

// 验证密码
function checkPassword() {
	var userpasswd = document.getElementById('userPasword');
	var errPasswd = document.getElementById('tipPassword');
	var pattern = /^[\w+&@#$%*~]{8,20}$/;
	if (userpasswd.value.length == 0) {
		errPasswd.innerHTML = "提示：密码不能为空"
		errPasswd.className = "error"
		return false;
	}
	if (!pattern.test(userpasswd.value)) {
		errPasswd.innerHTML = "提示：密码不合规范"
		errPasswd.className = "error"
		return false;
	} else {
		errPasswd.innerHTML = "提示：输入合法"
		errPasswd.className = "success";
		return true;
	}
}
// 确认密码
function ConfirmPassword() {
	var userpasswd = document.getElementById('userPasword');
	var userConPassword = document.getElementById('userConfirmPasword');
	var errConPasswd = document.getElementById('tipConPassword');
	if ((userpasswd.value) != (userConPassword.value)
			|| userConPassword.value.length == 0) {
		errConPasswd.innerHTML = "提示：两次输入的密码不一致"
		errConPasswd.className = "error"
		return false;
	} else {
		errConPasswd.innerHTML = "提示：输入合法"
		errConPasswd.className = "success";
		return true;
	}
}