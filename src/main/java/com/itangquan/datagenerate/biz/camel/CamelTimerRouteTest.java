package com.itangquan.datagenerate.biz.camel;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mengdexuan on 2019/6/27 17:43.
 */
@Slf4j
@Component
public class CamelTimerRouteTest extends RouteBuilder {


	@Autowired
	CamelContext camelContext;

	@Override
	public void configure() throws Exception {

		from("timer://foo?period=2s").autoStartup(false).routeDescription("测试 process：每2秒执行1次")
				.process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				log.info("Hello world  :" + DateUtil.now());
			}
		});


		from("timer://myTimer2?period=3s").routeDescription("测试 bean：每3秒执行1次").autoStartup(false).bean(EchoBean.class);

		String fromFtpPolicy = "sftp://192.168.4.192:22/test?username=tq&password=tq123456&binary=true&passiveMode=true&delay=100&readLock=changed&maxMessagesPerPoll=10&include=.*\\.zip&delete=true&localWorkDirectory=C:\\Users\\18514\\Desktop\\test\\tmp";

		from(fromFtpPolicy).autoStartup(false).to("file://C:\\Users\\18514\\Desktop\\test").routeDescription("测试 sftp 下载文件")
				.log("Downloaded file ${file:name} from sftp complete.");


		String ftp = "ftp://localhost:21/?username=tq&password=123456&binary=true&passiveMode=true&delay=1s" +
				"&include=.*\\.zip&delete=true&localWorkDirectory=C:\\Users\\18514\\Desktop\\test\\tmp";

		from(ftp).to("file://C:\\Users\\18514\\Desktop\\test").routeDescription("测试 ftp 下载文件")
				.log("Downloaded file ${file:name} from ftp complete.");



//		文件组件
//		https://github.com/apache/camel/blob/master/components/camel-file/src/main/docs/file-component.adoc

		/**
		 *
		 * from 	file://C:\Users\18514\Desktop\test?delete=true
		 * to	 	file://C:\Users\18514\Desktop\test2?tempPrefix=..\\temp\\
		 *
		 * 	在传输完成之前，会先将文件存储到 C:\Users\18514\Desktop\temp 目录，
		 * 	传输完成之后 ，再移动到 C:\Users\18514\Desktop\test2 目录
		 *
		 */
		from("file://C:\\Users\\18514\\Desktop\\test?delete=true").autoStartup(false).routeDescription("文件传输示例：tempPrefix")
				.to("file://C:\\Users\\18514\\Desktop\\test2?tempPrefix=..\\temp\\")
				.log("trans file ${file:name} complete.");


		/**
		 * tempFileName：临时文件名，传输完成后，会自动改为源文件名
		 */
		from("file://C:\\Users\\18514\\Desktop\\test?delete=true").autoStartup(false).routeDescription("文件传输示例：tempFileName")
				.to("file://C:\\Users\\18514\\Desktop\\test2?tempFileName=${file:name}.temp")
				.log("trans file ${file:name} complete.");


	/**
	 * maxMessagesPerPoll：每次处理的文件个数
	 */
		from("file://C:\\Users\\18514\\Desktop\\test?delete=true&maxMessagesPerPoll=3")
				.autoStartup(false).routeDescription("文件传输示例：maxMessagesPerPoll")
				.to("file://C:\\Users\\18514\\Desktop\\test2?tempFileName=${file:name}.temp")
				.log("trans file ${file:name} complete.");

	}








}
