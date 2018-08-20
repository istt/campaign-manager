package com.ft.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
public class VasCloudSendSmsService {

    private final Logger log = LoggerFactory.getLogger(VasCloudSendSmsService.class);

    @Autowired
    SmsRepository smsRepo;

    @Autowired
    CampaignRepository cpRepo;

    @Autowired
    Executor taskExecutor;

    @Autowired
    ApplicationContext applicationContext;

    public static Map<String, Future<Long>> runningThreads = new ConcurrentHashMap<String, Future<Long>>();

    /**
     * Deliver SMS Response to customer every 10 seconds
     * TODO: Using message broker for modern pattern
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 1000)
    public long submitPendingCampaign() throws InterruptedException {
    	CompletionService<Long> completionService = new ExecutorCompletionService<Long>(taskExecutor);
    	Pageable pageable = PageRequest.of(0, 1000);
    	for (Campaign cp : cpRepo.findAllByState(2)) {
//    		log.debug("Found campaign: " + cp + " -- cpid" + cp.getId());
			List<Sms> tobeSubmit = smsRepo.findAllByCampaignIdAndState(cp.getId(), 0, pageable)
					.getContent();
//			log.debug("Found SMS: " + tobeSubmit);
    		if (tobeSubmit.size() == 0) {
    			cpRepo.save(cp.state(9)); // No more SMS? Mark it as submitted
    			continue;
    		}
    		if (cp.getChannel().contains("VASCLOUD")) {
//    			log.debug("Campaign Channel: " + cp.getChannel());
    			VasCloudSmsSubmitCallable sendSmsTask = applicationContext.getBean(VasCloudSmsSubmitCallable.class);
    			sendSmsTask.setSmsList(tobeSubmit);
    			sendSmsTask.setCampaign(cp);
    			runningThreads.put(cp.getId(), completionService.submit(sendSmsTask));
    		}
    	}
    	Future<Long> completedFuture;
        long submitCnt = 0L;
        Iterator<Entry<String, Future<Long>>> iterate = runningThreads.entrySet().iterator();
        while (iterate.hasNext()) {
        	Entry<String, Future<Long>> entry = iterate.next();
        	completedFuture = entry.getValue();
    		// get the Widget, if the Callable was able to create it
            try {
            	Long res = completedFuture.get();
                if (completedFuture.isDone()) {
                        submitCnt += res;
                        iterate.remove();
                        log.info("=== ONE THREAD COMPLETED: Campaign #" + entry.getKey() + " REQUEST SEND: " + res);
                } else {
                        log.error("=== WHY THREAD IS NOT DONE HERE ===" );
                }
            } catch (ExecutionException | CancellationException e) {
                log.error("== CANNOT RUN SEND SMS TASK", e);
                iterate.remove();
                continue;
            }
        }
    	return submitCnt;
    }

	public void stopCampaign(String id) {
		if (runningThreads.get(id) != null) runningThreads.get(id).cancel(true);
	}
}
