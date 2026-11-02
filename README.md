# 心理咨询平台（omo24110_psychology）

一个完整的心理咨询平台，包含**用户端前端**、**管理端前端**、**Spring Boot 后端服务**，并集成 **Dify AI 心理咨询助手**，支持心理安慰、咨询师推荐、预约引导等场景。

---

## 目录结构

```
omo24110_psychology/
├── psychology-front/          # 用户端前端（Vue 2 + Element UI，默认端口 8080）
│   ├── src/views/MainView.vue   # ← Dify AI 聊天助手的前端加载入口
│   ├── src/                    # Vue 源码
│   ├── package.json
│   └── vue.config.js
├── admin-front/                # 管理端前端（Vue 2 + Element UI，默认端口 8082）
│   ├── src/
│   ├── package.json
│   └── vue.config.js
├── src/                        # 后端（Spring Boot + MyBatis + Maven）
│   ├── main/java/com/cykj/     # 业务代码（Controller / Service / Mapper / VO / DO）
│   └── main/resources/         # 配置文件 + MyBatis XML
├── sql/
│   └── omo2411_psychology.sql  # 数据库初始化脚本
├── 心理咨询平台ai.yml           # Dify 基础版应用配置（DSL）
├── 心理咨询平台ai（全血版）.yml  # Dify 完整版应用配置（DSL）
├── pom.xml
├── README.md
└── .gitignore
```

> **Dify 配置位置**：`psychology-front/src/views/MainView.vue` 中的 `fetchDifyConfigAndLoad()` / `loadDify()` 方法。该文件启动时会向后端 `/ai/selectAiByStatus` 接口请求一条已启用的 AI 配置，然后动态构造 Dify 聊天窗口。

---

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot、Spring MVC、MyBatis、Maven、JWT |
| 数据库 | MySQL 5.7+ / 8.x |
| 用户端前端 | Vue 2、Vue Router、Vuex、Element UI、Axios |
| 管理端前端 | Vue 2、Vue Router、Vuex、Element UI、Axios |
| AI 平台 | Dify（本地或云端）、DeepSeek 模型插件、数据库插件 |

---

## 环境要求

- **JDK**：8 或 11
- **Maven**：3.6+
- **Node.js**：16+
- **MySQL**：5.7 或 8.x
- **Dify 平台**：本地 Docker 部署，或使用 [Dify Cloud](https://cloud.dify.ai)

---

## 快速启动

### 5.1 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库（名称可自定，默认为 omo2411_psychology）
CREATE DATABASE omo2411_psychology DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# 导入初始化脚本
USE omo2411_psychology;
SOURCE sql/omo2411_psychology.sql;
```

修改后端数据库连接配置：编辑 `src/main/resources/db.properties`，填入你本地 MySQL 的用户名、密码、IP 和端口。

### 5.2 启动后端

```bash
# 项目根目录
mvn clean package -DskipTests
mvn spring-boot:run
```

或直接在 IDE 中运行 Spring Boot 主类。默认端口 **8080**，端口号可在 `src/main/resources/spring_mvc.xml` 或 `db.properties` 中修改。

### 5.3 启动用户端 psychology-front

```bash
cd psychology-front
npm install
npm run serve
```

默认端口 **8080**。如需修改端口，编辑 `psychology-front/vue.config.js` 中的 `devServer.port`。

### 5.4 启动管理端 admin-front

```bash
cd admin-front
npm install
npm run serve
```

当前默认端口 **8082**。如需调整，编辑 `admin-front/vue.config.js`。确保用户端和管理端端口**不要冲突**。

---

## Dify AI 助手配置（重点）

### 工作原理

用户端首页右下角的 AI 聊天助手由 **Dify 聊天嵌入脚本**提供。前端代码**不再硬编码** token 和 baseUrl，而是在页面加载时调用后端接口动态获取：

1. 前端 `psychology-front/src/views/MainView.vue` 调用 `GET /ai/selectAiByStatus`
2. 后端从数据库 `ai_dify` 表中取出 `status = 2`（已启用）的那条记录
3. 前端将返回的 `difyNumber` 作为 **token**、`difyLocal` 作为 **baseUrl**，写入 `window.difyChatbotConfig` 并加载 Dify 嵌入脚本

### ai_dify 表字段

| 字段 | 含义 | 用途 |
|---|---|---|
| ai_id | 主键 | 管理端 AI 管理使用 |
| dify_number | Dify API 密钥（token） | 前端 `window.difyChatbotConfig.token` |
| dify_local | Dify 服务地址 | 前端 `window.difyChatbotConfig.baseUrl` |
| remarks | 备注 | 管理端查看 |
| status | 状态（0=未启用, 1=删除, 2=启用） | 只有 status=2 的记录会被前端加载 |
| create_time / update_time | 时间戳 | 管理端展示 |

### 在管理端配置 Dify

1. 启动后端 + 管理端，登录管理员账号
2. 左侧菜单「**系统管理** → **AI 管理**」
3. 点击「**新增**」，填写：
   - **Dify 工作流编号**：在 Dify 控制台 → 你的应用 → **API** 页签中生成的 **Secret Key**（以 `app-` 开头）
   - **Dify 链接**：你的 Dify 服务地址（如本地 `http://localhost` 或云端 `https://api.dify.ai`）
   - **备注**：可选
4. 点击保存后，切换该行的 **启用状态** 为开启
5. 如果已有一条启用的记录，新启用会自动替换旧的（后端 `enableAi` 逻辑）

### 导入 Dify 应用配置（DSL 文件）

项目根目录有两个 Dify 应用配置文件：

- **心理咨询平台ai.yml** — 基础版应用配置
- **心理咨询平台ai（全血版）.yml** — 完整版应用配置

导入步骤：

1. 登录 [Dify](https://cloud.dify.ai) 或你本地的 Dify 服务
2. 进入 **工作室** → 点击 **创建应用**
3. 选择 **导入 DSL 文件** → 上传对应的 yml 文件
4. 按向导配置 **DeepSeek 模型插件**（或你使用的模型）和 **数据库插件**
5. 应用创建完成后，进入 **API** 页签，点击「**生成密钥**」，复制 Secret Key
6. 回到管理端「AI 管理」，把 Secret Key 和 Dify 服务地址填入

### MainView.vue 关键代码位置

文件路径：`psychology-front/src/views/MainView.vue`

| 方法 | 行号（约） | 说明 |
|---|---|---|
| `mounted()` | 224 | 页面加载后调用 `initDify()` |
| `initDify()` | 187 | 判断用户 ID 有效后调用 `fetchDifyConfigAndLoad()` |
| `fetchDifyConfigAndLoad()` | 198 | 请求后端 `/ai/selectAiByStatus`，拿到配置后传给 `loadDify()` |
| `loadDify(aiConfig)` | 210 | 从 `aiConfig.difyNumber` 取 token，从 `aiConfig.difyLocal` 取 baseUrl，注入 `window.difyChatbotConfig` 并加载 `embed.min.js` |

---

## 功能说明

### 用户端（psychology-front）

- **首页**：平台介绍、服务列表、推荐咨询师
- **咨询师库**：浏览咨询师列表，查看详情、评价
- **我要预约**：选择咨询师 → 选择时段 → 提交预约
- **在线评估**：心理健康测评量表
- **我的报告**：历史测评报告、咨询记录
- **个人中心**：个人信息维护
- **AI 心理助手**（Dify 聊天嵌入）：心理安慰、咨询师推荐、预约引导

### 管理端（admin-front）

- **控制台（Dashboard）**：数据概览
- **用户管理**：注册用户 CRUD
- **咨询师管理**：咨询师信息、资质审核
- **咨询排班**：咨询师工作时段管理
- **咨询订单**：预约订单管理、诊断记录
- **题目管理**：心理测评题库
- **系统管理**：菜单管理、权限配置、日志查询、AI 管理

### AI 聊天助手（Dify）

- 情绪识别与心理安慰
- 咨询师推荐（按领域、价格、评分）
- 预约流程引导
- 测评量表推荐

---

## 注意事项

1. **端口不要冲突**：用户端 8080、管理端 8082、后端 8080（用户端已代理到后端 8080）。如果需要调整，修改各自的 `vue.config.js` 和后端端口配置即可。
2. **Dify 必须先配置**：用户端启动时会请求 Dify 配置，如果后端 `ai_dify` 表中没有启用的记录，AI 聊天助手不会加载，但不影响页面其他功能。
3. **敏感信息**：**不要提交** `.env.local`、API Key、数据库密码、任何 `.log` 文件。本仓库 `.gitignore` 已处理。
4. **数据库字段**：`ai_dify.status` 的含义是 `0=未启用, 1=删除, 2=启用`，前端只加载 `status=2` 的那条记录。
5. **管理端启用逻辑**：后端 `enableAi(id)` 会先把当前启用的那条置为 0，再把目标 id 置为 2，保证同时只有一条启用。
