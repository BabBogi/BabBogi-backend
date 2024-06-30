package BobBogi.BobBogispring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 스레드 풀 크기 설정 스레드 풀의 크기를 10으로 설정하여  동시에 10개의 스레드를 사용할 수 있습니다.
        scheduler.setThreadNamePrefix("scheduled-task-"); // 스레드의 이름에 접두사를 설정해서 디버깅 시 어떤 스레드가 스케줄링 작업을 수행하는지 확인 가능
        scheduler.setWaitForTasksToCompleteOnShutdown(true); //애플리케이션 종료될 때, 현재 실행 중인 작업이 완료될 때까지 기다려서 중간에 작업 종료 방지
        scheduler.setAwaitTerminationSeconds(30); //최대 30까지 애플리에케이션 종료 기다리고, 넘으면 강제 종료
        return scheduler;
    }
}
