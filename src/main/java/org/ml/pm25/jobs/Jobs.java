package org.ml.pm25.jobs;

import java.util.List;

import org.apache.log4j.Logger;
import org.ml.pm25.activemq.provider.ProviderMQ;
import org.ml.pm25.dao.SpiderCrawlDao;
import org.ml.pm25.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class Jobs {
	private static Logger logger = Logger.getLogger(Jobs.class);
	@Autowired
	private SpiderCrawlDao spiderCrawlDao;
	@Autowired
	private ProviderMQ providerMQ;
	@Scheduled(cron="0 0/1 * * * ?")
	public void cronJob(){
		List<String> list = spiderCrawlDao.queryCityCode();
		if(CollectionUtils.isEmpty(list)){
			logger.info("list为空");
			return;
		}
		logger.info("开始定时任务");
		for(String str : list){
			try {
				providerMQ.sendMessage(str);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
		}
		logger.info("结束定时任务");
	}
}
