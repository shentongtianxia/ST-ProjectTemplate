package cn.iocoder.st.module.system.framework.validation;

import cn.iocoder.st.framework.common.util.validation.ValidationUtils;
import cn.iocoder.st.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAvatarUrlTest {

    @Test
    public void acceptsAllTenDefaultAvatarPathsWhenSavingProfile() {
        for (int index = 1; index <= 10; index++) {
            String avatar = String.format(Locale.ROOT, "/avatars/nature/nature-%02d.webp", index);
            assertDoesNotThrow(() -> ValidationUtils.validate(new UserProfileUpdateReqVO().setAvatar(avatar)));
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"https://example.com/uploaded-avatar.webp", "http://localhost:48080/files/avatar.png"})
    public void preservesExistingOptionalAndUploadedAvatarValidation(String avatar) {
        assertDoesNotThrow(() -> ValidationUtils.validate(new UserProfileUpdateReqVO().setAvatar(avatar)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/avatars/nature/nature-00.webp", "/avatars/nature/nature-11.webp",
            "/avatars/nature/../other.webp", "/arbitrary-local-file.png", "not a URL"
    })
    public void rejectsOtherRelativePathsAndMalformedUrls(String avatar) {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> ValidationUtils.validate(new UserProfileUpdateReqVO().setAvatar(avatar)));
        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch(violation -> "avatar".equals(violation.getPropertyPath().toString())));
    }

}
