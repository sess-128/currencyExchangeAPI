package org.example.currencyexchangerefactoring.errorhandle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

public class ErrorCodesGetter {
    public static String getMessage (HttpServletResponse response) throws JsonProcessingException {

        String messageByCode = ErrorCodes.getMessageByCode(response.getStatus());
        MessageResponse messageResponse = new MessageResponse(messageByCode);
        return new ObjectMapper().writeValueAsString(messageResponse);
    }
    public static String getMessage (int error) throws JsonProcessingException {

        String messageByCode = ErrorCodes.getMessageByCode(error);
        MessageResponse messageResponse = new MessageResponse(messageByCode);
        return new ObjectMapper().writeValueAsString(messageResponse);
    }
}
