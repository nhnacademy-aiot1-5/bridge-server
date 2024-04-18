package org.example.queueingserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class Data {
    @Setter
    private String topic;
    private Long time;
    @Setter
    private Float value;
}
