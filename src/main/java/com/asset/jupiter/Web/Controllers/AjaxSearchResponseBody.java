package com.asset.jupiter.Web.Controllers;

import com.asset.jupiter.Web.DTO.staticsDTO;
import org.springframework.data.domain.Page;

public class AjaxSearchResponseBody {

    String msg;
    staticsDTO result;

    public AjaxSearchResponseBody() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public staticsDTO getResult() {
        return result;
    }

    public void setResult(staticsDTO result) {
        this.result = result;
    }
}
