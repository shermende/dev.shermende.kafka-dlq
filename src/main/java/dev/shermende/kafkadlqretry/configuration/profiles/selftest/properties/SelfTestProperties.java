package dev.shermende.kafkadlqretry.configuration.profiles.selftest.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Created by abdys on 20/12/20
 * n.u.abdysamat@gmail.com,developer@shermende.dev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelfTestProperties {
    @NotEmpty
    private String topic;
    @NotEmpty
    private String dlqTopic;
}