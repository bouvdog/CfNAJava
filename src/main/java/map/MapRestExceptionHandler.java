package map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MapRestExceptionHandler {
    @ExceptionHandler(HexValueNotOnMap.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MapErrorResponseMessage badRequest(Exception ex) {
        MapErrorResponseMessage responseMsg = new MapErrorResponseMessage(ex.getMessage());
        return responseMsg;
    }


}
