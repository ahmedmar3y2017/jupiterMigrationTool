package com.asset.jupiter.Web.webServices;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/migration")
public class migrationService {

    @CrossOrigin
    @PostMapping(value = "/processJson")
    public void signPdfDocument(
            @RequestParam(name = "jsonFile", required = true) MultipartFile attachment,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response) {

        // check If Json Empty return Error
        // check If Extension Not Json return Error
        //

    }

}
