package live.ioteatime.bridgeserver.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 지원하는 함수 코드입니다.
 */
@RequiredArgsConstructor
public enum FunctionCode {
    READ_COILS(1, "coil"),
    READ_DISCRETE_INPUTS(2, "discrete-input"),
    READ_HOLDING_REGISTERS(3, "holding-register"),
    READ_INPUT_REGISTERS(4, "input-register");

    private final Integer code;
    @Getter
    private final String memoryArea;

    /**
     * 함수 코드 번호를 함수 코드 열거형 객체로 변환합니다.
     *
     * @param code 함수 코드 번호입니다.
     * @return 함수 코드 열거형 객체를 반환합니다.
     */
    public static FunctionCode valueOf(int code) {

        return Arrays.stream(values())
                     .filter(functionCode -> functionCode.code.equals(code))
                     .findFirst()
                     .orElseThrow();
    }
}
