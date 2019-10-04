package com.asset.jupiter.Web.Controllers;

import com.asset.jupiter.JUPITER.Model.Entities.InfoFile;
import com.asset.jupiter.JUPITER.Model.Entities.JsonFile;
import com.asset.jupiter.JUPITER.jupiterService.infoFileService;
import com.asset.jupiter.JUPITER.jupiterService.jsonFileService;
import com.asset.jupiter.Util.defines.Defines;
import com.asset.jupiter.Web.DTO.staticsDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {


    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.infoFileService infoFileService;
    @Autowired
    com.asset.jupiter.JUPITER.jupiterService.jsonFileService jsonFileService;
    @Autowired
    ModelMapper modelMapper;
    // configuration Check
    @Autowired
    com.asset.jupiter.Util.configurationService.config config;
    @PostMapping("/api/search")
    public ResponseEntity<?> getSearchResultViaAjax(  @Valid @RequestBody SearchCriteria  search, Errors errors ){


        AjaxSearchResponseBody result = new AjaxSearchResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        // if not end with .json add .json
        if (!search.getFileName().endsWith(".json"))
            search.setFileName(search.getFileName()+".json");

        // create File path
        String integrationFolderPath = config.getIntegrationFolderPath();
        // set file path
        search.setFileName(integrationFolderPath+"\\"+search.getFileName());

        InfoFile allByPathAndStatus = infoFileService.findAllByPathAndStatus(search.getFileName(), Defines.finished);

        if (allByPathAndStatus == null) {
            result.setMsg("no File found!");
            return ResponseEntity.badRequest().body(result);
        } else {
            result.setMsg("success");
        }
        JsonFile byInfoFileId = jsonFileService.findByInfoFileId(allByPathAndStatus.getInfoId());

        // convert info file to static DTO
        staticsDTO staticsDTO = convertToDto(allByPathAndStatus, byInfoFileId);

        // set result to
        result.setResult(staticsDTO);

        return ResponseEntity.ok(result);

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
