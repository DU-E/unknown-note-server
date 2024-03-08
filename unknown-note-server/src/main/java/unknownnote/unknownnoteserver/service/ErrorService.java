package unknownnote.unknownnoteserver.service;

import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.ErrorResponse;

@Service
public class ErrorService {

    public ErrorResponse setError(int code, String message){
        ErrorResponse errorResponse = new ErrorResponse(code, message);
        return errorResponse;
    }
}
