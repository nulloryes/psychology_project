<template>
    <div class="main-container">
        <el-container>
            <el-header class="main-header">
                <div class="logo">
                    <h2>心理咨询平台</h2>
                </div>
                <div class="nav-menu">
                    <el-menu :default-active="activeMenu" mode="horizontal" @select="handleMenuSelect"
                        background-color="#409EFF" text-color="#fff" active-text-color="#ffd04b">
                        <el-menu-item index="home">
                            <i class="el-icon-s-home"></i>
                            <span slot="title">首页</span>
                        </el-menu-item>
                        <el-menu-item index="consultants">
                            <i class="el-icon-s-custom"></i>
                            <span slot="title">咨询师库</span>
                        </el-menu-item>
                        <el-menu-item index="appointment">
                            <i class="el-icon-date"></i>
                            <span slot="title">我要预约</span>
                        </el-menu-item>
                        <el-menu-item index="evaluation">
                            <i class="el-icon-question"></i>
                            <span slot="title">在线评估</span>
                        </el-menu-item>
                        <el-menu-item index="reports">
                            <i class="el-icon-document"></i>
                            <span slot="title">我的报告</span>
                        </el-menu-item>
                        <el-menu-item index="profile">
                            <i class="el-icon-user"></i>
                            <span slot="title">个人中心</span>
                        </el-menu-item>
                    </el-menu>
                </div>
                <div class="user-info">
                    <span class="username">{{ userName }}</span>
                    <el-button type="danger" size="small" @click="logout">退出</el-button>
                </div>
            </el-header>

            <el-main class="main-content">
                <!-- 首页 -->
                <div v-if="activeMenu === 'home'" class="home-page">
                    <div class="banner">
                        <h1>专业心理咨询服务</h1>
                        <p>为您提供专业、私密、便捷的心理健康服务</p>
                        <el-button type="primary" size="large" @click="handleMenuSelect('consultants')">立即咨询</el-button>
                    </div>

                    <div class="feature-section">
                        <h2>我们的服务</h2>
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <div class="feature-card">
                                    <i class="el-icon-s-custom"></i>
                                    <h4>专业咨询师</h4>
                                    <p>资深心理咨询师团队，为您提供专业服务</p>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="feature-card">
                                    <i class="el-icon-question"></i>
                                    <h4>在线评估</h4>
                                    <p>科学的心理测评工具，了解您的心理状态</p>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="feature-card">
                                    <i class="el-icon-date"></i>
                                    <h4>便捷预约</h4>
                                    <p>在线预约系统，随时安排咨询时间</p>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="feature-card">
                                    <i class="el-icon-document"></i>
                                    <h4>个人报告</h4>
                                    <p>详细的咨询报告，记录您的心理成长</p>
                                </div>
                            </el-col>
                        </el-row>
                    </div>

                    <div class="consultant-preview">
                        <h2>推荐咨询师</h2>
                        <el-row :gutter="20">
                            <el-col :span="6" v-for="consultant in topConsultants" :key="consultant.consultantId">
                                <el-card class="consultant-card" shadow="hover"
                                    @click.native="viewConsultant(consultant.consultantId)">
                                    <el-avatar :size="80" :src="consultant.headPicture || defaultAvatar"></el-avatar>
                                    <h4>{{ consultant.name }}</h4>
                                    <p>{{ consultant.domainName }}</p>
                                    <el-rate v-model="consultant.rateScore" disabled show-score></el-rate>
                                </el-card>
                            </el-col>
                        </el-row>
                    </div>
                </div>

                <!-- 其他页面内容 -->
                <router-view v-else></router-view>
            </el-main>

            <el-footer class="main-footer">
                <p>心理咨询平台 &copy;</p>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
import { mapState } from 'vuex';
import authToken from '@/utils/authToken';

export default {
    data() {
        return {
            activeMenu: 'home',
            topConsultants: [],
            defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
        }
    },
    computed: {
        ...mapState(['userId', 'userName']),
    },
    methods: {
        handleMenuSelect(index) {
            const routes = {
                'home': '/main',
                'consultants': '/consultant-list',
                'appointment': '/appointment',
                'evaluation': '/evaluation',
                'reports': '/reports',
                'profile': '/profile'
            }
            if (routes[index]) {
                if (this.$route.path !== routes[index]) {
                    this.$router.push(routes[index])
                }
                this.activeMenu = index
            }
        },
        getTopConsultants() {
            this.$axios.get("/consultant/allConsultant", {
                params: {
                    pageNum: 1,
                    pageSize: 4
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.topConsultants = res.data.data.list || []
                }
            }).catch(error => {
                console.log(error)
            })
        },
        viewConsultant(id) {
            this.activeMenu = 'consultants';
            this.$router.push(`/consultant-detail/${id}`)
        },
        logout() {
            // 先清掉 Dify，再清登录态
            authToken.removeToken()
            localStorage.clear()
            this.$store.commit('RESETUSER')
            this.$router.push("/login")
        },
        updateActiveMenu() {
            const path = this.$route.path;
            if (path === '/main') {
                this.activeMenu = 'home';
            } else if (path.includes('/consultant-list') || path.includes('/consultant-detail')) {
                this.activeMenu = 'consultants';
            } else if (path.includes('/appointment')) {
                this.activeMenu = 'appointment';
            } else if (path.includes('/evaluation')) {
                this.activeMenu = 'evaluation';
            } else if (path.includes('/reports')) {
                this.activeMenu = 'reports';
            } else if (path.includes('/profile')) {
                this.activeMenu = 'profile';
            }
        },
        // ========== Dify 相关 ==========
        initDify() {
            if (this.userId !== -1) {
                this.fetchDifyConfigAndLoad();
            } else {
                this.$store.dispatch('getuserinfo').then(() => {
                    this.fetchDifyConfigAndLoad();
                }).catch(() => {
                    console.warn('用户信息获取失败，Dify 加载跳过');
                });
            }
        },
        fetchDifyConfigAndLoad() {
            this.$axios.get('/ai/selectAiByStatus')
                .then(res => {
                    const aiConfig = res.data.data;
                    const token = aiConfig.difyNumber;
                    const baseUrl = (
                        aiConfig.difyLocal || 'http://localhost'
                    ).replace(/\/+$/, '');

                    window.difyChatbotConfig = {
                        token: token,
                        baseUrl: baseUrl,
                        dynamicScript: true,
                        systemVariables: {
                            user_id: this.userId
                        }
                    };

                    const script = document.createElement('script');
                    script.id = token;
                    script.src = baseUrl + '/embed.min.js';
                    script.defer = true;
                    document.body.appendChild(script);
                });
        }
    },
    mounted() {
        this.getTopConsultants();
        this.updateActiveMenu();
        this.initDify();
    }
}
</script>

<style scoped>
.main-container {
    min-height: 100vh;
}

.main-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #409EFF;
    color: white;
    padding: 0 20px;
}

.logo h2 {
    margin: 0;
    color: white;
}

.nav-menu {
    flex: 1;
    margin-left: 50px;
}

.nav-menu .el-menu {
    border-bottom: none;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 15px;
}

.username {
    color: white;
}

.main-content {
    min-height: calc(100vh - 120px);
    background: #f5f7fa;
}

.main-footer {
    background: #333;
    color: white;
    text-align: center;
    line-height: 60px;
}

.home-page {
    padding: 20px;
}

.banner {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 80px 20px;
    text-align: center;
    border-radius: 8px;
    margin-bottom: 40px;
}

.banner h1 {
    font-size: 48px;
    margin-bottom: 20px;
}

.banner p {
    font-size: 20px;
    margin-bottom: 30px;
}

.feature-section {
    margin-bottom: 40px;
}

.feature-section h2 {
    text-align: center;
    margin-bottom: 30px;
}

.feature-card {
    background: white;
    padding: 30px;
    text-align: center;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.feature-card i {
    font-size: 48px;
    color: #409EFF;
    margin-bottom: 15px;
}

.feature-card h4 {
    margin: 10px 0;
}

.feature-card p {
    color: #666;
}

.consultant-preview {
    margin-bottom: 40px;
}

.consultant-preview h2 {
    text-align: center;
    margin-bottom: 30px;
}

.consultant-card {
    text-align: center;
    cursor: pointer;
    transition: transform 0.3s;
}

.consultant-card:hover {
    transform: translateY(-5px);
}

.consultant-card h4 {
    margin: 15px 0 5px 0;
}

.consultant-card p {
    color: #666;
    margin: 5px 0;
}
</style>
