<template>
    <div class="main-container">
        <el-container>
            <el-aside width="240px" class="sidebar" :class="{ 'sidebar-collapsed': collapsed }">
                <div class="logo">
                    <h2 class="logo-title">管理系统</h2>
                    <el-button class="collapse-btn" type="text" @click="toggleSidebar"
                        :icon="collapsed ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></el-button>
                </div>
                <el-menu class="sidebar-menu" @select="handleMenuSelect" background-color="#304156" text-color="#fff"
                    active-text-color="#409EFF" :collapse="collapsed" :collapse-transition="false"
                    :unique-opened="true">
                    <template v-for="parent in menuList">
                        <el-submenu v-if="parent.children && parent.children.length > 0" :index="`${parent.menuId}`"
                            :key="parent.menuId">
                            <template slot="title">
                                <i :class="parent.icon"></i>
                                <span slot="title">{{ parent.name }}</span>
                            </template>
                            <el-menu-item v-for="child in parent.children" :key="child.menuId"
                                :index="`${child.menuId}`">
                                <i :class="parent.icon"></i>
                                <span slot="title">{{ child.name }}</span>
                            </el-menu-item>
                        </el-submenu>
                        <el-menu-item v-if="parent.children.length == 0" :index="`${parent.menuId}`"
                            :key="parent.menuId">
                            <i :class="parent.icon"></i>
                            <span slot="title">{{ parent.name }}</span>
                        </el-menu-item>
                    </template>
                </el-menu>
            </el-aside>

            <el-container class="main-content">
                <el-header class="header">
                    <div class="header-right">
                        <span class="user-info">欢迎，{{ $store.state.adminName }}</span>
                        <el-button type="primary" @click="logout">退出登录</el-button>
                    </div>
                </el-header>

                <el-main class="content">
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
import authToken from '@/utils/authToken';
export default {
    data() {
        return {
            activeMenu: '2',
            collapsed: false,
            userPermissions: [],
            mapList: [],
            allMap: [],
            menuList: []
        }
    },
    methods: {
        handleMenuSelect(index) {
            const routes = {
                2: '/main/dashboard',
                3: '/main/user-management',
                4: '/main/consultant-management',
                6: '/main/consultation-list',
                7: '/main/consultation-diagnosis',
                8: '/main/question-bank',
                10: '/main/menu-management',
                11: '/main/permission-config',
                12: '/main/log-query',
                14: '/main/profile',
                16: '/main/appointment-setting',
                17: '/main/ai-management',
            }
            if (routes[index] && this.activeMenu !== index) {
                this.activeMenu = index
                this.$router.push(routes[index])
            }
        },
        // 获取用户权限
        getUserPermissions() {
            const roleId = this.$store.state.roleId
            if (roleId && roleId !== -1) {
                this.$axios.get("/role/selectDivide", {
                    params: {
                        roleId: roleId
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.userPermissions = res.data.data.map(item => item.menuId)
                    }
                }).catch(error => {
                    console.error('获取权限失败:', error)
                })
            }
        },
        // 查看所有菜单
        selectMenu() {
            this.$axios.get("/system/Menus")
                .then(res => {
                    if (res.data.code == 1) {
                        this.allMap = res.data.data
                        this.menuList = this.filterTreeData(this.allMap)
                        //前端做树状数据
                        // this.menuList = this.convertToTree(this.mapList, 0);
                    }
                }).catch(error => {
                    console.error('获取菜单失败:', error)
                })
        },
        // 检查权限
        hasPermission(menu) {
            if (this.userPermissions.length > 0) {
                return this.userPermissions.includes(menu.menuId)
            }
        },
        filterTreeData(treeMenu) {
            return treeMenu.filter(menu => {
                const hasPrem = this.hasPermission(menu)
                if (hasPrem) {
                    if (menu.children && menu.children.length > 0) {
                        menu.children = this.filterTreeData(menu.children)
                    }
                    return true
                }
                return false
            });
        },
        //前端做树状数据
        // convertToTree(data, parentId) {
        //     return data
        //         .filter(item => item.upId === parentId)
        //         .map(item => ({
        //             ...item,
        //             children: this.convertToTree(data, item.menuId)
        //         }));
        // },
        logout() {
            authToken.removeToken()
            this.$store.commit('RESETADMIN')
            this.$router.push("/login")
        },
        toggleSidebar() {
            this.collapsed = !this.collapsed
        },
    },
    mounted() {
        if (!this.$store.state.adminId || this.$store.state.adminId === -1) {
            this.$router.replace('/login')
            return
        }
        const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        if (!this.$route.path.includes('/main/')) {
            this.$router.push('/main/dashboard')
        }
        this.getUserPermissions()
        this.selectMenu()
        loading.close();
    },

}
</script>

<style scoped>
.main-container {
    height: 100vh;
    overflow: hidden;
    display: flex;
}

.sidebar {
    background: #304156;
    color: white;
    transition: width 0.3s ease;
    height: 100vh;
    position: relative;
    z-index: 100;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.sidebar-collapsed {
    width: 64px !important;
}

.logo {
    padding: 0 20px;
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #404E67;
}

.logo-title {
    margin: 0;
    color: white;
    font-size: 18px;
    font-weight: 600;
    transition: opacity 0.3s ease;
}

.sidebar-collapsed .logo-title {
    opacity: 0;
    width: 0;
    overflow: hidden;
}

.collapse-btn {
    color: white;
    font-size: 16px;
    transition: transform 0.3s ease;
}

.collapse-btn:hover {
    color: #409EFF;
}

.sidebar-menu {
    height: calc(100vh - 60px);
    overflow-y: auto;
    padding: 10px 0;
}

.menu-icon {
    font-size: 16px;
    margin-right: 12px;
}

.sidebar-collapsed .menu-icon {
    margin-right: 0;
}

.main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    height: 100vh;
    overflow: hidden;
}

.header {
    background: white;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    height: 60px;
}

.menu-toggle {
    color: #666;
    font-size: 18px;
    margin-right: 20px;
}

.menu-toggle:hover {
    color: #409EFF;
}

.header-right {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-left: auto;
}

.user-info {
    color: #666;
    font-size: 14px;
}

.user-name {
    margin-left: 8px;
    margin-right: 4px;
}

.content {
    background: #f5f7fa;
    overflow-y: auto;
    flex: 1;
    padding: 20px;
}

/* 菜单交互效果 */
.el-menu-item {
    transition: all 0.3s ease;
    height: 48px;
    line-height: 48px;
    margin: 0 10px;
    border-radius: 4px;
}

.el-menu-item:hover {
    background-color: rgba(255, 255, 255, 0.1) !important;
}

.el-menu-item.is-active {
    background-color: #2f5a83 !important;
}

.el-submenu__title {
    transition: all 0.3s ease;
    height: 48px;
    line-height: 48px;
    margin: 0 10px;
    border-radius: 4px;
}

.el-submenu__title:hover {
    background-color: rgba(255, 255, 255, 0.1) !important;
}

.el-submenu .el-menu {
    background-color: #2c3e50;
}

.el-submenu .el-menu-item {
    margin: 0 10px;
    padding-left: 36px !important;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
    .sidebar {
        position: fixed;
        left: 0;
        top: 0;
        z-index: 1000;
    }

    .sidebar-collapsed {
        transform: translateX(-100%);
    }

    .main-content {
        margin-left: 0;
    }

    .header-right {
        gap: 10px;
    }

    .user-info {
        display: none;
    }

    .content {
        padding: 10px;
    }
}
</style>
