package com.nhnacademy.aiotone.event.listener;

import com.nhnacademy.common.notification.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Component
@Slf4j
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "prod")
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {
    private final StringBuilder sb = new StringBuilder();
    private final MessageSender messageSender;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Instant startupDate = Instant.ofEpochMilli(event.getApplicationContext().getStartupDate());
        Instant shutdownDate = Instant.ofEpochMilli(event.getTimestamp());

        long runningTimeSec = Duration.between(startupDate, shutdownDate).getSeconds();

        String message = sb.append("mqtt 프로그램이 종료되었습니다.").append("\n\n")
                .append("시작 일시 : ").append(startupDate)
                .append('\n')
                .append("종료 일시 : ").append(shutdownDate)
                .append('\n')
                .append("실행 시간(sec) : ").append(runningTimeSec)
                .toString();

        messageSender.send(message);
    }
}
