package live.ioteatime.bridgeserver.exception;

/**
 * 등록된 콜백이 없을 때 발생하는 예외입니다.
 */
public class NoCallbackException extends RuntimeException {

    public NoCallbackException() {
        super();
    }
}
