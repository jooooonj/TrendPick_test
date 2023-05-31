package project.trendpick_pro.domain.common.base.filetranslator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.trendpick_pro.domain.common.file.CommonFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileTranslator {

    @Value("${file.dir}")
    private String filePath; //저장경로

    //이미지 업로드할때 url경로
    public String getFilePath(String filename) {
        return filePath + filename;
    }

    //단일 멀티파일 들어왔을때 저장하고 반환
    public CommonFile translateFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String translatedFileName = translateFileName(originalFilename);
        multipartFile.transferTo(new File(getFilePath(translatedFileName)));

        return CommonFile
                .builder()
                .originalFileName(originalFilename)
                .translatedFileName(translatedFileName)
                .build()
                ;
    }

    //파일 여러개를 한 번에 묶어서 변환할때
    public List<CommonFile> translateFileList(List<MultipartFile> MultipartFiles) throws IOException {
        List<CommonFile> fileList = new ArrayList<>();
        for(MultipartFile multipartFile : MultipartFiles){
            if(!multipartFile.isEmpty()){
                fileList.add(translateFile(multipartFile));
            }
        }
        return fileList;
    }

    private String translateFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}