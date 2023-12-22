package com.example.myboard.global.scheduler;

import com.example.myboard.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final PostRepository postRepository;

    // 수정된지 90일이 지난 데이터는 자동으로 지우는 스케줄러 기능
    @Transactional
    @Scheduled(cron = "0 0 0/1 * * *") //1시간마다 실행
    public void cleanupPost() {
        LocalDateTime removeTime = LocalDateTime.now().minusDays(90);
        log.info(removeTime.toString());
        postRepository.deleteByModifiedAtBefore(removeTime);
    }
}
