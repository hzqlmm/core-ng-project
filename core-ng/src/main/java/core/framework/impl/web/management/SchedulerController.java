package core.framework.impl.web.management;

import core.framework.api.http.ContentType;
import core.framework.api.util.Lists;
import core.framework.api.web.Request;
import core.framework.api.web.Response;
import core.framework.impl.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.List;

/**
 * @author neo
 */
public class SchedulerController {
    private final Logger logger = LoggerFactory.getLogger(SchedulerController.class);
    private final Scheduler scheduler;

    public SchedulerController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Response listJobs(Request request) throws UnknownHostException {
        ControllerHelper.validateFromLocalNetwork(request.clientIP());

        List<JobView> jobs = Lists.newArrayList();

        scheduler.triggers.forEach((name, trigger) -> {
            JobView job = new JobView();
            job.name = trigger.name;
            job.jobClass = trigger.job.getClass().getCanonicalName();
            job.schedule = trigger.scheduleInfo();
            jobs.add(job);
        });

        return Response.bean(jobs);
    }

    public Response triggerJob(Request request) throws UnknownHostException {
        ControllerHelper.validateFromLocalNetwork(request.clientIP());

        String jobName = request.pathParam("job");
        logger.info("trigger job, jobName={}, clientIP={}", jobName, request.clientIP());
        scheduler.triggerNow(jobName);
        return Response.text("job triggered, name=" + jobName, ContentType.TEXT_PLAIN);
    }
}
