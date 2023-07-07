package project.trendpick_pro.domain.common.base.filetranslator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FileTranslator {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("https://kr.object.ncloudstorage.com/{cloud.aws.s3.bucket}/")
    private String filePath; //저장경로

    public String getFilePath(String filename) {
        return filePath + filename;
    }

    //단일 멀티파일 들어왔을때 저장하고 반환
    public CommonFile translateFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String translatedFileName = translateFileName(multipartFile.getOriginalFilename());
        ObjectMetadata oj = new ObjectMetadata();
        oj.setContentLength(multipartFile.getInputStream().available());

        PutObjectRequest poj = new PutObjectRequest(bucket, translatedFileName, multipartFile.getInputStream(), new ObjectMetadata());
        amazonS3.putObject(poj);

        return CommonFile.builder()
                .fileName(translatedFileName)
                .build();
    }

    public void deleteFile(CommonFile commonFile) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, commonFile.getFileName());
        amazonS3.deleteObject(deleteObjectRequest);

        for (CommonFile subFile : commonFile.getChild()) {
            deleteObjectRequest = new DeleteObjectRequest(bucket, subFile.getFileName());
            amazonS3.deleteObject(deleteObjectRequest);
        }
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