-- 项目自用：停用租户套餐管理，底层只保留编号 1 的内部权限模板。
-- 不删除表，不改变租户划分，不创建账号或角色，不调整已有角色授权。
-- 依赖：平台基础表已建，全局菜单已初始化。
-- 执行前保存 system_tenant_package(id=1) 和 system_menu(id=182~186) 的原始值。
-- 回滚：先回退应用，再按执行前备份恢复这些记录；不删除已有租户及授权。
-- 可重复执行；已有非空菜单集合保留，避免覆盖已维护的内部权限。
SET NAMES utf8mb4;

START TRANSACTION;

INSERT INTO system_tenant_package
    (id, name, status, remark, menu_ids, creator, updater, deleted)
SELECT 1, '内部权限模板', 0, '系统内部授权使用，不提供租户套餐管理',
       '[]', 'system', 'system', b'0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM system_tenant_package WHERE id = 1);

UPDATE system_tenant_package
SET name = '内部权限模板',
    remark = '系统内部授权使用，不提供租户套餐管理',
    menu_ids = CASE
        WHEN (menu_ids IS NULL OR JSON_LENGTH(menu_ids) = 0)
             AND (SELECT COUNT(*) FROM system_menu
                  WHERE id IN (1,4,7,8,9,23,24,25,26,27,28,29,39,40,41,42,43,44,45,46,47,48,52)
                    AND deleted = b'0' AND status = 0) = 23
        THEN '[1,4,7,8,9,23,24,25,26,27,28,29,39,40,41,42,43,44,45,46,47,48,52]'
        ELSE menu_ids
    END,
    updater = 'system', update_time = NOW()
WHERE id = 1 AND deleted = b'0';

-- 仅停用套餐菜单与按钮，不删除历史记录，便于核对和回滚。
UPDATE system_menu
SET status = 1, visible = b'0', updater = 'system', update_time = NOW()
WHERE (id = 182 AND component = 'system/tenantPackage/index')
   OR permission LIKE 'system:tenant-package:%';

COMMIT;

-- 核对：套餐管理可用菜单应为 0；内部模板为启用状态且具有基础菜单。
SELECT COUNT(*) AS active_package_menus FROM system_menu
WHERE deleted = b'0' AND status = 0
  AND (component = 'system/tenantPackage/index' OR permission LIKE 'system:tenant-package:%');
SELECT id, name, status, JSON_LENGTH(menu_ids) AS menu_count
FROM system_tenant_package WHERE id = 1 AND deleted = b'0';
