-- =====================================================================
-- 神通小工单：移除模板示例菜单
-- 文件：backend/sql/mysql/20261019_remove_demo_menus.sql
-- 说明：隐藏并逻辑删除历史初始化脚本中遗留的代码生成示例菜单，
--       同时撤销角色与这些菜单的关联，避免旧数据库重新展示示例页面。
--       脚本可重复执行；菜单采用逻辑删除，回滚时需先按备份恢复角色关联，
--       再将对应 system_menu 的 deleted/status/visible 恢复为启用状态。
-- =====================================================================
SET NAMES utf8mb4;

START TRANSACTION;

UPDATE `system_role_menu`
SET `deleted` = b'1',
    `updater` = 'system',
    `update_time` = NOW()
WHERE `menu_id` IN (83, 536, 537, 538, 539, 540, 541, 542, 543, 544,
                    545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555)
  AND `deleted` = b'0';

UPDATE `system_menu`
SET `status` = 1,
    `visible` = b'0',
    `deleted` = b'1',
    `updater` = 'system',
    `update_time` = NOW()
WHERE `id` IN (83, 536, 537, 538, 539, 540, 541, 542, 543, 544,
               545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555)
  AND `deleted` = b'0';

COMMIT;

-- 核对：以下查询应返回 0。
SELECT COUNT(*) AS remaining_demo_menus
FROM `system_menu`
WHERE `id` IN (83, 536, 537, 538, 539, 540, 541, 542, 543, 544,
               545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555)
  AND `deleted` = b'0';
