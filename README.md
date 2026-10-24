# ST 基础项目模板

基于开源项目精简的前后端分离、模块化单体基础工程。

- `backend/`：JDK 21、Spring Boot 3.5、Maven，包含系统管理、基础设施、公共框架和业务扩展骨架。
- `frontend/`：Vue 3、TypeScript、Vite、Element Plus 管理端。

## 本地运行

准备 JDK 21、Maven、Node.js `>=20.19.0`、pnpm `>=8.6.0`、MySQL 和 Redis。数据库脚本位于 `backend/sql/mysql/`，使用前核对目标环境和初始化数据。

后端连接配置放在 `backend/st-server/src/main/resources/application-local.yaml`；前端接口地址和端口放在 `frontend/.env.local`，通过 `VITE_BASE_URL`、`VITE_API_URL` 和 `VITE_PORT` 配置。这些本地文件已加入 Git 忽略，需要自行准备。

在 `backend/` 中构建并启动：

```powershell
mvn -pl st-server -am package -DskipTests
java -jar st-server/target/st-server.jar --spring.profiles.active=local
```

在 `frontend/` 中安装依赖并启动：

```powershell
pnpm install --frozen-lockfile
pnpm dev
```

## 源码与许可证

后端来源：[YunaiV/ruoyi-vue-pro](https://github.com/YunaiV/ruoyi-vue-pro)，复制基线为 `master-jdk17` 分支的 `7c397bbfae3e768b26e3ee962c5aa7064e8d7c96`；当前工程使用 JDK 21。

保留原始 MIT 许可及版权声明：[后端许可证](backend/LICENSE)（ruoyi-vue-pro）、[前端许可证](frontend/LICENSE)（Archer）。
