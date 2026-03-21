package com.htm.e20nomics.user.service;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.domain.UserPreference;
import com.htm.e20nomics.user.dto.MyPreferenceCreateRequest;
import com.htm.e20nomics.user.repository.UserPreferenceRepository;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    @Transactional
    public void saveOrUpdate(MyPreferenceCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException());

        UserPreference preference = userPreferenceRepository.findByUserId(userId);


        if (preference == null) {
            preference = new UserPreference(
                    user,
                    request.getSummaryLength(),
                    request.getSummaryDifficulty(),
                    request.getSummaryFormat(),
                    request.getSummaryExplainStyle(),
                    request.getTermLength(),
                    request.getTermDifficulty(),
                    request.isIncludeExample(),
                    request.isIncludeRelatedConcept()
            );
            userPreferenceRepository.save(preference);
            return;
        }

        preference.update(
                request.getSummaryLength(),
                request.getSummaryDifficulty(),
                request.getSummaryFormat(),
                request.getSummaryExplainStyle(),
                request.getTermLength(),
                request.getTermDifficulty(),
                request.isIncludeExample(),
                request.isIncludeRelatedConcept()
        );
    }
}
