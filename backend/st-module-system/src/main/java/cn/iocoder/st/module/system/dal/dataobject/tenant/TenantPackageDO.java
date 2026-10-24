package cn.iocoder.st.module.system.dal.dataobject.tenant;

import cn.iocoder.st.framework.common.enums.CommonStatusEnum;
import cn.iocoder.st.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.st.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Set;

/**
 * 内部权限模板 DO。沿用上游表名，不提供套餐管理或商业套餐功能。
 */
@TableName(value = "system_tenant_package", autoResultMap = true)
@KeySequence("system_tenant_package_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TenantIgnore
public class TenantPackageDO extends BaseDO {

    /** 所有业务租户统一使用的内部权限模板；0 仍保留给系统租户。 */
    public static final Long ID_INTERNAL = 1L;

    /**
     * 套餐编号，自增
     */
    private Long id;
    /**
     * 套餐名，唯一
     */
    private String name;
    /**
     * 租户套餐状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 关联的菜单编号
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> menuIds;

}
