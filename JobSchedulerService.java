@Service
public class JobSchedulerService {

    @Autowired
    private TaskScheduler taskScheduler;

    private Map<Long, ScheduledFuture<?>> scheduledJobs = new HashMap<>();

    public void scheduleJob(Long jobId, Runnable task, String cronExpression) {
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        scheduledJobs.put(jobId, scheduledTask);
    }

    public void cancelJob(Long jobId) {
        ScheduledFuture<?> scheduledTask = scheduledJobs.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            scheduledJobs.remove(jobId);
        }
    }

    public void triggerJobManually(Runnable task) {
        task.run();
    }
}
