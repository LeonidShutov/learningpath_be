package com.leonidshutov.learningpath_be.listener;

import com.leonidshutov.learningpath_be.event.UserRegisteredEvent;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.model.UserProfile;
import com.leonidshutov.learningpath_be.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class UserProfileCreationListener {

    private final UserProfileRepository userProfileRepository;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void handleUserRegistration(UserRegisteredEvent event) {
        User user = event.getUser();

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setDataStructuresLevel(SkillLevel.NOVICE);
        userProfile.setAlgorithmsLevel(SkillLevel.NOVICE);
        userProfile.setSystemDesignLevel(SkillLevel.NOVICE);

        // Set both sides of the relationship to keep the object graph consistent
        user.setUserProfile(userProfile);

        // We only need to save the profile; the user relationship is handled by the association
        userProfileRepository.save(userProfile);
    }
}
