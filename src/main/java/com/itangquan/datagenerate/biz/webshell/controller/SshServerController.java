package com.itangquan.datagenerate.biz.webshell.controller;

import ch.ethz.ssh2.Connection;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.itangquan.datagenerate.base.util.HelpMe;
import com.itangquan.datagenerate.biz.webshell.SshServerFile;
import com.itangquan.datagenerate.biz.webshell.SshServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class SshServerController {

	@RequestMapping("/sshServerList")
	public String sshServerList(Model model) {

		log.info("sshServerList页面");

		List<SshServerInfo> infoList = Lists.newArrayList();

		if (FileUtil.exist(SshServerFile.file)){
			List<String> list = FileUtil.readUtf8Lines(SshServerFile.file);

			infoList = list.stream().map(item -> {
				return JSONUtil.toBean(item, SshServerInfo.class);
			}).collect(Collectors.toList());
		}


		model.addAttribute("infoList",infoList);

		return "sshServerList";
	}



	@RequestMapping("/webShell")
	public String webShell(Model model,String id) {

		log.info("进入 webShell 页面...");

		List<String> list = FileUtil.readUtf8Lines(SshServerFile.file);

		List<String> tempList = list.stream().filter(item -> {
			return JSONUtil.toBean(item, SshServerInfo.class).getId().equals(id);
		}).collect(Collectors.toList());

		model.addAttribute("sshInfo",JSONUtil.toBean(tempList.get(0),SshServerInfo.class));

		return "webShell";
	}



	@RequestMapping("/checkSshServer")
	@ResponseBody
	public Boolean checkSshServer(SshServerInfo info){

		log.info("验证信息：{}",info);

		// 建立连接
		Connection conn = new Connection(info.getHostname(), 22);
		// 连接
		try {
			conn.connect();
		} catch (IOException e) {
		}

		// 授权
		boolean isAuthenticate = false;
		try {
			isAuthenticate = conn.authenticateWithPassword(info.getUsername(), info.getPassword());
		} catch (IOException e) {
		}
		if (isAuthenticate) {
			conn.close();
			return true;
		}

		return false;
	}



	@RequestMapping("/addSshServer")
	@ResponseBody
	public Boolean addSshServer(SshServerInfo info){

		log.info("添加SshServer信息：{}",info);

		if (checkSshServer(info)){
			info.setId(HelpMe.uuid());

			FileUtil.appendUtf8Lines(Lists.newArrayList(JSONUtil.toJsonStr(info)), SshServerFile.file);

		}else {
			return false;
		}

		return true;
	}




	@RequestMapping("/delSshServer")
	@ResponseBody
	public Boolean delSshServer(String id){

		log.info("删除SshServer信息，id：{}",id);

		List<SshServerInfo> infoList = Lists.newArrayList();

		if (FileUtil.exist(SshServerFile.file)){
			List<String> list = FileUtil.readUtf8Lines(SshServerFile.file);

			list.stream().forEach(item -> {
				boolean flag = JSONUtil.
						toBean(item, SshServerInfo.class).getId().equals(id);
				if (!flag){
					infoList.add(JSONUtil.toBean(item,SshServerInfo.class));
				}
			});


			List<String> jsonList = infoList.stream().map(item -> {
				return JSONUtil.toJsonStr(item);
			}).collect(Collectors.toList());

			FileUtil.writeUtf8Lines(jsonList, SshServerFile.file);
		}

		return true;
	}




}



















