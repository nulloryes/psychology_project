<template>
    <div class="permission-config-container">
        <el-card>
            <div slot="header" class="card-header">
                <span>权限配置</span>
                <el-button type="primary" size="small" @click="openAddRoleDialog">
                    <i class="el-icon-plus"></i> 新增角色
                </el-button>
            </div>
            <el-tabs v-model="activeName">
                <el-tab-pane label="列表形式" name="first">
                    <div class="role-list">
                        <el-table :data="roleList" style="width: 100%" border stripe>
                            <el-table-column prop="roleId" label="角色ID" width="100"></el-table-column>
                            <el-table-column prop="name" label="角色名称" min-width="180"></el-table-column>
                            <el-table-column prop="desc" label="角色描述" min-width="200"></el-table-column>
                            <el-table-column label="分配菜单" min-width="200">
                                <template slot-scope="scope">
                                    <el-button size="small" @click="openView(scope.row.roleId)"
                                        type="primary">查看详情</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" width="180" fixed="right" align="center">
                                <template slot-scope="scope">
                                    <el-button type="primary" size="mini" @click="editRole(scope.row)">编辑</el-button>
                                    <el-button type="danger" size="mini"
                                        @click="deleteRole(scope.row.roleId)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div v-if="roleList.length === 0" class="empty-state">
                            <el-empty description="暂无角色数据"></el-empty>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="可视化形式" name="second">
                    <permission :role-list="roleList"></permission>
                </el-tab-pane>
            </el-tabs>
        </el-card>

        <!-- 新增/编辑角色对话框 -->
        <el-dialog :title="roleDialogTitle" :visible.sync="roleDialogVisible" width="500px"
            :close-on-click-modal="false">
            <el-form :model="roleForm" label-width="100px" :rules="roleRules" ref="roleForm">
                <el-form-item label="角色名称" prop="name">
                    <el-input v-model="roleForm.name" placeholder="请输入角色名称"></el-input>
                </el-form-item>
                <el-form-item label="菜单设置" prop="menu">
                    <el-select v-model="roleForm.menu" multiple style="width: 100%" placeholder="选择菜单">
                        <el-option v-for="item in menuList" :key="item.menuId" :label="item.name"
                            :value="item.menuId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="角色描述" prop="desc">
                    <el-input type="textarea" v-model="roleForm.desc" :rows="3" placeholder="请输入角色描述"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="roleDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveRole" :loading="saving">保存</el-button>
            </div>
        </el-dialog>

        <el-dialog title="分配菜单" :visible.sync="infoVisible" width="500px">
            <div v-if="this.selectMenu">
                <el-table :data="selectMenu" style="width: 100%" border stripe>
                    <el-table-column prop="menuId" label="菜单ID" width="100"></el-table-column>
                    <el-table-column prop="name" label="菜单名称" min-width="180"></el-table-column>
                    <el-table-column prop="order" label="排序" width="80" align="center"></el-table-column>
                </el-table>
            </div>
            <p v-else>{{ '暂无信息' }}</p>
        </el-dialog>
    </div>
</template>

<script>
import Permission  from '@/components/Permission.vue'
export default {
    components: {
        Permission 
    },  
    data() {
        return {
            roleList: [],
            menuList: [],
            selectMenu: [],
            roleDialogVisible: false,
            infoVisible: false,
            roleDialogTitle: '',
            saving: false,
            loading: false,
            roleForm: {
                roleId: null,
                name: '',
                menu: [],
                desc: ''
            },
            roleRules: {
                name: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' },
                    { min: 1, max: 50, message: '角色名称长度在 1 到 50 个字符', trigger: 'blur' }
                ],
                desc: [
                    { max: 200, message: '角色描述长度不能超过 200 个字符', trigger: 'blur' }
                ]
            },
            activeName: 'first'
        }
    },
    methods: {
        getRoles() {
            this.loading = true
            this.$axios.get("/role/allRole").then(res => {
                this.loading = false
                if (res.data.code == 1) {
                    this.roleList = res.data.data || []
                } else {
                    this.$message({
                        message: res.data.msg || "获取角色失败",
                        type: "error"
                    })
                }
            }).catch(error => {
                this.loading = false
                console.error('获取角色失败:', error)
                this.$message({
                    message: "网络错误，获取角色失败",
                    type: "error"
                })
            })
        },
        openView(id) {
            this.getDivide(id)
            this.infoVisible = true
        },
        getDivide(id) {
            this.$axios.get("/role/selectDivide", {
                params: {
                    roleId: id
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.selectMenu = res.data.data
                    if (this.roleForm) {
                        this.roleForm.menu = this.selectMenu.map(item => item.menuId);
                    }
                }
            }).catch(error => {
                console.log(error)
            })
        },
        getMenu() {
            this.$axios.get("/system/selectMenu").then(res => {
                if (res.data.code == 1) {
                    this.menuList = res.data.data || []
                }
            }).catch(error => {
                console.error('获取菜单失败:', error)
            })
        },
        openAddRoleDialog() {
            this.roleDialogTitle = '新增角色'
            this.roleForm = {
                roleId: null,
                menu: [],
                name: '',
                desc: ''
            }
            if (this.$refs.roleForm) {
                this.$refs.roleForm.resetFields()
            }
            this.roleDialogVisible = true
        },
        editRole(role) {
            this.getDivide(role.roleId)
            this.roleForm.roleId = role.roleId
            this.roleForm.name = role.name
            this.roleForm.desc = role.desc
            this.roleDialogTitle = '编辑角色'
            this.roleDialogVisible = true
        },
        saveRole() {
            if (!this.$refs.roleForm) return
            this.$refs.roleForm.validate((valid) => {
                if (!valid) {
                    return
                }
                this.saving = true
                const url = this.roleForm.roleId ? "/role/updateRole" : "/role/insertRole"
                this.$axios.get(url, {
                    params: {
                        roleId: this.roleForm.roleId,
                        desc: this.roleForm.desc,
                        name: this.roleForm.name,
                        menuIds: this.roleForm.menu
                    }
                }).then(res => {
                    this.saving = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "保存成功",
                            type: "success"
                        })
                        this.roleDialogVisible = false
                        this.getRoles()
                    } else {
                        this.$message({
                            message: res.data.msg || "保存失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.saving = false
                    console.error('保存角色失败:', error)
                    this.$message({
                        message: "网络错误，保存失败",
                        type: "error"
                    })
                })
            })
        },
        deleteRole(roleId) {
            this.$confirm('确定要删除该角色吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                if (roleId == 1 || roleId == 2 || roleId == 3) {
                    this.$message({
                        message: "不可删除重要角色",
                        type: "error"
                    })
                    return
                }
                this.loading = true
                this.$axios.get("/role/delRole", {
                    params: {
                        id: roleId
                    }
                }).then(res => {
                    this.loading = false
                    if (res.data.code == 1) {
                        this.$message({
                            message: "删除成功",
                            type: "success"
                        })
                        this.getRoles()
                    } else {
                        this.$message({
                            message: res.data.msg || "删除失败",
                            type: "error"
                        })
                    }
                }).catch(error => {
                    this.loading = false
                    console.error('删除角色失败:', error)
                    this.$message({
                        message: "网络错误，删除失败",
                        type: "error"
                    })
                })
            }).catch(() => { })
        }
    },
    mounted() {
        this.getRoles()
        this.getMenu()
    }
}
</script>

<style scoped>
.permission-config-container {
    padding: 20px;
    min-height: 100vh;
    background-color: #f5f7fa;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.role-list {
    margin-top: 20px;
    min-height: 400px;
}

.el-table {
    border-radius: 4px;
    overflow: hidden;
}

.el-table th {
    background-color: #f5f7fa;
    font-weight: 600;
}

.el-table tr:hover {
    background-color: rgba(0, 0, 0, 0.02);
}

.el-table .el-button {
    margin-right: 8px;
}

.el-table .el-button:last-child {
    margin-right: 0;
}

.empty-state {
    margin-top: 40px;
    text-align: center;
}
</style>
