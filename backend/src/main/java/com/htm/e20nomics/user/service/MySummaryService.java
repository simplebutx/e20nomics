package com.htm.e20nomics.user.service;


import com.htm.e20nomics.global.exception.SummaryNotFoundException;
import com.htm.e20nomics.summary.domain.Summary;
import com.htm.e20nomics.summary.repository.SummaryRepository;
import com.htm.e20nomics.user.dto.MySummariesResponse;
import com.htm.e20nomics.user.dto.MySummaryDetailResponse;
import com.htm.e20nomics.user.dto.MySummaryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MySummaryService {

    private final SummaryRepository summaryRepository;

    @Transactional(readOnly = true)
    public List<MySummariesResponse> mySummaries(Long userId) {
        return summaryRepository.findAllByAuthorId(userId).stream()
                .map(summary -> new MySummariesResponse(summary.getId(), summary.getSummaryTitle(), summary.getSummaryText(),
                        summary.getCreatedAt(), summary.getUpdatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public MySummaryDetailResponse getSummaryDetail(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(()-> new SummaryNotFoundException("요약을 찾을 수 없습니다."));
        return new MySummaryDetailResponse(summary.getSummaryTitle(), summary.getSummaryText(), summary.getMemo(), summary.getCreatedAt());
    }

    @Transactional
    public void updateSummary(Long summaryId, Long userId, MySummaryUpdateRequest dto) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("요약을 찾을 수 없습니다."));

        if (!summary.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 요약만 수정할 수 있습니다.");
        }

        summary.update(dto.getSummaryTitle(), dto.getSummaryText(), dto.getMemo());
    }

    @Transactional
    public void deleteSummary(Long summaryId, Long userId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new SummaryNotFoundException("요약을 찾을 수 없습니다."));

        if (!summary.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("본인의 요약만 삭제할 수 있습니다.");
        }

        summaryRepository.delete(summary);
    }
}
