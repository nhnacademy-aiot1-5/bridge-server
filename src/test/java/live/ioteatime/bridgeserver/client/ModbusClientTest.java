package live.ioteatime.bridgeserver.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import live.ioteatime.bridgeserver.client.impl.ModbusClientImpl;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.junit.jupiter.api.Test;

class ModbusClientTest {

    @Test
    void modbusClientTest() throws Exception {
        // given
        ServerSocket serverSocket = new ServerSocket(0);
        String uri = "tcp://localhost:" + serverSocket.getLocalPort();
        ModbusClient modbusClient = new ModbusClientImpl(uri);
        List<Integer> list = new ArrayList<>();
        BiConsumer<PlcReadResponse, Throwable> callback =
            (response, throwable) -> response.getTagNames()
                                             .stream()
                                             .map(response::getInteger)
                                             .forEach(list::add);
        modbusClient.setCallback(callback);
        List<String> channels = List.of(
            "3/0/UDINT"
        );
        modbusClient.addChannels(channels);

        // when
        modbusClient.connect();
        Socket socket = serverSocket.accept();
        byte[] b = {0x00, 0x01, 0x00, 0x00, 0x00, 0x07, 0x01, 0x03, 0x04, 0x00, 0x01, 0x00, 0x40};
        socket.getOutputStream()
              .write(b);

        Thread.sleep(100);

        // then
        assertThat(list).contains(65600);
    }
}
