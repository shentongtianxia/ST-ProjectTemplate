package cn.iocoder.st.module.system.enums.permission;

import cn.iocoder.st.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色标识枚举
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    // 保留 super_admin 编码以兼容框架的超级管理员判断，业务展示名称统一为平台管理员。
    SUPER_ADMIN("super_admin", "平台管理员"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    OPERATOR("operator", "作业员");
    ;

    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        return ObjectUtils.equalsAny(code, SUPER_ADMIN.getCode());
    }

}
