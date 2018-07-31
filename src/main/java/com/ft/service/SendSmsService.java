package com.ft.service;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.components.VasCloudSmsSubmitCallable;
import com.ft.domain.Campaign;
import com.ft.domain.Sms;
import com.ft.repository.CampaignRepository;
import com.ft.repository.SmsRepository;

@EnableScheduling
@Service
public class SendSmsService {

    private final Logger log = LoggerFactory.getLogger(SendSmsService.class);

    @Autowired
    SmsRepository smsRepo;

    @Autowired
    CampaignRepository cpRepo;

    @Autowired
    Executor taskExecutor;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Deliver SMS Response to customer every 10 seconds
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 60000)
    public long submitPendingCampaign() throws InterruptedException {
    	int threads = 0;
    	CompletionService<Long> completionService = new ExecutorCompletionService<Long>(taskExecutor);
    	Pageable pageable = PageRequest.of(0, 1000);
    	for (Campaign cp : cpRepo.findAllPendingCampaign()) {
//    		log.debug("Found campaign: " + cp + " -- cpid" + cp.getId());
			List<Sms> tobeSubmit = smsRepo.findAllByCampaignIdAndStateLessThan(cp.getId(), 1, pageable)
					.getContent();
//			log.debug("Found SMS: " + tobeSubmit);
    		if (tobeSubmit.size() == 0) continue;
    		if (cp.getChannel().contains("VASCLOUD")) {
//    			log.debug("Campaign Channel: " + cp.getChannel());
    			VasCloudSmsSubmitCallable sendSmsTask = applicationContext.getBean(VasCloudSmsSubmitCallable.class);
    			sendSmsTask.setSmsList(tobeSubmit);
    			sendSmsTask.setCampaign(cp);
    			completionService.submit(sendSmsTask);
    			threads++;
    		}
    	}
    	Future<Long> completedFuture;
        long submitCnt = 0L;
        while (threads > 0) {
            // block until a callable completes
            completedFuture = completionService.take();
            threads --;

            // get the Widget, if the Callable was able to create it
            try {
            	Long res = completedFuture.get();
                if (completedFuture.isDone()) {
                        submitCnt += res;
                        log.info("=== ONE THREAD COMPLETED: " + threads + " REQUEST SEND: " + res);
                } else {
                        log.error("=== WHY THREAD IS NOT DONE HERE ===" );
                }
            } catch (ExecutionException e) {
                log.error("== CANNOT RUN RENEW TASK", e);
                continue;
            }
        }

    	return submitCnt;
    }
}
