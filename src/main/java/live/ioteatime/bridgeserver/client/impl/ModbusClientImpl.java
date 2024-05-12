package live.ioteatime.bridgeserver.client.impl;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import live.ioteatime.bridgeserver.client.ModbusClient;
import live.ioteatime.bridgeserver.domain.Channel;
import live.ioteatime.bridgeserver.domain.FunctionCode;
import live.ioteatime.bridgeserver.domain.Type;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.PlcConnectionManager;
import org.apache.plc4x.java.api.PlcDriverManager;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadRequest.Builder;
import org.apache.plc4x.java.api.messages.PlcReadResponse;

/**
 * 모드버스 클라이언트 구현체 클래스입니다.
 */
@Slf4j
public class ModbusClientImpl implements ModbusClient, Runnable {

    private static final String SEPARATOR = "/";
    private static final String PROTOCOL = "modbus-tcp:";
    private static final String CONNECTION_CLOSED = "connection closed";
    private static final String CONNECTION_CLOSE_FAILED = "connection close failed";
    private static final String CONNECTED_TO = "Connected to {}";
    private static final int DELAY = 60 * 1_000;

    private final String uri;
    private final Thread thread;
    private final PlcConnectionManager connectionManager;
    private BiConsumer<PlcReadResponse, Throwable> callback;
    private List<Channel> channels;
    private PlcConnection connection;
    private PlcReadRequest request;

    public ModbusClientImpl(@NonNull String uri) {
        this.uri = uri;
        thread = new Thread(this);
        connectionManager = PlcDriverManager.getDefault()
                                            .getConnectionManager();
    }

    @Override
    public void setCallback(@NonNull BiConsumer<PlcReadResponse, Throwable> callback) {
        this.callback = callback;
    }

    @Override
    public void addChannels(@NonNull List<String> channels) {
        this.channels = channels.stream()
                                .map(this::convertChannel)
                                .collect(Collectors.toUnmodifiableList());
    }

    private Channel convertChannel(String raw) {
        String[] tokens = raw.split(SEPARATOR);
        FunctionCode functionCode = FunctionCode.valueOf(Integer.parseInt(tokens[0]));
        Integer address = Integer.parseInt(tokens[1]);
        Type type = Type.valueOf(tokens[2]);

        return new Channel(raw, functionCode, address, type);
    }

    @Override
    public void connect() throws PlcConnectionException {
        connection = connectionManager.getConnection(PROTOCOL + uri);
        log.debug(CONNECTED_TO, uri);

        thread.start();
    }

    @Override
    public void run() {
        preProcess();
        while (!Thread.interrupted()) {
            process();
        }
        postProcess();
    }

    private void preProcess() {
        Builder builder = connection.readRequestBuilder();
        channels.forEach(channel -> builder.addTagAddress(channel.getName(), channel.getTagAddress()));
        request = builder.build();
    }

    private void process() {
        request.execute()
               .whenComplete(callback);
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }

    private void postProcess() {
        try {
            connection.close();
        } catch (Exception ignore) {
            log.error(CONNECTION_CLOSE_FAILED);
        }
        log.debug(CONNECTION_CLOSED);
    }
}
