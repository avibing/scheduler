@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobSchedulerService jobSchedulerService;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "job-list";
    }

    @GetMapping("/schedule")
    public String showScheduleForm(Model model) {
        model.addAttribute("job", new Job());
        return "schedule-job";
    }

    @PostMapping("/schedule")
    public String scheduleJob(@ModelAttribute Job job) {
        jobRepository.save(job);
        jobSchedulerService.scheduleJob(job.getId(), () -> executeJob(job), job.getCronExpression());
        return "redirect:/jobs";
    }

    @PostMapping("/trigger/{id}")
    public String triggerJob(@PathVariable Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        jobSchedulerService.triggerJobManually(() -> executeJob(job));
        return "redirect:/jobs";
    }

    private void executeJob(Job job) {
        // Custom job logic here
        System.out.println("Executing job: " + job.getName());
    }
}
