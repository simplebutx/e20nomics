package com.htm.e20nomics.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PresignService {
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    // 허용할 content-type만 화이트리스트로 제한
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    public PresignResponse createPresignedUrl(String contentType) {
        //  content-type 검증
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("contentType이 필요합니다.");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다. (jpg/png/webp)");
        }

        // 확장자 안전 매핑 (클라 입력에 의존하지 않음)
        String ext = switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg"; // image/jpeg
        };

        String filename = UUID.randomUUID() + ext;   // 절대 겹치지 않는 파일 이름을 생성

        String key = "e20nomics/images/posts/" + filename;  // key 규칙 고정 (최소한 prefix를 서버가 강제)

        // S3에 어떤 파일을, 어디에, 어떤 타입으로 올릴건지에 대한 업로드 명세서 생성
        PutObjectRequest objectRequest =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build();

        // 이 업로드 요처에 대해, 얼마동안 유효한 임시 업로드 권한을 줄지 설정
        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(objectRequest)
                        .build();

        // 임시 업로드 권한 URL 생성
        PresignedPutObjectRequest presigned =
                s3Presigner.presignPutObject(presignRequest);

        return new PresignResponse(presigned.url().toString(), key);   // 프론트에 전달
    }

    public record PresignResponse(String url, String key) {}  // PresignService에서만 쓰이는 응답 DTO (밖에 public클래스로 뺄 필요없음)
}
