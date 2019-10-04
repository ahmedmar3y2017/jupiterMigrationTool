package com.asset.jupiter.Web.Controllers;

import com.asset.jupiter.JUPITER.Dao.infoFileDao;
import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.Model.Entities.JsonFile;
import com.asset.jupiter.JUPITER.jupiterService.infoFileService;
import com.asset.jupiter.JUPITER.jupiterService.jsonFileService;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Web.DTO.staticsDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class controller {
    // file path to download documentation
    private static final String FILE_PATH = "D:\\QNB Design Document 5.2.pdf";
    @Autowired
    infoFileService infoFileService;

    @Autowired
    jsonFileService jsonFileService;
    @Autowired
    ModelMapper modelMapper;

    @RequestMapping("/")
    public String Home() {

        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String Index() {

        return "home";
    }

//    @RequestMapping(value = "/statics" , method = RequestMethod.GET)
    @GetMapping("/staticsList")
    public String staticssPage(HttpServletRequest request, Model model) {

        // get create Mode ---- get Update Mode

        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
//        customerRepository.findAll(PageRequest.of(page, size));

        // get from database status (create , update)
        Pageable pageRequest = PageRequest.of(page, size);
        Page<InfoFile> allByStatus = infoFileService.findAllByStatus(Defines.finished,pageRequest);

        // Convert to list Of DTO
        List<staticsDTO> staticsDTOList = allByStatus.stream()
                .map(post -> {
                    JsonFile byInfoFileId = jsonFileService.findByInfoFileId(post.getInfoId());
                    return convertToDto(post, byInfoFileId);
                })
                .collect(Collectors.toList());

        // convert list ro Page Again
        Page<staticsDTO> pages = new PageImpl<>(staticsDTOList ,pageRequest, allByStatus.getTotalElements());


        model.addAttribute("staticsList",pages);
        // display these files in table pagination
        // if success -> blue
        // contains errors  -> red

        // when click On status Button One create Model Display action Listener


        return "statics";
    }

    // settings request
    @RequestMapping("/settings")
    public String settingsPage() {


        return "settings";
    }

    @PostMapping("/uploadExcelFile")
    public String uploadFile(Model model, MultipartFile file) throws IOException {


        return "welcome";
    }

    // download documentation File
    @RequestMapping(value = "/downloadDoc", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody
    Resource downloadC(HttpServletResponse response) throws FileNotFoundException {
        File file = getFile();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

    // get file from resource
    private File getFile() throws FileNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new FileNotFoundException("file with path: " + FILE_PATH + " was not found.");
        }
        return file;
    }

    // get PostDto
    private staticsDTO convertToDto(InfoFile infoFile, JsonFile jsonFile) {
        staticsDTO staticsDTO = modelMapper.map(infoFile, staticsDTO.class);
        // extract File Name from path
        String path =infoFile.getPath();
        String fileName = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));

        staticsDTO.setFileName(fileName);
        if (jsonFile != null) {
            // set data Json File
            staticsDTO.setCountDocuments(jsonFile.getCountDocuments());
            staticsDTO.setCountFolders(jsonFile.getCountFolders());
            staticsDTO.setType(jsonFile.getType());
            staticsDTO.setThreadName(jsonFile.getThreadName());
        }

        return staticsDTO;
    }
}
