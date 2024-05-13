package live.ioteatime.bridgeserver.domain;

import lombok.RequiredArgsConstructor;

/**
 * 채널의 정보를 담는 클래스입니다.
 */
@RequiredArgsConstructor
public class Channel {

    private static final String TAG_ADDRESS_FORMAT = "%s:%d:%s[1]";

    private final String id;
    private final FunctionCode functionCode;
    private final Integer address;
    private final Type type;

    /**
     * 모드버스 요청 포맷을 가져옵니다.
     *
     * @return 모드버스 요청 포맷의 문자열을 반환합니다.
     */
    public String getTagAddress() {

        // address 가 1 부터 시작하기 때문에 +1
        return String.format(TAG_ADDRESS_FORMAT, functionCode.getMemoryArea(), address + 1, type);
    }

    /**
     * 태그 이름을 가져옵니다.
     *
     * @return 태그 이름의 문자열을 반환합니다.
     */
    public String getName() {

        return id;
    }
}
