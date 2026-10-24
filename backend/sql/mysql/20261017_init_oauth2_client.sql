-- =====================================================================
-- 神通小工单：初始化 OAuth2 默认客户端
-- 文件：backend/sql/mysql/20261017_init_oauth2_client.sql
-- 目标库：st_gy_mes（MySQL 8.0）
-- 背景：账号密码登录成功后，后端用 client_id = 'default' 签发访问令牌
--       （OAuth2ClientConstants.CLIENT_ID_DEFAULT）。表为空时登录报“OAuth2 客户端不存在”。
-- 说明：
--   * 访问令牌有效期 30 分钟，刷新令牌有效期 30 天（与原型 1 默认值一致）。
--   * secret 在执行时随机生成，账号密码登录不使用它，无需记录。
--   * 可重复执行：已存在 client_id = 'default' 时跳过。
-- =====================================================================
SET NAMES utf8mb4;

START TRANSACTION;

INSERT INTO system_oauth2_client
    (id, client_id, secret, name, logo, description, status,
     access_token_validity_seconds, refresh_token_validity_seconds,
     redirect_uris, authorized_grant_types, scopes, auto_approve_scopes,
     authorities, resource_ids, additional_information, creator, updater, deleted)
SELECT 1, 'default', REPLACE(UUID(), '-', ''), '神通小工单', '', '管理端账号密码登录默认客户端', 0,
       1800, 2592000,
       '[]', '["password","refresh_token"]', '[]', '[]',
       '[]', '[]', '{}', '1', '1', b'0'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM system_oauth2_client WHERE client_id = 'default' AND deleted = b'0');

COMMIT;

-- 执行后核对（应返回 1 行）
SELECT id, client_id, name, status, access_token_validity_seconds, refresh_token_validity_seconds, authorized_grant_types
FROM system_oauth2_client WHERE client_id = 'default';

-- =====================================================================
-- 回滚（仅在需要撤销本脚本时手动执行）
-- DELETE FROM system_oauth2_client WHERE client_id = 'default' AND name = '神通小工单';
-- =====================================================================