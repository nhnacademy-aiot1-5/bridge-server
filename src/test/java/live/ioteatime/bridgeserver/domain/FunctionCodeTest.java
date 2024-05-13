package live.ioteatime.bridgeserver.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FunctionCodeTest {

    public static Stream<Arguments> source() {

        return Stream.of(
            Arguments.of(1, FunctionCode.READ_COILS),
            Arguments.of(2, FunctionCode.READ_DISCRETE_INPUTS),
            Arguments.of(3, FunctionCode.READ_HOLDING_REGISTERS),
            Arguments.of(4, FunctionCode.READ_INPUT_REGISTERS)
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    void valueOf(int code, FunctionCode expected) {
        FunctionCode functionCode = FunctionCode.valueOf(code);
        assertThat(functionCode).isEqualTo(expected);
    }
}
