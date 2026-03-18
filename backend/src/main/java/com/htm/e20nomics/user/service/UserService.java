package com.htm.e20nomics.user.service;
import com.htm.e20nomics.global.exception.UserNotFoundException;
import com.htm.e20nomics.user.domain.User;
import com.htm.e20nomics.user.domain.UserPreference;
import com.htm.e20nomics.user.dto.MyPreferenceCreateRequest;
import com.htm.e20nomics.user.repository.UserPreferenceRepository;
import com.htm.e20nomics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public void savePreference(MyPreferenceCreateRequest dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException());

        UserPreference userPreference = new UserPreference(user, dto.getSummaryLength(), dto.getSummaryDifficulty(), dto.getSummaryFormat(),
                dto.getSummaryExplainStyle(), dto.getTermLength(), dto.getTermDifficulty(), dto.isIncludeExample(), dto.isIncludeRelatedConcept());

        userPreferenceRepository.save(userPreference);
    }
}
