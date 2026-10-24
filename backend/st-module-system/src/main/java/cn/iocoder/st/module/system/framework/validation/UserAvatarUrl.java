package cn.iocoder.st.module.system.framework.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.URL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 保留上传头像的 URL 校验，同时允许本项目固定的自然元素头像路径。
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@URL
@Pattern(regexp = "^/avatars/nature/nature-(?:0[1-9]|10)\\.webp$")
public @interface UserAvatarUrl {

    String message() default "头像地址格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
