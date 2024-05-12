package live.ioteatime.bridgeserver.client;

import java.util.List;
import java.util.function.BiConsumer;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.messages.PlcReadResponse;

/**
 * 모드버스 클라이언트 인터페이스입니다.
 */
public interface ModbusClient {

    /**
     * 요청한 모드버스 데이터를 수신했을 때 데이터를 처리할 콜백을 등록합니다.
     *
     * @param callback 등록할 콜백 객체입니다.
     */
    void setCallback(BiConsumer<PlcReadResponse, Throwable> callback);

    /**
     * 요청할 모드버스 채널을 추가합니다.
     *
     * @param channels 추가할 채널 객체입니다.
     */
    void addChannels(List<String> channels);

    /**
     * 모드버스 서버에 연결하고 요청을 보냅니다.
     *
     * @throws PlcConnectionException 모드버스 서버와 통신 중 문제가 발생할 경우
     */
    void connect() throws PlcConnectionException;
}
