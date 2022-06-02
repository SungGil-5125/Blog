package com.project.blog.response;

import com.project.blog.response.result.CommonResultResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    @Getter
    @AllArgsConstructor
    private enum CommonResponse {
        SUCCESS(true, "성공했습니다.", HttpStatus.OK);
        private boolean success;
        private String msg;
        private HttpStatus httpStatus;
    }

    public CommonResultResponse getSuccessResult() {
        return getCommonResultResponse();
    }

    private CommonResultResponse getCommonResultResponse() {
        return new CommonResultResponse(CommonResponse.SUCCESS.isSuccess(), CommonResponse.SUCCESS.getMsg(), CommonResponse.SUCCESS.getHttpStatus());
    }
}
