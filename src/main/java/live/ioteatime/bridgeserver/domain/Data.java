package live.ioteatime.bridgeserver.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 센서 데이터를 담는 DTO 클래스입니다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Data {

    @Setter
    private Protocol protocol;

    @Setter
    private String id;

    private Long time;

    private Float value;
}
